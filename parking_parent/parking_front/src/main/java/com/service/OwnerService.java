package com.service;

import com.pojo.Owner;
import java.util.List;

/**
 * 业主服务类
 *
 * @author : 闫宏宇
 * @date : 2021/12/21
 */
public interface OwnerService {
    /**
     * 完成业主身份认证
     *
     * @param owner 业主信息
     * @return void
     */
    void doOwnerIden(Owner owner);

    /**
     * 完成实名认证
     *
     * @param owner 业主信息
     * @return void
     */
    void doOwnerCert(Owner owner);

    /**
     * 完成人脸识别
     *
     * @param id 业主ID
     * @return void
     */
    void doOwnerCheck(String id);

    /**
     * 完成业主信息维护
     *
     * @param owner 业主信息
     * @return void
     */
    void doOwnerModify(Owner owner);

    /**
     * 根据业主ID查询业主真实姓名
     *
     * @param id 业主ID
     * @return {@link String }
     */
    String getOname(String id);

    /**
     * 根据业主ID查询业主信息
     *
     * @param id 业主ID
     * @return {@link Owner }
     */
    Owner findOwnerById(String id);

    /**
     * 根据业主ID查询业主信息
     *
     * @param ono 业主编号
     * @return {@link List<Owner> }
     */
    List<Owner> queryByOno(String ono);
}
