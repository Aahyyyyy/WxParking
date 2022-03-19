package com.pojo;

/**
 * 订单Pojo类
 *
 * @author 闫宏宇
 * @date 2021/12
 */
public class Ordering {
    private String oacconut;
    private String dname;
    private String cname;
    private String aname;
    private String pno;
    private String pbuildarra;
    private String pusearea;
    private String psalernuitprice;
    private String pstatus;
    private String orno;
    private String ordate;
    private String orstatue;
    private String orpayment;

    public Ordering() { }

    public Ordering(String orno) {
        this.orno = orno;
    }

    public Ordering(String oacconut, String dname, String cname, String aname, String pno, String pbuildarra, String pusearea, String psalernuitprice, String pstatus, String orno, String ordate, String orstatue, String orpayment) {
        this.oacconut = oacconut;
        this.dname = dname;
        this.cname = cname;
        this.aname = aname;
        this.pno = pno;
        this.pbuildarra = pbuildarra;
        this.pusearea = pusearea;
        this.psalernuitprice = psalernuitprice;
        this.pstatus = pstatus;
        this.orno = orno;
        this.ordate = ordate;
        this.orstatue = orstatue;
        this.orpayment = orpayment;
    }

    public String getOacconut() {
        return oacconut;
    }

    public void setOacconut(String oacconut) {
        this.oacconut = oacconut;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPbuildarra() {
        return pbuildarra;
    }

    public void setPbuildarra(String pbuildarra) {
        this.pbuildarra = pbuildarra;
    }

    public String getPusearea() {
        return pusearea;
    }

    public void setPusearea(String pusearea) {
        this.pusearea = pusearea;
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

    public String getOrdate() {
        return ordate;
    }

    public void setOrdate(String ordate) {
        this.ordate = ordate;
    }

    public String getOrstatue() {
        return orstatue;
    }

    public void setOrstatue(String orstatue) {
        this.orstatue = orstatue;
    }

    public String getOrpayment() {
        return orpayment;
    }

    public void setOrpayment(String orpayment) {
        this.orpayment = orpayment;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getOrno() {
        return orno;
    }

    public void setOrno(String orno) {
        this.orno = orno;
    }
}
