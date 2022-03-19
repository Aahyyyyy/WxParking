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
public class DeveloperController {

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
    private InvoicecontractMapper invoicecontractMapper;

    /* 个人信息主页 */
    @RequestMapping("/developer/information")
    String toInformation(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        session.setAttribute("user", developer);
        return "developer/information";
    }

    /* 楼盘信息主页 */
    @RequestMapping("/developer/community")
    String toCommunity(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有楼盘信息
        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("communityInfo",viewCommunityinfos);
        return "developer/community";
    }

    /* 楼盘信息查询页 */
    @RequestMapping("/developer/queryCommunity")
    public String queryCommunity(@RequestParam("queryCommunity")String queryCommunity,
                                 @RequestParam("queryArea")String queryArea,
                                 Model model, HttpSession session) {

        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        if( !queryCommunity.equals("")  && !queryArea.equals("") ) {
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if( queryCommunity.equals("")  && !queryArea.equals("") ){
            queryWrapper.eq("Aname", queryArea);
        }else if( !queryCommunity.equals("")  && queryArea.equals("")  ){
            queryWrapper.eq("Cname", queryCommunity);
        }
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("communityInfo",viewCommunityinfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        return "developer/community";
    }

    /* 楼盘区域管理管理页 添加楼盘区域 */
    @RequestMapping("/developer/addCommunity")
    public String addCommunity(@RequestParam("Dno")String Dno,@RequestParam("Cname")String Cname,
                               @RequestParam("Caddress")String Caddress,@RequestParam("Ccity")String Ccity,
                               @RequestParam("Aname")String Aname,
                               Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        // Cno逻辑处理操作
        int maxcno = 0;
        for (Community communitie : communities) {
            int cno = communitie.getCno();
            if(cno > maxcno)maxcno = cno;
        }
        maxcno++;

        // Ano逻辑处理操作
        int maxano = 0;
        for (Area area : areas) {
            int ano = area.getAno();
            if(ano > maxano)maxano = ano;
        }
        maxano++;

        // insert操作 community and area
        Community community = new Community();
        community.setCname(Cname);
        community.setCaddress(Caddress);
        community.setCcity(Ccity);
        community.setCphoto("0");
        community.setCno(maxcno);
        community.setDno(developer.getDno());
        if (communityMapper.insert(community)>0){
            System.out.println("community添加成功！");
        }

        Area area = new Area();
        area.setAno(maxano);
        area.setCno(maxcno);
        area.setAname(Aname);
        area.setAphoto("0");
        area.setAstatus("0");
        if (areaMapper.insert(area)>0){
            System.out.println("area添加成功！");
        }

        return "redirect:/developer/community";
    }


    /* 楼盘区域管理管理页 修改楼盘区域信息 */
    @RequestMapping("/developer/revise")
    public String toRevise(@RequestParam("Cname")String Cname,
                           @RequestParam("Caddress")String Caddress, @RequestParam("Ccity")String Ccity,
                           @RequestParam("hiddenCname2")String hiddenCname2, @RequestParam("hiddenAname2")String hiddenAname2,
                           @RequestParam("Aname")String Aname,
                           Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        System.out.println("user:"+developer.getDno()+"正确");

        Community community = new Community();
        community.setCname(Cname);
        community.setCaddress(Caddress);
        community.setCcity(Ccity);
        community.setDno(developer.getDno());
        community.setCphoto("0");
        System.out.println("Cname:"+community.getCname()+"正确");

        Area area = new Area();
        area.setAname(Aname);
        area.setAphoto("0");
        area.setAstatus("未开盘");
        System.out.println("Aname:"+area.getAname()+"正确");

        // 查询到所有楼盘信息
        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);

        // equals 获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if (viewCommunityinfo.getCname().equals(hiddenCname2)){
                if (viewCommunityinfo.getAname().equals(hiddenAname2)){
                    System.out.println("find ");
                    community.setCno(viewCommunityinfo.getCno());
                    area.setCno(viewCommunityinfo.getCno());
                    area.setAno(viewCommunityinfo.getAno());
                }
            }
        }

