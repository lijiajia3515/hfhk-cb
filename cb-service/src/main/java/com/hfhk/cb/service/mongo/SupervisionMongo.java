package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupervisionMongo implements Serializable {

	private String id;

	private List<String> files;

	private String project;

	private String unit;

	private Set<String> checks;

	private Long problemNum;

	@Builder.Default
	private Metadata metadata = new Metadata();

	public static final MongoField FIELD = new MongoField();

	public static class MongoField extends AbstractUpperCamelCaseField {
		public String FILES = field("Files");
		public String PROJECT = field("Project");
		public String UNIT = field("Unit");
		public String CHECKS = field("Checks");
		public String PROBLEM_NUM = field("ProblemNum");
	}
}
