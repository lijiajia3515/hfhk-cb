package com.hfhk.cb.unit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit implements Serializable {
	/**
	 * id 标识
	 */
	private String id;
	/**
	 * 单位类型
	 */
	private UnitType type;
	/**
	 * 单位名称
	 */
	private String name;
	/**
	 * 负责人名称
	 */
	private String leaderName;
	/**
	 * 负责人电话
	 */
	private String leaderPhoneNumber;
}
