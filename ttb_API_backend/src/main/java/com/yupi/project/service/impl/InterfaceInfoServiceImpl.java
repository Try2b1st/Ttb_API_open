package com.yupi.project.service.impl;

import com.apicommon.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.InterfaceInfoMapper;
import com.yupi.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author 下水道的小老鼠
 * &#064;description  针对表【interface_info(接口信息)】的数据库操作Service实现
 * &#064;createDate  2023-06-10 14:39:19
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo> implements InterfaceInfoService {

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        String url = interfaceInfo.getUrl();
        String requestHeader = interfaceInfo.getRequestHeader();
        String responseHeader = interfaceInfo.getResponseHeader();
        String method = interfaceInfo.getMethod();

        if (StringUtils.isBlank(name) || name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "name:参数为空，或者过长");
        }

        if (StringUtils.isBlank(description) || description.length() > 200) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "description:参数为空，或者过长");
        }

        if (StringUtils.isBlank(url) || url.length() > 512) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "url:参数为空，或者过长");
        }

        if (StringUtils.isBlank(requestHeader)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "requestHeader:参数为空");
        }

        if (StringUtils.isBlank(responseHeader)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "responseHeader:参数为空");
        }

        if (StringUtils.isBlank(method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "method:参数为空，或者过长");
        }

    }
}




