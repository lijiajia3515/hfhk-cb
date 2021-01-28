package com.hfhk.cb.service.modules.instruct;

import com.hfhk.auth.Metadata;
import com.hfhk.auth.modules.user.User;
import com.hfhk.cairo.mongo.data.Created;
import com.hfhk.cairo.mongo.data.LastModified;

import java.util.Map;
import java.util.Optional;

public class MetadataConverter {
	public static Metadata metadata(com.hfhk.cairo.mongo.data.Metadata metadata, Map<String, User> userMap) {
		return Metadata.builder()
			.created(
				Optional.ofNullable(metadata)
					.map(com.hfhk.cairo.mongo.data.Metadata::getCreated)
					.map(x ->
						Metadata.Action.builder()
							.user(Optional.ofNullable(x.getUid()).map(userMap::get).orElse(null))
							.at(x.getAt())
							.build()
					)
					.orElse(null)
			)
			.lastModified(
				Optional.ofNullable(metadata)
					.map(com.hfhk.cairo.mongo.data.Metadata::getLastModified)
					.map(x -> Metadata.Action.builder()
						.user(Optional.ofNullable(x.getUid()).map(userMap::get).orElse(null))
						.at(x.getAt())
						.build()
					)
					.orElse(null)
			)
			.build();
	}

	public static Metadata metadata(com.hfhk.cairo.mongo.data.Metadata metadata, User createdUser, User lastModifiedUser) {
		return Metadata.builder()
			.created(Metadata.Action.builder()
				.user(createdUser)
				.at(Optional.ofNullable(metadata)
					.map(com.hfhk.cairo.mongo.data.Metadata::getCreated).
						map(Created::getAt)
					.orElse(null)
				)
				.build()
			)
			.lastModified(Metadata.Action.builder()
				.user(lastModifiedUser)
				.at(Optional.ofNullable(metadata)
					.map(com.hfhk.cairo.mongo.data.Metadata::getLastModified)
					.map(LastModified::getAt)
					.orElse(null)
				)
				.build()
			)
			.build();
	}
}
