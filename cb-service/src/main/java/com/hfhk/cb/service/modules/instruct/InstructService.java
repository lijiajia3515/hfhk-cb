package com.hfhk.cb.service.modules.instruct;

import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.auth.modules.user.User;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cb.audit.AuditState;
import com.hfhk.cb.instruct.*;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.modules.project.ProjectService;
import com.hfhk.cb.service.mongo.InstructMongo;
import com.hfhk.system.client.FileClientCredentialsClient;
import com.hfhk.system.file.File;
import com.hfhk.system.file.FileFindParam;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class InstructService {
	private final HfhkMongoProperties properties;
	private final MongoTemplate mongoTemplate;
	private final ProjectService projectService;
	private final UserClientCredentialsClient userClientCredentialsClient;
	private final FileClientCredentialsClient fileClientCredentialsClient;

	public InstructService(HfhkMongoProperties properties, MongoTemplate mongoTemplate, ProjectService projectService, UserClientCredentialsClient userClientCredentialsClient, FileClientCredentialsClient fileClientCredentialsClient) {
		this.properties = properties;
		this.mongoTemplate = mongoTemplate;
		this.projectService = projectService;
		this.userClientCredentialsClient = userClientCredentialsClient;
		this.fileClientCredentialsClient = fileClientCredentialsClient;
	}

	@Transactional(rollbackFor = Exception.class)
	public Instruct apply(InstructApplyParam param) {
		InstructMongo instruct = InstructMongo.builder()
			.type(param.getType())
			.files(param.getFiles())
			.project(param.getProject())
			.unit(param.getUnit())
			.designateAuditUser(param.getDesignateAuditUser())
			.auditState(AuditState.Submit)
			.appliedAt(LocalDateTime.now())
			.build();
		instruct = mongoTemplate.insert(instruct, properties.COLLECTION.INSTRUCT);
		return findByMongo(instruct);
	}

	@Transactional(rollbackFor = Exception.class)
	public List<Instruct> pass(String uid, InstructPassParam param) {
		Query query = Query.query(
			Criteria.where(InstructMongo.FIELD._ID).in(param.getIds())
		);
		Update update = Update.update(InstructMongo.FIELD.AUDIT_STATE, AuditState.Pass)
			.set(InstructMongo.FIELD.AUDITED_AT, LocalDateTime.now())
			.set(InstructMongo.FIELD.PASSED_AT, LocalDateTime.now())
			.set(InstructMongo.FIELD.AUDITED_UID, uid)
			.set(InstructMongo.FIELD.REMARK, param.getRemark());
		UpdateResult updateResult = mongoTemplate.updateMulti(query, update, properties.COLLECTION.INSTRUCT);
		log.debug("[project authority][pass] result-> {}", updateResult);
		List<InstructMongo> contents = mongoTemplate.find(query, InstructMongo.class, properties.COLLECTION.INSTRUCT);
		return findByMongo(contents);
	}

	@Transactional(rollbackFor = Exception.class)
	public List<Instruct> reject(String uid, InstructRejectParam param) {
		Query query = Query.query(
			Criteria.where(InstructMongo.FIELD._ID).in(param.getIds())
		);
		Update update = Update.update(InstructMongo.FIELD.AUDIT_STATE, AuditState.Reject)
			.set(InstructMongo.FIELD.AUDITED_AT, LocalDateTime.now())
			.set(InstructMongo.FIELD.REJECTED_AT, LocalDateTime.now())
			.set(InstructMongo.FIELD.AUDITED_UID, uid)
			.set(InstructMongo.FIELD.REMARK, param.getRemark());
		UpdateResult updateResult = mongoTemplate.updateMulti(query, update, properties.COLLECTION.INSTRUCT);
		log.debug("[project authority][pass] result-> {}", updateResult);
		List<InstructMongo> contents = mongoTemplate.find(query, InstructMongo.class, properties.COLLECTION.INSTRUCT);
		return findByMongo(contents);
	}

	public List<Instruct> find(InstructFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria).with(defaultSort());
		List<InstructMongo> contents = mongoTemplate.find(query, InstructMongo.class, properties.COLLECTION.INSTRUCT);

		return findByMongo(contents);
	}

	public Page<Instruct> findPage(InstructFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria);

		long total = mongoTemplate.count(query, InstructMongo.class, properties.COLLECTION.INSTRUCT);
		query.with(param.pageable()).with(defaultSort());
		List<InstructMongo> contents = mongoTemplate.find(query, InstructMongo.class, properties.COLLECTION.INSTRUCT);
		List<Instruct> instructs = findByMongo(contents);
		return new Page<>(param, instructs, total);
	}

	private Criteria buildCriteria(InstructFindParam param) {
		Criteria criteria = new Criteria();
		Optional.ofNullable(param.getIds()).filter(x -> !x.isEmpty()).ifPresent(ids -> criteria.and(InstructMongo.FIELD._ID).in(ids));
		Optional.ofNullable(param.getKeyword()).ifPresent(keyword -> criteria.and(InstructMongo.FIELD._ID).regex(keyword));
		Optional.ofNullable(param.getProjects()).filter(x -> !x.isEmpty()).ifPresent(projects -> criteria.and(InstructMongo.FIELD.PROJECT).in(projects));
		Optional.ofNullable(param.getDesignateAuditUsers()).filter(x -> !x.isEmpty()).ifPresent(users -> criteria.and(InstructMongo.FIELD.DESIGNATE_AUDIT_USER).in(users));
		return criteria;
	}

	private Sort defaultSort() {
		return Sort.by(
			Sort.Order.desc(InstructMongo.FIELD.METADATA.SORT),
			Sort.Order.desc(InstructMongo.FIELD.METADATA.LAST_MODIFIED.AT),
			Sort.Order.desc(InstructMongo.FIELD.METADATA.CREATED.AT),
			Sort.Order.desc(InstructMongo.FIELD._ID)
		);
	}

	private List<Instruct> findByMongo(List<InstructMongo> instructs) {
		Map<String, User> userMap = Optional.of(instructs.stream()
			.flatMap(i -> Stream.of(i.getAuditedUid(), i.getDesignateAuditUser()))
			.collect(Collectors.toSet()))
			.map(userClientCredentialsClient::findMap)
			.orElse(Collections.emptyMap());

		Map<String, File> fileMap = Optional.of(instructs.stream()
			.filter(x -> x.getFiles() != null)
			.flatMap(x -> x.getFiles().stream())
			.collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.map(x -> FileFindParam.builder().ids(x).build())
			.map(fileClientCredentialsClient::findFile)
			.map(y -> y.stream().collect(Collectors.toMap(File::getId, x -> x)))
			.orElse(Collections.emptyMap());

		Map<String, Project> projectMap = Optional.of(instructs.stream()
			.map(InstructMongo::getProject)
			.filter(Objects::nonNull)
			.collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.map(projectService::findMap)
			.orElse(Collections.emptyMap());

		return instructs.stream()
			.map(i -> {
				List<File> files = Optional.ofNullable(i.getFiles()).orElse(Collections.emptyList()).stream().map(fileMap::get).filter(Objects::nonNull).collect(Collectors.toList());
				Project project = projectMap.get(i.getProject());
				User designateAuditUser = userMap.get(i.getDesignateAuditUser());
				User auditedUser = userMap.get(i.getAuditedUid());
				return InstructConverter.mongo(i, files, project, designateAuditUser, auditedUser);
			})
			.collect(Collectors.toList());
	}

	private Instruct findByMongo(InstructMongo instruct) {
		Map<String, User> userMap = Optional.of(Stream.of(instruct.getAuditedUid(), instruct.getDesignateAuditUser())
			.filter(Objects::nonNull)
			.collect(Collectors.toList()))
			.map(userClientCredentialsClient::findMap)
			.orElse(Collections.emptyMap());
		Map<String, Project> projectMap = projectService.findMap(Collections.singleton(instruct.getProject()));
		List<File> files = Optional.of(Optional.ofNullable(instruct.getFiles()).orElse(Collections.emptyList()))
			.map(ids ->
				fileClientCredentialsClient.findFile(FileFindParam.builder().ids(new HashSet<>(ids)).build()).stream()
					.filter(x -> ids.contains(x.getId()))
					.collect(Collectors.toList())
			)
			.orElse(Collections.emptyList());

		return InstructConverter.mongo(instruct, files, projectMap.get(instruct.getProject()), userMap.get(instruct.getDesignateAuditUser()), userMap.get(instruct.getAuditedUid()));
	}
}
