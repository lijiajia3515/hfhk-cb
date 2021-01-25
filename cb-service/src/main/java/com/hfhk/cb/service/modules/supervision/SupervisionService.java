package com.hfhk.cb.service.modules.supervision;

import com.hfhk.auth.Metadata;
import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.auth.modules.user.User;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.modules.project.ProjectService;
import com.hfhk.cb.service.mongo.SupervisionMongo;
import com.hfhk.cb.supervision.Supervision;
import com.hfhk.cb.supervision.SupervisionDeleteParam;
import com.hfhk.cb.supervision.SupervisionFindParam;
import com.hfhk.cb.supervision.SupervisionSaveParam;
import com.hfhk.system.client.FileClientCredentialsClient;
import com.hfhk.system.file.File;
import com.hfhk.system.file.FileFindParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class SupervisionService {
	private final HfhkMongoProperties mongoProperties;

	private final MongoTemplate mongoTemplate;
	private final UserClientCredentialsClient userClientCredentialsClient;
	private final FileClientCredentialsClient fileClientCredentialsClient;
	private final ProjectService projectService;

	public SupervisionService(HfhkMongoProperties mongoProperties, MongoTemplate mongoTemplate, UserClientCredentialsClient userClientCredentialsClient, FileClientCredentialsClient fileClientCredentialsClient, ProjectService projectService) {
		this.mongoProperties = mongoProperties;
		this.userClientCredentialsClient = userClientCredentialsClient;
		this.fileClientCredentialsClient = fileClientCredentialsClient;
		this.mongoTemplate = mongoTemplate;
		this.projectService = projectService;
	}

	@Transactional(rollbackFor = Exception.class)
	public Supervision save(@Validated SupervisionSaveParam param) {
		SupervisionMongo supervisionMongo = SupervisionMongo.builder()
			.files(param.getFiles())
			.project(param.getProject())
			.unit(param.getUnit())
			.checks(param.getChecks())
			.problemNum(param.getProblemNum())
			.build();
		supervisionMongo = mongoTemplate.insert(supervisionMongo, mongoProperties.COLLECTION.SUPERVISION);
		return mapper(supervisionMongo);
	}

	@Transactional(rollbackFor = Exception.class)
	public List<Supervision> delete(@Validated SupervisionDeleteParam param) {
		Query query = Query.query(Criteria.where(SupervisionMongo.FIELD._ID).in(param.getIds()));
		List<SupervisionMongo> removeSupervisions = mongoTemplate.findAllAndRemove(query, SupervisionMongo.class, mongoProperties.COLLECTION.SUPERVISION);
		log.info("[supervision][delete]: -> {}", removeSupervisions);
		return mapper(removeSupervisions);
	}

	public List<Supervision> find(@Validated SupervisionFindParam param) {
		Criteria criteria = buildFindCriteria(param);
		Query query = Query.query(criteria).with(defaultSort());
		List<SupervisionMongo> contents = mongoTemplate.find(query, SupervisionMongo.class, mongoProperties.COLLECTION.SUPERVISION);
		return mapper(contents);
	}

	public Page<Supervision> findPage(@Validated SupervisionFindParam param) {
		Criteria criteria = buildFindCriteria(param);
		Query query = Query.query(criteria).with(defaultSort()).with(param.pageable());
		long total = mongoTemplate.count(query, SupervisionMongo.class, mongoProperties.COLLECTION.SUPERVISION);
		List<SupervisionMongo> contents = mongoTemplate.find(query, SupervisionMongo.class, mongoProperties.COLLECTION.SUPERVISION);
		return new Page<>(param, this.mapper(contents), total);
	}

	public Map<String, Supervision> findByMap(@NotNull Set<String> ids) {
		return find(SupervisionFindParam.builder().ids(ids).build()).stream()
			.collect(Collectors.toMap(Supervision::getId, x -> x));
	}

	public Sort defaultSort() {
		return Sort.by(
			Sort.Order.desc(SupervisionMongo.FIELD.METADATA.LAST_MODIFIED.AT),
			Sort.Order.desc(SupervisionMongo.FIELD.METADATA.CREATED.AT),
			Sort.Order.desc(SupervisionMongo.FIELD._ID)
		);
	}

	public Criteria buildFindCriteria(@Validated SupervisionFindParam param) {
		Criteria criteria = new Criteria();
		Optional.ofNullable(param.getIds()).filter(x -> !x.isEmpty()).ifPresent(p -> criteria.and(SupervisionMongo.FIELD._ID).in(p));
		Optional.ofNullable(param.getProjects())
			.filter(x -> !x.isEmpty())
			.flatMap(p -> Optional.of(p.stream()
				.filter(x -> x.getId() != null)
				.map(p1 -> {
					Criteria criteria1 = Criteria.where(SupervisionMongo.FIELD.PROJECT).is(p1.getId());
					Optional.ofNullable(p1.getUnit()).filter(x -> !x.isEmpty()).ifPresent(p0Unit -> criteria1.and(SupervisionMongo.FIELD.UNIT).regex(p0Unit));
					return criteria1;
				})
				.toArray(Criteria[]::new))
				.filter(x -> x.length > 0))
			.ifPresent(criteria::orOperator);
		Optional.ofNullable(param.getCreated())
			.filter(x -> !x.isEmpty())
			.ifPresent(p -> criteria.and(SupervisionMongo.FIELD.METADATA.CREATED.UID).in(p));
		return criteria;
	}

	private List<Supervision> mapper(@NotNull List<SupervisionMongo> supervisions) {
		Map<String, User> users = Optional.of(supervisions.stream()
			.flatMap(x -> Stream.of(x.getMetadata().getCreated().getUid(), x.getMetadata().getLastModified().getUid()))
			.filter(Objects::nonNull)
			.collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.map(userClientCredentialsClient::findMap)
			.orElse(Collections.emptyMap());

		Map<String, File> files = Optional.of(supervisions.stream()
			.filter(x -> x.getFiles() != null)
			.flatMap(x -> x.getFiles().stream())
			.filter(Objects::nonNull)
			.collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.map(x -> FileFindParam.builder().ids(x).build())
			.map(fileClientCredentialsClient::findFile)
			.map(x -> x.stream().collect(Collectors.toMap(File::getId, y -> y)))
			.orElse(Collections.emptyMap());
		Map<String, Project> projectMap = Optional.of(supervisions.stream().map(SupervisionMongo::getProject).collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.map(projectService::findMap)
			.orElse(Collections.emptyMap());

		return supervisions.stream().map(s -> mapper(s, projectMap, files, users)).collect(Collectors.toList());
	}

	private static Supervision mapper(@NotNull SupervisionMongo mongo, @NotNull Map<String, Project> projects, @NotNull Map<String, File> files, @NotNull Map<String, User> users) {
		return Supervision.builder()
			.id(mongo.getId())
			.files(Optional.ofNullable(mongo.getFiles()).stream().flatMap(Collection::stream).map(files::get).filter(Objects::nonNull).collect(Collectors.toList()))
			.project(projects.get(mongo.getProject()))
			.unit(mongo.getUnit())
			.problemNumber(mongo.getProblemNum())
			.metadata(
				Metadata.builder()
					.created(Metadata.Action.builder()
						.user(users.get(mongo.getMetadata().getCreated().getUid()))
						.at(mongo.getMetadata().getCreated().getAt())
						.build())
					.lastModified(Metadata.Action.builder()
						.user(users.get(mongo.getMetadata().getLastModified().getUid()))
						.at(mongo.getMetadata().getLastModified().getAt())
						.build())
					.build()
			)
			.build();
	}

	private Supervision mapper(@NotNull SupervisionMongo mongo) {
		return mapper(Collections.singletonList(mongo)).stream().findFirst().orElse(null);
	}


}
