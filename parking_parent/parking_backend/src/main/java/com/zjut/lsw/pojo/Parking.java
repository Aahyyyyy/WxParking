package com.zjut.lsw.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("parking")
/**
 * show 车位的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 25
 * param  Ano和Sno是int类型，
 * Pbuildarra和Pusearea是Double类型,其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class Parking {
    @TableId(type = IdType.INPUT)
    private String Pno;
    private Integer Ano;
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
}
