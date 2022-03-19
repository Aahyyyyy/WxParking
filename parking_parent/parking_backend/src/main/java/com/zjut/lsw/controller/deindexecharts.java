package com.zjut.lsw.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjut.lsw.entity.Echars;
import com.zjut.lsw.entity.allorder;
import com.zjut.lsw.mapper.*;
import com.zjut.lsw.pojo.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/developer/index")
public class deindexecharts {
    /* @Autowired
     private ordercommunityinfoMapper ordercommunityinfoMapper;*/
    @Autowired
    private SalerMapper salerMapper;
    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private ViewParkingInfoMapper viewParkingInfoMapper;
    @Autowired
    private ViewCommunityMapper viewCommunityMapper;
    @Autowired
    private OwnerMapper ownerMapper;
    @Autowired
    private OrderingMapper orderingMapper;
    @Autowired
    private ViewOrderingInfoMapper orderingInfoMapper;
    @Autowired
    private AllorderMapper allorderMapper;
    @RequestMapping(value="/list")
    @ResponseBody
    public List<Echars> findById(Model model, HttpSession session) {
        List<Echars> list=new ArrayList<Echars>();
        Developer developer = (Developer) session.getAttribute("user");
        //查询到所有订单情况详细信息
        QueryWrapper<allorder> queryWrapper = new QueryWrapper<>();
        /* queryWrapper.eq("Sno",saler.getSno());*/
        List<allorder> lists = allorderMapper.selectList(queryWrapper);
        for(allorder allorder : lists){
            list.add(new Echars(allorder.getCname(),allorder.getNum()));
        }
        System.out.println(list.toString());
        model.addAttribute("lists",lists);
        return list;
    }


   /* @RequestMapping(value = "/EcharsShow")
    @ResponseBody
    public List<Echars> findById(Model model) {
        List<Echars> list = new ArrayList<Echars>();
        list.add(new Echars("帽子",50));
        list.add(new Echars("鞋子",126));
        list.add(new Echars("毛衣",75));
        list.add(new Echars("羽绒服",201));
        list.add(new Echars("羊毛衫",172));
        System.err.println(list.toString());

        return list;
    }*/

    /*@GetMapping(value = "/Echars.do")
    public String echarts4(Model model){
        System.err.println("========开始");
        return "login";
    }*/

}
