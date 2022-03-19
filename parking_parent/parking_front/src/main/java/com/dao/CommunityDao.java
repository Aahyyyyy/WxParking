package com.dao;

import com.pojo.*;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 小区DAO
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
@Repository
public interface CommunityDao {
    /**
     * 查询全部小区
     *
     * @return {@link List<Community> }
     */
    List<Community> findAll();

    /**
     * 根据城市筛选小区
     *
     * @param address 地址
     * @return {@link List<Community> }
     */
    @Select("select * from community where Ccity = #{address}")
    List<Community> findAllByCity(String address);

    /**
     * 根据小区编号查找小区
     *
     * @param cno 小区编号
     * @return {@link List<Community> }
     */
    @Select("Select * from community Where Cno=#{cno}")
    List<Community> queryComByCno(String cno);

    /**
     * 根据小区编号查找小区详细信息
     *
     * @param cno 小区编号
     * @return {@link List<Community> }
     */
    @Select("Select * from com_detail Where Cno=#{cno}")
    List<Community> queryByCno(String cno);

    /**
     * 根据搜索词模糊查找小区
     *
     * @param key 搜索词
     * @return {@link List<Community> }
     */
    List<Community> getComByKey(String key);
}
