package com.hfhk.cb.service.modules.project;

import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.project.ProjectSaveParm;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.mongo.ProjectMongo;
import com.hfhk.cb.unit.UnitType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
public class ProjectService {
	private final HfhkMongoProperties mongoProperties;
	private final MongoTemplate mongoTemplate;
	private final UserClientCredentialsClient userClient;

	public ProjectService(HfhkMongoProperties mongoProperties, MongoTemplate mongoTemplate, UserClientCredentialsClient userClient) {
		this.mongoProperties = mongoProperties;
		this.mongoTemplate = mongoTemplate;
		this.userClient = userClient;
	}

	@Transactional(rollbackFor = Exception.class)
	public Project save(@Validated ProjectSaveParm param) {
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
			.qualityUnit(UnitConverter.mongoMapper(param.getConstructionUnits(), UnitType.Quality))
			.constructionStartAt(param.getConstructionStartAt())
			.constructionEndAt(param.getConstructionEndAt())
			.supervisionLeader(param.getSupervisionLeader())
			.supervisionStartAt(param.getSupervisionStartAt())
			.schedule(param.getSchedule())
			.remark(param.getRemark())
			.build();
		ProjectMongo insert = mongoTemplate.insert(mongo, mongoProperties.COLLECTION.PROJECT);
		return null;
	}

	public Optional<Project> findById(@NotNull String id) {
		Criteria criteria = Criteria.where(ProjectMongo.FIELD._ID).is(id);
		Query query = Query.query(criteria);
	}

	public Optional<Project> findByMongo(@NotNull ProjectMongo project){

	}

}
