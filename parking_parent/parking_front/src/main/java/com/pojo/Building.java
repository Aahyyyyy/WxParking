package com.pojo;

import java.math.BigDecimal;

/**
 * 小区楼房Pojo类
 *
 * @author 闫宏宇
 * @date 2021/12
 */
public class Building {
    private Integer bno;
    private Integer cno;
    private String bname;
    private BigDecimal lat;
    private BigDecimal lng;

    public Building() { }

    public Building(Integer bno) {
        this.bno = bno;
    }

    public Building(Integer bno, Integer cno, String bname, BigDecimal lat, BigDecimal lng) {
        this.bno = bno;
        this.cno = cno;
        this.bname = bname;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getBno() { return bno; }

    public void setBno(Integer bno) {
        this.bno = bno;
    }

    public Integer getCno() {
        return cno;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) { this.bname = bname; }

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
}

