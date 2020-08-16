package com.xr.elkmarket.service;

import com.alibaba.fastjson.JSON;
import com.xr.elkmarket.pojo.Content;
import com.xr.elkmarket.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ContentService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    public static void main(String[] args) throws Exception {
        new ContentService().parseContent("java");
    }

    public boolean parseContent(String keyWord) throws Exception {
        List<Content> contents = new HtmlParseUtil().parseJD(keyWord);
        BulkRequest request = new BulkRequest();
        request.timeout("2m");
        for (int i = 0; i < contents.size(); i++) {
            request.add(new IndexRequest("jd_goods").source(
                    JSON.toJSONString(contents.get(i)), XContentType.JSON)
            );
        }

        BulkResponse bulkResponse = restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        return !bulkResponse.hasFailures();
    }

    public List<Map<String, Object>> searchPage(String keyWord, int pageNo, int pageSize) throws IOException {
        if (pageNo <= 1) {
            pageNo = 1;
        }
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermQueryBuilder title = QueryBuilders.termQuery("title", keyWord);
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        searchSourceBuilder.query(title);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //hightlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        // For multiple highlight display
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : response.getHits().getHits()) {

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField titleField = highlightFields.get("title");
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            if (titleField != null) {
                Text[] fragments = titleField.fragments();
                String titles = "";
                for (Text fragment : fragments) {
                    titles += fragment;
                }
                sourceAsMap.put("title", titles);
            }
            list.add(sourceAsMap);

        }
        return list;

    }
}
