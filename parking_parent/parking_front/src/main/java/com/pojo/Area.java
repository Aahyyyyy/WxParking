package com.pojo;

/**
 * 小区车位区域Pojo类
 *
 * @author 闫宏宇
 * @date 2021/12
 */
public class Area {
    private String ano;
    private String aname;
    private String aphoto;
    private String cno;
    private String astatus;

    public Area() { }

    public Area(String ano) {
        this.ano = ano;
    }

    public Area(String ano, String aname, String aphoto, String cno, String astatus) {
        this.ano = ano;
        this.aname = aname;
        this.aphoto = aphoto;
        this.cno = cno;
        this.astatus = astatus;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getAname() {
        return aname;
    }

    public void setAname(String aname) {
        this.aname = aname;
    }

    public String getAphoto() {
        return aphoto;
    }

    public void setAphoto(String aphoto) {
        this.aphoto = aphoto;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getAstatus() {
        return astatus;
    }

    public void setAstatus(String astatus) {
        this.astatus = astatus;
    }
}
