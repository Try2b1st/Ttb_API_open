package com.yupi.project.service.impl;

import com.apicommon.model.entity.UserInterfaceInfo;
import com.apicommon.service.InnerUserInterfaceInfoService;
import com.yupi.project.service.UserInterfaceInfoService;

import javax.annotation.Resource;

/**
 * @author 下水道的小老鼠
 */
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long userId, long interfaceInfoId) {
        return userInterfaceInfoService.invokeCount(userId,interfaceInfoId);
    }
}
