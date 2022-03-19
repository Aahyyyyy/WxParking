package com.zjut.lsw.controller;
/**
 * @author linshiwei
 * @date 2021 / 11 / 7  17 : 30
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
import java.util.ArrayList;
import java.util.List;

@Controller
public class SalerController {
    @Autowired
    private SalerMapper salerMapper;
    @Autowired
    private ParkingMapper parkingMapper;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private  ViewParkingInfoMapper viewParkingInfoMapper;
    @Autowired
    private  ViewCommunityMapper viewCommunityMapper;
    @Autowired
    private  OwnerMapper ownerMapper;
    @Autowired
    private   OrderingMapper orderingMapper;
    @Autowired
    private ViewOrderingInfoMapper orderingInfoMapper;
    @Autowired
    private InvoicecontractMapper invoicecontractMapper;

    /*个人信息主页*/
    @RequestMapping("/saler/information")
    String toInformation(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有订单信息
        session.setAttribute("user", saler);
        return "saler/information";
    }

    /*车位信息主页*/
    @RequestMapping("/saler/parking")
    String toParling(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        //  查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());

        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        // 映射销售状态
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        model.addAttribute("parkingInfo",viewParkingInfos);

        return "saler/parking";
    }

    /*车位信息查询页*/
    @RequestMapping("/saler/queryParking")
    public String queryParking(@RequestParam("queryCommunity")String queryCommunity,
                               @RequestParam("queryArea")String queryArea,
                               Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") ) {
            queryWrapper.eq("Sname", saler.getSname()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") ){
            queryWrapper.eq("Sname", saler.getSname()).eq("Aname", queryArea);
        } else if (!queryCommunity.equals("")  && queryArea.equals("") ){
            queryWrapper.eq("Sname", saler.getSname()).eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        model.addAttribute("parkingInfo",viewParkingInfos);
        return "saler/parking";
    }

    /*车位管理管理页 修改车位销售价格*/
    @RequestMapping("/saler/reviseParkingSaler")
    public String reviseParkingSaler(@RequestParam("hiddenPno")String hiddenPno,
                                     @RequestParam("Psalernuitprice")String Psalernuitprice,
                                     Model model,HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        Parking parking = new Parking();
        parking.setPno(hiddenPno);
        parking.setPsalernuitprice(Double.valueOf(Psalernuitprice));

        for (ViewParkingInfo viewParkingInfo  : viewParkingInfos) {
            System.out.println("finding ");
            if (viewParkingInfo.getPno().equals(hiddenPno)){
                System.out.println("find ");
                double psalernuitprice = Double.valueOf(Psalernuitprice);
                int pbuildarra = Integer.valueOf(viewParkingInfo.getPbuildarra());
                parking.setPsalerprice(Double.valueOf(String.valueOf(psalernuitprice * pbuildarra)));
            }
        }

        QueryWrapper<Parking> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("Pno",parking.getPno());
        if (parkingMapper.update(parking,queryWrapper2) > 0){
            System.out.println("parking更新成功！");
        }

        return "redirect:/saler/parking";
    }

    /*车位管理管理页 修改车位销售状态*/
    @RequestMapping("/saler/reviseParkingStatus")
    public String reviseParkingStatus(@RequestParam("hiddenRevisePno")String hiddenRevisePno,
                                      @RequestParam("Pstatus")String Pstatus,
                                      Model model,HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        if (Pstatus.equals("可售"))Pstatus="0";
        else if (Pstatus.equals("已预约"))Pstatus="1";
        else if (Pstatus.equals("已出售"))Pstatus="2";

        Parking parking = new Parking();
        parking.setPno(hiddenRevisePno);
        parking.setPstatus(Pstatus);

        QueryWrapper<Parking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Pno",parking.getPno());
        if (parkingMapper.update(parking,queryWrapper) > 0){
            System.out.println("parking更新成功！");
        }

        return "redirect:/saler/parking";
    }

    /*订单管理管理页*/
    @RequestMapping("/saler/ordermanage")
    String toOrderInfo(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有订单信息
        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewOrderingInfo> viewOrderInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        List<Parking> parkings = parkingMapper.selectList(null);
        List<Owner> owners = ownerMapper.selectList(null);
        List<Ordering> orderings = orderingMapper.selectList(null);

        // 设置List date
        List<String> date = new ArrayList<>();
        date.add(orderings.get(0).getORdate());
        for (Ordering ordering : orderings){
            for (int i = 0 ; i < date.size() ; i ++){
                if (date.get(i).equals(ordering.getORdate()))break;
                if (i == date.size() - 1)date.add(ordering.getORdate());
            }
        }

        model.addAttribute("viewOrderInfos",viewOrderInfos);
        model.addAttribute("parkings",parkings);
        model.addAttribute("owners",owners);
        model.addAttribute("orderings",orderings);
        session.setAttribute("date",date);

        return "saler/ordermanage";
    }

    /*订单信息查询页*/
    @RequestMapping("/saler/queryOrdering")
    public String queryOrdering(@RequestParam("queryOwner")String queryOwner,
                                @RequestParam("queryDate")String queryDate,@RequestParam("queryPayment")String queryPayment,
                                Model model, HttpSession session){
        session.setAttribute("date",session.getAttribute("date"));
        Saler saler = (Saler) session.getAttribute("user");
        List<Owner> owners = ownerMapper.selectList(null);
        if (queryPayment.equals("一次性"))queryPayment="1";
        else if (queryPayment.equals("分期"))queryPayment="2";
        else if (queryPayment.equals("按揭"))queryPayment="3";


        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryOwner.equals("")  && !queryDate.equals("")  && !queryPayment.equals("") ) {
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORdate", queryDate).eq("ORpayment", queryPayment);
        }else if (!queryOwner.equals("")  && !queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORdate", queryDate);
        }else if (!queryOwner.equals("")  && queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORpayment", queryPayment);
        }else if (queryOwner.equals("")  && !queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORdate", queryDate).eq("ORpayment", queryPayment);
        }else if (!queryOwner.equals("")  && queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner);
        }else if (queryOwner.equals("")  && !queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORdate", queryDate);
        }else if (queryOwner.equals("")  && queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORpayment", queryPayment);
        }

        List<ViewOrderingInfo> viewOrderingInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderingInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        model.addAttribute("viewOrderInfos",viewOrderingInfos);
        model.addAttribute("owners",owners);
        return "saler/ordermanage";
    }


    /*订单管理管理页 删除订单*/
    @RequestMapping("/saler/deleteOrder")
    public String deleteOrder(@RequestParam("hiddenORno")String hiddenORno,
                              HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        Ordering ordering = new Ordering();
        ordering.setORno(Integer.valueOf(hiddenORno));

        if (orderingMapper.deleteById(ordering.getORno())> 0) System.out.println("ORno删除成功！");;

        return "redirect:/saler/ordermanage";
    }


    /*订单管理管理页 修改订单*/
    @RequestMapping("/saler/reviseOrder")
    public String toReviseOrder(@RequestParam("hiddenORno2")String hiddenORno2,
                                @RequestParam("Oname")String Oname, @RequestParam("Pno")String Pno,
                                @RequestParam("ORmonry")String ORmonry, @RequestParam("ORdate")String ORdate,
                                @RequestParam("ORstatue")String ORstatue, @RequestParam("ORpayment")String ORpayment,
                                HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        Ordering ordering = new Ordering();
        ordering.setORno(Integer.valueOf(hiddenORno2));
        ordering.setPno(Pno);
        ordering.setORmonry(ORmonry);
        ordering.setORdate(ORdate);

        if (ORstatue.equals("未交预约金"))ORstatue="0";
        else if (ORstatue.equals("已预约"))ORstatue="1";
        else if (ORstatue.equals("未交定金"))ORstatue="2";
        else if (ORstatue.equals("待签约"))ORstatue="3";
        else if (ORstatue.equals("未交尾款"))ORstatue="4";
        else if (ORstatue.equals("已完成"))ORstatue="5";

        if (ORpayment.equals("一次性"))ORpayment="1";
        else if (ORpayment.equals("分期"))ORpayment="2";
        else if (ORpayment.equals("按揭"))ORpayment="3";

        ordering.setORstatue(ORstatue);
        ordering.setORpayment(ORpayment);

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner  : owners) {
            System.out.println("finding ");
            if (owner.getOname().equals(Oname)){
                ordering.setOno(owner.getOno());
            }
        }

        QueryWrapper<Ordering> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ORno",ordering.getORno());
        if (orderingMapper.update(ordering,queryWrapper) > 0){
            System.out.println("order更新成功！");
        }
        return "redirect:/saler/ordermanage";
    }

    /*订单管理管理页 增加订单*/
    @RequestMapping("/saler/addOrder")
    public String addOrdering(@RequestParam("Oname")String Oname,@RequestParam("Pno")String Pno,
                              @RequestParam("ORmonry")String ORmonry, @RequestParam("ORdate")String ORdate,
                              @RequestParam("ORstatue")String ORstatue, @RequestParam("ORpayment")String ORpayment,
                              HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        if (ORstatue.equals("未交预约金"))ORstatue="0";
        else if (ORstatue.equals("已预约"))ORstatue="1";
        else if (ORstatue.equals("未交定金"))ORstatue="2";
        else if (ORstatue.equals("待签约"))ORstatue="3";
        else if (ORstatue.equals("未交尾款"))ORstatue="4";
        else if (ORstatue.equals("已完成"))ORstatue="5";

        if (ORpayment.equals("一次性"))ORpayment="1";
        else if (ORpayment.equals("分期"))ORpayment="2";
        else if (ORpayment.equals("按揭"))ORpayment="3";

        Ordering ordering = new Ordering();
        ordering.setPno(Pno);
        ordering.setORmonry(ORmonry);
        ordering.setORdate(ORdate);
        ordering.setORstatue(ORstatue);
        ordering.setORpayment(ORpayment);

        // 逻辑处理得到ORno
        List<Ordering> orderings = orderingMapper.selectList(null);
        int maxorno = 0;
        String ORno;
        for (Ordering ordering1 : orderings) {
            int orno = ordering1.getORno();
            if (orno > maxorno) maxorno = orno;
        }
        maxorno++;
        if (maxorno <10 && maxorno >0)
            ORno = "0"+String.valueOf(maxorno);
        else ORno = String.valueOf(maxorno);

        ordering.setORno(Integer.valueOf(ORno));

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner  : owners) {
            System.out.println("finding ");
            if (owner.getOname().equals(Oname)){
                ordering.setOno(owner.getOno());
            }
        }

        if (orderingMapper.insert(ordering)>0){
            System.out.println("order添加成功！");
        }
        return "redirect:/saler/ordermanage";
    }

    /*业主认证管理页*/
    @RequestMapping("/saler/ownerinfo")
    String toOwnerinfo(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }

        model.addAttribute("owners",owners);

        return "saler/ownerinfo";
    }

    /*业主信息查询页*/
    @RequestMapping("/saler/queryOwner")
    public String queryOwner(@RequestParam("queryOname")String queryOname,
                             Model model, HttpSession session){

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        if (!queryOname.equals("") ) {
            System.out.println("owner"+queryOname);
            queryWrapper.eq("Oname", queryOname);
        }
        List<Owner> owners = ownerMapper.selectList(queryWrapper);

        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }

        model.addAttribute("owners",owners);

        return "saler/ownerinfo";
    }

    /*业主信息管理页 删除业主信息*/
    @RequestMapping("/saler/deleteOwnering")
    public String deleteOwner(@RequestParam("hiddenOacconut")String hiddenOacconut,
                              @RequestParam("hiddenOidnum")String hiddenOidnum,
                              Model model,HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        Owner ownerinfo = new Owner();
        ownerinfo.setOacconut(hiddenOacconut);

        // 逻辑处理得到Ono
        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner  : owners) {
            if (owner.getOidnum().equals(hiddenOidnum)){
                ownerinfo.setOno(owner.getOno());
            }
        }

        if (ownerMapper.deleteById(ownerinfo.getOno())> 0) System.out.println("Ono删除成功！");

        return "redirect:/saler/ownerinfo";
    }

    //业主信息管理页 添加业主信息
    @RequestMapping("/saler/addOwner")
    public String addOwnering(@RequestParam("Oacconut")String Oacconut, @RequestParam("Oname")String Oname,
                              @RequestParam("Oaddress")String Oaddress,@RequestParam("Ophonenum")String Ophonenum,
                              @RequestParam("Oidnum")String Oidnum, @RequestParam("Oidauthflog")String Oidauthflog,
                              @RequestParam("Ocertificationflog")String Ocertificationflog,
                              HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        Owner ownerinfo = new Owner();
        ownerinfo.setOacconut(Oacconut);
        ownerinfo.setOname(Oname);
        ownerinfo.setOaddress(Oaddress);
        ownerinfo.setOphonenum(Ophonenum);
        ownerinfo.setOidnum(Oidnum);
        if (Oidauthflog.equals("未认证"))Oidauthflog="0";
        else if (Oidauthflog.equals("认证"))Oidauthflog="1";
        ownerinfo.setOidauthflog(Oidauthflog);
        if (Ocertificationflog.equals("未身份认证"))Ocertificationflog="0";
        else if (Ocertificationflog.equals("未认证"))Ocertificationflog="1";
        else if (Ocertificationflog.equals("认证"))Ocertificationflog="2";
        ownerinfo.setOcertificationflog(Ocertificationflog);

        // 逻辑处理得到Ono
        List<Owner> owners = ownerMapper.selectList(null);
        int maxono = 0;
        for (Owner owner : owners) {
            int ono = owner.getOno();
            if (ono > maxono) maxono = ono;
        }
        maxono++;
        ownerinfo.setOno(maxono);

        if (ownerMapper.insert(ownerinfo)>0){
            System.out.println("owner添加成功！");
        }
        return "redirect:/saler/ownerinfo";
    }

    /*业主信息管理页 修改业主信息*/
    @RequestMapping("/saler/reviseOwner")
    public String toReviseOwner(@RequestParam("Oacconut")String Oacconut, @RequestParam("Oname")String Oname,
                                @RequestParam("Oaddress")String Oaddress,@RequestParam("Ophonenum")String Ophonenum,
                                @RequestParam("Oidnum")String Oidnum, @RequestParam("Oidauthflog")String Oidauthflog,
                                @RequestParam("oldOidnum")String oldOidnum, @RequestParam("Ocertificationflog")String Ocertificationflog,
                                HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        Owner ownerinfo = new Owner();
        ownerinfo.setOacconut(Oacconut);
        ownerinfo.setOname(Oname);
        ownerinfo.setOaddress(Oaddress);
        ownerinfo.setOphonenum(Ophonenum);
        ownerinfo.setOidnum(Oidnum);
        if (Oidauthflog.equals("未认证"))Oidauthflog="0";
        else if (Oidauthflog.equals("认证"))Oidauthflog="1";
        ownerinfo.setOidauthflog(Oidauthflog);
        if (Ocertificationflog.equals("未身份认证"))Ocertificationflog="0";
        else if (Ocertificationflog.equals("未认证"))Ocertificationflog="1";
        else if (Ocertificationflog.equals("认证"))Ocertificationflog="2";
        ownerinfo.setOcertificationflog(Ocertificationflog);

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner  : owners) {
            if (owner.getOidnum().equals(oldOidnum)){
                ownerinfo.setOno(owner.getOno());
            }
        }

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Ono",ownerinfo.getOno());
        if (ownerMapper.update(ownerinfo,queryWrapper) > 0){
            System.out.println("owner更新成功！");
        }
        return "redirect:/saler/ownerinfo";
    }

    /*业主认证批量身份认证*/
    @RequestMapping("/saler/identityAuth")
    String identityAuth(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "saler/identityAuth";
    }

    /*业主认证批量实名认证*/
    @RequestMapping("/saler/realNameAuth")
    String realNameAuth(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "saler/realNameAuth";
    }

    /*业主信息批量管理 身份认证查询页*/
    @RequestMapping("/saler/queryIdentityAuth")
    public String queryIdentityAuth(@RequestParam("queryIdnum")String queryIdnum,
                                    @RequestParam("queryCertification")String queryCertification,
                                    Model model, HttpSession session){
        if (queryIdnum.equals("未认证"))queryIdnum="0";
        else if (queryIdnum.equals("认证"))queryIdnum="1";

        if (queryCertification.equals("未身份认证"))queryCertification="0";
        else if (queryCertification.equals("未认证"))queryCertification="1";
        else if (queryCertification.equals("认证"))queryCertification="2";

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        if (!queryIdnum.equals("") && !queryCertification.equals("") ) {
            queryWrapper.eq("Oidauthflog", queryIdnum).eq("Ocertificationflog", queryCertification);
        }else if (!queryIdnum.equals("") && queryCertification.equals("") ){
            queryWrapper.eq("Oidauthflog", queryIdnum);
        }else if (queryIdnum.equals("") && !queryCertification.equals("") ){
            queryWrapper.eq("Ocertificationflog", queryCertification);
        }
        List<Owner> owners = ownerMapper.selectList(queryWrapper);

        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);

        return "saler/identityAuth";
    }

    /*业主信息批量管理 实名认证查询页*/
    @RequestMapping("/saler/queryRealNameAuth")
    public String queryRealNameAuth(@RequestParam("queryIdnum")String queryIdnum,
                                    @RequestParam("queryCertification")String queryCertification,
                                    Model model, HttpSession session){
        if (queryIdnum.equals("未认证"))queryIdnum="0";
        else if (queryIdnum.equals("认证"))queryIdnum="1";

        if (queryCertification.equals("未身份认证"))queryCertification="0";
        else if (queryCertification.equals("未认证"))queryCertification="1";
        else if (queryCertification.equals("认证"))queryCertification="2";

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        if (!queryIdnum.equals("") && !queryCertification.equals("") ) {
            queryWrapper.eq("Oidauthflog", queryIdnum).eq("Ocertificationflog", queryCertification);
        }else if (!queryIdnum.equals("") && queryCertification.equals("") ){
            queryWrapper.eq("Oidauthflog", queryIdnum);
        }else if (queryIdnum.equals("") && !queryCertification.equals("") ){
            queryWrapper.eq("Ocertificationflog", queryCertification);
        }
        List<Owner> owners = ownerMapper.selectList(queryWrapper);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);

        return "saler/realNameAuth";
    }

    /*业主信息批量管理 修改身份认证*/
    @RequestMapping("/saler/reviseidentityAuth")
    String reviseidentityAuth(@RequestParam("ownerList")String ownerList,
                              Model model, HttpSession session){
        List<Owner> ownersinfo = ownerMapper.selectList(null);

        for (String res : ownerList.split(",")){
            Owner owner = new Owner();
            owner.setOno(Integer.valueOf(res));
            for (Owner ownerinfo : ownersinfo){
                if (String.valueOf(ownerinfo.getOno()).equals(res)){
                    if (ownerinfo.getOidauthflog().equals("0")){
                        owner.setOidauthflog("1");
                    }else if (ownerinfo.getOidauthflog().equals("1")) {
                        owner.setOidauthflog("0");
                    }
                }
            }

            QueryWrapper<Owner> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Ono",owner.getOno());
            if (ownerMapper.update(owner,queryWrapper3) > 0){
                System.out.println("owner更新成功！");
            }
        }

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "saler/identityAuth";
    }

    /*业主信息批量管理 修改实名认证*/
    @RequestMapping("/saler/reviseRealNameAuth")
    String reviseRealNameAuth(@RequestParam("realNameStatus")String realNameStatus,
                              @RequestParam("ownerList")String ownerList,
                              Model model, HttpSession session){
        if (realNameStatus.equals("未身份认证"))realNameStatus="0";
        else if (realNameStatus.equals("未认证"))realNameStatus="1";
        else if (realNameStatus.equals("认证"))realNameStatus="2";

        List<Owner> ownersinfo = ownerMapper.selectList(null);

        for (String res: ownerList.split(",")){
            Owner owner = new Owner();
            owner.setOno(Integer.valueOf(res));
            for (Owner ownerinfo : ownersinfo){
                if (String.valueOf(ownerinfo.getOno()).equals(res)){
                    owner.setOcertificationflog(realNameStatus);
                }
            }

            QueryWrapper<Owner> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Ono",owner.getOno());
            if (ownerMapper.update(owner,queryWrapper3) > 0){
                System.out.println("owner更新成功！");
            }
        }

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "saler/realNameAuth";
    }

    /*车位信息批量 销售状态修改*/
    @RequestMapping("/saler/reviseStatus")
    String reviseStatus(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("parkingInfo",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);

        return "saler/reviseStatus";
    }

    /*车位信息批量管理 修改销售状态 查询页*/
    @RequestMapping("/saler/queryReviseStatus")
    public String queryReviseStatus(@RequestParam("queryCommunity")String queryCommunity,
                                    @RequestParam("queryArea")String queryArea,
                                    Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") ) {
            queryWrapper.eq("Sname", saler.getSname()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") ){
            queryWrapper.eq("Sname", saler.getSname()).eq("Aname", queryArea);
        } else if (!queryCommunity.equals("")  && queryArea.equals("") ){
            queryWrapper.eq("Sname", saler.getSname()).eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("parkingInfo",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        return "saler/reviseStatus";
    }

    /*车位信息批量管理 修改销售状态*/
    @RequestMapping("/saler/reviseStatusParking")
    String reviseBatchParking(@RequestParam("reviseStatusName")String reviseStatusName,
                              @RequestParam("parkingList")String parkingList,
                              Model model, HttpSession session){
        if (reviseStatusName.equals("可售"))reviseStatusName="0";
        else if (reviseStatusName.equals("已预约"))reviseStatusName="1";
        else if (reviseStatusName.equals("已出售"))reviseStatusName="2";

        Saler saler = (Saler) session.getAttribute("user");
        List<Parking> parkings = parkingMapper.selectList(null);

        for (String res: parkingList.split(",")){
            Parking park = new Parking();
            park.setPno(res);
            for (Parking parkinginfo : parkings){
                if (parkinginfo.getPno().equals(res)){
                    park.setPstatus(reviseStatusName);
                }
            }

            QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("Pno",res);
            if (parkingMapper.update(park,queryWrapper3) > 0){
                System.out.println("parking更新成功！");
            }
        }
        // 查询到所有车位信息
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

        model.addAttribute("parkingInfo",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "saler/reviseStatus";
    }

    /*订单信息批量 订单修改*/
    @RequestMapping("/saler/revisePayStatus")
    String revisePayStatus(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有订单信息
        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewOrderingInfo> viewOrderInfos = orderingInfoMapper.selectList(queryWrapper);
        for(ViewOrderingInfo viewOrderingInfo : viewOrderInfos){
            if(viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if(viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if(viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if(viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if(viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if(viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if(viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if(viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if(viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        List<Parking> parkings = parkingMapper.selectList(null);
        List<Owner> owners = ownerMapper.selectList(null);
        List<Ordering> orderings = orderingMapper.selectList(null);

        // 设置List date
        List<String> date = new ArrayList<>();
        date.add(orderings.get(0).getORdate());
        for(Ordering ordering : orderings){
            for(int i = 0 ; i < date.size() ; i ++){
                if(date.get(i).equals(ordering.getORdate()))break;
                if(i == date.size() - 1)date.add(ordering.getORdate());
            }
        }
        model.addAttribute("viewOrderInfos",viewOrderInfos);
        model.addAttribute("parkings",parkings);
        model.addAttribute("owners",owners);
        model.addAttribute("orderings",orderings);
        session.setAttribute("date",date);

        return "saler/revisePayStatus";
    }

    /*订单信息批量管理 修改订单 查询页*/
    @RequestMapping("/saler/queryPayStatus")
    public String queryPayStatus(@RequestParam("queryOwner")String queryOwner,
                                 @RequestParam("queryDate")String queryDate,
                                 @RequestParam("queryPayment")String queryPayment,
                                 Model model, HttpSession session){
        session.setAttribute("date",session.getAttribute("date"));
        Saler saler = (Saler) session.getAttribute("user");
        List<Owner> owners = ownerMapper.selectList(null);

        if (queryPayment.equals("一次性"))queryPayment="1";
        else if (queryPayment.equals("分期"))queryPayment="2";
        else if (queryPayment.equals("按揭"))queryPayment="3";

        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        if ( !queryOwner.equals("")  && !queryDate.equals("")  && !queryPayment.equals("") ) {
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORdate", queryDate).eq("ORpayment", queryPayment);
        }else if ( !queryOwner.equals("")  && !queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORdate", queryDate);
        } else if ( !queryOwner.equals("")  && queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORpayment", queryPayment);
        } else if ( queryOwner.equals("")  && !queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORdate", queryDate).eq("ORpayment", queryPayment);
        } else if ( !queryOwner.equals("")  && queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner);
        }else if ( queryOwner.equals("")  && !queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORdate", queryDate);
        }else if ( queryOwner.equals("")  && queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORpayment", queryPayment);
        }

        List<ViewOrderingInfo> viewOrderingInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderingInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        model.addAttribute("viewOrderInfos",viewOrderingInfos);
        model.addAttribute("owners",owners);
        return "saler/revisePayStatus";
    }

    /*订单信息批量管理 修改订单*/
    @RequestMapping("/saler/revisePayStatusOrdering")
    String revisePayStatusOrdering(@RequestParam("ORpayStatus")String ORpayStatus,
                                   @RequestParam("orderingList")String orderingList,
                                   Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        if (ORpayStatus.equals("未交预约金"))ORpayStatus="0";
        else if (ORpayStatus.equals("已预约"))ORpayStatus="1";
        else if (ORpayStatus.equals("未交定金"))ORpayStatus="2";
        else if (ORpayStatus.equals("待签约"))ORpayStatus="3";
        else if (ORpayStatus.equals("未交尾款"))ORpayStatus="4";
        else if (ORpayStatus.equals("已完成"))ORpayStatus="5";
        List<Ordering> orderingsinfo = orderingMapper.selectList(null);

        for (String res: orderingList.split(",")){
            Ordering ordering = new Ordering();
            ordering.setORno(Integer.valueOf(res));
            for (Ordering ordering1 : orderingsinfo){
                if (String.valueOf(ordering1.getORno()).equals(res)){
                    ordering.setORstatue(ORpayStatus);
                }
            }

            QueryWrapper<Ordering> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("ORno",res);
            if (orderingMapper.update(ordering,queryWrapper3) > 0){
                System.out.println("order更新成功！");
            }
        }
        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewOrderingInfo> viewOrderInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        List<Parking> parkings = parkingMapper.selectList(null);
        List<Owner> owners = ownerMapper.selectList(null);
        List<Ordering> orderings = orderingMapper.selectList(null);

        model.addAttribute("viewOrderInfos",viewOrderInfos);
        model.addAttribute("parkings",parkings);
        model.addAttribute("owners",owners);
        model.addAttribute("orderings",orderings);

        return "saler/revisePayStatus";
    }

    /*订单信息批量 订单修改*/
    @RequestMapping("/saler/revisePayment")
    public String revisePayment(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有订单信息
        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewOrderingInfo> viewOrderInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        List<Parking> parkings = parkingMapper.selectList(null);
        List<Owner> owners = ownerMapper.selectList(null);
        List<Ordering> orderings = orderingMapper.selectList(null);

        // 设置List date
        List<String> date = new ArrayList<>();
        date.add(orderings.get(0).getORdate());
        for (Ordering ordering : orderings){
            for (int i = 0 ; i < date.size() ; i ++){
                if (date.get(i).equals(ordering.getORdate()))break;
                if (i == date.size() - 1)date.add(ordering.getORdate());
            }
        }
        model.addAttribute("viewOrderInfos",viewOrderInfos);
        model.addAttribute("parkings",parkings);
        model.addAttribute("owners",owners);
        model.addAttribute("orderings",orderings);
        session.setAttribute("date",date);

        return "saler/revisePayment";
    }

    /*订单信息批量管理 修改支付方式 查询页*/
    @RequestMapping("/saler/queryPayment")
    public  String queryPayment(@RequestParam("queryOwner")String queryOwner,
                                @RequestParam("queryDate")String queryDate,
                                @RequestParam("queryPayment")String queryPayment,
                                Model model, HttpSession session){
        session.setAttribute("date",session.getAttribute("date"));
        Saler saler = (Saler) session.getAttribute("user");
        List<Owner> owners = ownerMapper.selectList(null);

        if (queryPayment.equals("一次性"))queryPayment="1";
        else if (queryPayment.equals("分期"))queryPayment="2";
        else if (queryPayment.equals("按揭"))queryPayment="3";

        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryOwner.equals("")  && !queryDate.equals("")  && !queryPayment.equals("") ) {
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORdate", queryDate).eq("ORpayment", queryPayment);
        }else if (!queryOwner.equals("")  && !queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORdate", queryDate);
        } else if (!queryOwner.equals("")  && queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner).eq("ORpayment", queryPayment);
        } else if (queryOwner.equals("")  && !queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORdate", queryDate).eq("ORpayment", queryPayment);
        } else if (!queryOwner.equals("")  && queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("Oname", queryOwner);
        }else if (queryOwner.equals("")  && !queryDate.equals("")  && queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORdate", queryDate);
        }else if (queryOwner.equals("")  && queryDate.equals("")  && !queryPayment.equals("") ){
            queryWrapper.eq("Sno", saler.getSno()).eq("ORpayment", queryPayment);
        }

        List<ViewOrderingInfo> viewOrderingInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderingInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }
        model.addAttribute("viewOrderInfos",viewOrderingInfos);
        model.addAttribute("owners",owners);
        return "saler/revisePayment";
    }

    /*订单信息批量管理 修改支付方式*/
    @RequestMapping("/saler/revisePaymentOrdering")
    public String revisePaymentOrdering(@RequestParam("ORpayment")String ORpayment,
                                        @RequestParam("orderingList")String orderingList,
                                        Model model, HttpSession session){
        session.setAttribute("date",session.getAttribute("date"));

        if (ORpayment.equals("一次性"))ORpayment="1";
        else if (ORpayment.equals("分期"))ORpayment="2";
        else if (ORpayment.equals("按揭"))ORpayment="3";

        Saler saler = (Saler) session.getAttribute("user");
        List<Ordering> orderingsinfo = orderingMapper.selectList(null);

        for (String res: orderingList.split(",")){
            Ordering ordering = new Ordering();
            ordering.setORno(Integer.valueOf(res));
            ordering.setORpayment(ORpayment);
            QueryWrapper<Ordering> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("ORno",res);
            if (orderingMapper.update(ordering,queryWrapper3) > 0){
                System.out.println("order更新成功！");
            }
        }
        QueryWrapper<ViewOrderingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewOrderingInfo> viewOrderInfos = orderingInfoMapper.selectList(queryWrapper);
        for (ViewOrderingInfo viewOrderingInfo : viewOrderInfos){
            if (viewOrderingInfo.getORstatue().equals("0"))viewOrderingInfo.setORstatue("未交预约金");
            else if (viewOrderingInfo.getORstatue().equals("1"))viewOrderingInfo.setORstatue("已预约");
            else if (viewOrderingInfo.getORstatue().equals("2"))viewOrderingInfo.setORstatue("未交定金");
            else if (viewOrderingInfo.getORstatue().equals("3"))viewOrderingInfo.setORstatue("待签约");
            else if (viewOrderingInfo.getORstatue().equals("4"))viewOrderingInfo.setORstatue("未交尾款");
            else if (viewOrderingInfo.getORstatue().equals("5"))viewOrderingInfo.setORstatue("已完成");

            if (viewOrderingInfo.getORpayment().equals("1"))viewOrderingInfo.setORpayment("一次性");
            else if (viewOrderingInfo.getORpayment().equals("2"))viewOrderingInfo.setORpayment("分期");
            else if (viewOrderingInfo.getORpayment().equals("3"))viewOrderingInfo.setORpayment("按揭");
        }

        List<Parking> parkings = parkingMapper.selectList(null);
        List<Owner> owners = ownerMapper.selectList(null);
        List<Ordering> orderings = orderingMapper.selectList(null);

        model.addAttribute("viewOrderInfos",viewOrderInfos);
        model.addAttribute("parkings",parkings);
        model.addAttribute("owners",owners);
        model.addAttribute("orderings",orderings);
        return "saler/revisePayment";
    }

    /*合同信息主页*/
    @RequestMapping("/saler/contract")
    String toContract(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有车位信息
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
            if (viewParkingInfo.getPcontract().equals("1"))parkingContoact.add(viewParkingInfo);
            else if (viewParkingInfo.getPcontract().equals("0"))noParkingContoact.add(viewParkingInfo);
        }

        model.addAttribute("parkingContracts",parkingContoact);
        model.addAttribute("noParkingContracts",noParkingContoact);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "saler/contract";
    }

    /*合同信息查询页*/
    @RequestMapping("/saler/queryContract")
    public String queryContract(@RequestParam("queryCommunity")String queryCommunity,
                                @RequestParam("queryArea")String queryArea,
                                @RequestParam("querySaler")String querySaler,
                                Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<ViewParkingInfo> parkingContoact = new ArrayList<>();
        List<ViewParkingInfo> noParkingContoact = new ArrayList<>();
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPcontract().equals("1"))parkingContoact.add(viewParkingInfo);
            else if (viewParkingInfo.getPcontract().equals("0"))noParkingContoact.add(viewParkingInfo);
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("parkingContracts",parkingContoact);
        model.addAttribute("noParkingContracts",noParkingContoact);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "saler/contract";
    }

    /*合同信息删除页*/
    @RequestMapping("/saler/deleteContract")
    public String deleteContract(@RequestParam("hiddenPno")String hiddenPno,
                                 Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        Invoicecontract contract = new Invoicecontract();
        contract.setPno(hiddenPno);
        contract.setContract("0");
        List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
        for (Invoicecontract invoicecontract : invoicecontracts){
            if (invoicecontract.getPno().equals(hiddenPno))contract.setICno(invoicecontract.getICno());
        }
        QueryWrapper<Invoicecontract> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("ICno",contract.getICno());
        if (invoicecontractMapper.update(contract,queryWrapper2) > 0){
            System.out.println("contract更新成功！");
        }

        Parking park = new Parking();
        park.setPno(hiddenPno);
        park.setPcontract("0");

        QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("Pno",park.getPno());
        if (parkingMapper.update(park,queryWrapper3) > 0){
            System.out.println("parking更新成功！");
        }
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        List<ViewParkingInfo> parkingContoact = new ArrayList<>();
        List<ViewParkingInfo> noParkingContoact = new ArrayList<>();
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPcontract().equals("1"))parkingContoact.add(viewParkingInfo);
            else if (viewParkingInfo.getPcontract().equals("0"))noParkingContoact.add(viewParkingInfo);
        }

        model.addAttribute("parkingContracts",parkingContoact);
        model.addAttribute("noParkingContracts",noParkingContoact);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "saler/contract";
    }


   /* 发票信息主页*/
    @RequestMapping("/saler/invoice")
    String toInvoice(Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<ViewParkingInfo> parkingInvoice = new ArrayList<>();
        List<ViewParkingInfo> noParkingInvoice = new ArrayList<>();
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPinvoice().equals("1"))parkingInvoice.add(viewParkingInfo);
            else if (viewParkingInfo.getPinvoice().equals("0"))noParkingInvoice.add(viewParkingInfo);
        }

        model.addAttribute("parkingInvoices",parkingInvoice);
        model.addAttribute("noParkingInvoices",noParkingInvoice);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "saler/invoice";
    }

    /*发票信息查询页*/
    @RequestMapping("/saler/queryInvoice")
    public String queryInvoice(@RequestParam("queryCommunity")String queryCommunity,
                               @RequestParam("queryArea")String queryArea,
                               @RequestParam("querySaler")String querySaler,
                               Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Sno", saler.getSno()).eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<ViewParkingInfo> parkingInvoice = new ArrayList<>();
        List<ViewParkingInfo> noParkingInvoice = new ArrayList<>();
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPinvoice().equals("1"))parkingInvoice.add(viewParkingInfo);
            else if (viewParkingInfo.getPinvoice().equals("0"))noParkingInvoice.add(viewParkingInfo);
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("parkingInvoices",parkingInvoice);
        model.addAttribute("noParkingInvoices",noParkingInvoice);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "saler/invoice";
    }

    /*发票信息删除页*/
    @RequestMapping("/saler/deleteInvoice")
    public String deleteInvoice(@RequestParam("hiddenPno")String hiddenPno,
                                Model model, HttpSession session){
        Saler saler = (Saler) session.getAttribute("user");

        Invoicecontract contract = new Invoicecontract();
        contract.setPno(hiddenPno);
        contract.setInvoice("0");
        List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
        for (Invoicecontract invoicecontract : invoicecontracts){
            if (invoicecontract.getPno().equals(hiddenPno))contract.setICno(invoicecontract.getICno());
        }
        QueryWrapper<Invoicecontract> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("ICno",contract.getICno());
        if (invoicecontractMapper.update(contract,queryWrapper2) > 0){
            System.out.println("contract更新成功！");
        }

        Parking park = new Parking();
        park.setPno(hiddenPno);
        park.setPinvoice("0");
        QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("Pno",park.getPno());
        if (parkingMapper.update(park,queryWrapper3) > 0){
            System.out.println("parking更新成功！");
        }
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Sno",saler.getSno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        List<ViewParkingInfo> parkingInvoice = new ArrayList<>();
        List<ViewParkingInfo> noParkingInvoice = new ArrayList<>();
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPinvoice().equals("1"))parkingInvoice.add(viewParkingInfo);
            else if (viewParkingInfo.getPinvoice().equals("0"))noParkingInvoice.add(viewParkingInfo);
        }

        model.addAttribute("parkingInvoices",parkingInvoice);
        model.addAttribute("noParkingInvoices",noParkingInvoice);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "saler/invoice";
    }


}
