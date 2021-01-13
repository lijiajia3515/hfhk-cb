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
public class ProjectAuthorityAuditRejectParam implements Serializable {
	private Set<String> ids;
	private String remark;
}
