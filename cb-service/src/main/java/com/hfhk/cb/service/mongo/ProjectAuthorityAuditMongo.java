package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import com.hfhk.cb.audit.AuditState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Project Authority Audit
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAuthorityAuditMongo implements Serializable {
	/**
	 * id
	 */
	private String id;
	/**
	 * 申请人
	 */
	private String applyUser;

	/**
	 * 项目信息
	 */
	private Set<String> projects;

	/**
	 * 所属单位
	 */
	private String unit;

	/**
	 * 状态
	 */
	private AuditState auditState;

	/**
	 * 审批人
	 */
	private String auditedUid;

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


	/**
	 * 备注
	 */
	private String remark;


	/**
	 * metadata
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();

	public static MongoField FIELD = new MongoField();

	public static class MongoField extends AbstractUpperCamelCaseField {
		public MongoField() {

		}

		public final String USER = field("User");
		public final String PROJECTS = field("Projects");
		public final String UNIT = field("Unit");
		public final String AUDIT_STATE = field("AuditState");
		public final String AUDITED_UID = field("AuditedUid");


		public final String AUDITED_AT = field("AuditedAt");
		public final String APPLIED_AT = field("AppliedAt");
		public final String PASSED_AT = field("PassedAt");
		public final String REJECTED_AT = field("RejectedAt");
		public final String REMARK = field("Remark");

	}
}
