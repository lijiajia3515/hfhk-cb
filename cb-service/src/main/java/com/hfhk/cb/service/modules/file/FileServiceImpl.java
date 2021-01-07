package com.hfhk.cb.service.modules.file;

import com.hfhk.auth.client.UserClientCredentialsClient;
import com.hfhk.auth.domain.Metadata;
import com.hfhk.cairo.core.page.Page;
import com.hfhk.cairo.mongo.data.Created;
import com.hfhk.cairo.mongo.data.LastModified;
import com.hfhk.cb.file.domain.File;
import com.hfhk.cb.file.domain.request.FileFindParam;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.mongo.FileMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j

@Service
public class FileServiceImpl implements FileService {

	private final HfhkMongoProperties mongoProperties;
	private final MongoTemplate mongoTemplate;

	private final GridFsTemplate gridFsTemplate;

	private final UserClientCredentialsClient userClient;

	public FileServiceImpl(HfhkMongoProperties mongoProperties,
						   MongoTemplate mongoTemplate,
						   GridFsTemplate gridFsTemplate,
						   UserClientCredentialsClient userClient) {
		this.mongoProperties = mongoProperties;
		this.mongoTemplate = mongoTemplate;
		this.gridFsTemplate = gridFsTemplate;
		this.userClient = userClient;
	}

	@Override
	public List<File> store(String client, String uid, String folderPath, Collection<MultipartFile> files) {
		return files
			.parallelStream()
			.flatMap(file -> {
				String filename = file.getOriginalFilename();
				String contentType = file.getContentType();

				Created created = Created.builder().uid(uid).at(LocalDateTime.now()).build();
				LastModified lastModified = LastModified.builder().uid(uid).at(LocalDateTime.now()).build();

				try (InputStream in = file.getInputStream()) {
					String id = gridFsTemplate.store(in, filename, contentType, null).toString();
					Query query = Query.query(Criteria.where(FileMongo.FIELD._ID).is(id));
					Update update = new Update()
						.set(FileMongo.FIELD.CLIENT, client)
						.set(FileMongo.FIELD.FOLDER_PATH, folderPath)
						.set(FileMongo.FIELD.METADATA.CREATED.SELF, created)
						.set("metadata.lastModified", lastModified);
					mongoTemplate.updateFirst(query, update, FileMongo.class, mongoProperties.Collection.File);
					return Optional.ofNullable(mongoTemplate.findById(id, FileMongo.class))
						.flatMap(this::optionalFile).stream();
				} catch (IOException e) {
					log.warn("file store error", e);
					return Stream.empty();
				}
			}).collect(Collectors.toList());
	}

	@Override
	public Optional<GridFsResource> findResource(String id) {
		return Optional.ofNullable(gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id))))
			.map(gridFsTemplate::getResource);
	}

	@Override
	public void delete(String client, String uid, List<String> ids) {
		if (!ids.isEmpty()) {
			final Criteria criteria = Criteria.where("client").is(client).and("_id").in(ids);
			gridFsTemplate.delete(Query.query(criteria));
		}
	}

	@Override
	public List<File> find(String client, FileFindParam param) {
		Criteria criteria = Criteria.where(FileMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(param)
			.ifPresent(x -> {
				Optional.ofNullable(x.getFilepath()).ifPresent(y -> criteria.and(FileMongo.FIELD.FOLDER_PATH).regex(y));
				Optional.ofNullable(x.getFilename()).ifPresent(y -> criteria.and(FileMongo.FIELD.FILENAME).regex(y));
			});

		Query query = Query.query(criteria);
		return mongoTemplate.find(query, FileMongo.class, mongoProperties.Collection.File)
			.stream()
			.flatMap(x -> optionalFile(x).stream())
			.collect(Collectors.toList());
	}

	@Override
	public Page<File> pageFind(String client, FileFindParam param) {
		Criteria criteria = Criteria.where("client").is(client);

		Optional.ofNullable(param.getFilepath()).ifPresent(x -> criteria.and("folderPath").regex(x));
		Optional.ofNullable(param.getFilename()).ifPresent(x -> criteria.and("filename").regex(x));
		Query query = Query.query(criteria);
		long total = mongoTemplate.count(query, FileMongo.class);
		query.with(param.pageable());
		List<File> files = mongoTemplate.find(query, FileMongo.class).stream().flatMap(x -> optionalFile(x).stream()).collect(Collectors.toList());
		return new Page<>(param, files, total);
	}


	private Optional<File> optionalFile(FileMongo file) {
		return Optional.ofNullable(file)
			.map(x ->
				File.builder()
					.id(x.get_id())
					.folderPath(x.getFolderPath())
					.filename(x.getFilename())
					.contentType(x.getMetadata().get_contentType())
					.length(x.getLength())
					.md5(x.getMd5())
					.chuckSize(x.getChunkSize())
					.metadata(Metadata.builder()
						.created(
							Metadata.Action.builder()
								.user(userClient.findById(x.getMetadata().getCreated().getUid()))
								.at(x.getMetadata().getCreated().getAt())
								.build()
						).lastModified(
							Metadata.Action.builder()
								.user(userClient.findById(x.getMetadata().getLastModified().getUid()))
								.at(x.getMetadata().getLastModified().getAt())
								.build()
						)
						.build()
					)
					.build()
			);

	}

}
