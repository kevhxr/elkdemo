package com.hxr.elkdemo.service;


import com.hxr.elkdemo.entity.Item;
import com.hxr.elkdemo.entity.ItemRepo;
import com.hxr.elkdemo.entity.Poet;
import com.hxr.elkdemo.entity.PoetRepo;
import com.hxr.elkdemo.util.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ItemRepoService {

    Logger logger = LoggerFactory.getLogger(ItemRepoService.class);

    @Autowired
    ItemRepo itemRepo;
    @Autowired
    PoetRepo poetRepo;

    public void addItem(Item item) {
        Long id = itemRepo.save(item).getId();
        logger.info("{} been saved to es", id);
    }

    public void findItem() {
        Optional<Item> itemOp = itemRepo.findById(1l);
        if(!itemOp.isPresent()){
            logger.info("empty");
            return;
        }
        Item item = itemOp.get();
        logger.info("Get from es: {}", item);
    }

    public void deleteItem() {
        itemRepo.deleteById(1l);
        logger.info("delete success");
    }

    public void updateItem(Item item) {
        Long id = itemRepo.save(item).getId();
        logger.info("{} been updated to es", id);
    }

    public void addPoet(){
        Iterable<Poet> posts = poetRepo.findAll();
/*        if (posts.iterator().hasNext()) {
            return;
        }*/
        for (int i = 0; i < 40; i++) {
            Poet post = new Poet();
            post.setId(i);
            post.setTitle(DataGenerator.getTitle().get(i));
            post.setContent(DataGenerator.getContent().get(i));
            post.setWeight(i);
            post.setUserId(i % 10);
            poetRepo.save(post);
        }
    }
}
