package com.hfhk.cb.supervision;

import com.hfhk.cairo.core.page.AbstractPage;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupervisionFindParam extends AbstractPage<SupervisionFindParam> {
	private String keyword;
	private LocalDateTime startAt;
	private LocalDateTime endAt;

	private Set<String> ids;
	private Set<Project> projects;

	private Set<String> created;

	@Data
	@Accessors(chain = true)

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Project {
		private String id;
		private String unit;
	}
}
