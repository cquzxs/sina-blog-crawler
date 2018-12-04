package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.model.BlogUserInfoModel;
import com.zxs.ssh.template.service.info.api.IBlogUserInfoService;
import com.zxs.ssh.template.util.IpProxyUtil;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:weibo-crawler
 * File Name:CrawlerInfoThread
 * Package Name:com.zxs.ssh.template.thread
 * Date:2018/11/21
 * Author:zengxueshan
 * Description:爬取用户信息线程
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class CrawlerInfoThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerInfoThread.class);
    //初始用户网页，通过其关注的人和评论的人不断迭代(https://m.weibo.cn/u/1195230310)
    private static final String originUrl = "https://m.weibo.cn/api/container/getIndex?type=uid&value=1195230310&containerid=1005051195230310";
    private static final LinkedBlockingQueue<String> userInfoUrlQueue = new LinkedBlockingQueue<>(); //用户主页url队列
    public static List<String> userInfoUrlList = Collections.synchronizedList(new ArrayList<>()); //用户主页url列表，用于去重
    private boolean isBreakdown = false; //线程是否崩溃
    private int ipCount = 0;//处理10个任务换一个ip
    private String ip = "219.234.5.128:3128"; //当前使用的代理ip
    private static int saveCount = 0;

    private IBlogUserInfoService blogUserInfoService;

    public CrawlerInfoThread(IBlogUserInfoService blogUserInfoService) {
        this.blogUserInfoService = blogUserInfoService;
    }

    /**
     * 爬取用户信息
     */
    @Override
    public void run() {
        try {
            if(!userInfoUrlList.contains(originUrl)){
                userInfoUrlList.add(originUrl);
                userInfoUrlQueue.put(originUrl);
            }
        } catch (Exception e) {
            logger.info("初始化失败");
        }
        while (!isBreakdown) {
            try {
                Thread.sleep(10 * 1000);
                System.out.println("爬取用户信息线程等待10秒");
                while (userInfoUrlQueue.size() > 0 && IpProxyThread.getQueueCount() > 0) {
                    if (ipCount >= 10) {
                        ip = IpProxyThread.pull(5000);
                    }
                    if (ip == null) {
                        return;
                    }
                    String url = pull(5000);
                    if (url == null) {
                        return;
                    }
                    IpProxyUtil.setIpProxy(ip);
                    Document document = null;
                    try {
                        document = Jsoup.connect(url).ignoreContentType(true).timeout(200000).data().get();
                    } catch (Exception e) {
                        logger.info("请求url异常");
                    }
                    if (document != null) {
                        getUserInfo(document);
                        saveCount++;
                    }
                    System.out.println("*************爬取用户信息线程当前代理ip:" + ip+"***************");
                    System.out.println("已爬取得用户数：" + userInfoUrlList.size());
                    System.out.println("剩余任务数：" + userInfoUrlQueue.size());
                    System.out.println("剩余代理ip数:" + IpProxyThread.getQueueCount());
                    System.out.println("已存储到数据库用户数：" + saveCount);
                    ipCount++;
                }
            } catch (Exception e) {
                logger.info("爬取用户信息异常", e);
                isBreakdown = true;
            }
        }
    }

    /**
     * 获取用户信息,并存入数据库
     *
     * @param document 文档
     */
    private void getUserInfo(Document document) {
        try {
            JSONObject jsonObject = new JSONObject(document.body().text());
            JSONObject userInfoJsonObject = jsonObject.getJSONObject("data").getJSONObject("userInfo");
            BlogUserInfoModel blogUserInfoModel = new BlogUserInfoModel();
            blogUserInfoModel.setUserId(userInfoJsonObject.get("id").toString());
            blogUserInfoModel.setUsername(userInfoJsonObject.get("screen_name").toString());
            blogUserInfoModel.setAvatar(userInfoJsonObject.get("avatar_hd").toString());
            try{
                blogUserInfoModel.setIntroduce(userInfoJsonObject.get("verified_reason").toString());//很多用户没有认证信息
            }catch (Exception e){

            }
            blogUserInfoModel.setBlogLevel(Integer.parseInt(userInfoJsonObject.get("urank").toString()));
            blogUserInfoModel.setFollowCount(Integer.parseInt(userInfoJsonObject.get("follow_count").toString()));
            blogUserInfoModel.setFansCount(Integer.parseInt(userInfoJsonObject.get("followers_count").toString()));
            blogUserInfoModel.setBlogCount(Integer.parseInt(userInfoJsonObject.get("statuses_count").toString()));
            blogUserInfoModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
            blogUserInfoModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.blogUserInfoService.saveModel(blogUserInfoModel);
            CrawlerFollowListThread.push(blogUserInfoModel);
        } catch (Exception e) {
            logger.info("用户信息获取失败", e);
        }
    }

    /**
     * 入队
     *
     * @param url 用户主页url
     * @throws Exception 异常
     */
    public static void push(String url) throws Exception {
        userInfoUrlQueue.put(url);
    }

    /**
     * 出队
     *
     * @param timeout 超时时间
     * @return url
     * @throws Exception 异常
     */
    public static String pull(long timeout) throws Exception {
        return userInfoUrlQueue.poll(timeout, TimeUnit.MILLISECONDS);
    }
}