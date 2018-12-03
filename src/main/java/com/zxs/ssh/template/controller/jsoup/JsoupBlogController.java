package com.zxs.ssh.template.controller.jsoup;

import com.zxs.ssh.template.util.IpProxyUtil;
import org.apache.regexp.RE;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

/**
 * Project Name:blog-crawler
 * File Name:JsoupBlogController
 * Package Name:com.zxs.ssh.template.controller.jsoup
 * Date:2018/12/3
 * Author:zengxueshan
 * Description:通过jsoup方式爬取用户博客内容
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("jsoupBlogController")
public class JsoupBlogController {

    private static final Logger logger = LoggerFactory.getLogger(JsoupBlogController.class);

    /**
     * 通过jsoup方式爬取用户博客内容
     *
     * @return 博客内容
     */
    @RequestMapping("jsoup/crawlerBlog")
    public String crawlerBlog(){
        String res = new Timestamp(System.currentTimeMillis()).toString();
        long startTime = System.currentTimeMillis();
        IpProxyUtil.setIpProxy("219.234.5.128:3128");
        String url = "https://m.weibo.cn/api/container/getIndex?type=uid&value=1195230310&containerid=1076031195230310&page=1";
        Document document = null;
        try{
            document = Jsoup.connect(url).ignoreContentType(true).timeout(20000).data().get();
        }catch (Exception e){

        }
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        if(document != null){
            getBlogInfo(document);
        }
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取博客信息
     *
     * @param document 文档
     */
    private void getBlogInfo(Document document) {
        try{
            JSONObject jsonObject = new JSONObject(document.body().text());
            JSONArray blogInfoJsonArray = jsonObject.getJSONObject("data").getJSONArray("cards");
            System.out.println("当前页博客总数" + blogInfoJsonArray.length());
            for (int i = 0; i < blogInfoJsonArray.length(); i++) {
                try{
                    System.out.println("****************第"+(i+1)+"条博客******************");
                    JSONObject blogInfoJsonObject = blogInfoJsonArray.getJSONObject(i).getJSONObject("mblog");
                    System.out.println("博客创建时间：" + blogInfoJsonObject.get("created_at").toString());
                    System.out.println("博客内容：" + blogInfoJsonObject.get("text").toString());
                    System.out.println("博客转发数：" + blogInfoJsonObject.get("reposts_count").toString());
                    System.out.println("博客评论数：" + blogInfoJsonObject.get("comments_count").toString());
                    System.out.println("博客点赞数：" + blogInfoJsonObject.get("attitudes_count").toString());
                    System.out.println("博客id：" + blogInfoJsonObject.get("id").toString());
                }catch (Exception e){
                    logger.info("博客信息获取失败");
                }

            }
        }catch (Exception e){
            logger.info("博客信息获取失败", e);
        }
    }

    /**
     * 将document转换为json字符串
     *
     * @param document 文档
     * @return json字符串
     */
    private String getJsonStr(Document document) {
        String json = "";
        int startIndex = 0;
        int endIndex = 0;
        for (int i = 0; i < document.toString().length(); i++) {
            String temp = document.toString().charAt(i) + "";
            if(temp.equals("{")){
                startIndex = i;
                break;
            }
        }
        for (int i = document.toString().length()-1; i >= 0; i--) {
            String temp = document.toString().charAt(i) + "";
            if(temp.equals("}")){
                endIndex = i;
                break;
            }
        }
        json = document.toString().substring(startIndex,endIndex+1);
        return json;
    }
}
