package com.hfhk.cb;

public enum Dictionary {
	/**
	 * 园区
	 */
	Park("Park"),

	/**
	 * 项目类型
	 */
	ProjectType("ProjectType"),

	/**
	 * 施工进度
	 */
	ConstructionSchedule("ConstructionSchedule");

	private String id;

	Dictionary(String id) {
		this.id = id;
	}

}
