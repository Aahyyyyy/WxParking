package com.zjut.lsw.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjut.lsw.pojo.Invoicecontract;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface InvoicecontactMapper extends BaseMapper<Invoicecontract> {
}
