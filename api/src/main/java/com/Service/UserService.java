package com.Service;

import com.dao.vo.UserVo;
import com.utils.Result;

public interface UserService {
    Result getSeesionId(String code);

    Result login(UserVo userVo);

    Result userinfo(String token);
}
