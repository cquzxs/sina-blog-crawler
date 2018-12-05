package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.model.BlogUserInfoModel;
import com.zxs.ssh.template.model.Queue.IpQueue;
import com.zxs.ssh.template.model.Queue.UserInfoModelQueue;
import com.zxs.ssh.template.model.Queue.UserInfoUrlQueue;
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
    private boolean isBreakdown = false; //线程是否崩溃
    private static final int MAX_IP_COUNT = 10;//每个IP处理任务的最大值
    private static final long QUEUE_PULL_TIME_OUT = 5000;//出队超时时间
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
                while (UserInfoModelQueue.getQueueCount() > 0 && IpQueue.getQueueCount() > 0){
                    if(ipCount >= MAX_IP_COUNT){
                        ip = IpQueue.pull(QUEUE_PULL_TIME_OUT).getIp();
                    }
                    if(ip == null){
                        return;
                    }
                    BlogUserInfoModel blogUserInfoModel = UserInfoModelQueue.pull(QUEUE_PULL_TIME_OUT);
                    if(blogUserInfoModel == null){
                        return;
                    }
                    IpProxyUtil.setIpProxy(ip);
                    getFollowList(blogUserInfoModel);
                    ipCount++;
                }
            } catch (Exception e) {
                logger.info("爬取用户关注列表异常", e);
                isBreakdown = true;
            }
        }
        logger.info("爬取用户关注列表线程已停止");
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
                        if(!UserInfoUrlQueue.userInfoUrlList.contains(infoUrl)){
                            UserInfoUrlQueue.userInfoUrlList.add(infoUrl);
                            UserInfoUrlQueue.push(infoUrl);
                        }
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
    }

    public void setBreakdown(boolean breakdown) {
        isBreakdown = breakdown;
    }
}
