package com.pojo;
import java.lang.Long;
import java.lang.Integer;

/**
 * 统计小区中的车位预定销售状况视图Pojo类
 *
 * @author 付伊豪
 * @date 2021/12
 */
public class ViewParkingCount {

    private String cno;
    private Integer maxPrice;
    private Integer minPrice;
    private Integer avgPrice;
    private Long parkingsum;
    private Long reserved;
    private Long sold;

    public ViewParkingCount() { }

    public ViewParkingCount(String cno) {
        this.cno = cno;
    }

    public ViewParkingCount(String cno, Integer maxPrice, Integer minPrice, Integer avgPrice, Long parkingsum, Long reserved, Long sold) {
        this.cno = cno;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.avgPrice = avgPrice;
        this.parkingsum = parkingsum;
        this.reserved = reserved;
        this.sold = sold;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public Integer getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Integer maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Integer getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(Integer avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Long getParkingsum() {
        return parkingsum;
    }

    public void setParkingsum(Long parkingsum) {
        this.parkingsum = parkingsum;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    public Long getSold() {
        return sold;
    }

    public void setSold(Long sold) {
        this.sold = sold;
    }
}
