package com.hfhk.cb.review;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReviewFindParam extends AbstractPage<ProjectReviewFindParam> {
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
	private Set<ReviewState> states;
}
