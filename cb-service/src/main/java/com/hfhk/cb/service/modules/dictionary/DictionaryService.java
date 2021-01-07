package com.hfhk.cb.service.modules.dictionary;

import cn.hutool.core.util.IdUtil;
import com.hfhk.cairo.core.Constants;
import com.hfhk.cairo.core.exception.UnknownBusinessException;
import com.hfhk.cairo.mongo.data.Metadata;
import com.hfhk.cb.dictionary.domain.Dictionary;
import com.hfhk.cb.service.constants.HfhkMongoProperties;
import com.hfhk.cb.service.modules.dictionary.domain.request.*;
import com.hfhk.cb.service.mongo.DictionaryMongo;
import com.hfhk.system.service.modules.dictionary.domain.request.*;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j(topic = "[dictionary][service]")
@Service
public class DictionaryService {
	private final HfhkMongoProperties mongoProperties;
	private final MongoTemplate mongoTemplate;

	public DictionaryService(HfhkMongoProperties mongoProperties, MongoTemplate mongoTemplate) {
		this.mongoProperties = mongoProperties;
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * dictionary save
	 *
	 * @param params params
	 * @return dictionary optional
	 */
	public Optional<Dictionary> save(String client, DictionarySaveParams params) {
		DictionaryMongo dictionaryMongo = DictionaryMongo.builder()
			.client(client)
			.code(Optional.ofNullable(params.getCode()).orElse(IdUtil.objectId()))
			.name(params.getName())
			.items(
				Optional.ofNullable(params.getItems())
					.stream()
					.flatMap(Collection::stream)
					.map(x ->
						DictionaryMongo.Item.builder()
							.code(Optional.ofNullable(x.getCode()).orElse(Constants.SNOWFLAKE.nextIdStr()))
							.value(x.getValue())
							.name(x.getName())
							.metadata(new Metadata().setSort(Constants.SNOWFLAKE.nextId()))
							.build()
					)
					.collect(Collectors.toList())
			)
			.build();
		dictionaryMongo = mongoTemplate.insert(dictionaryMongo, mongoProperties.Collection.Dictionary);
		return convert(dictionaryMongo);
	}

	/**
	 * dictionary modify
	 *
	 * @param request request
	 * @return 1
	 */
	public Optional<Dictionary> modify(String client, DictionarySaveParams request) {
		final List<DictionaryMongo.Item> items = Optional.ofNullable(request.getItems())
			.stream()
			.flatMap(Collection::stream)
			.map(x ->
				DictionaryMongo.Item.builder()
					.code(Optional.ofNullable(x.getCode()).orElse(IdUtil.objectId()))
					.value(x.getValue())
					.name(x.getName())
					.metadata(new Metadata().setSort(Constants.SNOWFLAKE.nextId()))
					.build()
			)
			.collect(Collectors.toList());
		Query query = Query.query(
			Criteria.where(DictionaryMongo.FIELD.CLIENT).is(client)
				.and(DictionaryMongo.FIELD.CODE).is(request.getCode())
		);

		Update update = Update.update(DictionaryMongo.FIELD.Name, request.getName())
			.set(DictionaryMongo.FIELD.ITEMS.SELF, items);

		final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, DictionaryMongo.class, mongoProperties.Collection.Dictionary);

		log.debug("[modify] result-> {}", updateResult);
		return convert(mongoTemplate.findOne(query, DictionaryMongo.class, mongoProperties.Collection.Dictionary));

	}

