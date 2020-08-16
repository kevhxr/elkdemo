package com.xr.elkone;

import com.alibaba.fastjson.JSON;
import com.xr.elkone.pojo.User;
import org.elasticsearch.action.admin.indices.create.CreateIndexAction;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EsAppTest {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testCreatedIndex() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest("xr_idx1");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        System.out.println(createIndexResponse);
    }

    @Test
    public void testGetIndex() throws IOException {
        GetIndexRequest idx = new GetIndexRequest("xr_idx1");
        boolean exists = restHighLevelClient.indices().exists(idx, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @Test
    public void testDelIndex() throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest("xr_idx1");
        AcknowledgedResponse response = restHighLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    @Test
    public void testAddDoc() throws IOException {
        User user = new User("huang", 22);
        IndexRequest request = new IndexRequest("xr_idx");
        request.id("1");
        request.timeout(TimeValue.timeValueSeconds(1));
        request.timeout("1s");
        IndexRequest source = request.source(JSON.toJSONString(user), XContentType.JSON);
        IndexResponse indexResponse = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());

    }


    @Test
    public void testGetDocIndex() throws IOException {
        GetRequest getRequest = new GetRequest("xr_idx","1");
        GetResponse response = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
        System.out.println(response);
    }

    @Test
    public void testUpdateDocIndex() throws IOException {
        UpdateRequest request = new UpdateRequest("xr_idx","1");
        User user = new User("huang", 29);
        request.doc(JSON.toJSONString(user),XContentType.JSON);
        UpdateResponse update = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(update.status());
    }

    @Test
    public void testBulkInsert() throws IOException {
        BulkRequest bulkRequest = new BulkRequest("xr_idx");
        List<User> users = new ArrayList<>();
        users.add(new User("u1", 11));
        users.add(new User("u2", 12));
        users.add(new User("u3", 15));
        users.add(new User("u4", 22));
        users.add(new User("u5", 44));

        for (int i = 0; i < users.size() ; i++) {
            bulkRequest.add(new IndexRequest("xr_idx")
            .id(""+(i+1))
            .source(JSON.toJSONString(users.get(i)),XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures());
    }

}
