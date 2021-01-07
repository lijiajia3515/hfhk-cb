package com.hfhk.cb.service.modules.file;

import com.hfhk.cairo.core.page.Page;
import com.hfhk.cb.file.domain.Folder;
import com.hfhk.cb.file.domain.request.FolderFindParam;
import com.hfhk.system.file.util.FolderUtil;
import com.hfhk.cb.service.mongo.FolderMongo;
import com.mongodb.client.result.DeleteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Component
public class FolderServiceImpl implements FolderService {
	private final MongoTemplate mongoTemplate;

	public FolderServiceImpl(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public Page<String> pageFind(String client, FolderFindParam request) {
		final Query query = Query.query(Criteria.where("client").is(client).and("_id").regex(request.getPath()));
		query.fields().include("path");
		long total = mongoTemplate.count(query, FolderMongo.class);
		query.with(request.getPage().pageable());
		List<String> paths = mongoTemplate.find(query, FolderMongo.class).stream()
			.map(FolderMongo::getPath).sorted().collect(Collectors.toList());
		return new Page<>(request.getPage(), paths, total);
	}

	@Override
	public List<Folder> treeFind(String client, String filepath) {
		final Criteria criteria = Criteria.where("client").is(client);
		Optional.ofNullable(filepath).filter(x -> !x.isEmpty()).ifPresent(x -> criteria.and("path").regex(filepath));
		final Query query = Query.query(criteria);
		final Set<Folder> folders = mongoTemplate.find(query, FolderMongo.class)
			.stream()
			.map(FolderMongo::getPath)
			.flatMap(x -> FolderUtil.allPaths(x).stream())
			.flatMap(x -> FolderConverter.optionalFolder(x).stream())
			.collect(Collectors.toSet());

		return FolderConverter.tree(folders, FolderUtil.parentPath(filepath));
	}

	@Override
	public void create(String client, String filepath) {
		Collection<String> paths = FolderUtil.allPaths(filepath);
		if (!paths.isEmpty()) {
			Query query = Query.query(Criteria.where("path").in(paths));
			query.fields().include("path");
			Set<String> existsPaths = mongoTemplate.find(query, FolderMongo.class).stream()
				.map(FolderMongo::getPath)
				.collect(Collectors.toSet());
			paths.removeAll(existsPaths);
			Collection<FolderMongo> folders = paths.stream()
				.map(x -> FolderMongo.builder().client(client).path(x).build())
				.collect(Collectors.toSet());
			folders = mongoTemplate.insertAll(folders);
			log.debug("[folder][create] result -> {}", folders);
		}
	}

	@Override
	public void rename(String client, String filepath, String newFilepath) {
		Query pathQuery = Query.query(Criteria.where("client").is(client).and("path").is(filepath));
		boolean pathExists = mongoTemplate.exists(pathQuery, FolderMongo.class);
		if (pathExists) {
			DeleteResult pathDeleteResult = mongoTemplate.remove(pathQuery, FolderMongo.class);
			log.debug("[folder][modify]-[pathDelete]->{}", pathDeleteResult);
			Collection<String> newPaths = FolderUtil.allPaths(newFilepath);
			Query newPathsQuery = Query.query(Criteria.where("client").is(client).and("path").in(newFilepath));
			Set<String> existsNewPaths = mongoTemplate.find(newPathsQuery, FolderMongo.class).stream()
				.map(FolderMongo::getPath)
				.collect(Collectors.toSet());
			newPaths.removeAll(existsNewPaths);
			Collection<FolderMongo> folders = newPaths.stream()
				.map(x -> FolderMongo.builder().client(client).path(x).build())
				.collect(Collectors.toSet());
			folders = mongoTemplate.insertAll(folders);
			log.debug("[folder][modify]-[newPathSave]->{}", folders);
		}
	}

	@Override
	public void delete(String client, String filepath) {
		Query pathQuery = Query.query(Criteria.where("path").is(filepath));
		DeleteResult deleteResult = mongoTemplate.remove(pathQuery, FolderMongo.class);
		log.debug("[folder][delete]-> {}", deleteResult);
	}
}
