package com.zxs.ssh.template.dao.ip.impl;

import com.zxs.ssh.template.dao.common.api.ICommonDao;
import com.zxs.ssh.template.dao.ip.api.IIpProxyDao;
import com.zxs.ssh.template.model.IpProxyModel;
import com.zxs.ssh.template.model.Queue.IpQueue;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Name:blog-crawler
 * File Name:IpProxyDaoImpl
 * Package Name:com.zxs.ssh.template.dao.ip.impl
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:Ip代理Dao层 实现
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Repository("ipProxyDao")
public class IpProxyDaoImpl implements IIpProxyDao {

    private static final Logger logger = LoggerFactory.getLogger(IpProxyDaoImpl.class);

    @Resource(name = "commonDao")
    private ICommonDao commonDao;

    /**
     * 存储ip代理
     *
     * @param ipProxyModel ip代理
     */
    @Override
    public void saveModel(IpProxyModel ipProxyModel) {
        try{
            Session session = this.commonDao.getSession();
            session.save(ipProxyModel);
            session.flush();
            session.clear();
        }catch (Exception e){
            logger.info("存储ip代理异常", e);
        }
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
        List<IpProxyModel> res = new ArrayList<>();
        String hql = "from IpProxyModel";
        try{
            Session session = this.commonDao.getSession();
            Query<IpProxyModel> query = session.createQuery(hql,IpProxyModel.class);
            query.setFirstResult(startIndex);
            query.setMaxResults(maxCount);
            res = query.list();
            session.clear();
        }catch (Exception e){
            logger.info("查询代理ip列表异常", e);
        }
        return res;
    }
}
