package com.hxr.elkdemo.entity;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ItemRepo extends ElasticsearchRepository<Item,Long> {
}
