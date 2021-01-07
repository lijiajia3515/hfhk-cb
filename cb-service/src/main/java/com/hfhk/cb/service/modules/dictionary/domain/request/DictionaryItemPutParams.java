package com.hfhk.cb.service.modules.dictionary.domain.request;

import com.hfhk.cb.dictionary.domain.Dictionary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 字典 刷新 值
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DictionaryItemPutParams {

	/**
	 * code
	 */
	private String code;

	/**
	 * 项
	 */
	private List<Dictionary.Item> items;

}
