package com.pojo;

/**
 * 收藏Pojo类
 *
 * @author 闫宏宇
 * @date 2021/12
 */
public class Like {
    private String pno;
    private String psalernuitprice;
    private String pstatus;
    private String cname;
    private String caddress;
    private String aname;
    private String oacconut;

    public Like() { }

    public Like(String pno) {
        this.pno = pno;
    }

    public Like(String pno, String psalernuitprice, String pstatus, String cname, String caddress, String aname, String oacconut) {
        this.pno = pno;
        this.psalernuitprice = psalernuitprice;
        this.pstatus = pstatus;
        this.cname = cname;
        this.caddress = caddress;
        this.aname = aname;
        this.oacconut = oacconut;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPsalernuitprice() {
        return psalernuitprice;
    }

    public void setPsalernuitprice(String psalernuitprice) {
        this.psalernuitprice = psalernuitprice;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCaddress() {
        return caddress;
    }

    public void setCaddress(String caddress) {
        this.caddress = caddress;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getOacconut() {
        return oacconut;
    }

    public void setOacconut(String oacconut) {
        this.oacconut = oacconut;
    }
}
