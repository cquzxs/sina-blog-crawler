package com.zxs.ssh.template.dao.ip.api;

import com.zxs.ssh.template.model.IpProxyModel;
import com.zxs.ssh.template.model.Queue.IpQueue;

import java.util.List;

/**
 * Project Name:blog-crawler
 * File Name:IIpProxyDao
 * Package Name:com.zxs.ssh.template.dao.ip.api
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:Ip代理Dao层 接口
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public interface IIpProxyDao {

    /**
     * 存储ip代理
     *
     * @param ipProxyModel ip代理
     */
    void saveModel(IpProxyModel ipProxyModel);

    /**
     * 查询代理ip列表
     *
     * @param startIndex 开始索引
     * @param maxCount   最大个数
     * @return 代理ip列表
     */
    List<IpProxyModel> queryModels(int startIndex, int maxCount);
}
