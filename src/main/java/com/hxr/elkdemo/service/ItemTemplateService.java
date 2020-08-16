package com.hxr.elkdemo.service;


import com.google.gson.Gson;
import com.hxr.elkdemo.entity.Item;
import com.hxr.elkdemo.entity.Poet;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

@Service
public class ItemTemplateService {

    private static final String INDEX_NAME = "items";
    private static final String INDEX_TYPE = "item";
    Logger logger = LoggerFactory.getLogger(ItemTemplateService.class);

    @Autowired
    ElasticsearchTemplate template;

    public void addItem(List<Item> items) {
        if (!template.indexExists(INDEX_NAME)) {
            template.createIndex(INDEX_NAME);
        }
        Gson gson = new Gson();
        List<IndexQuery> queries = new ArrayList<>();
        items.forEach(item -> {
            IndexQuery indexQuery = new IndexQuery();
            indexQuery.setId(item.getId().toString());
            indexQuery.setSource(gson.toJson(item));
            indexQuery.setIndexName(INDEX_NAME);
            indexQuery.setType(INDEX_TYPE);
            queries.add(indexQuery);
            template.bulkIndex(queries);
            queries.clear();
        });
    }

    public List<Map<String, Object>> findItem(String title) {
        MatchQueryBuilder match = QueryBuilders.matchQuery("title", title);
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("items")
                //.withFilter(match)
                .withQuery(queryStringQuery(title))
                .build();
        SearchHits hits = template.query(searchQuery, searchResponse -> searchResponse.getHits());
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap();
            list.add(source);
            logger.info("result: {}", source.toString());
        }
        return list;
    }

    public void deleteItem() {
    }

    public void updateItem(Item item) {
    }

    public List<Map<String, Object>> matchAll() {
        Client client = template.getClient();
        SearchRequestBuilder srb = client.prepareSearch("items").setTypes("item");
        // 查询所有
        SearchResponse sr = srb.setQuery(QueryBuilders.matchAllQuery()).execute().actionGet();
        SearchHits hits = sr.getHits();
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap();
            list.add(source);
            logger.info("result: {}", source.toString());
        }
        return list;
    }

    @Cacheable(cacheNames = "authority")
    public Object getPoet1() {
        String word = "如梦令";
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(queryStringQuery(word)).build();
        List poets = template.queryForList(searchQuery, Poet.class);
        poets.forEach(poet -> logger.info(poet.toString()));
        return poets;
    }

    @Cacheable(cacheNames = "cache2")
    public List<Map<String, Object>> getPoetAll() {
        System.out.println("to get all");
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("poets")
                .withPageable(PageRequest.of(0,5))
                .withQuery(QueryBuilders.matchAllQuery())
                .build();
        SearchHits hits = template.query(searchQuery, searchResponse -> searchResponse.getHits());
        List<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSourceAsMap();
            list.add(source);
            logger.info("result: {}", source.toString());
        }
        return list;
    }
}
