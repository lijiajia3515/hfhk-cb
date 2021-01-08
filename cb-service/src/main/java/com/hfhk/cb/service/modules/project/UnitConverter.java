package com.hfhk.cb.service.modules.project;

import com.hfhk.cb.service.mongo.ProjectUnitMongo;
import com.hfhk.cb.unit.Unit;
import com.hfhk.cb.unit.UnitType;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UnitConverter {
	public static ProjectUnitMongo mongoMapper(Unit unit, UnitType type) {
		if (unit == null) return null;
		return ProjectUnitMongo.builder()
			.id(unit.getId())
			.type(Optional.ofNullable(type).orElse(unit.getType()))
			.name(unit.getName())
			.leaderName(unit.getLeaderName())
			.leaderPhoneNumber(unit.getLeaderPhoneNumber())
			.build();
	}

	public static List<ProjectUnitMongo> mongoMapper(List<Unit> units, UnitType type) {
		return Optional.ofNullable(units).orElse(Collections.emptyList()).stream().map(x -> UnitConverter.mongoMapper(x, type)).collect(Collectors.toList());
	}

	public static Unit unitMapper(ProjectUnitMongo unit) {
		if (unit == null) return null;
		return Unit.builder()
			.id(unit.getId())
			.type(unit.getType())
			.name(unit.getName())
			.leaderName(unit.getLeaderName())
			.leaderPhoneNumber(unit.getLeaderPhoneNumber())
			.build();
	}

	public static List<Unit> unitMapper(List<ProjectUnitMongo> units) {
		return Optional.ofNullable(units)
			.orElse(Collections.emptyList())
			.stream()
			.filter(Objects::nonNull)
			.map(UnitConverter::unitMapper)
			.collect(Collectors.toList());
	}


}
