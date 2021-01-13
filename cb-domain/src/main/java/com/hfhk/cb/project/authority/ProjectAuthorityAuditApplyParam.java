package com.hfhk.cb.project.authority;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAuthorityAuditApplyParam implements Serializable {
	private Set<String> projects;
	private String unit;
}
