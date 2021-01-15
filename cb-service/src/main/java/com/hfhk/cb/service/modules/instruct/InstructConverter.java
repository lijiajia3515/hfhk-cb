package com.hfhk.cb.service.modules.instruct;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cb.instruct.Instruct;
import com.hfhk.cb.service.mongo.InstructMongo;
import com.hfhk.system.file.File;

import java.util.List;

public class InstructConverter {

	public static Instruct mongo(InstructMongo mongo, List<File> files, User designateAuditUser, User auditedUser) {
		return Instruct.builder()
			.id(mongo.getId())
			.type(mongo.getType())
			.files(files)
			.project(mongo.getProject())
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
