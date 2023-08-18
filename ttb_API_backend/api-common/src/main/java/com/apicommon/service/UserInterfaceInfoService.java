package com.apicommon.service;

import com.apicommon.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.apicommon.model.entity.User;
import com.apicommon.model.entity.UserInterfaceInfo;

/**
 * @author 下水道的小老鼠
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
 * @createDate 2023-08-15 16:17:02
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 检查用户是否分配密钥
     *
     * @param accessKey
     * @param secretKey
     * @return
     */
    User getInvokeUser(String accessKey, String secretKey);

    /**
     * 获取被请求接口的信息，查询是否存在
     *
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

    /**
     * 调用接口 接口调用次数加一
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long userId, long interfaceInfoId);
}
