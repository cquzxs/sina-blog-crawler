package com.zxs.ssh.template.model.Queue;

import com.zxs.ssh.template.model.IpProxyModel;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:blog-crawler
 * File Name:IpQueue
 * Package Name:com.zxs.ssh.template.model.Queue
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:IP队列
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class IpQueue {

    private static final LinkedBlockingQueue<IpProxyModel> ipProxyQueue = new LinkedBlockingQueue<>(); //代理ip队列

    /**
     * 入队
     *
     * @param ipProxyModel ip代理Model
     * @throws Exception 异常
     */
    public static void push(IpProxyModel ipProxyModel) throws Exception {
        ipProxyQueue.put(ipProxyModel);
    }

    /**
     * 出队
     *
     * @param timeout 超时时间
     * @return ip
     * @throws Exception 异常
     */
    public static IpProxyModel pull(long timeout) throws Exception {
        return ipProxyQueue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    public static int getQueueCount(){
        return ipProxyQueue.size();
    }
}
