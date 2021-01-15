package com.hfhk.cb.instruct;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cb.audit.AuditState;
import com.hfhk.system.file.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instruct implements Serializable {
	private String id;

	private InstructType type;

	private List<File> files;

	private String project;

	private String unit;

	private User designateAuditUser;

	private AuditState auditState;
	private User auditedUser;
	
	private LocalDateTime auditedAt;
	private LocalDateTime appliedAt;
	private LocalDateTime passedAt;
	private LocalDateTime rejectedAt;
	private String remark;

}
