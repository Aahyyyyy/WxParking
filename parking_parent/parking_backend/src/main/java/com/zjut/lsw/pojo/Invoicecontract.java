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
@TableName("invoice")
/**
 * show 合同发票的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 21
 * param  ICno是int类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class Invoicecontract {
    @TableId(type = IdType.INPUT)
    private Integer ICno;
    private String contract;
    private String invoice;
    private String Pno;
}
