package com.hfhk.cb.project.authority;

import com.hfhk.cairo.core.page.AbstractPage;
import com.hfhk.cb.audit.AuditState;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectAuthorityAuditFindParam extends AbstractPage<ProjectAuthorityAuditFindParam> {
	/**
	 * keyword
	 */
	private String keyword;

	/**
	 * ids
	 */
	private Set<String> ids;

	private Set<String> projects;

	private Set<String> users;

	/**
	 * 状态
	 */
	private Set<AuditState> states;
}
