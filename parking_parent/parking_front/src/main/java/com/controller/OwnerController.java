package com.controller;

import com.pojo.Owner;
import com.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private OwnerService ownerService;

    @RequestMapping("/doOwnerIden")
    public void doOwnerIden(@RequestBody Owner owner) {
        ownerService.doOwnerIden(owner);
    }

    @RequestMapping("/doOwnerCert")
    public void doOwnerCert(@RequestBody Owner owner) {
        ownerService.doOwnerCert(owner);
    }

    @RequestMapping("/doOwnerCheck")
    @ResponseBody
    public void doOwnerCheck(String id) {
        ownerService.doOwnerCheck(id);
    }

    @RequestMapping("/doOwnerModify")
    public void doOwnerModify(@RequestBody Owner owner) {
        ownerService.doOwnerModify(owner);
    }

    @RequestMapping(value="/getOname", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String getOname(String id, HttpServletRequest request, HttpServletResponse response) {
        return ownerService.getOname(id);
    }

    @RequestMapping("/findOwnerById")
    @ResponseBody
    public Owner findOwnerById(String id) {
        return ownerService.findOwnerById(id);
    }

    @RequestMapping("/findOwnerStatus")
    public @ResponseBody
    List findOwnerStatus(String ono){
        return ownerService.queryByOno(ono);
    }
}
