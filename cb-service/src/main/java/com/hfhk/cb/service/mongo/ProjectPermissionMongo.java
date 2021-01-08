package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 项目权限
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectPermissionMongo {

	/**
	 * id
	 */
	private String id;

	/**
	 * uid
	 */
	private String uid;

	/**
	 * project
	 */
	private Set<String> project;

	/**
	 * metadata
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();
}
