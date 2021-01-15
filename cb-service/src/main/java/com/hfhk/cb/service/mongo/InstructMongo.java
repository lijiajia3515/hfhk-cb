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
	/**
	 * she
	 */
	private AuditState auditState;
	private String auditedUser;
	private LocalDateTime auditAt;
	private String remark;
	private LocalDateTime passedAt;
	private LocalDateTime rejectedAt;

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
		public final String AUDIT_USER = field("AuditUser");
		public final String AUDIT_AT = field("AuditAt");
		public final String REMARK = field("Remark");
		public final String PassedAt = field("PassedAt");
		public final String RejectedAt = field("RejectedAt");
	}
}
