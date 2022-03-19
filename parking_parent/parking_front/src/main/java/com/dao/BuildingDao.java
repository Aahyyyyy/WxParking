package com.dao;

import com.pojo.Building;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 小区楼房DAO
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
public interface BuildingDao {
    /**
     * 根据小区编号查询小区全部楼房信息
     *
     * @param cno 小区编号
     * @return {@link List<Building> }
     */
    List<Building> findBuildByCno(Integer cno);
}
