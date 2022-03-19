package com.dao;

import com.pojo.Like;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 收藏DAO
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
public interface LikeDao {
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
    @Select("Insert into `like` (Pno,Ano,Cno,Oacconut) values(#{pno},#{ano},#{cno},#{oacconut}) ")
    List<Like> addLike(@Param("pno") String pno, @Param("ano") int ano, @Param("cno") int cno, @Param("oacconut") String oacconut);

    /**
     * 查询收藏
     *
     * @param pno      车位编号
     * @param ano      区域编号
     * @param cno      小区编号
     * @param oacconut 业主账号
     * @return {@link List<Like> }
     */
    @Select("Select * from `like` Where Pno = #{pno} and Ano = #{ano} and Cno= #{cno} and Oacconut= #{oacconut} ")
    List<Like> queryLike(@Param("pno") String pno, @Param("ano") int ano, @Param("cno") int cno, @Param("oacconut") String oacconut);

    /**
     * 取消收藏
     *
     * @param pno      车位编号
     * @param ano      区域编号
     * @param cno      小区编号
     * @param oacconut 业主账号
     * @return void
     */
    @Select("Delete from `like` Where Pno = #{pno} and Ano = #{ano} and Cno= #{cno} and Oacconut= #{oacconut} ")
    void deleteLike(@Param("pno") String pno, @Param("ano") Integer ano, @Param("cno") Integer cno, @Param("oacconut") String oacconut);
}
