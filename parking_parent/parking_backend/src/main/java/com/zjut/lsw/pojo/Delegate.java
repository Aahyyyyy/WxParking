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
@TableName("delegate")
/**
 * show 委托关系的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 19
 * param  Dno和Sno是int类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class Delegate {
    @TableId(type = IdType.INPUT)
    private Integer Dno;
    private Integer Sno;
}
