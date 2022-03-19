package com.service;

import com.pojo.Like;
import java.util.List;

/**
 * 收藏服务类
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
public interface LikeService {
    /**
     * 根据业主ID查询全部收藏
     *
     * @param id 业主ID
     * @return {@link List<Like> }
     */
    List<Like> findLike(String id);

    /**
     * 新增收藏
     *
     * @param pno      车位编号
     * @param ano      区域编号
     * @param cno      小区编号
     * @param oacconut 业主账号
     * @return {@link List<Like> }
     */
    List<Like> addLike(String pno, Integer ano, Integer cno, String oacconut);

    /**
     * 查询收藏
     *
     * @param pno      车位编号
     * @param ano      区域编号
     * @param cno      小区编号
     * @param oacconut 业主账号
     * @return {@link List<Like> }
     */
    List<Like> queryLike(String pno, Integer ano, Integer cno, String oacconut);

    /**
     * 取消收藏
     *
     * @param pno      车位编号
     * @param ano      区域编号
     * @param cno      小区编号
     * @param oacconut 业主账号
     * @return void
     */
    void deleteLike(String pno, Integer ano, Integer cno, String oacconut);
}
