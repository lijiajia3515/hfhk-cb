package com.hfhk.cb.service.modules.instruct;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cb.instruct.Instruct;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.service.mongo.InstructMongo;
import com.hfhk.system.file.File;

import java.util.List;
import java.util.Optional;

public class InstructConverter {

	public static Instruct mongo(InstructMongo mongo, List<File> files, Project project, User designateAuditUser, User auditedUser) {
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
			.build();
	}
}
