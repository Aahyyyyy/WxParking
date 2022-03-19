package com.dao;

import com.pojo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 车位DAO
 *
 * @author : 付伊豪
 * @date : 2021/12/21
 */
@Repository
public interface ParkingDao {
    /**
     * 按区域，小区，楼盘编号查找车位
     *
     * @param ano 区域编号
     * @param cno 小区编号
     * @param bno 楼盘编号
     * @return {@link List<Parking> }
     */
    @Select("Select * from parking_detail Where Ano = #{ano} and Cno = #{cno} and Bno = #{bno}")
    List<Parking> queryAllParking(@Param("ano") String ano, @Param("cno") Integer cno, @Param("bno") Integer bno);

    /**
     * 按小区，楼盘编号查找车位
     *
     * @param cno 小区编号
     * @param bno 楼盘编号
     * @return {@link List<Parking> }
     */
    @Select("Select * from parking_detail Where Cno = #{cno} and Bno = #{bno}")
    List<Parking> queryAllParking1(Integer cno, Integer bno);

    /**
     * 按小区编号查找所有区域
     *
     * @param cno 小区编号
     * @return {@link List<Area> }
     */
    @Select("Select * from area Where Cno = #{cno}")
    List<Area> allParkingAname(String cno);

    /**
     * 根据区域，车位编号修改车位状态
     *
     * @param pno 车位编号
     * @param ano 区域编号
     */
    @Update("Update parking set Pstatus=1 Where Pno = #{pno} And Ano = #{ano}")
    void updateParkStatus(@Param("pno") String pno, @Param("ano") String ano);

    /**
     * 根据区域，小区，楼盘编号以及车位的状态查找小区
     *
     * @param ano     区域编号
     * @param cno     小区编号
     * @param pstatus 车位状态
     * @param bno     楼盘编号
     * @return {@link List<Parking> }
     */
    @Select("Select * from parking_detail Where Ano = #{ano} and Cno = #{cno} and Pstatus= #{pstatus} and Bno = #{bno}")
    List<Parking> queryParkingPstatus(@Param("ano") String ano, @Param("cno") Integer cno, @Param("pstatus") String pstatus, @Param("bno") Integer bno);

    /**
     * 根据车位编号查询该车位是否开盘
     *
     * @param pno 车位编号
     * @return {@link List<Parking> }
     */
    @Select("Select Astatus from parking_detail Where Pno = #{pno} ")
    List<Parking> queryParkingAstatus(@Param("pno") String pno);

    /**
     * 根据车位编号查询车位信息
     *
     * @param pno pno 车位编号
     * @return {@link List<Parking> }
     */
    @Select("Select * from parking_detail Where Pno = #{pno} ")
    List<Parking> queryParkingInfoPno(@Param("pno") String pno);

    /**
     * 根据区域，小区，楼盘编号以及搜索词模糊查询车位
     *
     * @param key 搜索词
     * @param ano 区域编号
     * @param cno 小区编号
     * @param bno 楼盘编号
     * @return {@link List<Parking> }
     */
    @Select("select * from parking_detail where Pno like CONCAT('%', #{key}, '%') and Ano = #{ano} and Cno = #{cno} and  Bno = #{bno}")
    List<Parking> getParkByKey(@Param("key") String key, @Param("ano") String ano, @Param("cno") Integer cno, @Param("bno") Integer bno);

    /**
     * 根据小区编号查找小区车位数据
     *
     * @param cno 小区编号
     * @return {@link List<ViewParkingCount> }
     */
    @Select("Select * from parking_count Where Cno=#{cno}")
    List<ViewParkingCount> findParkingCount(String cno);

    /**
     * 查找全部小区的车位信息
     *
     * @return {@link List<ViewParkingCount> }
     */
    @Select("Select * from parking_count ")
    List<ViewParkingCount> findParkingCountAll();
}
