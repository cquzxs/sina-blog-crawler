package com.zxs.ssh.template.dao.info.impl;

import com.zxs.ssh.template.dao.common.api.ICommonDao;
import com.zxs.ssh.template.dao.info.api.IBlogUserInfoDao;
import com.zxs.ssh.template.model.BlogUserInfoModel;
import com.zxs.ssh.template.thread.CrawlerInfoThread;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Project Name:blog-crawler
 * File Name:BlogUserInfoDaoImpl
 * Package Name:com.zxs.ssh.template.dao.info.impl
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:博客用户信息Dao 实现
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Repository("blogUserInfoDao")
public class BlogUserInfoDaoImpl implements IBlogUserInfoDao{

    private static final Logger logger = LoggerFactory.getLogger(BlogUserInfoDaoImpl.class);

    @Resource(name = "commonDao")
    private ICommonDao commonDao;

    /**
     * 存储博客用户信息
     *
     * @param blogUserInfoModel 博客用户信息
     */
    @Override
    public void saveModel(BlogUserInfoModel blogUserInfoModel) {
        try{
            Session session = this.commonDao.getSession();
            session.save(blogUserInfoModel);
            session.flush();
            session.clear();
        }catch (Exception e){
            logger.info("存储博客用户信息异常", e);
        }
    }
}
