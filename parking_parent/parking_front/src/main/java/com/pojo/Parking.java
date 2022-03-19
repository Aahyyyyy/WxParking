package com.pojo;

/**
 * 车位Pojo类
 *
 * @author 付伊豪
 * @date 2021/12
 */
public class Parking {
    private String pno;
    private String plength;
    private String pwide;
    private String pbuildarra;
    private String pusearea;
    private String psalerprice;
    private String psalernuitprice;
    private String pstatus;
    private String sno;
    private String aname;
    private String ano;
    private String cname;
    private Integer cno;
    private String astatus;
    private Integer bno;
    private Double Pdistance;

    public Parking() { }

    public Parking(String pno) {
        this.pno = pno;
    }

    public Parking(String pno, String plength, String pwide, String pbuildarra, String pusearea, String psalerprice, String psalernuitprice, String pstatus, String sno, String aname, String ano, String cname, Integer cno, String astatus, Integer bno, Double pdistance) {
        this.pno = pno;
        this.plength = plength;
        this.pwide = pwide;
        this.pbuildarra = pbuildarra;
        this.pusearea = pusearea;
        this.psalerprice = psalerprice;
        this.psalernuitprice = psalernuitprice;
        this.pstatus = pstatus;
        this.sno = sno;
        this.aname = aname;
        this.ano = ano;
        this.cname = cname;
        this.cno = cno;
        this.astatus = astatus;
        this.bno = bno;
        Pdistance = pdistance;
    }

    public Double getPdistance() {
        return Pdistance;
    }

    public void setPdistance(Double pdistance) {
        Pdistance = pdistance;
    }

    public Integer getCno() {
        return cno;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public Integer getBno() {
        return bno;
    }

    public void setBno(Integer bno) {
        this.bno = bno;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPlength() {
        return plength;
    }

    public void setPlength(String plength) {
        this.plength = plength;
    }

    public String getPwide() {
        return pwide;
    }

    public void setPwide(String pwide) {
        this.pwide = pwide;
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

    public String getPsalerprice() {
        return psalerprice;
    }

    public void setPsalerprice(String psalerprice) {
        this.psalerprice = psalerprice;
    }

    public String getPsalernuitprice() {
        return psalernuitprice;
    }

    public void setPsalernuitprice(String psalernuitprice) {
        this.psalernuitprice = psalernuitprice;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }


    public String getAstatus() {
        return astatus;
    }

    public void setAstatus(String astatus) {
        this.astatus = astatus;
    }
}
