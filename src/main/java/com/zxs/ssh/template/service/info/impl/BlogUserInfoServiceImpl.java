package com.zxs.ssh.template.service.info.impl;

import com.zxs.ssh.template.dao.info.api.IBlogUserInfoDao;
import com.zxs.ssh.template.dao.info.impl.BlogUserInfoDaoImpl;
import com.zxs.ssh.template.model.BlogUserInfoModel;
import com.zxs.ssh.template.service.info.api.IBlogUserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Project Name:blog-crawler
 * File Name:BlogUserInfoServiceImpl
 * Package Name:com.zxs.ssh.template.service.info.impl
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Service("blogUserInfoService")
@Transactional
public class BlogUserInfoServiceImpl implements IBlogUserInfoService{

    private static final Logger logger = LoggerFactory.getLogger(BlogUserInfoServiceImpl.class);

    @Resource(name = "blogUserInfoDao")
    private IBlogUserInfoDao blogUserInfoDao;

    /**
     * 存储博客用户信息
     *
     * @param blogUserInfoModel 博客用户信息
     */
    @Override
    public void saveModel(BlogUserInfoModel blogUserInfoModel) {
        this.blogUserInfoDao.saveModel(blogUserInfoModel);
    }
}
