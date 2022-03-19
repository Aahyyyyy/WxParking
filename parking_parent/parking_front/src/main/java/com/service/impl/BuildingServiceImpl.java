package com.service.impl;

import com.dao.BuildingDao;
import com.pojo.Building;
import com.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Aahyyyyy
 * @date : 2021/12/21 20:26
 */
@Service
public class BuildingServiceImpl implements BuildingService {
    @Autowired
    private BuildingDao buildingDao;

    @Override
    public List<Building> findBuildByCno(Integer cno) {
        return buildingDao.findBuildByCno(cno);
    }
}
