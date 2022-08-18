package com.controller;

import com.Service.UserService;
import com.dao.vo.UserVo;
import com.utils.Result;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping ("getSeesionId")
    public Result getSeesionId(@RequestParam("code") String code){
        return userService.getSeesionId(code);
    }

    @PostMapping ("login")
    public Result login(@RequestBody UserVo userVo){
        return userService.login(userVo);
    }

    @GetMapping("userinfo")
    public Result userinfo(@RequestParam("token") String token){
        return userService.userinfo(token);
    }

}
