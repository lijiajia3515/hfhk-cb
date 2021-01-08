package com.hfhk.cb;

public enum CbDictionary {
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

	public final String ID;

	CbDictionary(String id) {
		this.ID = id;
	}

}
