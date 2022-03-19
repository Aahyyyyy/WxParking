package com.controller;

import com.pojo.*;
import com.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
@RequestMapping("/community")
public class CommunityController {
    @Autowired
    private CommunityService communityService;

    @RequestMapping("/findAll2")
    public @ResponseBody List findAll2(){
        return communityService.findAll();
    }

    @RequestMapping("/queryComByCno")
    public @ResponseBody List queryComByCno(String cno){
        return communityService.queryComByCno(cno);
    }

    @RequestMapping("/findAllByCity")
    public @ResponseBody List findAllByCity(String address) {
        return communityService.findAllByCity(address);
    }

    @RequestMapping("/findCom")
    public @ResponseBody List findCom(String cno) { return communityService.queryByCno(cno); }

    @RequestMapping("/getComByKey")
    public @ResponseBody List<Community> getComByKey(String key) {
        return communityService.getComByKey(key);
    }
}
