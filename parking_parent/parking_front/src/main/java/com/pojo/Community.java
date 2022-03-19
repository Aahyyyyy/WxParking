package com.pojo;
import java.math.BigDecimal;

/**
 * 小区Pojo类
 *
 * @author 闫宏宇
 * @date 2021/12
 */
public class Community {
    private Integer cno;
    private String cname;
    private String caddress;
    private String cphoto;
    private String dno;
    private BigDecimal lat;
    private BigDecimal lng;
    private String Ccity;

    public Community() { }

    public Community(Integer cno) {
        this.cno = cno;
    }

    public Community(Integer cno, String cname, String caddress, String cphoto, String dno, String ccity, BigDecimal lat, BigDecimal lng) {
        this.cno = cno;
        this.cname = cname;
        this.caddress = caddress;
        this.cphoto = cphoto;
        this.dno = dno;
        this.Ccity = ccity;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCcity() {
        return Ccity;
    }

    public void setCcity(String ccity) {
        Ccity = ccity;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public Integer getCno() {
        return cno;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
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

    public String getCphoto() {
        return cphoto;
    }

    public void setCphoto(String cphoto) {
        this.cphoto = cphoto;
    }

    public String getDno() {
        return dno;
    }

    public void setDno(String dno) {
        this.dno = dno;
    }
}
