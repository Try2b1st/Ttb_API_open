package com.apicommon.service;


import com.apicommon.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {

    /**
     * 检查用户是否分配密钥
     *
     * @param accessKey
     * @param secretKey
     * @return
     */
    User getInvokeUser(String accessKey, String secretKey);
}
