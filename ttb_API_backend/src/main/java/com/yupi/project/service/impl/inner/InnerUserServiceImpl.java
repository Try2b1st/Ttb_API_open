package com.yupi.project.service.impl.inner;

import com.apicommon.model.entity.User;
import com.apicommon.service.InnerUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yupi.project.common.ErrorCode;
import com.yupi.project.exception.BusinessException;
import com.yupi.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author 下水道的小老鼠
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey, String secretKey) {
        if(StringUtils.isAnyBlank(accessKey,secretKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        queryWrapper.eq("secretKey",secretKey);
        return userMapper.selectOne(queryWrapper);
    }
}
