package service;

import com.baomidou.mybatisplus.extension.service.IService;
import model.entity.UserInterfaceInfo;

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
//    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     *
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long userId,long interfaceInfoId);
}
