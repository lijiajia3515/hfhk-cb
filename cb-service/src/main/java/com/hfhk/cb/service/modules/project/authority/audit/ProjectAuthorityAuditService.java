package com.hfhk.cb.service.modules.project.authority.audit;

import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.auth.domain.user.User;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.project.authority.*;
import com.hfhk.cb.audit.*;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.modules.project.ProjectService;
import com.hfhk.cb.service.mongo.ProjectReviewMongo;
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
		ProjectReviewMongo mongo = ProjectReviewMongo.builder()
			.applyUser(uid)
			.unit(param.getUnit())
			.projects(param.getProjects())
			.applyAt(LocalDateTime.now())
			.state(AuditState.Submit)
			.build();

		ProjectReviewMongo insert = mongoTemplate.insert(mongo, mongoProperties.COLLECTION.PROJECT_REVIEW);
		log.debug("[project review][insert] result-> {}", insert);

		return build(mongo);
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectAuthorityAudit pass(@NotNull String uid, @Validated ProjectAuthorityAuditPassParam param) {
		Criteria criteria = Criteria.where(ProjectReviewMongo.FIELD._ID).in(param.getIds());

		Query query = Query.query(criteria);
		LocalDateTime now = LocalDateTime.now();
		Update update = new Update()
			.set(ProjectReviewMongo.FIELD.REVIEWED_AT, now)
			.set(ProjectReviewMongo.FIELD.PASSED_AT, now)
			.set(ProjectReviewMongo.FIELD.STATE, AuditState.Pass)
			.set(ProjectReviewMongo.FIELD.REVIEWED_USER, uid);

		ProjectReviewMongo contents = mongoTemplate.findAndModify(query, update, ProjectReviewMongo.class, mongoProperties.COLLECTION.PROJECT_REVIEW);
		log.debug("[project review][pass] result-> {}", contents);

		return build(contents);
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectAuthorityAudit reject(@NotNull String uid, @Validated ProjectAuthorityAuditRejectParam param) {
		Criteria criteria = Criteria.where(ProjectReviewMongo.FIELD._ID).in(param.getIds());

		Query query = Query.query(criteria);
		LocalDateTime now = LocalDateTime.now();
		Update update = new Update()
			.set(ProjectReviewMongo.FIELD.REVIEWED_AT, now)
			.set(ProjectReviewMongo.FIELD.REJECTED_AT, now)
			.set(ProjectReviewMongo.FIELD.STATE, AuditState.Reject)
			.set(ProjectReviewMongo.FIELD.REVIEWED_USER, uid);
		ProjectReviewMongo contents = mongoTemplate.findAndModify(query, update, ProjectReviewMongo.class, mongoProperties.COLLECTION.PROJECT_REVIEW);
		log.debug("[project review][reject] result-> {}", contents);

		return build(contents);
	}

	public List<ProjectAuthorityAudit> find(@Validated ProjectAuthorityAuditFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria);
		List<ProjectReviewMongo> contents = mongoTemplate.find(query, ProjectReviewMongo.class, mongoProperties.COLLECTION.PROJECT_REVIEW);
		return build(contents);
	}

	public Page<ProjectAuthorityAudit> findPage(@Validated ProjectAuthorityAuditFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria);
		long total = mongoTemplate.count(query, ProjectReviewMongo.class, mongoProperties.COLLECTION.PROJECT_REVIEW);
		query.with(param.pageable()).with(defaultSort());

		List<ProjectReviewMongo> contents = mongoTemplate.find(query, ProjectReviewMongo.class, mongoProperties.COLLECTION.PROJECT_REVIEW);
		List<ProjectAuthorityAudit> reviews = build(contents);
		return new Page<>(param, reviews, total);
	}

	public Sort defaultSort() {
		return Sort.by(
			Sort.Order.desc(ProjectReviewMongo.FIELD.METADATA.SORT),
			Sort.Order.desc(ProjectReviewMongo.FIELD.APPLY_AT),
			Sort.Order.desc(ProjectReviewMongo.FIELD.METADATA.CREATED.AT),
			Sort.Order.desc(ProjectReviewMongo.FIELD._ID)
		);
	}

	private ProjectAuthorityAudit build(ProjectReviewMongo mongo) {
		return build(Collections.singleton(mongo)).stream().findFirst().orElseThrow();
	}

	private List<ProjectAuthorityAudit> build(Collection<ProjectReviewMongo> reviews) {
		Map<String, User> userMap = userClientCredentialsClient.findMap(reviews.stream().map(ProjectReviewMongo::getApplyUser).collect(Collectors.toSet()));
		Map<String, Project> projectMap = projectService.findMap(reviews.stream().map(ProjectReviewMongo::getProjects).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet()));
		return reviews.stream()
			.map(x -> ProjectAuthorityAuditConverter.reviewMapper(x, userMap.get(x.getApplyUser()), x.getProjects().stream().map(projectMap::get).filter(Objects::nonNull).collect(Collectors.toList())))
			.collect(Collectors.toList());
	}

	private Criteria buildCriteria(@Validated ProjectAuthorityAuditFindParam param) {
		Criteria criteria = new Criteria();
		Optional.ofNullable(param.getIds()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectReviewMongo.FIELD._ID).in(x));
		Optional.ofNullable(param.getProjects()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectReviewMongo.FIELD.PROJECTS).in(x));
		Optional.ofNullable(param.getUsers()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectReviewMongo.FIELD.USER).in(x));
		Optional.ofNullable(param.getStates()).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and(ProjectReviewMongo.FIELD.STATE).in(x));
		return criteria;
	}
}
