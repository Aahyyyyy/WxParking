package com.zjut.lsw.controller;
/**
 * @author linshiwei
 * @date 2021 / 11 / 7  17 : 27
 * @version 3.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjut.lsw.mapper.*;
import com.zjut.lsw.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private SalerMapper salerMapper;
    @Autowired
    private DeveloperMapper developerMapper;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private ViewOrderingInfoMapper orderingInfoMapper;
    @Autowired
    private ViewCommunityMapper viewCommunityMapper;

    @RequestMapping(value = {"/login","/logout"})
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/user/login")
    public String login(@RequestParam("account")String account, @RequestParam("pwd") String pwd,
                        Model model, HttpSession session) {
        if (account.equals("root")) {
            Admin admin = adminMapper.selectOne(null);
            if (pwd.equals(admin.getAdminpassword())) {
                session.setAttribute("user", admin);
                //完成验证

                return "redirect:/admin/index";
            }
            model.addAttribute("msg", "密码错误！");
            return "login";
        }
        if (account.startsWith("S")) {
            List<Saler> salers = salerMapper.selectList(null);
            for (Saler saler : salers) {
                if (saler.getSacconut().equals(account)) {
                    if (pwd.equals(saler.getSpassword())) {
                        //完成验证
                        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
                        queryWrapper.eq("Sno",saler.getSno());
                        List<ViewOrderingInfo> viewOrderInfos = orderingInfoMapper.selectList(queryWrapper);

                        session.setAttribute("user", saler);
                        session.setAttribute("viewOrderInfos", viewOrderInfos);
                        return "redirect:/saler/index";
                    }
                    model.addAttribute("msg", "密码错误！");
                    return "login";
                }

            }
        }
        if (account.startsWith("D")) {
        List<Developer> developerList = developerMapper.selectList(null);
        for (Developer d : developerList) {
            if (account.equals(d.getDaccount())) {
                if (d.getDpassword().equals(pwd)) {

                    QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("Dno",d.getDno());
                    List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);

                    session.setAttribute("user", d);
                    session.setAttribute("viewCommunityinfos", viewCommunityinfos);
                    return "redirect:/developer/index";
                }
                model.addAttribute("msg", "密码错误！");
                return "login";
            }
        }
    }
        model.addAttribute("msg","账号不存在");
        return "login";
    }
}
