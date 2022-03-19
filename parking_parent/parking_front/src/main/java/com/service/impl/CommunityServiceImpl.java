package com.service.impl;

import com.dao.CommunityDao;
import com.pojo.*;
import com.service.CommunityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("CommunityService")
public class CommunityServiceImpl implements CommunityService {
    @Autowired
    private CommunityDao communityDao;
    @Override
    public List<Community> findAll() {
        return communityDao.findAll();
    };

    @Override
    public List<Community> queryComByCno(String cno) {
        return communityDao.queryComByCno(cno);
    };

    @Override
    public List<Community> findAllByCity(String address) {
        return communityDao.findAllByCity(address);
    }

    @Override
    public List<Community> queryByCno(String cno) { return communityDao.queryByCno(cno); };

    @Override
    public List<Community> getComByKey(String key) { return communityDao.getComByKey(key); }
}
