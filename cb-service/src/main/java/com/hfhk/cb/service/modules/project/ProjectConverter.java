package com.hfhk.cb.service.modules.project;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.service.mongo.ProjectMongo;
import com.hfhk.system.dictionary.domain.Dictionary;

import java.util.Optional;

public class ProjectConverter {
	public static Project projectMapper(ProjectMongo mongo,
										Dictionary.Item park, Dictionary.Item type, Dictionary.Item schedule,
										User supervisionLeader) {
		return Project.builder()
			.id(mongo.getId())
			.name(mongo.getName())
			.type(Optional.ofNullable(type).orElse(Dictionary.Item.builder().id(mongo.getType()).build()))
			.park(Optional.ofNullable(park).orElse(Dictionary.Item.builder().id(mongo.getPark()).build()))
			.area(mongo.getArea())
			.price(mongo.getPrice())
			.address(mongo.getAddress())
			.ownerUnit(UnitConverter.unitMapper(mongo.getOwnerUnit()))
			.supervisionUnits(UnitConverter.unitMapper(mongo.getSupervisionUnits()))
			.constructionUnits(UnitConverter.unitMapper(mongo.getConstructionUnits()))
			.surveyUnits(UnitConverter.unitMapper(mongo.getSurveyUnits()))
			.designUnits(UnitConverter.unitMapper(mongo.getDesignUnits()))
			.qualityUnit(UnitConverter.unitMapper(mongo.getQualityUnit()))
			.constructionStartAt(mongo.getConstructionStartAt())
			.constructionEndAt(mongo.getConstructionEndAt())
			.supervisionLeader(Optional.ofNullable(supervisionLeader).orElse(User.builder().uid(mongo.getSupervisionLeader()).build()))
			.supervisionStartAt(mongo.getSupervisionStartAt())
			.schedule(Optional.ofNullable(schedule).orElse(Dictionary.Item.builder().id(mongo.getSchedule()).build()))
			.remark(mongo.getRemark())
			.build();
	}
}
