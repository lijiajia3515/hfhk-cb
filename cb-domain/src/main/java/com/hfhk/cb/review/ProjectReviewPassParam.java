package com.hfhk.cb.review;

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
public class ProjectReviewPassParam implements Serializable {
	private Set<String> ids;
	private String remark;
}
