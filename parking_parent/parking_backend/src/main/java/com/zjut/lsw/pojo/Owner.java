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
@TableName("owner")
/**
 * show 业主的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 24
 * param  Ono是int类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class Owner {
    @TableId(type = IdType.INPUT)
    private Integer Ono;
    private String Oacconut;
    private String Oname;
    private String Ophonenum;
    private String Oidnum;
    private String Oidauthflog;
    private String Ocertificationflog;
    private String Oaddress;
    private String Oemail;

}
