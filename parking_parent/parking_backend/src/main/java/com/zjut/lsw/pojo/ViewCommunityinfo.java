package com.zjut.lsw.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("communityinfo")
/**
 *  @show 楼盘视图信息的实体类，框架自动生成set、get和无参方法
 *  @author linshiwei
 *  @date 2021 / 11 / 7  19 : 27
 *  @param  Cno、Ano和Dno是int类型，其他都是String类型
 *  @return 具体看框架生成的方法
 * */
public class ViewCommunityinfo {
    private Integer Cno;
    private String Cname;
    private String Caddress;
    private String Ccity;
    private Integer Ano;
    private String Aname;
    private String Astatus;
    private Integer Dno;

}
