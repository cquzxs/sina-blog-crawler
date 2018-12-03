package com.zxs.ssh.template.controller.jsoup;

import com.zxs.ssh.template.util.IpProxyUtil;
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
 * File Name:JsoupCommentController
 * Package Name:com.zxs.ssh.template.controller.jsoup
 * Date:2018/12/3
 * Author:zengxueshan
 * Description:通过jsoup方式爬取用户博客评论
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("jsoupCommentController")
public class JsoupCommentController {

    private static final Logger logger = LoggerFactory.getLogger(JsoupCommentController.class);

    /**
     * 通过jsoup方式爬取用户博客评论
     *
     * @return 用户博客评论
     */
    @RequestMapping("jsoup/crawlerComment")
    public String crawlerComment(){
        String res = new Timestamp(System.currentTimeMillis()).toString();
        long startTime = System.currentTimeMillis();
        IpProxyUtil.setIpProxy("219.234.5.128:3128");
        String url = "https://m.weibo.cn/comments/hotflow?id=4312552961373487&mid=4312552961373487&max_id_type=0";
        Document document = null;
        try{
            document = Jsoup.connect(url).ignoreContentType(true).timeout(20000).data().get();
        }catch (Exception e){

        }
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        if(document != null){
            getBlogComment(document);
        }
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取博客评论
     *
     * @param document 文档
     */
    private void getBlogComment(Document document) {
        try{
            JSONObject jsonObject = new JSONObject(document.body().text());
            JSONArray blogCommentJsonArray = jsonObject.getJSONObject("data").getJSONArray("data");
            System.out.println("博客当前页评论总数" + blogCommentJsonArray.length());
            for (int i = 0; i < blogCommentJsonArray.length(); i++) {
                try{
                    System.out.println("****************第"+(i+1)+"条评论******************");
                    JSONObject blogCommentJsonObject = blogCommentJsonArray.getJSONObject(i);
                    System.out.println("评论者昵称：" + blogCommentJsonObject.getJSONObject("user").get("screen_name").toString());
                    System.out.println("评论者头像：" + blogCommentJsonObject.getJSONObject("user").get("avatar_hd").toString());
                    System.out.println("评论者id：" + blogCommentJsonObject.getJSONObject("user").get("id").toString());
                    System.out.println("评论创建时间：" + blogCommentJsonObject.get("created_at").toString());
                    System.out.println("评论内容：" + blogCommentJsonObject.get("text").toString());
                    System.out.println("评论点赞数：" + blogCommentJsonObject.get("like_count").toString());
                }catch (Exception e){
                    logger.info("博客信息获取失败");
                }

            }
        }catch (Exception e){
            logger.info("博客评论获取失败", e);
        }
    }
}
