package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.model.BlogUserInfoModel;
import com.zxs.ssh.template.util.IpProxyUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:blog-crawler
 * File Name:CrawlerFollowListThread
 * Package Name:com.zxs.ssh.template.thread
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:爬取用户关注列表线程
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class CrawlerFollowListThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(CrawlerInfoThread.class);
    private static final LinkedBlockingQueue<BlogUserInfoModel> blogUserInfoModelQueue = new LinkedBlockingQueue<>(); //用户信息队列
    private boolean isBreakdown = false; //线程是否崩溃
    private int ipCount = 0;//处理10个任务换一个ip
    private String ip = "219.234.5.128:3128"; //当前使用的代理ip

    /**
     * 爬取用户关注列表
     */
    @Override
    public void run() {
        while (!isBreakdown) {
            try {
                Thread.sleep(10*1000);
                System.out.println("爬取用户关注列表线程等待10秒");
                while (blogUserInfoModelQueue.size() > 0 && IpProxyThread.getQueueCount() > 0){
                    if(ipCount >= 10){
                        ip = IpProxyThread.pull(5000);
                    }
                    if(ip == null){
                        return;
                    }
                    BlogUserInfoModel blogUserInfoModel = blogUserInfoModelQueue.poll(5000, TimeUnit.MILLISECONDS);
                    if(blogUserInfoModel == null){
                        return;
                    }
                    IpProxyUtil.setIpProxy(ip);
                    getFollowList(blogUserInfoModel);
                    ipCount++;
                }
            } catch (Exception e) {
                logger.info("爬取用户信息异常", e);
                isBreakdown = true;
            }
        }
    }

    /**
     * 获取关注列表
     *
     * @param blogUserInfoModel 博客用户信息Model
     */
    private void getFollowList(BlogUserInfoModel blogUserInfoModel) {
        String userId = blogUserInfoModel.getUserId();
        int pageCount = blogUserInfoModel.getFollowCount() % 20 == 0 ? (blogUserInfoModel.getFollowCount() / 20) : (blogUserInfoModel.getFollowCount() / 20) + 1;
        for (int i = 1; i <= pageCount; i++) {
            String url = "https://m.weibo.cn/api/container/getIndex?containerid=231051_-_followers_-_" +
                    userId + "_-_1042015:tagCategory_050&type=uid&value=" + userId + "&page=" + i;
            Document document = null;
            try {
                document = Jsoup.connect(url).ignoreContentType(true).timeout(200000).data().get();
            } catch (Exception e) {
                logger.info("请求url异常");
            }
            if (document != null) {
                try {
                    JSONObject jsonObject = new JSONObject(document.body().text());
                    JSONArray temp = jsonObject.getJSONObject("data").getJSONArray("cards");
                    JSONArray followInfoJsonArray = temp.getJSONObject(temp.length()-1).getJSONArray("card_group");
                    for (int j = 0; j < followInfoJsonArray.length(); j++) {
                        String uid = followInfoJsonArray.getJSONObject(j).getJSONObject("user").get("id").toString();
                        String infoUrl = "https://m.weibo.cn/api/container/getIndex?type=uid&value=" + uid + "&containerid=100505" + uid;
                        if(!CrawlerInfoThread.userInfoUrlList.contains(infoUrl)){
                            CrawlerInfoThread.userInfoUrlList.add(infoUrl);
                            CrawlerInfoThread.push(infoUrl);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    /**
     * 入队
     *
     * @param blogUserInfoModel 用户信息Model
     * @throws Exception 异常
     */
    public static void push(BlogUserInfoModel blogUserInfoModel) throws Exception {
        blogUserInfoModelQueue.put(blogUserInfoModel);
    }

    /**
     * 出队
     *
     * @param timeout 超时时间
     * @return blogUserInfoModel
     * @throws Exception 异常
     */
    public static BlogUserInfoModel pull(long timeout) throws Exception {
        return blogUserInfoModelQueue.poll(timeout, TimeUnit.MILLISECONDS);
    }
}
