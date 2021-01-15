package com.hfhk.cb.instruct;

import com.hfhk.auth.domain.user.User;
import com.hfhk.system.file.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InstructApplyParam implements Serializable {

	private InstructType type;

	private List<String> files;

	private String project;

	private String unit;

	private String designateAuditUser;
}
