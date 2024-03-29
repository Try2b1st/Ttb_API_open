package com.yupi.project.service;

import com.apicommon.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 下水道的小老鼠
* &#064;description  针对表【interface_info(接口信息)】的数据库操作Service
* &#064;createDate  2023-06-10 14:39:19
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 校验请求参数
     *
     * @param interfaceInfo 接口信息
     * @param add 对数据库的操作
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);
}
