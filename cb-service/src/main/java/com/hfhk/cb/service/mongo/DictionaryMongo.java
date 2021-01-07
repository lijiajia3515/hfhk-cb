package com.hfhk.cb.service.mongo;

import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractMongoField;
import com.hfhk.cairo.mongo.data.mapping.model.AbstractUpperCamelCaseField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 字典
 */
@Data
@Accessors(chain = true)

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DictionaryMongo {

	/**
	 * id
	 */
	private String id;

	/**
	 * client
	 */
	private String client;

	/**
	 * code 值
	 */
	private String code;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * value值
	 */
	private List<Item> items;

	@Builder.Default
	private Metadata metadata = new Metadata();

	public static final Field FIELD = new Field();

	/**
	 * Dictionary Item
	 */
	@Data
	@Accessors(chain = true)

	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class Item {
		/**
		 * value code
		 */
		private String code;

		/**
		 * value
		 */
		private Object value;

		/**
		 * 名称
		 */
		private String name;
		/**
		 * metadata
		 */
		@Builder.Default
		private Metadata metadata = new Metadata();
	}

	public static class Field extends AbstractUpperCamelCaseField {
		public final String CLIENT = field("Client");
		public final String CODE = field("Code");
		public final String Name = field("Name");
		public final Items ITEMS = new Items(this,"Items");

		public static class Items extends AbstractUpperCamelCaseField {
			public Items() {
			}

			public Items(AbstractMongoField parent, String prefix) {
				super(parent, prefix);
			}
			public final String CODE = field("Code");
			public final String VALUE = field("Value");
		}
	}
}
