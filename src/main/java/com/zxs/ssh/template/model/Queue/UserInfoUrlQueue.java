package com.zxs.ssh.template.model.Queue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:blog-crawler
 * File Name:UserInfoUrlQueue
 * Package Name:com.zxs.ssh.template.model.Queue
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:用户信息接口地址队列
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class UserInfoUrlQueue {

    private static final LinkedBlockingQueue<String> userInfoUrlQueue = new LinkedBlockingQueue<>(); //用户信息接口地址队列

    public static List<String> userInfoUrlList = Collections.synchronizedList(new ArrayList<>()); //用户信息接口地址列表，用于去重

    /**
     * 入队
     *
     * @param url 用户信息接口地址
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

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    public static int getQueueCount(){
        return userInfoUrlQueue.size();
    }
}
