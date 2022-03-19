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
@TableName("developer")
/**
 * show 项目公司的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 20
 * param  Dno是int类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class Developer {
    @TableId(type = IdType.INPUT)
    private Integer Dno;
    private String Daccount;
    private String Dpassword;
    private String Dname;
    private String Daddress;
    private String Dphonenum;
}
