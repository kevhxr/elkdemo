package com.xr.elkone;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ElkMain {
    public static void main(String[] args) {
        SpringApplication.run(ElkMain.class,args);
    }
}
