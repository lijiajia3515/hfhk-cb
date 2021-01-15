package com.hfhk.cb.service.modules.project.authority.audit;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.project.authority.ProjectAuthorityAudit;
import com.hfhk.cb.service.mongo.ProjectAuthorityAuditMongo;
import com.hfhk.cb.unit.Unit;

import java.util.List;

public class ProjectAuthorityAuditConverter {

	public static ProjectAuthorityAudit convert(ProjectAuthorityAuditMongo mongo, User user, List<Project> projects) {
		if (mongo == null) return null;
		return ProjectAuthorityAudit.builder()
			.id(mongo.getId())
			.user(user)
			.unit(Unit.builder().name(mongo.getUnit()).build())
			.projects(projects)
			.state(mongo.getAuditState())
			.remark(mongo.getRemark())
			.appliedAt(mongo.getAppliedAt())
			.auditedAt(mongo.getAuditedAt())
			.passedAt(mongo.getPassedAt())
			.rejectedAt(mongo.getRejectedAt())
			.build();
	}
}
