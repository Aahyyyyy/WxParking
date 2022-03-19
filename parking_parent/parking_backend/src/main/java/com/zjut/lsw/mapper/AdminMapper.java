package com.zjut.lsw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjut.lsw.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
/**
 * show 管理员的mapper类，框架自动生成方法
 *  @author linshiwei
 * @date 2021 / 11 / 7  19 : 30
 * param 没有参数
 *  @return 具体看框架生成的方法
 * */
public interface AdminMapper extends BaseMapper<Admin> {

}
