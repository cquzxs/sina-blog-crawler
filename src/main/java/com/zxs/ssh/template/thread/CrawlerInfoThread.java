package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.model.BlogUserInfoModel;
import com.zxs.ssh.template.model.Queue.IpQueue;
import com.zxs.ssh.template.model.Queue.UserInfoModelQueue;
import com.zxs.ssh.template.model.Queue.UserInfoUrlQueue;
import com.zxs.ssh.template.service.info.api.IBlogUserInfoService;
import com.zxs.ssh.template.util.IpProxyUtil;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

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
    //初始用户信息接口地址，通过其关注的人和评论的人不断迭代(主页：https://m.weibo.cn/u/1195230310)
    private static final String originUrl = "https://m.weibo.cn/api/container/getIndex?type=uid&value=1195230310&containerid=1005051195230310";
    private boolean isBreakdown = false; //线程是否崩溃
    private static final int MAX_IP_COUNT = 100;//每个IP处理任务的最大值
    private static final long QUEUE_PULL_TIME_OUT = 5000;//出队超时时间
    private int ipCount = 0;//处理10个任务换一个ip
    private String ip = "219.234.5.128:3128"; //当前使用的代理ip

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
            if(!UserInfoUrlQueue.userInfoUrlList.contains(originUrl)){
                UserInfoUrlQueue.userInfoUrlList.add(originUrl);
                UserInfoUrlQueue.push(originUrl);
            }
        } catch (Exception e) {
            logger.info("初始化失败");
        }
        while (!isBreakdown) {
            try {
                Thread.sleep(10 * 1000);
                while (UserInfoUrlQueue.getQueueCount() > 0 && IpQueue.getQueueCount() > 0 && !isBreakdown) {
                    if (ipCount >= MAX_IP_COUNT) {
                        ip = IpQueue.pull(QUEUE_PULL_TIME_OUT).getIp();
                    }
                    if (ip == null) {
                        return;
                    }
                    String url = UserInfoUrlQueue.pull(QUEUE_PULL_TIME_OUT);
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
                    }
                    ipCount++;
                }
            } catch (Exception e) {
                logger.info("爬取用户信息异常", e);
                isBreakdown = true;
            }
        }
        logger.info("爬取用户信息线程已停止");
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
                //logger.info(blogUserInfoModel.getId()+" 用户无认证信息");
            }
            blogUserInfoModel.setBlogLevel(Integer.parseInt(userInfoJsonObject.get("urank").toString()));
            blogUserInfoModel.setFollowCount(Integer.parseInt(userInfoJsonObject.get("follow_count").toString()));
            blogUserInfoModel.setFansCount(Integer.parseInt(userInfoJsonObject.get("followers_count").toString()));
            blogUserInfoModel.setBlogCount(Integer.parseInt(userInfoJsonObject.get("statuses_count").toString()));
            blogUserInfoModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
            blogUserInfoModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            this.blogUserInfoService.saveModel(blogUserInfoModel);
            UserInfoModelQueue.push(blogUserInfoModel);
        } catch (Exception e) {
            logger.info("用户信息获取失败", e);
        }
    }

    public void setBreakdown(boolean breakdown) {
        isBreakdown = breakdown;
    }
}