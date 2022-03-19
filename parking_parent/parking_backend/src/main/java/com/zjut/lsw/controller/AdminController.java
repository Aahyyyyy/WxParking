package com.zjut.lsw.controller;

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
public class AdminController {
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
    private AdminMapper adminMapper;

    //车位信息主页
    @RequestMapping("/admin/parking")
    String toParking(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
        //查询到所有车位信息

        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(null);

        //映射销售状态
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

        return "admin/parking";
    }

    //信息发布页 发布车位
    @RequestMapping("/admin/addParking")
    public String addParking(@RequestParam("Sname")String Sname,@RequestParam("Cname")String Cname,
                             @RequestParam("Aname")String Aname, @RequestParam("Plength")String Plength,
                             @RequestParam("Pwide")String Pwide, @RequestParam("Pbuildarra")String Pbuildarra,
                             @RequestParam("Pusearea")String Pusearea, @RequestParam("Psalernuitprice")String Psalernuitprice,
                             Model model,HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");

        Area area = new Area();
        area.setAname(Aname);


        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(null);

        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        //Sno逻辑处理操作
        List<Saler> salers = salerMapper.selectList(null);
        Integer Sno = -1;
        for (Saler saler : salers) {
            if(saler.getSname().equals(Sname)){
                Sno = saler.getSno();
            }
        }

        //Pno 逻辑处理操作
        int maxpno = 0;
        String Pno;
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos) {
            System.out.println("Pno:"+Integer.parseInt(viewParkingInfo.getPno().substring(1)));
            int pno = Integer.parseInt(viewParkingInfo.getPno().substring(1));
            if(pno > maxpno) maxpno = pno;
        }
        maxpno++;
        if(maxpno <10 && maxpno >0)
            Pno = "A"+"0000"+String.valueOf(maxpno);
        else Pno = "A"+"000"+String.valueOf(maxpno);

        //insert操作
        Parking park = new Parking();
        park.setPno(Pno);
        park.setSno(Sno);

