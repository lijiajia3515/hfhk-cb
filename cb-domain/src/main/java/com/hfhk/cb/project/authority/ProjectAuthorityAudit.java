package com.hfhk.cb.project.authority;

import com.hfhk.auth.modules.user.User;
import com.hfhk.cb.project.Project;
import com.hfhk.cb.audit.AuditState;
import com.hfhk.cb.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目 审核
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAuthorityAudit {
	/**
	 * id
	 */
	private String id;
	/**
	 * 申请人
	 */
	private User user;

	/**
	 * 项目信息
	 */
	private List<Project> projects;

	/**
	 * 参建单位
	 */
	private Unit unit;

	/**
	 * 状态
	 */
	private AuditState state;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 申请时间
	 */
	private LocalDateTime appliedAt;
	/**
	 * 审批时间
	 */
	private LocalDateTime auditedAt;
	/**
	 * 通过时间
	 */
	private LocalDateTime passedAt;
	/**
	 * 拒绝时间
	 */
	private LocalDateTime rejectedAt;
}
