package com.zjut.lsw.controller;

/**
 * @author linshiwei
 * @date 2021 / 12 / 11  16: 53
 * @version 3.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjut.lsw.mapper.*;
import com.zjut.lsw.pojo.*;
import com.zjut.lsw.util.QiniuUtils;
import com.zjut.lsw.util.PictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/developer/contract")
public class TestQiNiuController {

    @Autowired
    private SalerMapper salerMapper;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private  ViewParkingInfoMapper viewParkingInfoMapper;
    @Autowired
    private InvoicecontractMapper invoicecontractMapper;
    @Autowired
    private ParkingMapper parkingMapper;

    @RequestMapping("/toIndex")
    public String index(){
        return "img";
    }

    @ResponseBody
    @RequestMapping(value = "/image", method = RequestMethod.POST)
    private String postContractUpDate(HttpServletRequest request,
                                      @RequestParam("Pno") String Pno,
                                      @RequestParam("file") MultipartFile multipartFile,
                                      Model model, HttpSession session) throws IOException {
        Developer developer = (Developer)session.getAttribute("user");

        System.out.println("Pno:"+Pno);
        // 用来获取其他参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String name = params.getParameter("username");
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = null;
            try {
                inputStream = (FileInputStream) multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = QiniuUtils.uploadQNImg(inputStream, PictureUtil.getRandomFileName());
            // KeyUtil.genUniqueKey()生成图片的随机名
            System.out.print("七牛云返回的图片链接:" + path);

            //ICno 逻辑处理操作
            int maxicno = 0;
            List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
            for(Invoicecontract invoicecontract : invoicecontracts){
                if(invoicecontract.getPno().equals(Pno))maxicno = invoicecontract.getICno();
            }

            // 存储path
            Invoicecontract contract = new Invoicecontract();
            contract.setICno(maxicno);
            contract.setContract(path);
            contract.setPno(Pno);
            QueryWrapper<Invoicecontract> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("ICno",contract.getICno());
            if (invoicecontractMapper.update(contract,queryWrapper2) > 0){
                System.out.println("contract更新成功！");
            }
            // 更新parking标记
            Parking park = new Parking();
            park.setPno(Pno);
            park.setPcontract("1");
            QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Pno",park.getPno());
            if (parkingMapper.update(park,queryWrapper3) > 0){
                System.out.println("parking更新成功！");
            }

            QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Dno",developer.getDno());
            List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
            for(ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
                else if(viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
                else if(viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
            }

            List<Community> communities = communityMapper.selectList(null);
            List<Area> areas = areaMapper.selectList(null);
            List<Saler> salers = salerMapper.selectList(null);

            List<ViewParkingInfo> parkingContoact = new ArrayList<>();
            List<ViewParkingInfo> noParkingContoact = new ArrayList<>();
            for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPcontract().equals("1"))parkingContoact.add(viewParkingInfo);
                else if(viewParkingInfo.getPinvoice().equals("0"))noParkingContoact.add(viewParkingInfo);
            }

            model.addAttribute("parkingContracts",parkingContoact);
            model.addAttribute("noParkingContracts",noParkingContoact);
            model.addAttribute("communities",communities);
            model.addAttribute("areas",areas);
            model.addAttribute("salers",salers);
            return "developer/contract";
        }
        return "developer/contract";
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST)
    private String postInvoiceUpDate(HttpServletRequest request,
                                     @RequestParam("Pno") String Pno,
                                     @RequestParam("file") MultipartFile multipartFile,
                                     Model model, HttpSession session) throws IOException {
        Developer developer = (Developer)session.getAttribute("user");
        // 用来获取其他参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String name = params.getParameter("username");
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = null;
            try {
                inputStream = (FileInputStream) multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = QiniuUtils.uploadQNImg(inputStream, PictureUtil.getRandomFileName());
            // KeyUtil.genUniqueKey()生成图片的随机名
            System.out.print("七牛云返回的图片链接:" + path);

            //ICno 逻辑处理操作
            int maxicno = 0;
            List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
            for(Invoicecontract invoicecontract : invoicecontracts){
                if(invoicecontract.getPno().equals(Pno))maxicno = invoicecontract.getICno();
            }

            // 存储path
            Invoicecontract invoice = new Invoicecontract();
            invoice.setICno(maxicno);
            invoice.setInvoice(path);
            invoice.setPno(Pno);
            QueryWrapper<Invoicecontract> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("ICno",invoice.getICno());
            if (invoicecontractMapper.update(invoice,queryWrapper2) > 0){
                System.out.println("invoice更新成功！");
            }
            // 更新parking标记
            Parking park = new Parking();
            park.setPno(Pno);
            park.setPinvoice("1");
            QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Pno",park.getPno());
            if (parkingMapper.update(park,queryWrapper3) > 0){
                System.out.println("parking更新成功！");
            }
            QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Dno",developer.getDno());
            List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
            for(ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
                else if(viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
                else if(viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
            }

            List<Community> communities = communityMapper.selectList(null);
            List<Area> areas = areaMapper.selectList(null);
            List<Saler> salers = salerMapper.selectList(null);

            List<ViewParkingInfo> parkingInvoice = new ArrayList<>();
            List<ViewParkingInfo> noParkingInvoice = new ArrayList<>();
            for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPinvoice().equals("1"))parkingInvoice.add(viewParkingInfo);
                else if(viewParkingInfo.getPinvoice().equals("0"))noParkingInvoice.add(viewParkingInfo);
            }

            model.addAttribute("parkingInvoices",parkingInvoice);
            model.addAttribute("noParkingInvoices",noParkingInvoice);
            model.addAttribute("communities",communities);
            model.addAttribute("areas",areas);
            model.addAttribute("salers",salers);
            return "developer/invoice";
        }
        return "developer/invoice";
    }

    @RequestMapping(value = "/saler/image", method = RequestMethod.POST)
    private String postSalerContraCTUpDate(HttpServletRequest request,
                                           @RequestParam("Pno") String Pno,
                                           @RequestParam("file") MultipartFile multipartFile,
                                           Model model, HttpSession session) throws IOException {
        Saler saler = (Saler) session.getAttribute("user");
        // 用来获取其他参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String name = params.getParameter("username");
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = null;
            try {
                inputStream = (FileInputStream) multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = QiniuUtils.uploadQNImg(inputStream, PictureUtil.getRandomFileName());
            // KeyUtil.genUniqueKey()生成图片的随机名
            System.out.print("七牛云返回的图片链接:" + path);

            //ICno 逻辑处理操作
            int maxicno = 0;
            List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
            for(Invoicecontract invoicecontract : invoicecontracts){
                if(invoicecontract.getPno().equals(Pno))maxicno = invoicecontract.getICno();
            }

            // 存储path
            Invoicecontract contract = new Invoicecontract();
            contract.setICno(maxicno);
            contract.setContract(path);
            contract.setPno(Pno);
            QueryWrapper<Invoicecontract> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("ICno",contract.getICno());
            if (invoicecontractMapper.update(contract,queryWrapper2) > 0){
                System.out.println("contract更新成功！");
            }
            // 更新parking标记
            Parking park = new Parking();
            park.setPno(Pno);
            park.setPcontract("1");
            QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Pno",park.getPno());
            if (parkingMapper.update(park,queryWrapper3) > 0){
                System.out.println("parking更新成功！");
            }

            QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Sno",saler.getSno());
            List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
            for(ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
                else if(viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
                else if(viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
            }

            List<Community> communities = communityMapper.selectList(null);
            List<Area> areas = areaMapper.selectList(null);
            List<Saler> salers = salerMapper.selectList(null);

            List<ViewParkingInfo> parkingContoact = new ArrayList<>();
            List<ViewParkingInfo> noParkingContoact = new ArrayList<>();
            for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPcontract().equals("1"))parkingContoact.add(viewParkingInfo);
                else if(viewParkingInfo.getPinvoice().equals("0"))noParkingContoact.add(viewParkingInfo);
            }

            model.addAttribute("parkingContracts",parkingContoact);
            model.addAttribute("noParkingContracts",noParkingContoact);
            model.addAttribute("communities",communities);
            model.addAttribute("areas",areas);
            model.addAttribute("salers",salers);
            return "saler/contract";
        }
        return "saler/contract";
    }

    @RequestMapping(value = "/saler/invoice", method = RequestMethod.POST)
    private String postSalerInvoiceUpDate(HttpServletRequest request,
                                          @RequestParam("Pno") String Pno,
                                          @RequestParam("file") MultipartFile multipartFile,
                                          Model model, HttpSession session) throws IOException {
        Saler saler = (Saler) session.getAttribute("user");
        // 用来获取其他参数
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        String name = params.getParameter("username");
        if (!multipartFile.isEmpty()) {
            FileInputStream inputStream = null;
            try {
                inputStream = (FileInputStream) multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = QiniuUtils.uploadQNImg(inputStream, PictureUtil.getRandomFileName());
            // KeyUtil.genUniqueKey()生成图片的随机名
            System.out.print("七牛云返回的图片链接:" + path);

            //ICno 逻辑处理操作
            int maxicno = 0;
            List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
            for(Invoicecontract invoicecontract : invoicecontracts){
                if(invoicecontract.getPno().equals(Pno))maxicno = invoicecontract.getICno();
            }

            // 存储path
            Invoicecontract invoice = new Invoicecontract();
            invoice.setICno(maxicno);
            invoice.setInvoice(path);
            invoice.setPno(Pno);
            QueryWrapper<Invoicecontract> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("ICno",invoice.getICno());
            if (invoicecontractMapper.update(invoice,queryWrapper2) > 0){
                System.out.println("invoice更新成功！");
            }
            // 更新parking标记
            Parking park = new Parking();
            park.setPno(Pno);
            park.setPinvoice("1");
            QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Pno",park.getPno());
            if (parkingMapper.update(park,queryWrapper3) > 0){
                System.out.println("parking更新成功！");
            }
            QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("Sno",saler.getSno());
            List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
            for(ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
                else if(viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
                else if(viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
            }

            List<Community> communities = communityMapper.selectList(null);
            List<Area> areas = areaMapper.selectList(null);
            List<Saler> salers = salerMapper.selectList(null);

            List<ViewParkingInfo> parkingInvoice = new ArrayList<>();
            List<ViewParkingInfo> noParkingInvoice = new ArrayList<>();
            for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
                if(viewParkingInfo.getPinvoice().equals("1"))parkingInvoice.add(viewParkingInfo);
                else if(viewParkingInfo.getPinvoice().equals("0"))noParkingInvoice.add(viewParkingInfo);
            }

            model.addAttribute("parkingInvoices",parkingInvoice);
            model.addAttribute("noParkingInvoices",noParkingInvoice);
            model.addAttribute("communities",communities);
            model.addAttribute("areas",areas);
            model.addAttribute("salers",salers);
            return "saler/invoice";
        }
        return "saler/invoice";
    }


}