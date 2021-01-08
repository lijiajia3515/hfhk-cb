package com.hfhk.cb.service.modules.project;

import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.auth.domain.user.User;
import com.hfhk.auth.domain.user.UserFindParam;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cb.CbDictionary;
import com.hfhk.cb.project.*;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.mongo.ProjectMongo;
import com.hfhk.cb.unit.UnitType;
import com.hfhk.system.client.DictionaryClientCredentialsClient;
import com.hfhk.system.dictionary.Dictionary;
import com.hfhk.system.dictionary.DictionaryFindParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
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
public class ProjectService {
	private final HfhkMongoProperties mongoProperties;
	private final MongoTemplate mongoTemplate;
	private final UserClientCredentialsClient userClient;
	private final DictionaryClientCredentialsClient dictionaryClient;

	public ProjectService(HfhkMongoProperties mongoProperties, MongoTemplate mongoTemplate, UserClientCredentialsClient userClient, DictionaryClientCredentialsClient dictionaryClient) {
		this.mongoProperties = mongoProperties;
		this.mongoTemplate = mongoTemplate;
		this.userClient = userClient;
		this.dictionaryClient = dictionaryClient;
	}

	/**
	 * 项目 保存
	 *
	 * @param param param
	 * @return 项目
	 */
	@Transactional(rollbackFor = Exception.class)
	public Project save(@Validated ProjectSaveParam param) {
		ProjectMongo mongo = ProjectMongo.builder()
			.name(param.getName())
			.type(param.getType())
			.area(param.getArea())
			.price(param.getPrice())
			.park(param.getPark())
			.address(param.getAddress())
			.ownerUnit(UnitConverter.mongoMapper(param.getOwnerUnit(), UnitType.Owner))
			.supervisionUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Supervision))
			.constructionUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Construction))
			.surveyUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Survey))
			.designUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Design))
			.qualityUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Quality))
			.constructionStartAt(param.getConstructionStartAt())
			.constructionEndAt(param.getConstructionEndAt())
			.supervisionLeader(param.getSupervisionLeader())
			.supervisionStartAt(param.getSupervisionStartAt())
			.schedule(param.getSchedule())
			.remark(param.getRemark())
			.build();
		ProjectMongo insert = mongoTemplate.insert(mongo, mongoProperties.COLLECTION.PROJECT);
		return findByMongo(insert);
	}

	@Transactional(rollbackFor = Exception.class)
	public Project modify(@Validated ProjectModifyParam param) {
		ProjectMongo mongo = ProjectMongo.builder()
			.id(param.getId())
			.name(param.getName())
			.type(param.getType())
			.area(param.getArea())
			.price(param.getPrice())
			.park(param.getPark())
			.address(param.getAddress())
			.ownerUnit(UnitConverter.mongoMapper(param.getOwnerUnit(), UnitType.Owner))
			.supervisionUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Supervision))
			.constructionUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Construction))
			.surveyUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Survey))
			.designUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Design))
			.qualityUnits(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Quality))
			.constructionStartAt(param.getConstructionStartAt())
			.constructionEndAt(param.getConstructionEndAt())
			.supervisionLeader(param.getSupervisionLeader())
			.supervisionStartAt(param.getSupervisionStartAt())
			.schedule(param.getSchedule())
			.remark(param.getRemark())
			.build();
		ProjectMongo modify = mongoTemplate.save(mongo, mongoProperties.COLLECTION.PROJECT);
		return findByMongo(modify);
	}

	public List<Project> delete(@Validated ProjectDeleteParam param) {
		Criteria criteria = Criteria.where(ProjectMongo.FIELD._ID).in(param.getIds());
		Query query = Query.query(criteria);

		List<ProjectMongo> contents = mongoTemplate.findAllAndRemove(query, ProjectMongo.class, mongoProperties.COLLECTION.PROJECT);
		return findByMongo(contents);
	}

	public List<Project> find(@Validated ProjectFindParam param) {
		Criteria criteria = buildCriteria(param);
		Query query = Query.query(criteria).with(defaultSort());
		List<ProjectMongo> contents = mongoTemplate.find(query, ProjectMongo.class, mongoProperties.COLLECTION.PROJECT);
		return findByMongo(contents);
	}

	public Page<Project> findPage(@Validated ProjectFindParam param) {
		Criteria criteria = buildCriteria(param);

		Query query = Query.query(criteria);
		long total = mongoTemplate.count(query, ProjectMongo.class, mongoProperties.COLLECTION.PROJECT);

		query.with(param.pageable()).with(defaultSort());
		List<ProjectMongo> contents = mongoTemplate.find(query, ProjectMongo.class, mongoProperties.COLLECTION.PROJECT);
		List<Project> projects = findByMongo(contents);
		return new Page<>(param, projects, total);
	}

	public Map<String, Project> findMap(Set<String> ids) {
		return Optional.ofNullable(ids)
			.filter(x -> !x.isEmpty())
			.map(x -> ProjectFindParam.builder().ids(ids).build())
			.map(this::find)
			.stream()
			.flatMap(Collection::stream)
			.collect(Collectors.toMap(Project::getId, x -> x));
	}

	public Criteria buildCriteria(@Validated ProjectFindParam param) {
		Criteria criteria = new Criteria();
		Optional.ofNullable(param.getIds()).filter(ids -> !ids.isEmpty()).ifPresent(x -> criteria.and(ProjectMongo.FIELD._ID).in(x));
		Optional.ofNullable(param.getTypes()).filter(types -> !types.isEmpty()).ifPresent(x -> criteria.and(ProjectMongo.FIELD.TYPE).in(x));
		Optional.ofNullable(param.getParks()).filter(types -> !types.isEmpty()).ifPresent(x -> criteria.and(ProjectMongo.FIELD.PARK).in(x));
		Optional.ofNullable(param.getSchedules()).filter(schedules -> !schedules.isEmpty()).ifPresent(x -> criteria.and(ProjectMongo.FIELD.SCHEDULE).in(x));
		Optional.ofNullable(param.getSupervisionLeaders()).filter(leaders -> !leaders.isEmpty()).ifPresent(x -> criteria.and(ProjectMongo.FIELD.SUPERVISION_LEADER).in(x));
		return criteria;
	}

	public Sort defaultSort() {
		return Sort.by(
			Sort.Order.asc(ProjectMongo.FIELD.METADATA.SORT),
			Sort.Order.asc(ProjectMongo.FIELD.METADATA.CREATED.AT),
			Sort.Order.asc(ProjectMongo.FIELD._ID)
		);
	}

	public List<Project> findByMongo(List<ProjectMongo> projects) {
		DictionaryFindParam dictionaryFindParam = DictionaryFindParam.builder()
			.items(
				Stream.of(
					DictionaryFindParam.Dictionary.builder()
						.id(CbDictionary.ProjectType.ID)
						.itemValues(projects.stream().map(ProjectMongo::getType).filter(Objects::nonNull).collect(Collectors.toSet()))
						.build(),
					DictionaryFindParam.Dictionary.builder()
						.id(CbDictionary.Park.ID)
						.itemValues(projects.stream().map(ProjectMongo::getPark).filter(Objects::nonNull).collect(Collectors.toSet()))
						.build(),
					DictionaryFindParam.Dictionary.builder()
						.id(CbDictionary.ConstructionSchedule.ID)
						.itemValues(projects.stream().map(ProjectMongo::getSchedule).filter(Objects::nonNull).collect(Collectors.toSet()))
						.build()
				)
					.filter(x -> !x.getItemValues().isEmpty())
					.collect(Collectors.toSet())
			).build();
		List<Dictionary> dictionaries = dictionaryClient.find(dictionaryFindParam);
		Map<Object, Dictionary.Item> typeDictionaryMap = dictionaries.stream()
			.filter(x -> CbDictionary.ProjectType.ID.equals(x.getId())).findFirst()
			.map(Dictionary::getItems).orElse(Collections.emptyList())
			.stream().collect(Collectors.toMap(Dictionary.Item::getValue, x -> x));

		Map<Object, Dictionary.Item> parkDictionaryMap = dictionaries.stream()
			.filter(x -> CbDictionary.Park.ID.equals(x.getId())).findFirst()
			.map(Dictionary::getItems).orElse(Collections.emptyList())
			.stream().collect(Collectors.toMap(Dictionary.Item::getValue, x -> x));

		Map<Object, Dictionary.Item> scheduleDictionaryMap = dictionaries.stream()
			.filter(x -> CbDictionary.ConstructionSchedule.ID.equals(x.getId())).findFirst()
			.map(Dictionary::getItems).orElse(Collections.emptyList())
			.stream().collect(Collectors.toMap(Dictionary.Item::getValue, x -> x));
		Map<String, User> users = Optional.of(projects.stream().map(ProjectMongo::getSupervisionLeader).filter(Objects::nonNull).collect(Collectors.toSet()))
			.filter(x -> !x.isEmpty())
			.map(x -> userClient.find(UserFindParam.builder().uids(x).build()))
			.stream().flatMap(Collection::stream).collect(Collectors.toMap(User::getUid, x -> x));
		return projects.stream()
			.map(p -> ProjectConverter.projectMapper(
				p,
				typeDictionaryMap.get(p.getType()),
				parkDictionaryMap.get(p.getPark()),
				scheduleDictionaryMap.get(p.getSchedule()),
				users.get(p.getSupervisionLeader()))
			)
			.collect(Collectors.toList());
	}

	public Project findByMongo(@NotNull ProjectMongo project) {
		DictionaryFindParam dictionaryFindParam = DictionaryFindParam.builder()
			.items(
				Stream.of(
					Optional.ofNullable(project.getType())
						.map(type -> DictionaryFindParam.Dictionary.builder()
							.id(CbDictionary.ProjectType.ID)
							.itemValues(Collections.singleton(type))
							.build())
						.orElse(null),
					Optional.ofNullable(project.getPark())
						.map(park -> DictionaryFindParam.Dictionary.builder()
							.id(CbDictionary.Park.ID)
							.itemValues(Collections.singleton(park))
							.build())
						.orElse(null),
					Optional.ofNullable(project.getSchedule())
						.map(schedule -> DictionaryFindParam.Dictionary.builder()
							.id(CbDictionary.ConstructionSchedule.ID)
							.itemValues(Collections.singleton(schedule))
							.build())
						.orElse(null)
				)
					.filter(Objects::nonNull)
					.collect(Collectors.toSet())
			).build();
		List<Dictionary> dictionaries = dictionaryClient.find(dictionaryFindParam);
		Dictionary.Item type = Optional.ofNullable(project.getType())
			.flatMap(xType -> dictionaries.stream().filter(Objects::nonNull)
				.filter(x -> CbDictionary.ProjectType.ID.equals(x.getId()))
				.findFirst()
				.map(Dictionary::getItems)
				.filter(x -> !x.isEmpty())
				.flatMap(x -> x.stream().filter(y -> xType.equals(y.getValue())).findFirst())
				.stream().findFirst()
			)
			.orElse(null);

		Dictionary.Item park = Optional.ofNullable(project.getPark())
			.flatMap(xPark -> dictionaries.stream().filter(Objects::nonNull)
				.filter(x -> CbDictionary.Park.ID.equals(x.getId()))
				.findFirst()
				.map(Dictionary::getItems)
				.filter(x -> !x.isEmpty())
				.flatMap(x -> x.stream().filter(y -> xPark.equals(y.getValue())).findFirst())
				.stream().findFirst()
			)
			.orElse(null);

		Dictionary.Item schedule = Optional.ofNullable(project.getSchedule())
			.flatMap(xSchedule -> dictionaries.stream().filter(Objects::nonNull)
				.filter(x -> CbDictionary.ConstructionSchedule.ID.equals(x.getId()))
				.findFirst()
				.map(Dictionary::getItems)
				.filter(x -> !x.isEmpty())
				.flatMap(x -> x.stream().filter(y -> xSchedule.equals(y.getValue())).findFirst())
				.stream().findFirst()
			)
			.orElse(null);
		User supervisionLeader = userClient.findById(project.getSupervisionLeader());
		return ProjectConverter.projectMapper(project, park, type, schedule, supervisionLeader);
	}

}
