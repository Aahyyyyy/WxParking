package com.service;

import com.pojo.*;
import java.util.List;

/**
 * 小区服务类
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
public interface CommunityService {
    /**
     * 查询全部小区
     *
     * @return {@link List<Community> }
     */
    List<Community> findAll();

    /**
     * 按小区编号查找小区
     *
     * @param cno 小区编号
     * @return {@link List<Community> }
     */
    List<Community> queryComByCno(String cno);

    /**
     * 根据城市筛选小区
     *
     * @param address 地址
     * @return {@link List<Community> }
     */
    List<Community> findAllByCity(String address);

    /**
     * 根据小区编号查找小区详细信息
     *
     * @param cno 小区编号
     * @return {@link List<Community> }
     */
    List<Community> queryByCno(String cno);

    /**
     * 按搜索词迷糊查找小区
     *
     * @param key 搜索词
     * @return {@link List<Community> }
     */
    List<Community> getComByKey(String key);
}