        QueryWrapper<Community> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("Cno",community.getCno());
        if (communityMapper.update(community,queryWrapper2) > 0){
            System.out.println("community更新成功！");
        }
        QueryWrapper<Area> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("Ano",area.getAno());
        if (areaMapper.update(area,queryWrapper3) > 0){
            System.out.println("area更新成功！");
        }
        return "redirect:/developer/community";
    }

    /* 楼盘区域管理管理页 删除楼盘区域信息 */
    @RequestMapping("/developer/deleteCommunity")
    public String deleteCommunity(@RequestParam("hiddenCname")String hiddenCname,
                                  @RequestParam("hiddenAname")String hiddenAname,
                                  Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        Community community = new Community();
        community.setCname(hiddenCname);
        Area area = new Area();
        area.setAname(hiddenAname);
        // 通过Cname and Aname搜索获得Cno and Ano
        // 查询到所有楼盘信息
        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);
        // equals获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if (viewCommunityinfo.getCname().equals(community.getCname())){
                if (viewCommunityinfo.getAname().equals((area.getAname()))){
                    System.out.println("find ");
                    community.setCno(viewCommunityinfo.getCno());
                    area.setAno(viewCommunityinfo.getAno());
                }
            }
        }
        System.out.println("!!!!"+"Cname:"+community.getCname()+"Aname:"+area.getAname());
        System.out.println(" ");

        if (areaMapper.deleteById(area.getAno()) > 0) System.out.println("Ano删除成功！");

        if (communityMapper.deleteById(community.getCno()) > 0) System.out.println("Dno删除成功！");

        return "redirect:/developer/community";
    }

    /* 车位信息主页 */
    @RequestMapping("developer/parking")
    String toParking(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        // 映射销售状态
        for(ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if(viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if(viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if(viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "developer/parking";
    }

    /* 车位信息查询页 */
    @RequestMapping("/developer/queryParking")
    public String queryParking(@RequestParam("queryCommunity")String queryCommunity,
                               @RequestParam("queryArea")String queryArea,
                               @RequestParam("querySaler")String querySaler,
                               Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if( !queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Dno",developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if( queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno",developer.getDno()).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if( !queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno",developer.getDno()).eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if( !queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno",developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if( queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno",developer.getDno()).eq("Sname", querySaler);
        }else if( queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno",developer.getDno()).eq("Aname", queryArea);
        }else if( !queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno",developer.getDno()).eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        // 映射销售状态
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if(viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/parking";
    }

    /* 车位信息批量销售状态修改 */
    @RequestMapping("/developer/reviseStatus")
    String batchReviseStatus(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "developer/reviseStatus";
    }

    /* 车位信息批量管理 修改销售状态 查询页 */
    @RequestMapping("/developer/queryReviseStatus")
    public String queryBatchParking(@RequestParam("queryCommunity")String queryCommunity,
                                    @RequestParam("queryArea")String queryArea,
                                    @RequestParam("querySaler")String querySaler,
                                    Model model, HttpSession session){

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/reviseStatus";
    }

    /* 车位信息批量管理 修改销售状态 */
    @RequestMapping("/developer/reviseStatusParking")
    String reviseBatchParking(  @RequestParam("reviseStatusName")String reviseStatusName,
                                @RequestParam("parkingList")String parkingList,
                                Model model, HttpSession session){
        if(reviseStatusName.equals("可售"))reviseStatusName="0";
        else if(reviseStatusName.equals("已预约"))reviseStatusName="1";
        else if(reviseStatusName.equals("已出售"))reviseStatusName="2";

        List<Parking> parkings = parkingMapper.selectList(null);

        for (String res: parkingList.split(",")){
            System.out.println(res);
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

        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
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

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/reviseStatus";
    }

    /*车位信息批量修改销售公司*/
    @RequestMapping("/developer/reviseSaler")
    String batchReviseSaler(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/reviseSaler";
    }


    /*车位信息批量管理 修改销售公司 查询页*/
    @RequestMapping("/developer/queryReviseSaler")
    public String queryReviseSaler(@RequestParam("queryCommunity")String queryCommunity,
                                   @RequestParam("queryArea")String queryArea,
                                   @RequestParam("querySaler")String querySaler,
                                   Model model, HttpSession session){

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/reviseSaler";
    }


    /*车位信息批量管理 修改销售公司*/
    @RequestMapping("/developer/reviseSalerParking")
    String reviseSalerParking(@RequestParam("parkingList")String parkingList,
                              @RequestParam("reviseSalerName")String reviseSalerName,
                              Model model, HttpSession session){

        if (!reviseSalerName.equals("") || parkingList.equals("")){
            // 通过Sname,获取Sno
            List<Saler> salertwo = salerMapper.selectList(null);
            Integer sno = -1;
            List<Parking> parkings = parkingMapper.selectList(null);
            for (Saler saler : salertwo){
                if (saler.getSname().equals(reviseSalerName))
                    sno = saler.getSno();
            }
            for (String res : parkingList.split(",")){
                System.out.println("res:"+res);
                Parking park = new Parking();
                park.setPno(res);
                for (Parking parkinginfo : parkings){
                    if (parkinginfo.getPno().equals(res)){
                        park.setSno(sno);
                    }
                }
                QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("Pno",res);
                if (parkingMapper.update(park,queryWrapper3) > 0){
                    System.out.println("parking更新成功！");
                }
            }
        }

        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/reviseSaler";
    }

    /*车位信息批量管理页 删除车位*/
    @RequestMapping("/developer/batchDelete")
    String batchDeleteParking(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
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

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "developer/batchDelete";
    }

    /*车位信息批量管理 批量删除 查询页*/
    @RequestMapping("/developer/queryBatchDelete")
    public String queryBatchDelete(@RequestParam("queryCommunity")String queryCommunity,
                                   @RequestParam("queryArea")String queryArea,
                                   @RequestParam("querySaler")String querySaler,
                                   Model model, HttpSession session){

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Cname", queryCommunity);
        }
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);
        return "developer/batchDelete";
    }

    /*车位信息批量管理 批量删除*/
    @RequestMapping("/developer/batchDeleteParking")
    String batchDeleteParking(@RequestParam("parkingList")String parkingList,
                              Model model, HttpSession session){

        for (String res: parkingList.split(",")){
            System.out.println(res);
            if (parkingMapper.deleteById(res)> 0) System.out.println("Pno删除成功！");
        }

        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos){
            if (viewParkingInfo.getPstatus().equals("0"))viewParkingInfo.setPstatus("可售");
            else if (viewParkingInfo.getPstatus().equals("1"))viewParkingInfo.setPstatus("已预约");
            else if (viewParkingInfo.getPstatus().equals("2"))viewParkingInfo.setPstatus("已出售");
        }
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Saler> salers = salerMapper.selectList(null);

        model.addAttribute("viewParkingInfos",viewParkingInfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "developer/batchDelete";
    }

    /*车位管理管理页 添加车位*/
    @RequestMapping("/developer/addParking")
    public String addParking(@RequestParam("Sname")String Sname,@RequestParam("Cname")String Cname,
                             @RequestParam("Aname")String Aname, @RequestParam("Plength")String Plength,
                             @RequestParam("Pwide")String Pwide, @RequestParam("Pbuildarra")String Pbuildarra,
                             @RequestParam("Pusearea")String Pusearea, @RequestParam("Psalernuitprice")String Psalernuitprice,
                             Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        Area area = new Area();
        area.setAname(Aname);

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        QueryWrapper<ViewCommunityinfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper2);

        // Sno逻辑处理操作
        List<Saler> salers = salerMapper.selectList(null);
        Integer Sno = -1;
        for (Saler saler : salers) {
            if (saler.getSname().equals(Sname)){
                Sno = saler.getSno();
            }
        }

        // Pno逻辑处理操作
        int maxpno = 0;
        String Pno;
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos) {
            System.out.println("Pno:"+Integer.parseInt(viewParkingInfo.getPno().substring(1)));
            int pno = Integer.parseInt(viewParkingInfo.getPno().substring(1));
            if (pno > maxpno) maxpno = pno;
        }
        maxpno++;
        if(maxpno <10 && maxpno >0)
            Pno = "A"+"0000"+String.valueOf(maxpno);
        else Pno = "A"+"000"+String.valueOf(maxpno);

        // insert操作
        Parking park = new Parking();
        park.setPno(Pno);
        park.setSno(Sno);

        // 获得Ano Cno
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            if (viewCommunityinfo.getCname().equals(Cname)){
                if (viewCommunityinfo.getAname().equals((area.getAname()))){
                    park.setAno(viewCommunityinfo.getAno());
                }
            }
        }

        park.setPlength(Plength);
        park.setPwide(Pwide);
        park.setPbuildarra(Pbuildarra);
        park.setPusearea(Pusearea);
        park.setPsalernuitprice(Double.valueOf(Psalernuitprice));
        park.setPstatus("0");

        int psalernuitprice = Integer.valueOf(Psalernuitprice);
        int pbuildarra = Integer.valueOf(Pbuildarra);
        park.setPsalerprice(Double.valueOf(String.valueOf(psalernuitprice * pbuildarra)));

        List<Invoicecontract> invoicecontracts = invoicecontractMapper.selectList(null);
        Invoicecontract invoicecontract = new Invoicecontract();

        // ICno逻辑处理
        int maxicno = 0;
        for (Invoicecontract invoicecontract1 : invoicecontracts){
            if (Integer.valueOf(invoicecontract1.getICno()) > maxicno)maxicno = Integer.valueOf(invoicecontract1.getICno());
        }

        invoicecontract.setICno(maxicno);
        invoicecontract.setContract("0");
        invoicecontract.setInvoice("0");
        invoicecontract.setPno(park.getPno());

        if (invoicecontractMapper.insert(invoicecontract)>0){
            System.out.println("invoicecontract添加成功！");
        }

        if (parkingMapper.insert(park)>0){
            System.out.println("parking添加成功！");
        }
        return "redirect:/developer/parking";
    }

    /*车位信息管理页 删除车位信息*/
    @RequestMapping("/developer/deleteParking")
    public String deleteParking(@RequestParam("Dno")String Dno,@RequestParam("hiddenPno")String hiddenPno,
                                HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        Parking park = new Parking();
        park.setPno(hiddenPno);

        if (parkingMapper.deleteById(park.getPno())> 0) System.out.println("Pno删除成功！");

        return "redirect:/developer/parking";
    }

    /*楼车位信息管理页 修改车位信息*/
    @RequestMapping("/developer/revisePark")
    public String toReviseParking(@RequestParam("Sname")String Sname,@RequestParam("Cname")String Cname,
                                  @RequestParam("Aname")String Aname, @RequestParam("Plength")String Plength,
                                  @RequestParam("Pwide")String Pwide, @RequestParam("Pbuildarra")String Pbuildarra,
                                  @RequestParam("Pusearea")String Pusearea, @RequestParam("Psalernuitprice")String Psalernuitprice,
                                  @RequestParam("Pno")String Pno,
                                  Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(queryWrapper);

        Area area = new Area();
        area.setAname(Aname);

        Community community = new Community();
        community.setCname(Cname);

        Parking park = new Parking();
        park.setPno(Pno);
        park.setPlength(Plength);
        park.setPwide(Pwide);
        park.setPbuildarra(Pbuildarra);
        park.setPusearea(Pusearea);
        park.setPsalernuitprice(Double.valueOf(Psalernuitprice));


        // 修改销售状态
        park.setPstatus("0");

        // Psalerprice逻辑计算
        for (ViewParkingInfo viewParkingInfo  : viewParkingInfos) {
            System.out.println("finding ");
            if (viewParkingInfo.getPno().equals(Pno)){
                System.out.println("find ");
                double psalernuitprice = Double.valueOf(Psalernuitprice);
                double pbuildarra = Double.valueOf(viewParkingInfo.getPbuildarra());
                park.setPsalerprice(psalernuitprice * pbuildarra);
            }
        }

        // Sno逻辑处理操作
        List<Saler> salers = salerMapper.selectList(null);
        Integer Sno = -1;
        for (Saler saler : salers) {
            if (saler.getSname().equals(Sname)){
                Sno = saler.getSno();
            }
        }
        park.setSno(Sno);

        // 查询到所有楼盘信息
        QueryWrapper<ViewCommunityinfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper2);

        // equals 获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if(viewCommunityinfo.getCname().equals(community.getCname())){
                if(viewCommunityinfo.getAname().equals((area.getAname()))){
                    System.out.println("find ");
                    community.setCno(viewCommunityinfo.getCno());
                    area.setCno(viewCommunityinfo.getCno());
                    area.setAno(viewCommunityinfo.getAno());
                    park.setAno(viewCommunityinfo.getAno());
                }
            }
        }

        QueryWrapper<Parking> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("Pno",park.getPno());
        if (parkingMapper.update(park,queryWrapper3) > 0){
            System.out.println("parking更新成功！");
        }
        return "redirect:/developer/parking";
    }

    /*楼盘开盘管理页*/
    @RequestMapping("/developer/quotation")
    String toQuotation(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);
        List<Area> areas = areaMapper.selectList(null);
        List<Community> communities = communityMapper.selectList(null);

        for(ViewCommunityinfo viewCommunityinfo : viewCommunityinfos){
            if(viewCommunityinfo.getAstatus().equals("0"))viewCommunityinfo.setAstatus("未开盘");
            else if(viewCommunityinfo.getAstatus().equals("1"))viewCommunityinfo.setAstatus("开盘");
        }

        model.addAttribute("communities",communities);
        model.addAttribute("viewCommunityinfo",viewCommunityinfos);
        model.addAttribute("areas",areas);

        return "developer/quotation";
    }

    /*楼盘开盘查询页*/
    @RequestMapping("/developer/queryQuotation")
    public String queryQuotation(@RequestParam("queryCommunity")String queryCommunity,
                                 @RequestParam("queryArea")String queryArea,
                                 Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") ) {
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") ){
            queryWrapper.eq("Dno", developer.getDno()).eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("")  ){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity);
        }
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);

        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos){
            if (viewCommunityinfo.getAstatus().equals("0"))viewCommunityinfo.setAstatus("未开盘");
            else if (viewCommunityinfo.getAstatus().equals("1"))viewCommunityinfo.setAstatus("开盘");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("viewCommunityinfo",viewCommunityinfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        return "developer/quotation";
    }

    /*楼盘开盘管理页 修改开盘状态信息*/
    @RequestMapping("/developer/reviseSta")
    public String toReviseAstatus(@RequestParam("hiddenCname")String hiddenCname,@RequestParam("hiddenCaddress")String hiddenCaddress,
                                  @RequestParam("hiddenAname")String hiddenAname, @RequestParam("Astatus")String Astatus,
                                  Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        Area area = new Area();
        area.setAname(hiddenAname);
        area.setAphoto("0");

        if(Astatus.equals("开盘"))Astatus="1";
        else if(Astatus.equals("未开盘"))Astatus="0";
        area.setAstatus(Astatus);
        Community community = new Community();
        community.setCname(hiddenCname);

        // 查询到所有楼盘信息
        QueryWrapper<ViewCommunityinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(queryWrapper);

        // equals 获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if (viewCommunityinfo.getCname().equals(community.getCname())){
                if (viewCommunityinfo.getAname().equals((area.getAname()))){
                    System.out.println("find ");
                    community.setCno(viewCommunityinfo.getCno());
                    area.setCno(viewCommunityinfo.getCno());
                    area.setAno(viewCommunityinfo.getAno());
                }
            }
        }

        QueryWrapper<Area> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("Ano",area.getAno());

        if (areaMapper.update(area,queryWrapper2) > 0){
            System.out.println("area更新成功！");
        }
        return "redirect:/developer/quotation";
    }

    /*业主认证管理页*/
    @RequestMapping("/developer/ownerinfo")
    String toOwnerinfo(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }

        model.addAttribute("owners",owners);

        return "developer/ownerinfo";
    }

    /*业主信息查询页*/
    @RequestMapping("/developer/queryOwner")
    public String queryOwner(@RequestParam("queryOname")String queryOname,
                             Model model, HttpSession session){

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        if ( !queryOname.equals("") ) {
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

        return "developer/ownerinfo";
    }

    /*业主信息管理页 删除业主信息*/
    @RequestMapping("/developer/deleteOwnering")
    public String deleteOwner(@RequestParam("hiddenOacconut")String hiddenOacconut,
                              @RequestParam("hiddenOidnum")String hiddenOidnum,
                              Model model,HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
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

        return "redirect:/developer/ownerinfo";
    }

    /*业主信息管理页 添加业主信息*/
    @RequestMapping("/developer/addOwner")
    public String addOwnering(@RequestParam("Oacconut")String Oacconut, @RequestParam("Oname")String Oname,
                              @RequestParam("Oaddress")String Oaddress,@RequestParam("Ophonenum")String Ophonenum,
                              @RequestParam("Oidnum")String Oidnum, @RequestParam("Oidauthflog")String Oidauthflog,
                              @RequestParam("Ocertificationflog")String Ocertificationflog,
                              HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

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
        return "redirect:/developer/ownerinfo";
    }

    /*业主信息管理页 修改业主信息*/
    @RequestMapping("/developer/reviseOwner")
    public String toReviseOwner(@RequestParam("Oacconut")String Oacconut, @RequestParam("Oname")String Oname,
                                @RequestParam("Oaddress")String Oaddress,@RequestParam("Ophonenum")String Ophonenum,
                                @RequestParam("Oidnum")String Oidnum, @RequestParam("Oidauthflog")String Oidauthflog,
                                @RequestParam("oldOidnum")String oldOidnum, @RequestParam("Ocertificationflog")String Ocertificationflog,
                                HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

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
        return "redirect:/developer/ownerinfo";
    }

    /*业主认证批量身份认证*/
    @RequestMapping("/developer/identityAuth")
    String identityAuth(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "developer/identityAuth";
    }

    /*业主认证批量实名认证*/
    @RequestMapping("/developer/realNameAuth")
    String realNameAuth(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner : owners){
            if (owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if (owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if (owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if (owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if (owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "developer/realNameAuth";
    }

    /*业主信息批量管理 身份认证查询页*/
    @RequestMapping("/developer/queryIdentityAuth")
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

        return "developer/identityAuth";
    }

    /*业主信息批量管理 实名认证查询页*/
    @RequestMapping("/developer/queryRealNameAuth")
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

        return "developer/realNameAuth";
    }

    /*业主信息批量管理 修改身份认证*/
    @RequestMapping("/developer/reviseidentityAuth")
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
        for(Owner owner : owners){
            if(owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if(owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if(owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if(owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if(owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }
        model.addAttribute("owners",owners);
        return "developer/identityAuth";
    }

    /*业主信息批量管理 修改实名认证*/
    @RequestMapping("/developer/reviseRealNameAuth")
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
        return "developer/realNameAuth";
    }

    /*合同信息主页*/
    @RequestMapping("/developer/contract")
    public String toContract(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
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
            else if(viewParkingInfo.getPcontract().equals("0"))noParkingContoact.add(viewParkingInfo);
        }

        model.addAttribute("parkingContracts",parkingContoact);
        model.addAttribute("noParkingContracts",noParkingContoact);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        model.addAttribute("salers",salers);

        return "developer/contract";
    }

    /*合同信息查询页*/
    @RequestMapping("/developer/queryContract")
    public String queryContract(@RequestParam("queryCommunity")String queryCommunity,
                                @RequestParam("queryArea")String queryArea,
                                @RequestParam("querySaler")String querySaler,
                                Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity);
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
        return "developer/contract";
    }

    /*合同信息删除页*/
    @RequestMapping("/developer/deleteContract")
    public String deleteContract(@RequestParam("hiddenPno")String hiddenPno,
                                 Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

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
        queryWrapper.eq("Dno",developer.getDno());
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

        return "developer/contract";
    }


    /*发票信息主页*/
    @RequestMapping("/developer/invoice")
    String toInvoice(Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");
        // 查询到所有车位信息
        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Dno",developer.getDno());
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

        return "developer/invoice";
    }

    /*发票信息查询页*/
    @RequestMapping("/developer/queryInvoice")
    public String queryInvoice(@RequestParam("queryCommunity")String queryCommunity,
                               @RequestParam("queryArea")String queryArea,
                               @RequestParam("querySaler")String querySaler,
                               Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

        QueryWrapper<ViewParkingInfo> queryWrapper = new QueryWrapper<>();
        if (!queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")) {
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Aname", queryArea).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Sname", querySaler);
        }else if (!queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity).eq("Aname", queryArea);
        }else if (queryCommunity.equals("")  && queryArea.equals("") && !querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Sname", querySaler);
        }else if (queryCommunity.equals("")  && !queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Aname", queryArea);
        }else if (!queryCommunity.equals("")  && queryArea.equals("") && querySaler.equals("")){
            queryWrapper.eq("Dno", developer.getDno()).eq("Cname", queryCommunity);
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
        return "developer/invoice";
    }

    /*发票信息删除页*/
    @RequestMapping("/developer/deleteInvoice")
    public String deleteInvoice(@RequestParam("hiddenPno")String hiddenPno,
                                Model model, HttpSession session){
        Developer developer = (Developer)session.getAttribute("user");

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
        queryWrapper.eq("Dno",developer.getDno());
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

        return "developer/invoice";
    }




}
