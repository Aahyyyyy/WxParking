package com.dao;

import com.pojo.Ordering;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 订单DAO
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
public interface OrderDao {
    /**
     * 根据业主ID查询全部订单
     *
     * @param id 业主ID
     * @return {@link List<Ordering> }
     */
    List<Ordering> findOrder(String id);

    /**
     * 根据订单编号删除订单
     *
     * @param orno 订单编号
     * @return void
     */
    void deleteOrder(String orno);

    /**
     * 根据订单编号查询订单详情
     *
     * @param orno 订单编号
     * @return {@link Ordering }
     */
    Ordering findOrderById(String orno);

    /**
     * 新增订单
     *
     * @param ono       业主编号
     * @param pno       车位编号
     * @param ormonry   订单总金额
     * @param ordate    订单日期
     * @param orstatue  订单状态
     * @param orpayment 支付方式
     * @return {@link Integer }
     */
    @Select("Insert into `ordering` (Ono,Pno,ORmonry,ORdate,ORstatue,ORpayment) values(#{ono},#{pno},#{ormonry},#{ordate},#{orstatue},#{orpayment}) ")
    Integer addOrder(@Param("ono") String ono, @Param("pno") String pno, @Param("ormonry") String ormonry,
                     @Param("ordate") String ordate, @Param("orstatue") String orstatue, @Param("orpayment") String orpayment);

    /**
     * 根据订单编号查询订单
     *
     * @param ono 业主编号
     * @param pno 车位编号
     * @return {@link List<Ordering> }
     */
    @Select("Select * from ordering Where Ono=#{ono} and Pno = #{pno}")
    List<Ordering> findOrderByOther(@Param("ono") String ono, @Param("pno") String pno);

    /**
     * 根据业主编号和车位编号查询订单
     *
     * @param orno 订单编号
     * @return {@link List<Ordering> }
     */
    @Select("Select * from ordering Where ORno=#{orno}")
    List<Ordering> findOrderOrno(@Param("orno") Integer orno);

    /**
     * 更新订单状态并计算支付金额
     *
     * @param orno     订单编号
     * @param orstatue 订单状态
     * @param orlast   已支付金额
     * @return {@link List<Ordering> }
     */
    @Select("Update ordering set ORstatue= #{orstatue} , ORlast = #{orlast} Where  ORno= #{orno}")
    List<Ordering> orderUpdateStatueLast(@Param("orno") Integer orno, @Param("orstatue") String orstatue, @Param("orlast") Double orlast);

    /**
     * 更新订单状态
     *
     * @param orno     订单编号
     * @param orstatue 订单状态
     * @return {@link List<Ordering> }
     */
    @Select("Update ordering set ORstatue= #{orstatue} Where  ORno= #{orno}")
    List<Ordering> orderUpdateStatue(@Param("orno") Integer orno, @Param("orstatue") String orstatue);

    /**
     * 增加订单支付方式
     *
     * @param orno      订单编号
     * @param orpayment 订单支付方式
     * @return {@link List<Ordering> }
     */
    @Select("Update ordering set ORpayment= #{orpayment} Where  ORno= #{orno}")
    List<Ordering> orderUpdatePayway(@Param("orno") Integer orno, @Param("orpayment") String orpayment);
}
