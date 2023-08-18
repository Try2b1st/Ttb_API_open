package com.yupi.project.service;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author 下水道的小老鼠
 */
public interface UserInterfaceInfoService extends IService<com.apicommon.model.entity.UserInterfaceInfo> {

    /**
     * 添加
     *
     * @param userInterfaceInfo
     * @param add
     */
    void validUserInterfaceInfo(com.apicommon.model.entity.UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口 接口调用次数加一
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long userId, long interfaceInfoId);
}
