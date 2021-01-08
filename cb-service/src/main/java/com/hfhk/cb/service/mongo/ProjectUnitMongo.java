package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cb.unit.UnitType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectUnitMongo {
	/**
	 * id 标识
	 */
	private String id;

	/**
	 * 单位类型
	 */
	private UnitType type;

	/**
	 * 负责人名称
	 */
	private String leaderName;
	
	/**
	 * 负责人电话
	 */
	private String leaderPhoneNumber;

	@Builder.Default
	private Metadata metadata = new Metadata();
}
