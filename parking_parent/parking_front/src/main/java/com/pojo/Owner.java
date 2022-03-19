package com.pojo;

/**
 * 业主Pojo类
 *
 * @author 闫宏宇
 * @date 2021/12
 */
public class Owner {
    private String Ono;
    private String Oacconut;
    private String Oname;
    private String Ophonenum;
    private String Oidnum;
    private String Oaddress;
    private String Oidauthflog;
    private String Ocertificationflog;
    private String Oemail;
    private String Bname;

    public Owner() { }

    public Owner(String ono) {
        Ono = ono;
    }

    public Owner(String ono, String oaccount, String oname, String ophonenum, String oidnum, String oaddress, String oidauthflog, String ocertificationflog, String oemail, String bname) {
        Ono = ono;
        Oacconut = oaccount;
        Oname = oname;
        Ophonenum = ophonenum;
        Oidnum = oidnum;
        Oaddress = oaddress;
        Oidauthflog = oidauthflog;
        Ocertificationflog = ocertificationflog;
        Oemail = oemail;
        Bname = bname;
    }

    public String getOno() {
        return Ono;
    }

    public void setOno(String ono) {
        Ono = ono;
    }

    public String getOacconut() {
        return Oacconut;
    }

    public void setOacconut(String oaccount) {
        Oacconut = oaccount;
    }

    public String getOname() {
        return Oname;
    }

    public void setOname(String oname) {
        Oname = oname;
    }

    public String getOphonenum() {
        return Ophonenum;
    }

    public void setOphonenum(String ophonenum) {
        Ophonenum = ophonenum;
    }

    public String getOaddress() {
        return Oaddress;
    }

    public void setOaddress(String oaddress) {
        Oaddress = oaddress;
    }

    public String getOidauthflog() {
        return Oidauthflog;
    }

    public void setOidauthflog(String oidauthflog) {
        Oidauthflog = oidauthflog;
    }

    public String getOcertificationflog() {
        return Ocertificationflog;
    }

    public void setOcertificationflog(String ocertificationflog) {
        Ocertificationflog = ocertificationflog;
    }

    public String getOidnum() {
        return Oidnum;
    }

    public void setOidnum(String oidnum) { Oidnum = oidnum; }

    public String getOemail() {
        return Oemail;
    }

    public void setOemail(String oemail) { Oemail = oemail; }

    public String getBname() { return Bname; }

    public void setBname(String bname) { Bname = bname; }
}
