package com.hfhk.cb.project;

import com.hfhk.cb.unit.Unit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectSaveParm implements Serializable {
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 面积
	 */
	private BigDecimal area;
	/**
	 * 造价
	 */
	private BigDecimal price;
	/**
	 * 园区
	 */
	private String park;
	/**
	 * 项目地址
	 */
	private String address;

	/**
	 * 监督负责人
	 */
	private String supervisionLeader;

	// participant units
	/**
	 * 建设单位
	 */
	private Unit ownerUnit;
	/**
	 * 监理单位
	 */
	private List<Unit> supervisionUnits;
	/**
	 * 施工单位
	 */
	private List<Unit> constructionUnits;
	/**
	 * 勘测单位
	 */
	private List<Unit> surveyUnits;
	/**
	 * 设计单位
	 */
	private List<Unit> designUnits;
	/**
	 * 质检单位
	 */
	private List<Unit> qualityUnit;

	/**
	 * 开工时间
	 */
	private LocalDateTime constructionStartAt;
	/**
	 * 竣工时间
	 */
	private LocalDateTime constructionEndAt;
	/**
	 * 受监时间
	 */
	private LocalDateTime supervisionStartAt;

	/**
	 * 进度
	 */
	private String schedule;

	/**
	 * 备注
	 */
	private String remark;
}
