package com.hfhk.cb.project;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * project find
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectFindParam extends AbstractPage<ProjectFindParam> {
	/**
	 * ids
	 */
	private Set<String> ids;
	/**
	 * 类型
	 */
	private Set<String> types;
	/**
	 * 园区
	 */
	private Set<String> parks;
	/**
	 * 进度
	 */
	private Set<String> schedules;
	/**
	 * 监督负责人
	 */
	private Set<String> supervisionLeaders;
}
