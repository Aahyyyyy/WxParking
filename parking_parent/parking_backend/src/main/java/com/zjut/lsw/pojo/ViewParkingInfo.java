package com.zjut.lsw.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("parkinginfo")
/**
 *  @show 车位视图信息的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 *  @date 2021 / 11 / 7  19 : 29
 *  @param  Cno、Ano、Sno和Dno是int类型，
 *          Psalerprice和Psalernuitprice是Double类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class ViewParkingInfo {
    private Integer Cno;
    private String Cname;
    private Integer Ano;
    private String Aname;
    private String Pno;
    private String Plength;
    private String Pwide;
    private String Pbuildarra;
    private String Pusearea;
    private Double Psalerprice;
    private Double Psalernuitprice;
    private String Pstatus;
    private String Pcontract;
    private String Pinvoice;
    private Integer Sno;
    private String Sname;
    private String contract;
    private String invoice;
    private Integer Dno;
}
