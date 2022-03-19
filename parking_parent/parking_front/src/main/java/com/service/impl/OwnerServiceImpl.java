package com.service.impl;

import com.dao.OwnerDao;
import com.pojo.Owner;
import com.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private OwnerDao ownerDao;

    @Override
    public void doOwnerIden(Owner owner) {
        ownerDao.doOwnerIden(owner);
    }

    @Override
    public void doOwnerCert(Owner owner) {
        ownerDao.doOwnerCert(owner);
    }

    @Override
    public void doOwnerCheck(String id) {
        ownerDao.doOwnerCheck(id);
    }

    @Override
    public void doOwnerModify(Owner owner) {
        ownerDao.doOwnerModify(owner);
    }

    @Override
    public String getOname(String id) {
        return ownerDao.getOname(id);
    }

    @Override
    public Owner findOwnerById(String id) {
        return ownerDao.findOwnerById(id);
    }

    @Override
    public List<Owner> queryByOno(String ono) { return ownerDao.queryByOno(ono); };
}
