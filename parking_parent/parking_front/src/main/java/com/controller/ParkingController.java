package com.controller;

import com.pojo.*;
import com.service.ParkingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/parking")
public class ParkingController {
    @Autowired
    private ParkingService parkingService;

    @RequestMapping("/queryAllParking")
    public @ResponseBody List queryAllParking(String ano, Integer cno, Integer bno) {
        return parkingService.queryAllParking(ano, cno, bno);
    }

    @RequestMapping("/allparkingDetail1")
    public @ResponseBody List queryAllParking1(Integer cno, Integer bno) {
        return parkingService.queryAllParking1(cno, bno);
    }

    @RequestMapping("/allParkingAname")
    public @ResponseBody List allParkingAname(String cno) {
        return parkingService.allParkingAname(cno);
    }

    @RequestMapping("/updateParkStatus")
    public @ResponseBody void updateParkStatus(String pno, String ano) {
        parkingService.updateParkStatus(pno, ano);
    }

    @RequestMapping("/queryParkingPstatus")
    public @ResponseBody List queryParkingPstatus(String ano, Integer cno, String pstatus, Integer bno) {
        return parkingService.queryParkingPstatus(ano, cno, pstatus, bno);
    }

    @RequestMapping("/queryparking_Astatus")
    public @ResponseBody List queryParkingAstatus(String pno) {
        return parkingService.queryParkingAstatus(pno);
    }

    @RequestMapping("/queryParkingInfoPno")
    public @ResponseBody List queryParkingInfoPno(String pno) {
        return parkingService.queryParkingInfoPno(pno);
    }

    @RequestMapping("/getParkByKey")
    public @ResponseBody List<Parking> getParkByKey(String key, String ano, Integer cno, Integer bno) {
        return parkingService.getParkByKey(key, ano, cno, bno);
    }

    @RequestMapping("/findParkingCount")
    public @ResponseBody List findParkingCount(String cno){
        return parkingService.findParkingCount(cno);
    }

    @RequestMapping("/findParkingCountAll")
    public @ResponseBody List findParkingCountAll(){ return parkingService.findParkingCountAll(); }
}


