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
 * Project Review
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
	private AuditState state;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 申请时间
	 */
	private LocalDateTime applyAt;

	/**
	 * 审批人
	 */
	private String reviewedUser;

	/**
	 * 审批时间
	 */
	private String reviewedAt;

	/**
	 * 通过时间
	 */
	private LocalDateTime passedAt;

	/**
	 * 拒绝时间
	 */
	private LocalDateTime rejectedAt;

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
		public final String STATE = field("State");
		public final String REMARK = field("Remark");
		public final String APPLY_AT = field("ApplyAt");

		public final String REVIEWED_USER = field("ReviewedUser");
		public final String REVIEWED_AT = field("ReviewedAt");

		public final String PASSED_AT = field("PassedAt");
		public final String REJECTED_AT = field("RejectedAt");

	}
}
