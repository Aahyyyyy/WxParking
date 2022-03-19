package com.service;

import com.pojo.*;
import com.pojo.Area;

import java.util.List;

/**
 * 车位服务类
 *
 * @author : 付伊豪
 * @date : 2021/12/21
 */
public interface ParkingService {
    /**
     * 按区域，小区，楼盘编号查找车位
     *
     * @param ano 区域编号
     * @param cno 小区编号
     * @param bno 楼盘编号
     * @return {@link List<Parking> }
     */
    List<Parking> queryAllParking(String ano, Integer cno, Integer bno);

    /**
     * 按小区，楼盘编号查找车位
     *
     * @param cno 小区编号
     * @param bno 楼盘编号
     * @return {@link List<Parking> }
     */
    List<Parking> queryAllParking1(Integer cno, Integer bno);

    /**
     * 按小区编号查找所有区域
     *
     * @param cno 小区编号
     * @return {@link List<Area> }
     */
    List<Area> allParkingAname(String cno);

    /**
     * 根据区域，车位编号修改车位状态
     *
     * @param pno 车位编号
     * @param ano 区域编号
     * @return void
     */
    void updateParkStatus(String pno,String ano);

    /**
     * 根据区域，小区，楼盘编号以及车位的状态查找小区
     *
     * @param ano     区域编号
     * @param cno     小区编号
     * @param pstatus 车位状态
     * @param bno     楼盘编号
     * @return {@link List<Parking> }
     */
    List<Parking> queryParkingPstatus(String ano, Integer cno, String pstatus, Integer bno);

    /**
     * 根据车位编号查询该车位是否开盘
     *
     * @param pno 车位编号
     * @return {@link List<Parking> }
     */
    List<Parking> queryParkingAstatus(String pno);

    /**
     * 根据车位编号查询车位信息
     *
     * @param pno pno 车位编号
     * @return {@link List<Parking> }
     */
    List<Parking> queryParkingInfoPno(String pno);

    /**
     * 根据区域，小区，楼盘编号以及搜索词模糊查询车位
     *
     * @param key 搜索词
     * @param ano 区域编号
     * @param cno 小区编号
     * @param bno 楼盘编号
     * @return {@link List<Parking> }
     */
    public List<Parking> getParkByKey(String key, String ano, Integer cno, Integer bno);

    /**
     * 根据小区编号查找小区车位数据
     *
     * @param cno 小区编号
     * @return {@link List<ViewParkingCount> }
     */
    List<ViewParkingCount> findParkingCount(String cno);

    /**
     * 查找全部小区的车位信息
     *
     * @return {@link List<ViewParkingCount> }
     */
    List<ViewParkingCount> findParkingCountAll();
}
