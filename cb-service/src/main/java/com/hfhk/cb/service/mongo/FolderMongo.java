package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文件夹
 */

@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FolderMongo {
	/**
	 * id
	 */
	private String id;

	/**
	 * client
	 */
	private String client;

	/**
	 * path
	 */
	private String path;

	/**
	 * 元数据
	 */
	@Builder.Default
	private Metadata metadata = new Metadata();

	public static final Field FIELD = new Field();

	public static class Field extends AbstractUpperCamelCaseField {
		public final String CLIENT = field("Client");
		public final String PATH = field("path");
	}
}
