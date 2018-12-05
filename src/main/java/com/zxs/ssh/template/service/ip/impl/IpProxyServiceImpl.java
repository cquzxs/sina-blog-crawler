package com.zxs.ssh.template.service.ip.impl;

import com.zxs.ssh.template.dao.ip.api.IIpProxyDao;
import com.zxs.ssh.template.dao.ip.impl.IpProxyDaoImpl;
import com.zxs.ssh.template.model.IpProxyModel;
import com.zxs.ssh.template.service.ip.api.IIpProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Project Name:blog-crawler
 * File Name:IpProxyServiceImpl
 * Package Name:com.zxs.ssh.template.service.ip.impl
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:Ip代理Service层 实现
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Service("ipProxyService")
@Transactional
public class IpProxyServiceImpl implements IIpProxyService{

    private static final Logger logger = LoggerFactory.getLogger(IpProxyServiceImpl.class);

    @Resource(name = "ipProxyDao")
    private IIpProxyDao ipProxyDao;

    /**
     * 存储ip代理
     *
     * @param ipProxyModel ip代理
     */
    @Override
    public void saveModel(IpProxyModel ipProxyModel) {
        this.ipProxyDao.saveModel(ipProxyModel);
    }

    /**
     * 查询代理ip列表
     *
     * @param startIndex 开始索引
     * @param maxCount   最大个数
     * @return 代理ip列表
     */
    @Override
    public List<IpProxyModel> queryModels(int startIndex, int maxCount) {
        return this.ipProxyDao.queryModels(startIndex, maxCount);
    }
}
