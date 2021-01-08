package com.hfhk.cb.service.mongo;

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
	private List<ProjectUnitMongo> qualityUnit;


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

	public static final MongoField FIELD = new MongoField();


	public static class MongoField extends AbstractUpperCamelCaseField {

		public final String CODE = field("Code");
		public final String Name = field("Name");
		public final Items ITEMS = new Items(this, "Items");

		public static class Items extends AbstractUpperCamelCaseField {
			public Items() {
			}

			public Items(AbstractMongoField parent, String prefix) {
				super(parent, prefix);
			}

			public final String CODE = field("Code");
			public final String VALUE = field("Value");
		}
	}
}
