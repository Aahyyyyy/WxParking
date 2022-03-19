package com.service.impl;
import com.pojo.*;
import com.dao.ParkingDao;
import com.service.ParkingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("parking_detailService")
public class ParkingServiceImpl implements ParkingService {
    @Autowired
    private ParkingDao parkingDao;

    @Override
    public List<Parking> queryAllParking(String ano, Integer cno, Integer bno) {
        return parkingDao.queryAllParking(ano, cno, bno);
    }

    @Override
    public List<Parking> queryAllParking1(Integer cno, Integer bno) {
        return parkingDao.queryAllParking1(cno,bno);
    }

    @Override
    public List<Area> allParkingAname(String cno) {
        return parkingDao.allParkingAname(cno);
    }

    @Override
    public void updateParkStatus(String pno, String ano) {
         parkingDao.updateParkStatus(pno,ano);
    }

    @Override
    public List<Parking> queryParkingPstatus(String ano, Integer cno, String pstatus, Integer bno) {
        return parkingDao.queryParkingPstatus(ano, cno, pstatus, bno);
    }

    @Override
    public List<Parking> queryParkingAstatus(String pno) {
        return parkingDao.queryParkingAstatus(pno);
    }

    @Override
    public List<Parking> queryParkingInfoPno(String pno) {
        return parkingDao.queryParkingInfoPno(pno);
    }

    @Override
    public List<Parking> getParkByKey(String key , String ano, Integer cno, Integer bno) {
        return parkingDao.getParkByKey(key, ano, cno, bno);
    }

    @Override
    public List<ViewParkingCount> findParkingCountAll() { return parkingDao.findParkingCountAll(); }

    @Override
    public List<ViewParkingCount> findParkingCount(String cno) { return parkingDao.findParkingCount(cno); }
}
