package com.controller;

import com.pojo.Ordering;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/findOrder")
    public @ResponseBody List<Ordering> findOrder(String id) {
        return orderService.findOrder(id);
    }

    @RequestMapping("/deleteOrder")
    public void deleteOrder(HttpServletRequest request) {
        String orno = request.getParameter("orno");
        orderService.deleteOrder(orno);
    }

    @RequestMapping("/findOrderById")
    public @ResponseBody Ordering findOrderById(String orno) {
        return orderService.findOrderById(orno);
    }

    @RequestMapping("/insertOrder")
    public @ResponseBody void insertOrder(String ono, String pno, String ormonry, String ordate, String orstatue, String orpayment){
        orderService.addOrder(ono, pno, ormonry, ordate, orstatue, orpayment);
    }

    @RequestMapping("/findOrderByOrno")
    public @ResponseBody List findOrderOrno(Integer orno){
        return orderService.findOrderOrno(orno);
    }

    @RequestMapping("/findOrderByOther")
    public @ResponseBody List findOrderByOther(String ono,String pno){
        return orderService.findOrderByOther(ono,pno);
    }

    @RequestMapping("/orderUpdateStatueLast")
    public @ResponseBody List orderUpdateStatueLast(Integer orno, String orstatue, Double orlast) {
        return orderService.orderUpdateStatueLast(orno, orstatue, orlast);
    }

    @RequestMapping("/orderUpdateStatue")
    public @ResponseBody List orderUpdateStatue(Integer orno, String orstatue) {
        return orderService.orderUpdateStatue(orno, orstatue);
    }

    @RequestMapping("/orderUpdatePayway")
    public @ResponseBody List orderUpdatePayway(Integer orno, String orpayment) {
        return orderService.orderUpdatePayway(orno, orpayment);
    }
}
