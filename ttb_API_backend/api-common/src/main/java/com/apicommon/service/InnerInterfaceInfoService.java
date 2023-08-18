package com.apicommon.service;

import com.apicommon.model.entity.InterfaceInfo;

/**
* @author 下水道的小老鼠
* &#064;description  针对表【interface_info(接口信息)】的数据库操作Service
* &#064;createDate  2023-06-10 14:39:19
 */
public interface InnerInterfaceInfoService {

    /**
     * 获取被请求接口的信息，查询是否存在
     *
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);
}
