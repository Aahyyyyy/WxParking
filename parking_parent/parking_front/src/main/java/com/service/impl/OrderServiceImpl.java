package com.service.impl;

import com.dao.OrderDao;
import com.pojo.Ordering;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Override
    public List<Ordering> findOrder(String id) {
        return orderDao.findOrder(id);
    }

    @Override
    public void deleteOrder(String orno) {
        orderDao.deleteOrder(orno);
    }

    @Override
    public Ordering findOrderById(String orno) {
        return orderDao.findOrderById(orno);
    }

    @Override
    public Integer addOrder(String ono, String pno, String ormonry, String ordate, String orstatue, String orpayment) {
        return orderDao.addOrder(ono, pno, ormonry, ordate, orstatue, orpayment);
    }

    @Override
    public List<Ordering> findOrderOrno(Integer orno) { return orderDao.findOrderOrno(orno); };

    @Override
    public List<Ordering> findOrderByOther(String ono, String pno) { return orderDao.findOrderByOther(ono, pno); };

    @Override
    public List<Ordering> orderUpdateStatueLast(Integer orno, String orstatue, Double orlast) {
        return orderDao.orderUpdateStatueLast(orno, orstatue, orlast);
    }

    @Override
    public List<Ordering> orderUpdateStatue(Integer orno, String orstatue) {
        return orderDao.orderUpdateStatue(orno, orstatue);
    }

    @Override
    public List<Ordering> orderUpdatePayway(Integer orno, String orpayment) {
        return orderDao.orderUpdatePayway(orno, orpayment);
    }
}