	/**
	 * dictionary delete
	 *
	 * @param client client
	 * @param params params
	 * @return remove dictionary list
	 */
	public List<Dictionary> delete(String client, DictionaryDeleteParams params) {
		Criteria criteria = Criteria.where(DictionaryMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(params)
			.ifPresent(param -> Optional
				.ofNullable(param.getCodes())
				.filter(x -> !x.isEmpty())
				.ifPresentOrElse(x -> criteria.and(DictionaryMongo.FIELD.CODE).in(x), () -> {
					throw new UnknownBusinessException("Code为空");
				})
			);
		Query query = Query.query(criteria);
		return mongoTemplate.findAllAndRemove(query, DictionaryMongo.class, mongoProperties.Collection.Dictionary)
			.stream()
			.flatMap(a -> convert(a).stream())
			.collect(Collectors.toList());
	}

	/**
	 * find
	 *
	 * @param client client
	 * @param params query params
	 * @return x
	 */
	public List<Dictionary> find(String client, DictionaryFindParams params) {
		Criteria criteria = Criteria.where(DictionaryMongo.FIELD.CLIENT).is(client);
		Optional.ofNullable(params)
			.ifPresent(x -> {
				Optional.ofNullable(x.getCode()).ifPresent(y -> criteria.and(DictionaryMongo.FIELD.CODE).regex(y));
				Optional.ofNullable(x.getName()).ifPresent(y -> criteria.and(DictionaryMongo.FIELD.Name).regex(y));
			});
		Query query = Query.query(criteria);

		return mongoTemplate.find(query, DictionaryMongo.class, mongoProperties.Collection.Dictionary)
			.stream()
			.flatMap(x -> convert(x).stream())
			.collect(Collectors.toList());
	}

	/**
	 * put Item
	 *
	 * @param params params
	 * @return dictionary optional
	 */
	public Optional<Dictionary> putItems(String client, DictionaryItemPutParams params) {
		final Criteria criteria = Criteria
			.where(DictionaryMongo.FIELD.CLIENT).is(client)
			.and(DictionaryMongo.FIELD.CODE).is(params.getCode());

		final List<DictionaryMongo.Item> newItems = Optional
			.ofNullable(params.getItems())
			.stream()
			.flatMap(Collection::stream)
			.map(x -> DictionaryMongo.Item.builder()
				.code(x.getCode())
				.value(x.getValue())
				.metadata(new Metadata().setSort(Constants.SNOWFLAKE.nextId()))
				.build())
			.collect(Collectors.toList());

		Query query = Query.query(criteria);

		Update update = new Update().addToSet(DictionaryMongo.FIELD.ITEMS.SELF).each(newItems);

		final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, DictionaryMongo.class, mongoProperties.Collection.Dictionary);
		log.debug("[dictionary][putItem] result-> : {}", updateResult);

		return convert(mongoTemplate.findOne(query, DictionaryMongo.class, mongoProperties.Collection.Dictionary));
	}

	/**
	 * dictionary item modify
	 *
	 * @param request request
	 * @return dictionary optional
	 */
	public Optional<Dictionary> modifyItems(String client, DictionaryItemModifyParams request) {
		final Criteria criteria = Criteria
			.where(DictionaryMongo.FIELD.CODE).is(client)
			.and(DictionaryMongo.FIELD.CODE).is(request.getCode())
			.and(DictionaryMongo.FIELD.ITEMS.CODE).is(request.getItem());
		final Query query = Query.query(criteria);

		final Update update = Update
			.update(DictionaryMongo.FIELD.ITEMS.VALUE, request.getItem().getValue());

		final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, DictionaryMongo.class, mongoProperties.Collection.Dictionary);
		log.debug("[dictionary][putItem] result-> : {}", updateResult);

		return convert(mongoTemplate.findOne(Query.query(Criteria.where(DictionaryMongo.FIELD.CODE).is(request.getCode())), DictionaryMongo.class, mongoProperties.Collection.Dictionary));
	}

	/**
	 * dictionary item delete
	 *
	 * @param params params
	 * @return dictionary optional
	 */
	public Optional<Dictionary> deleteItems(String client, DictionaryItemDeleteParams params) {
		final Criteria criteria = Criteria
			.where(DictionaryMongo.FIELD.CLIENT).is(client);
		Optional.of(params)
			.ifPresent(x -> Optional
				.ofNullable(x.getCode())
				.ifPresentOrElse(
					code -> criteria.and(DictionaryMongo.FIELD.CODE).is(code),
					() -> {
						throw new UnknownBusinessException("Code不能为空");
					}
				)
			);

		final Query query = Query.query(criteria);

		final Update update = new Update();
		Optional.ofNullable(params.getItemCodes())
			.ifPresentOrElse(
				itemCodes -> update.pullAll(DictionaryMongo.FIELD.ITEMS.CODE, itemCodes.toArray()),
				() -> {
					throw new UnknownBusinessException("ItemCodes为空");
				}
			);
		update.pullAll(DictionaryMongo.FIELD.ITEMS.CODE, params.getItemCodes().toArray(new String[0]));

		final UpdateResult updateResult = mongoTemplate.updateFirst(query, update, DictionaryMongo.class, mongoProperties.Collection.Dictionary);
		log.debug("[dictionary][deleteItem] result-> : {}", updateResult);

		return convert(mongoTemplate.findOne(query, DictionaryMongo.class, mongoProperties.Collection.Dictionary));
	}

	/**
	 * mongo to model
	 *
	 * @param mongo mongo
	 * @return dictionary optional
	 */
	private Optional<Dictionary> convert(DictionaryMongo mongo) {
		return Optional.ofNullable(mongo)
			.map(x ->
				Dictionary.builder()
					.code(x.getCode())
					.name(x.getName())
					.items(
						Optional.ofNullable(x.getItems())
							.stream().flatMap(Collection::stream)
							.map(y -> Dictionary.Item.builder().code(y.getCode()).value(y.getValue()).name(y.getName()).build())
							.collect(Collectors.toList())
					)
					.build()
			);
	}
}
