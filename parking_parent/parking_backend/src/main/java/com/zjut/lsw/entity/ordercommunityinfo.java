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
@TableName("ordercommunityinfo")
public class ordercommunityinfo {
    @TableId(type = IdType.INPUT)
    private String ORno;//订单编号
    private String Cname;
    private String ORstatue;//订单状态

    public String getORno() {
        return ORno;
    }

    public String getCname() {
        return Cname;
    }

    public String getORstatue() {
        return ORstatue;
    }

    public void setCname(String cname) {
        Cname = cname;
    }

    public void setORno(String ORno) {
        this.ORno = ORno;
    }

    public void setORstatue(String ORstatue) {
        this.ORstatue = ORstatue;
    }
}
