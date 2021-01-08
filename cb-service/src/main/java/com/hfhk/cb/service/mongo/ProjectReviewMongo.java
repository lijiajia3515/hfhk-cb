package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cb.review.ReviewState;
import com.hfhk.cb.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Project Review
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectReviewMongo implements Serializable {
	/**
	 * id
	 */
	private String id;
	/**
	 * 申请人
	 */
	private String applyUser;

	/**
	 * 项目信息
	 */
	private Set<String> project;

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

	@Builder.Default
	private Metadata metadata = new Metadata();
}
