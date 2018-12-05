package com.zxs.ssh.template.controller.jsoup;

import com.zxs.ssh.template.util.IpProxyUtil;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project Name:blog-crawler
 * File Name:InfoController
 * Package Name:com.zxs.ssh.template.controller.jsoup
 * Date:2018/12/3
 * Author:zengxueshan
 * Description:通过jsoup方式爬取用户信息
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("jsoupInfoController")
public class JsoupInfoController {

    private static final Logger logger = LoggerFactory.getLogger(JsoupInfoController.class);

    /**
     * 通过jsoup方式爬取用户信息
     *
     * @return 用户信息
     */
    @RequestMapping("jsoup/crawlerInfo")
    public String crawlerInfo(){
        String res = "ok";
        long startTime = System.currentTimeMillis();
        IpProxyUtil.setIpProxy("219.234.5.128:3128");
        String url = "https://m.weibo.cn/api/container/getIndex?type=uid&value=1195230310&containerid=1005051195230310";
        Document document = null;
        try{
            document = Jsoup.connect(url).ignoreContentType(true).timeout(200000).data().get();
        }catch (Exception e){

        }
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        if(document != null){
            getUserInfo(document);
        }
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取用户信息
     *
     * @param document 文档
     */
    private void getUserInfo(Document document) {
        try{
            JSONObject jsonObject = new JSONObject(document.body().text());
            JSONObject userInfoJsonObject = jsonObject.getJSONObject("data").getJSONObject("userInfo");
            System.out.println("用户名：" + userInfoJsonObject.get("screen_name"));
            System.out.println("头像：" + userInfoJsonObject.get("avatar_hd"));
            System.out.println("介绍信息：" + userInfoJsonObject.get("verified_reason"));
            System.out.println("微博等级：" + userInfoJsonObject.get("urank"));
            System.out.println("关注数：" + userInfoJsonObject.get("follow_count"));
            System.out.println("粉丝数：" + userInfoJsonObject.get("followers_count"));
            System.out.println("微博数：" + userInfoJsonObject.get("statuses_count"));
        }catch (Exception e){
            logger.info("用户信息获取失败", e);
        }

    }
}
