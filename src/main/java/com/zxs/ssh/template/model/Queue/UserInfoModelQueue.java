package com.zxs.ssh.template.model.Queue;

import com.zxs.ssh.template.model.BlogUserInfoModel;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:blog-crawler
 * File Name:UserInfoModelQueue
 * Package Name:com.zxs.ssh.template.model.Queue
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:用户信息队列,可构造用户关注列表接口地址
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class UserInfoModelQueue {

    private static final LinkedBlockingQueue<BlogUserInfoModel> blogUserInfoModelQueue = new LinkedBlockingQueue<>(); //用户信息队列

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

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    public static int getQueueCount(){
        return blogUserInfoModelQueue.size();
    }
}
