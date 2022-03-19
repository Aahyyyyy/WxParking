package com.zjut.lsw.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("allorder")
public class allorder {//销售情况统计
    @TableId(type = IdType.INPUT)
    private String Cno;
    private String Cname;
    private int num;//订单状态

   /* public String getCname() {
        return Cname;
    }

    public int getNum() {
        return num;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public void setNum(int num) {
        this.num = num;
    }*/
}
