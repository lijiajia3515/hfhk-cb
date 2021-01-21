package com.hfhk.cb.service.modules.project.authority.audit;

import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.auth.domain.user.User;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.project.authority.*;
import com.hfhk.cb.audit.*;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.modules.project.ProjectService;
import com.hfhk.cb.service.mongo.ProjectAuthorityAuditMongo;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectAuthorityAuditService {
	private final HfhkMongoProperties mongoProperties;
	private final MongoTemplate mongoTemplate;
	private final ProjectService projectService;
	private final UserClientCredentialsClient userClientCredentialsClient;

	public ProjectAuthorityAuditService(HfhkMongoProperties mongoProperties, MongoTemplate mongoTemplate, ProjectService projectService, UserClientCredentialsClient userClientCredentialsClient) {
		this.mongoProperties = mongoProperties;
		this.mongoTemplate = mongoTemplate;
		this.projectService = projectService;
		this.userClientCredentialsClient = userClientCredentialsClient;
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectAuthorityAudit apply(@NotNull String uid, @Validated ProjectAuthorityAuditApplyParam param) {
		ProjectAuthorityAuditMongo mongo = ProjectAuthorityAuditMongo.builder()
			.applyUser(uid)
			.unit(param.getUnit())
			.projects(param.getProjects())
			.appliedAt(LocalDateTime.now())
			.auditState(AuditState.Submit)
			.build();

		ProjectAuthorityAuditMongo insert = mongoTemplate.insert(mongo, mongoProperties.COLLECTION.INSTRUCT);
		log.debug("[project authority audit][apply] result-> {}", insert);

		return build(mongo);
	}

	@Transactional(rollbackFor = Exception.class)
	public List<ProjectAuthorityAudit> pass(@NotNull String uid, @Validated ProjectAuthorityAuditPassParam param) {
		Criteria criteria = Criteria.where(ProjectAuthorityAuditMongo.FIELD._ID).in(param.getIds());

		Query query = Query.query(criteria);
		LocalDateTime now = LocalDateTime.now();
		Update update = new Update()
			.set(ProjectAuthorityAuditMongo.FIELD.AUDITED_AT, now)
			.set(ProjectAuthorityAuditMongo.FIELD.PASSED_AT, now)
			.set(ProjectAuthorityAuditMongo.FIELD.AUDIT_STATE, AuditState.Pass)
			.set(ProjectAuthorityAuditMongo.FIELD.AUDITED_UID, uid);

		UpdateResult updateResult = mongoTemplate.updateMulti(query, update, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.INSTRUCT);
		log.debug("[project authority audit][pass] result-> {}", updateResult);
		List<ProjectAuthorityAuditMongo> contents = mongoTemplate.find(query, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.PROJECT_AUTHORITY_AUDIT);

		return build(contents);
	}

	@Transactional(rollbackFor = Exception.class)
	public List<ProjectAuthorityAudit> reject(@NotNull String uid, @Validated ProjectAuthorityAuditRejectParam param) {
		Criteria criteria = Criteria.where(ProjectAuthorityAuditMongo.FIELD._ID).in(param.getIds());

		Query query = Query.query(criteria);
		LocalDateTime now = LocalDateTime.now();
		Update update = new Update()
			.set(ProjectAuthorityAuditMongo.FIELD.AUDITED_AT, now)
			.set(ProjectAuthorityAuditMongo.FIELD.REJECTED_AT, now)
			.set(ProjectAuthorityAuditMongo.FIELD.AUDIT_STATE, AuditState.Reject)
			.set(ProjectAuthorityAuditMongo.FIELD.AUDITED_UID, uid);
		UpdateResult updateResult = mongoTemplate.updateMulti(query, update, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.INSTRUCT);
		log.debug("[project authority audit][reject] result-> {}", updateResult);
		List<ProjectAuthorityAuditMongo> contents = mongoTemplate.find(query, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.PROJECT_AUTHORITY_AUDIT);
		return build(contents);
	}

	public List<ProjectAuthorityAudit> find(@Validated ProjectAuthorityAuditFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria);
		List<ProjectAuthorityAuditMongo> contents = mongoTemplate.find(query, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.INSTRUCT);
		return build(contents);
	}

	public Page<ProjectAuthorityAudit> findPage(@Validated ProjectAuthorityAuditFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria);
		long total = mongoTemplate.count(query, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.INSTRUCT);
		query.with(param.pageable()).with(defaultSort());

		List<ProjectAuthorityAuditMongo> audits = mongoTemplate.find(query, ProjectAuthorityAuditMongo.class, mongoProperties.COLLECTION.INSTRUCT);
		List<ProjectAuthorityAudit> contents = build(audits);
		return new Page<>(param, contents, total);
	}

	public Sort defaultSort() {
		return Sort.by(
			Sort.Order.desc(ProjectAuthorityAuditMongo.FIELD.METADATA.SORT),
			Sort.Order.desc(ProjectAuthorityAuditMongo.FIELD.APPLIED_AT),
			Sort.Order.desc(ProjectAuthorityAuditMongo.FIELD.METADATA.CREATED.AT),
			Sort.Order.desc(ProjectAuthorityAuditMongo.FIELD._ID)
		);
	}

	private ProjectAuthorityAudit build(ProjectAuthorityAuditMongo mongo) {
		return build(Collections.singleton(mongo)).stream().findFirst().orElseThrow();
	}

	private List<ProjectAuthorityAudit> build(Collection<ProjectAuthorityAuditMongo> audits) {
		Map<String, User> userMap = userClientCredentialsClient.findMap(audits.stream().map(ProjectAuthorityAuditMongo::getApplyUser).collect(Collectors.toSet()));
		Map<String, Project> projectMap = projectService.findMap(audits.stream().map(ProjectAuthorityAuditMongo::getProjects).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet()));
		return audits.stream()
			.map(x -> ProjectAuthorityAuditConverter.convert(x,
				userMap.get(x.getApplyUser()),
				Optional.ofNullable(x.getProjects()).orElse(Collections.emptySet()).stream().map(projectMap::get).filter(Objects::nonNull).collect(Collectors.toList())))
			.collect(Collectors.toList());
	}

	private Criteria buildCriteria(@Validated ProjectAuthorityAuditFindParam param) {
		Criteria criteria = new Criteria();
		Optional.ofNullable(param.getIds()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectAuthorityAuditMongo.FIELD._ID).in(x));
		Optional.ofNullable(param.getProjects()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectAuthorityAuditMongo.FIELD.PROJECTS).in(x));
		Optional.ofNullable(param.getUsers()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectAuthorityAuditMongo.FIELD.USER).in(x));
		Optional.ofNullable(param.getStates()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectAuthorityAuditMongo.FIELD.AUDIT_STATE).in(x));
		return criteria;
	}
}
