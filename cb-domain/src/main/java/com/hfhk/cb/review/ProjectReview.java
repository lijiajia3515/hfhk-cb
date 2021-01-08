package com.hfhk.cb.review;

import com.hfhk.auth.domain.user.User;
import com.hfhk.cb.project.SimpleProject;
import com.hfhk.cb.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 项目 审核
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReview {
	/**
	 * id
	 */
	private String id;
	/**
	 * 申请人
	 */
	private User applyUser;

	/**
	 * 项目信息
	 */
	private List<SimpleProject> project;

	/**
	 * 所属单位
	 */
	private Unit ownerUnit;

	/**
	 * 状态
	 */
	private ReviewState state;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 申请时间
	 */
	private LocalDateTime applyAt;
	/**
	 * 审批时间
	 */
	private String reviewedAt;
	/**
	 * 通过时间
	 */
	private LocalDateTime passedAt;
	/**
	 * 拒绝时间
	 */
	private LocalDateTime rejectedAt;
}
