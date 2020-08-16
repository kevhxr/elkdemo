package com.hxr.elkdemo.controller;

import com.hxr.elkdemo.entity.Item;
import com.hxr.elkdemo.entity.Poet;
import com.hxr.elkdemo.service.ItemRepoService;
import com.hxr.elkdemo.service.ItemTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/es")
public class EsController {

    @Autowired
    ItemRepoService itemService;
    @Autowired
    ItemTemplateService templateService;
    @RequestMapping("/test")
    public void test() {
        System.out.println("test");
    }

    @RequestMapping("/add")
    public void addData() {
        templateService.addItem(generateItems());
    }

    @RequestMapping("/addpo")
    public void addPoet() {
        itemService.addPoet();
    }

    @RequestMapping("/getp1")
    @ResponseBody
    public Object getPoet() {
        return templateService.getPoet1();
    }
    @RequestMapping("/getpa")
    @ResponseBody
    public List<Map<String, Object>> getPoetAll() {
        return templateService.getPoetAll();
    }


    public List<Item> generateItems() {
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        Item item2 = new Item();
        item.setId(1l);
        item.setBrand("Nike");
        item.setPrice(500d);
        item.setTitle("James");
        item2.setId(2l);
        item2.setBrand("Adiddas");
        item2.setPrice(500d);
        item2.setTitle("Durant");
        items.add(item);
        items.add(item2);
        return items;
    }

    @RequestMapping(value = "/get/{title}")
    @ResponseBody
    public List<Map<String, Object>> getData(@PathVariable("title") String title) {
        //itemService.findItem();
        return templateService.findItem(title);
    }

    @RequestMapping("/getall")
    @ResponseBody
    public List<Map<String, Object>> getAllData() {
        return templateService.matchAll();
    }


    @RequestMapping("/del")
    public void deleteData() {
        itemService.deleteItem();
    }


    @RequestMapping("/update")
    public void updateData() {
        Item item = new Item();
        item.setId(1l);
        item.setBrand("Nike");
        item.setPrice(1000d);
        item.setTitle("Kobe");
        itemService.updateItem(item);
    }

}
