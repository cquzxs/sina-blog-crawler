package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.model.IpProxyModel;
import com.zxs.ssh.template.model.Queue.IpQueue;
import com.zxs.ssh.template.service.ip.api.IIpProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Project Name:blog-crawler
 * File Name:QueryIpThread
 * Package Name:com.zxs.ssh.template.thread
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:从数据库中查询代理ip加入到ip队列中
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class QueryIpThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(QueryIpThread.class);
    private boolean isBreakdown = false; //线程是否崩溃
    private int startIndex = 0;//开始索引
    private static final int maxCount = 100; //最大个数
    private static final long QUERY_INTERVAL = 10 * 1000; //两次查询时间间隔

    private IIpProxyService ipProxyService;

    public QueryIpThread(IIpProxyService ipProxyService) {
        this.ipProxyService = ipProxyService;
    }

    /**
     * 从数据库中查询代理ip加入到ip队列中
     */
    @Override
    public void run() {
        while (!isBreakdown) {
            try {
                List<IpProxyModel> ipProxyModels = this.ipProxyService.queryModels(startIndex, maxCount);
                if (ipProxyModels != null && !ipProxyModels.isEmpty()) {
                    for (int i = 0; i < ipProxyModels.size(); i++) {
                        IpQueue.push(ipProxyModels.get(i));
                    }
                    startIndex = startIndex + maxCount;
                } else {
                    startIndex = 0;
                }
                Thread.sleep(QUERY_INTERVAL);
            } catch (Exception e) {
                logger.info("查询代理ip异常", e);
            }
        }
        logger.info("查询代理ip线程已停止");
    }

    public void setBreakdown(boolean breakdown) {
        isBreakdown = breakdown;
    }
}
