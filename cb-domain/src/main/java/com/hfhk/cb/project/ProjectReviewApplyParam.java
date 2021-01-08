package com.hfhk.cb.project;

import com.hfhk.cb.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReviewApplyParam {

	/**
	 * 所属单位
	 */
	private Unit ownerUnit;

	/**
	 * 项目
	 */
	Set<String> projects;
}
