package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import com.hfhk.cb.audit.AuditState;
import com.hfhk.cb.instruct.InstructType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructMongo {
	/**
	 * id
	 */
	private String id;

	/**
	 * 指令类型
	 */
	private InstructType type;

	/**
	 * 文件
	 */
	private List<String> files;

	/**
	 * 项目id
	 */
	private String project;

	/**
	 * 单位id
	 */
	private String unit;

	/**
	 * 指定审核人
	 */
	private String designateAuditUser;

	private AuditState auditState;
	private String auditedUid;
	private LocalDateTime appliedAt;
	private LocalDateTime auditedAt;
	private LocalDateTime passedAt;
	private LocalDateTime rejectedAt;
	private String remark;

	@Builder.Default
	private Metadata metadata = new Metadata();

	public static final MongoField FIELD = new MongoField();

	public static final class MongoField extends AbstractUpperCamelCaseField {
		public final String TYPE = field("Type");
		public final String FILES = field("Files");
		public final String PROJECT = field("Project");
		public final String UNIT = field("Unit");
		public final String DESIGNATE_AUDIT_USER = field("DesignateAuditUser");

		public final String AUDIT_STATE = field("AuditState");
		public final String AUDITED_UID = field("AuditUser");

		public final String AUDITED_AT = field("AuditAt");
		public final String APPLIED_AT = field("PassedAt");
		public final String PASSED_AT = field("PassedAt");
		public final String REJECTED_AT = field("RejectedAt");

		public final String REMARK = field("Remark");
	}
}
