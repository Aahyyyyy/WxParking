package com.service;

import com.pojo.Ordering;
import java.util.List;

/**
 * 订单服务类
 *
 * @author : 闫宏宇 付伊豪
 * @date : 2021/12/21
 */
public interface OrderService {
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
    Integer addOrder(String ono, String pno, String ormonry, String ordate, String orstatue, String orpayment);


    /**
     * 根据订单编号查询订单
     *
     * @param orno 订单编号
     * @return {@link List<Ordering> }
     */
    List<Ordering> findOrderOrno(Integer orno);

    /**
     * 根据业主编号和车位编号查询订单
     *
     * @param ono 业主编号
     * @param pno 车位编号
     * @return {@link List<Ordering> }
     */
    List<Ordering> findOrderByOther(String ono, String pno);

    /**
     * 更新订单状态并计算支付金额
     *
     * @param orno     订单编号
     * @param orstatue 订单状态
     * @param orlast   已支付金额
     * @return {@link List<Ordering> }
     */
    List<Ordering> orderUpdateStatueLast(Integer orno, String orstatue, Double orlast);

    /**
     * 更新订单状态
     *
     * @param orno     订单编号
     * @param orstatue 订单状态
     * @return {@link List<Ordering> }
     */
    List<Ordering> orderUpdateStatue(Integer orno, String orstatue);

    /**
     * 增加订单支付方式
     *
     * @param orno      订单编号
     * @param orpayment 订单支付方式
     * @return {@link List<Ordering> }
     */
    List<Ordering> orderUpdatePayway(Integer orno, String orpayment);
}
