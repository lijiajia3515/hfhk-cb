package com.hfhk.cb.audit;

public enum AuditState {
	/**
	 * 提交/待审核
	 */
	Submit,

	/*
	 * 审核中
	 */
	// Processing,
	/**
	 * 通过
	 */
	Pass,
	/**
	 * 拒绝
	 */
	Reject
}
