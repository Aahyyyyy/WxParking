package com.controller;

import com.pojo.Building;
import com.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;


@Controller
@RequestMapping("/building")
public class BuildingController {
    @Autowired
    private BuildingService buildingService;

    @RequestMapping("/findBuildByCno")
    @ResponseBody
    public List<Building> findBuildByCno(Integer cno) {
        return buildingService.findBuildByCno(cno);
    }
}
