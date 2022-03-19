package com.zjut.lsw.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("orderinfo")
/**
 *  @show 订单视图信息的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 *  @date 2021 / 11 / 7  19 : 28
 *  @param  Sno、ORno和Ono是int类型，
 *          Psalerprice是Double类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class ViewOrderingInfo {
    private Integer ORno;
    private Integer Ono;
    private String Oname;
    private String Oacconut;
    private String Ophonenum;
    private String Oidnum;
    private String Pno;
    private Double Psalerprice;
    private String Pstatus;
    private String ORmonry;
    private String ORdate;
    private String ORstatue;
    private String ORpayment;
    private Integer Sno;
}
