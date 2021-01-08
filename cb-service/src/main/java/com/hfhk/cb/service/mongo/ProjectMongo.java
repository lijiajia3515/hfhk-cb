package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractMongoField;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectMongo {
	/**
	 * id
	 */
	private String id;
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

	// participant units
	/**
	 * 建设单位
	 */
	private ProjectUnitMongo ownerUnit;
	/**
	 * 监理单位
	 */
	private List<ProjectUnitMongo> supervisionUnits;
	/**
	 * 施工单位
	 */
	private List<ProjectUnitMongo> constructionUnits;
	/**
	 * 勘测单位
	 */
	private List<ProjectUnitMongo> surveyUnits;
	/**
	 * 设计单位
	 */
	private List<ProjectUnitMongo> designUnits;
	/**
	 * 质检单位
	 */
	private List<ProjectUnitMongo> qualityUnits;


	// other info
	/**
	 * 开工时间
	 */
	private LocalDateTime constructionStartAt;
	/**
	 * 竣工时间
	 */
	private LocalDateTime constructionEndAt;
	/**
	 * 监督负责人
	 */
	private String supervisionLeader;
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

	@Builder.Default
	private Metadata metadata = new Metadata();

	public static final MongoField FIELD = new MongoField();


	public static class MongoField extends AbstractUpperCamelCaseField {

		public final String TYPE = field("Type");
		public final String PARK = field("Park");
		public final String AREA = field("Area");
		public final String PRICE = field("Price");
		public final String ADDRESS = field("Address");
		public final String OWNER_UNIT = field("OwnerUnit");
		public final String SUPERVISION_UNITS = field("SupervisionUnits");
		public final String CONSTRUCTION_UNITS = field("ConstructionUnits");
		public final String SURVEY_UNITS = field("SurveyUnits");
		public final String DESIGN_UNITS = field("DesignUnits");
		public final String QUALITY_UNITS = field("QualityUnits");
		public final String CONSTRUCTION_START_AT = field("ConstructionStartAt");
		public final String CONSTRUCTION_END_AT = field("ConstructionEndAt");
		public final String SUPERVISION_LEADER = field("SupervisionLeader");
		public final String SUPERVISION_START_AT = field("SupervisionStartAt");
		public final String SCHEDULE = field("Schedule");
		public final String REMARK = field("Remark");

	}
}
