package com.xr.elkmarket.controller;

import com.xr.elkmarket.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ContentController {
    @Autowired
    ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("") String keyword) throws Exception {
        boolean b = contentService.parseContent(keyword);
        return b;
    }


    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> search(
            @PathVariable("") String keyword
            , @PathVariable("pageNo") int pageNo
            , @PathVariable("pageSize") int pageSize
                          ) throws Exception {
        List<Map<String, Object>> b = contentService.searchPage(keyword, pageNo, pageSize);
        return b;
    }
}
