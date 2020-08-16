package com.xr.elkmarket.utils;

import com.xr.elkmarket.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class HtmlParseUtil {

    public static void main(String[] args) throws Exception {
        new HtmlParseUtil().parseJD("java").forEach(a-> System.out.println(a));
    }

    public List<Content> parseJD(String keyWords) throws Exception {

        List<Content> productList = new ArrayList<>();

        String url = "https://search.jd.com/Search?keyword=" + keyWords;
        Document document = Jsoup.parse(new URL(url), 30000);
        Element element = document.getElementById("J_goodsList");
        System.out.println(element.html());
        Elements lis = element.getElementsByTag("li");
        for (Element li : lis) {
            String img = li.getElementsByTag("img").eq(0).attr("src");
            String price = li.getElementsByClass("p-price").eq(0).text();
            String title = li.getElementsByClass("p-name").eq(0).text();
            Content content = new Content();
            content.setImg(img);
            //System.out.println(img);
            content.setPrice(price);
            content.setTitle(title);
            productList.add(content);
        }
        return productList;
    }
}