        //获得Ano Cno
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            if(viewCommunityinfo.getCname().equals(Cname)){
                if(viewCommunityinfo.getAname().equals((area.getAname()))){
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

        if (parkingMapper.insert(park)>0){
            System.out.println("parking添加成功！");
        }
        return "redirect:/admin/parking";
    }

    @RequestMapping("/admin/Parkingmanagement")
    String toParkingmanage(Model model, HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");
        //查询到所有车位信息

        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(null);

        //映射销售状态
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

        return "admin/Parkingmanagement";
    }

    //信息发布页 发布车位
    @RequestMapping("/admin/addParking1")
    public String addParking1(@RequestParam("Sname")String Sname,@RequestParam("Cname")String Cname,
                              @RequestParam("Aname")String Aname, @RequestParam("Plength")String Plength,
                              @RequestParam("Pwide")String Pwide, @RequestParam("Pbuildarra")String Pbuildarra,
                              @RequestParam("Pusearea")String Pusearea, @RequestParam("Psalernuitprice")String Psalernuitprice,
                              Model model,HttpSession session){
        Admin admin = (Admin) session.getAttribute("user");

        Area area = new Area();
        area.setAname(Aname);


        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(null);


        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        //Sno逻辑处理操作
        List<Saler> salers = salerMapper.selectList(null);
        Integer Sno = -1;
        for (Saler saler : salers) {
            if(saler.getSname().equals(Sname)){
                Sno = saler.getSno();
            }
        }

        //Pno 逻辑处理操作
        int maxpno = 0;
        String Pno;
        for (ViewParkingInfo viewParkingInfo : viewParkingInfos) {
            System.out.println("Pno:"+Integer.parseInt(viewParkingInfo.getPno().substring(1)));
            int pno = Integer.parseInt(viewParkingInfo.getPno().substring(1));
            if(pno > maxpno) maxpno = pno;
        }
        maxpno++;
        if(maxpno <10 && maxpno >0)
            Pno = "A"+"0000"+String.valueOf(maxpno);
        else Pno = "A"+"000"+String.valueOf(maxpno);

        //insert操作
        Parking park = new Parking();
        park.setPno(Pno);
        park.setSno(Sno);

        //获得Ano Cno
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            if(viewCommunityinfo.getCname().equals(Cname)){
                if(viewCommunityinfo.getAname().equals((area.getAname()))){
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

        if (parkingMapper.insert(park)>0){
            System.out.println("parking添加成功！");
        }
        return "redirect:/admin/Parkingmanagement";
    }

    //车位信息管理页 删除车位信息
    @RequestMapping("/admin/deleteParking")
    public String deleteParking(@RequestParam("Adminno")String Dno,@RequestParam("deletePno")String hiddenPno,
                                HttpSession session){
        Admin admin = (Admin)session.getAttribute("user");
        Parking park = new Parking();
        park.setPno(hiddenPno);

        if (parkingMapper.deleteById(park.getPno())> 0) System.out.println("Pno删除成功！");

        return "redirect:/admin/Parkingmanagement";
    }

    //楼车位信息管理页 修改车位信息
    @RequestMapping("/admin/revisePark")
    public String toReviseParking(@RequestParam("Sname")String Sname,@RequestParam("Cname")String Cname,
                                  @RequestParam("Aname")String Aname, @RequestParam("Plength")String Plength,
                                  @RequestParam("Pwide")String Pwide, @RequestParam("Pbuildarra")String Pbuildarra,
                                  @RequestParam("Pusearea")String Pusearea, @RequestParam("Psalernuitprice")String Psalernuitprice,
                                  @RequestParam("Pno")String Pno,
                                  Model model,HttpSession session){
        Admin admin = (Admin)session.getAttribute("user");
        List<ViewParkingInfo> viewParkingInfos = viewParkingInfoMapper.selectList(null);
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


        //修改销售状态
        park.setPstatus("0");

        //Psalerprice逻辑计算
        for (ViewParkingInfo viewParkingInfo  : viewParkingInfos) {
            System.out.println("finding ");
            if(viewParkingInfo.getPno().equals(Pno)){
                System.out.println("find ");
                double psalernuitprice = Double.valueOf(Psalernuitprice);
                double pbuildarra = Double.valueOf(viewParkingInfo.getPbuildarra());
                park.setPsalerprice(psalernuitprice * pbuildarra);
            }
        }

        //Sno逻辑处理操作
        List<Saler> salers = salerMapper.selectList(null);
        Integer Sno = -1;
        for (Saler saler : salers) {
            if(saler.getSname().equals(Sname)){
                Sno = saler.getSno();
            }
        }
        park.setSno(Sno);

        //查询到所有楼盘信息
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        //equals 获得Cno和Ano
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
        return "redirect:/admin/Parkingmanagement";
    }
    //楼盘开盘管理页
    @RequestMapping("/admin/quotation")
    String toQuotation(Model model, HttpSession session){
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);
        List<Community> communities = communityMapper.selectList(null);

        for(ViewCommunityinfo viewCommunityinfo : viewCommunityinfos){
            if(viewCommunityinfo.getAstatus().equals("0"))viewCommunityinfo.setAstatus("未开盘");
            else if(viewCommunityinfo.getAstatus().equals("1"))viewCommunityinfo.setAstatus("开盘");
        }

        model.addAttribute("communities",communities);
        model.addAttribute("viewCommunityinfo",viewCommunityinfos);
        model.addAttribute("areas",areas);

        return "admin/quotation";
    }
    //楼盘开盘查询页(需要优化)
    @RequestMapping("/admin/queryQuotation")
    public String queryQuotation(@RequestParam("queryCommunity")String queryCommunity,
                                 @RequestParam("queryArea")String queryArea,
                                 Model model, HttpSession session){

        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        for(ViewCommunityinfo viewCommunityinfo : viewCommunityinfos){
            if(viewCommunityinfo.getAstatus().equals("0"))viewCommunityinfo.setAstatus("未开盘");
            else if(viewCommunityinfo.getAstatus().equals("1"))viewCommunityinfo.setAstatus("开盘");
        }

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("viewCommunityinfo",viewCommunityinfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        return "admin/quotation";
    }
    //楼盘开盘管理页 修改开盘状态信息
    @RequestMapping("/admin/reviseSta")
    public String toReviseAstatus(@RequestParam("hiddenCname")String hiddenCname,@RequestParam("hiddenCaddress")String hiddenCaddress,
                                  @RequestParam("hiddenAname")String hiddenAname, @RequestParam("Astatus")String Astatus,
                                  Model model,HttpSession session){
        Admin admin = (Admin)session.getAttribute("user");
        Area area = new Area();
        area.setAname(hiddenAname);
        area.setAphoto("0");

        if(Astatus.equals("开盘"))Astatus="1";
        else if(Astatus.equals("未开盘"))Astatus="0";
        area.setAstatus(Astatus);
        Community community = new Community();
        community.setCname(hiddenCname);

        //查询到所有楼盘信息

        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        //equals 获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if(viewCommunityinfo.getCname().equals(community.getCname())){
                if(viewCommunityinfo.getAname().equals((area.getAname()))){
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
        return "redirect:/admin/quotation";
    }
    //业主认证管理页
    @RequestMapping("/admin/ownerinfo")
    String toOwnerinfo(Model model, HttpSession session){
        //查询到所有车位信息

        List<Owner> owners = ownerMapper.selectList(null);
        for(Owner owner : owners){
            if(owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if(owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if(owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if(owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if(owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }

        model.addAttribute("owners",owners);

        return "admin/ownerinfo";
    }
    //业主认证管理页
    @RequestMapping("/admin/ownerinfomanage")
    String selectOwnerinfo(Model model, HttpSession session){
        //查询到所有车位信息

        List<Owner> owners = ownerMapper.selectList(null);
        for(Owner owner : owners){
            if(owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if(owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if(owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if(owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if(owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }

        model.addAttribute("owners",owners);

        return "admin/ownerinfomanage";
    }
    //业主开盘查询页
    @RequestMapping("/admin/queryOwner")
    public String queryOwner(@RequestParam("queryOname")String queryOname,
                             Model model, HttpSession session){

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        if( !queryOname.equals("") ) {
            System.out.println("owner"+queryOname);
            queryWrapper.eq("Oname", queryOname);
        }
        List<Owner> owners = ownerMapper.selectList(queryWrapper);

        for(Owner owner : owners){
            if(owner.getOidauthflog().equals("0"))owner.setOidauthflog("未认证");
            else if(owner.getOidauthflog().equals("1"))owner.setOidauthflog("认证");

            if(owner.getOcertificationflog().equals("0"))owner.setOcertificationflog("未身份认证");
            else if(owner.getOcertificationflog().equals("1"))owner.setOcertificationflog("未认证");
            else if(owner.getOcertificationflog().equals("2"))owner.setOcertificationflog("认证");
        }

        model.addAttribute("owners",owners);

        return "admin/ownerinfo";
    }
    //业主信息管理页 删除业主信息
    @RequestMapping("/admin/deleteOwnering")
    public String deleteOwner(@RequestParam("hiddenOacconut")String hiddenOacconut,
                              @RequestParam("hiddenOidnum")String hiddenOidnum,
                              Model model,HttpSession session){
        Owner ownerinfo = new Owner();
        ownerinfo.setOacconut(hiddenOacconut);

        //逻辑处理得到Ono
        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner  : owners) {
            if(owner.getOidnum().equals(hiddenOidnum)){
                ownerinfo.setOno(owner.getOno());
            }
        }

        if (ownerMapper.deleteById(ownerinfo.getOno())> 0) System.out.println("Ono删除成功！");

        return "redirect:/admin/ownerinfo";
    }
    //业主信息管理页 添加业主信息
    @RequestMapping("/admin/addOwner")
    public String addOwnering(@RequestParam("Oacconut")String Oacconut, @RequestParam("Oname")String Oname,
                              @RequestParam("Oaddress")String Oaddress,@RequestParam("Ophonenum")String Ophonenum,
                              @RequestParam("Oidnum")String Oidnum, @RequestParam("Oidauthflog")String Oidauthflog,
                              @RequestParam("Ocertificationflog")String Ocertificationflog,
                              HttpSession session){

        Owner ownerinfo = new Owner();
        ownerinfo.setOacconut(Oacconut);
        ownerinfo.setOname(Oname);
        ownerinfo.setOaddress(Oaddress);
        ownerinfo.setOphonenum(Ophonenum);
        ownerinfo.setOidnum(Oidnum);
        if(Oidauthflog.equals("未认证"))Oidauthflog="0";
        else if(Oidauthflog.equals("认证"))Oidauthflog="1";
        ownerinfo.setOidauthflog(Oidauthflog);
        if(Ocertificationflog.equals("未身份认证"))Ocertificationflog="0";
        else if(Ocertificationflog.equals("未认证"))Ocertificationflog="1";
        else if(Ocertificationflog.equals("认证"))Ocertificationflog="2";
        ownerinfo.setOcertificationflog(Ocertificationflog);

        //逻辑处理得到Ono
        List<Owner> owners = ownerMapper.selectList(null);
        int maxono = 0;
        for (Owner owner : owners) {
            int ono = owner.getOno();
            if(ono > maxono) maxono = ono;
        }
        maxono++;
        ownerinfo.setOno(maxono);

        if (ownerMapper.insert(ownerinfo)>0){
            System.out.println("owner添加成功！");
        }
        return "redirect:/admin/ownerinfo";
    }
    //业主信息管理页 修改业主信息
    @RequestMapping("/admin/reviseOwner")
    public String toReviseOwner(@RequestParam("Oacconut")String Oacconut, @RequestParam("Oname")String Oname,
                                @RequestParam("Oaddress")String Oaddress,@RequestParam("Ophonenum")String Ophonenum,
                                @RequestParam("Oidnum")String Oidnum, @RequestParam("Oidauthflog")String Oidauthflog,
                                @RequestParam("oldOidnum")String oldOidnum, @RequestParam("Ocertificationflog")String Ocertificationflog,
                                HttpSession session){

        Owner ownerinfo = new Owner();
        ownerinfo.setOacconut(Oacconut);
        ownerinfo.setOname(Oname);
        ownerinfo.setOaddress(Oaddress);
        ownerinfo.setOphonenum(Ophonenum);
        ownerinfo.setOidnum(Oidnum);
        if(Oidauthflog.equals("未认证"))Oidauthflog="0";
        else if(Oidauthflog.equals("认证"))Oidauthflog="1";
        ownerinfo.setOidauthflog(Oidauthflog);
        if(Ocertificationflog.equals("未身份认证"))Ocertificationflog="0";
        else if(Ocertificationflog.equals("未认证"))Ocertificationflog="1";
        else if(Ocertificationflog.equals("认证"))Ocertificationflog="2";
        ownerinfo.setOcertificationflog(Ocertificationflog);

        List<Owner> owners = ownerMapper.selectList(null);
        for (Owner owner  : owners) {
            if(owner.getOidnum().equals(oldOidnum)){
                ownerinfo.setOno(owner.getOno());
            }
        }

        QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Ono",ownerinfo.getOno());
        if (ownerMapper.update(ownerinfo,queryWrapper) > 0){
            System.out.println("owner更新成功！");
        }
        return "redirect:/admin/ownerinfo";
    }
    //楼盘信息主页
    @RequestMapping("/admin/community")
    public String toCommunity(Model model, HttpSession session){

        //查询到所有楼盘信息
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);

        model.addAttribute("communityInfo",viewCommunityinfos);
        return "admin/community";
    }
    //楼盘信息查询页
    @RequestMapping("/admin/queryCommunity")
    public String queryCommunity(@RequestParam("queryCommunity")String queryCommunity,
                                 @RequestParam("queryArea")String queryArea,
                                 Model model, HttpSession session) {

        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("communityInfo",viewCommunityinfos);
        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);
        return "admin/community";
    }
    @RequestMapping("/admin/quomanage")
    public String selectCommunity(Model model, HttpSession session){

        //查询到所有楼盘信息
        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        model.addAttribute("communities",communities);
        model.addAttribute("areas",areas);

        model.addAttribute("communityInfo",viewCommunityinfos);
        return "admin/quomanage";
    }

    //楼盘区域管理管理页 添加楼盘区域
    @RequestMapping("/admin/addCommunity")
    public String addCommunity(@RequestParam("Dno")String Dno,@RequestParam("Cname")String Cname,
                               @RequestParam("Caddress")String Caddress,@RequestParam("Ccity")String Ccity,
                               @RequestParam("Aname")String Aname,
                               Model model,HttpSession session){
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        //Cno 逻辑处理操作
        int maxcno = 0;

        for (Community communitie : communities) {
            int cno = communitie.getCno();
            if(cno > maxcno)maxcno = cno;
            System.out.println("maxcno:"+maxcno);
        }
        maxcno++;
        System.out.println("lastmaxcno:"+maxcno);

        //Ano 逻辑处理操作
        int maxano = 0;
        for (Area area : areas) {
            int ano = area.getAno();
            if(ano > maxano)maxano = ano;
        }
        maxano++;

        //insert操作 community and area
        Community community = new Community();
        community.setCname(Cname);
        community.setCaddress(Caddress);
        community.setCcity(Ccity);
        community.setCphoto("0");

        community.setCno(maxcno);
        community.setDno(Integer.valueOf(Dno));
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
        return "redirect:/admin/quomanage";
    }


    //楼盘区域管理管理页 修改楼盘区域信息
    @RequestMapping("/admin/revise")
    public String toRevise(@RequestParam("Dno")String Dno,@RequestParam("Cname")String Cname,
                           @RequestParam("Caddress")String Caddress, @RequestParam("Ccity")String Ccity,
                           @RequestParam("hiddenCname2")String hiddenCname2, @RequestParam("hiddenAname2")String hiddenAname2,
                           @RequestParam("Aname")String Aname,
                           Model model,HttpSession session){

        Community community = new Community();
        community.setCname(Cname);
        community.setCaddress(Caddress);
        community.setCcity(Ccity);
        community.setDno(Integer.valueOf(Dno));
        community.setCphoto("0");
        System.out.println("Cname:"+community.getCname()+"正确");

        Area area = new Area();
        area.setAname(Aname);
        area.setAphoto("0");
        area.setAstatus("未开盘");
        System.out.println("Aname:"+area.getAname()+"正确");

        //查询到所有楼盘信息

        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);

        //equals 获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if(viewCommunityinfo.getCname().equals(hiddenCname2)){
                if(viewCommunityinfo.getAname().equals(hiddenAname2)){
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
        return "redirect:/admin/community";
    }

    //楼盘区域管理管理页 删除楼盘区域信息
    @RequestMapping("/admin/deleteCommunity")
    public String deleteCommunity(@RequestParam("hiddenCname")String hiddenCname,
                                  @RequestParam("hiddenAname")String hiddenAname,
                                  Model model,HttpSession session){

        Community community = new Community();
        community.setCname(hiddenCname);
        Area area = new Area();
        area.setAname(hiddenAname);
        //通过Cname and Aname搜索获得Cno and Ano
        //查询到所有楼盘信息

        List<ViewCommunityinfo> viewCommunityinfos = viewCommunityMapper.selectList(null);
        //equals 获得Cno和Ano
        for (ViewCommunityinfo viewCommunityinfo : viewCommunityinfos) {
            System.out.println("finding ");
            if(viewCommunityinfo.getCname().equals(community.getCname())){
                if(viewCommunityinfo.getAname().equals((area.getAname()))){
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

        return "redirect:/admin/community";
    }

   /* //楼盘区域管理管理页 添加楼盘区域
    @RequestMapping("/admin/addcommunity")
    public String addCommunity(@RequestParam("Dno")String Dno,@RequestParam("Cname")String Cname,
                               @RequestParam("Caddress")String Caddress,@RequestParam("Ccity")String Ccity,
                               @RequestParam("Aname")String Aname,
                               Model model,HttpSession session){
        List<Community> communities = communityMapper.selectList(null);
        List<Area> areas = areaMapper.selectList(null);

        //Cno 逻辑处理操作
        int maxcno = 0;

        for (Community communitie : communities) {
            int cno = communitie.getCno();
            if(cno > maxcno)maxcno = cno;
            System.out.println("maxcno:"+maxcno);
        }
        maxcno++;
        System.out.println("lastmaxcno:"+maxcno);

        //Ano 逻辑处理操作
        int maxano = 0;
        for (Area area : areas) {
            int ano = area.getAno();
            if(ano > maxano)maxano = ano;
        }
        maxano++;

        //insert操作 community and area
        Community community = new Community();
        community.setCname(Cname);
        community.setCaddress(Caddress);
        community.setCcity(Ccity);
        community.setCphoto("0");

        community.setCno(maxcno);
        community.setDno(Integer.valueOf(Dno));
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
        return "redirect:/admin/quomanage";
    }*/

}
