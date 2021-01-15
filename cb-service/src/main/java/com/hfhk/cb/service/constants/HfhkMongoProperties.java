package com.hfhk.cb.service.constants;

import org.springframework.stereotype.Component;

/**
 * MongoConfig
 */

@Component("hfhkMongoProperties")
public class HfhkMongoProperties {
	public final Collection COLLECTION;

	public HfhkMongoProperties(org.springframework.boot.autoconfigure.mongo.MongoProperties properties) {
		this.COLLECTION = new Collection(properties);
	}

	/**
	 * collection
	 */
	public static class Collection {
		private String Prefix = "cb";
		private String Bucket = "";

		public Collection(org.springframework.boot.autoconfigure.mongo.MongoProperties properties) {
			Bucket = properties.getGridfs().getBucket();
		}

		public Collection(org.springframework.boot.autoconfigure.mongo.MongoProperties properties, String prefix) {
			Bucket = properties.getGridfs().getBucket();
			this.Prefix = prefix;
		}

		public final String PROJECT = collection("projects");
		public final String PROJECT_AUTHORITY_AUDIT = collection("project_authority_audits");
		public final String INSTRUCT = collection("instructs");


		private String collection(String collection) {
			return Prefix.concat("_").concat(collection);
		}
	}
}
