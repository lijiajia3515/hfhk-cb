package com.hfhk.cb.service.modules.dictionary.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryFindParams {
	private String code;
	private String name;
}
