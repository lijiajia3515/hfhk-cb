package com.hfhk.cb.supervision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupervisionSaveParam {
	private String project;
	private List<String> files;
	private String unit;
	private Set<String> checks;
	private Long problemNum;
}
