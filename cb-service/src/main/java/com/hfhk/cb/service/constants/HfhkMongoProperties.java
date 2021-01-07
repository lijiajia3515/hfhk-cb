package com.hfhk.cb.service.constants;

import org.springframework.stereotype.Component;

/**
 * MongoConfig
 */

@Component("hfhkMongoProperties")
public class HfhkMongoProperties {
	public final Collection Collection;

	public HfhkMongoProperties(org.springframework.boot.autoconfigure.mongo.MongoProperties properties) {
		this.Collection = new Collection(properties);
	}

	/**
	 * collection
	 */
	public static class Collection {
		private String Prefix = "system";
		private String Bucket = "";

		public Collection(org.springframework.boot.autoconfigure.mongo.MongoProperties properties) {
			Bucket = properties.getGridfs().getBucket();
		}

		public Collection(org.springframework.boot.autoconfigure.mongo.MongoProperties properties, String prefix) {
			Bucket = properties.getGridfs().getBucket();
			this.Prefix = prefix;
		}

		public final String Dictionary = collection("dictionaries");
		public final String Folder = collection("folders");
		public final String File = Bucket.concat(".files");

		private String collection(String collection) {
			return Prefix.concat("_").concat(collection);
		}
	}
}
