package com.hfhk.cb.service.modules.instruct;

import com.hfhk.auth.Metadata;
import com.hfhk.auth.modules.user.User;
import com.hfhk.cb.instruct.Instruct;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.service.mongo.InstructMongo;
import com.hfhk.system.modules.file.File;

import java.util.*;
import java.util.stream.Collectors;

public class InstructConverter {

	public static Instruct mongo(InstructMongo mongo, Map<String, File> fileMap, Map<String, Project> projectMap, Map<String, User> userMap, Metadata metadata) {
		return Instruct.builder()
			.id(mongo.getId())
			.type(mongo.getType())
			.files(Optional.ofNullable(mongo.getFiles()).orElse(Collections.emptyList()).stream().map(fileMap::get).filter(Objects::nonNull).collect(Collectors.toList()))
			.project(Optional.ofNullable(mongo.getProject()).map(projectMap::get).orElse(Project.builder().id(mongo.getId()).build()))
			.unit(mongo.getUnit())
			.designateAuditUser(Optional.ofNullable(mongo.getDesignateAuditUser()).map(userMap::get).orElse(User.builder().uid(mongo.getDesignateAuditUser()).build()))
			.auditState(mongo.getAuditState())
			.auditedUser(Optional.ofNullable(mongo.getAuditedUid()).map(userMap::get).orElse(User.builder().uid(mongo.getAuditedUid()).build()))
			.auditedAt(mongo.getAuditedAt())
			.passedAt(mongo.getPassedAt())
			.rejectedAt(mongo.getRejectedAt())
			.remark(mongo.getRemark())
			.metadata(metadata)
			.build();
	}


	public static Instruct mongo(InstructMongo mongo, List<File> files, Project project, User designateAuditUser, User auditedUser, Metadata metadata) {
		return Instruct.builder()
			.id(mongo.getId())
			.type(mongo.getType())
			.files(files)
			.project(Optional.ofNullable(project).orElse(Project.builder().id(mongo.getProject()).build()))
			.unit(mongo.getUnit())
			.designateAuditUser(designateAuditUser)
			.auditState(mongo.getAuditState())
			.auditedUser(auditedUser)
			.auditedAt(mongo.getAuditedAt())
			.passedAt(mongo.getPassedAt())
			.rejectedAt(mongo.getRejectedAt())
			.remark(mongo.getRemark())
			.metadata(metadata)
			.build();
	}
}
