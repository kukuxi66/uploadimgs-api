package com.Service.impl;

import cn.hutool.http.HttpUtil;
import com.Service.UserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dao.dto.UserDto;
import com.dao.dto.UserInfoDto;
import com.dao.po.User;
import com.dao.vo.UserVo;
import com.dao.vo.WxUserInfoVo;
import com.mapper.UserMapper;
import com.utils.JWTUtils;
import com.utils.RedisKey;
import com.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private WxService wxService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${wxmini.secret}")
    private String secret;

    @Value("${wxmini.appid}")
    private String appid;

    @Resource
    private UserMapper userMapper;

    public Result getSeesionId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code";
        url = url.replace("{0}", appid).replace("{1}", secret).replace("{2}", code);
        String res = HttpUtil.get(url);
        String s = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID + s, res , 30, TimeUnit.MINUTES);
        return new Result(200,"操作成功",s);
    }

    @Override
    public Result login(UserVo userVo) {
        try {
            System.out.println(userVo);
            String json = wxService.wxDecrypt(userVo.getEncryptedData(), userVo.getSeesionId(), userVo.getIv());
            WxUserInfoVo wxUserInfoVo = JSON.parseObject(json, WxUserInfoVo.class);
            wxUserInfoVo.setOpenId(wxService.getOpenId(userVo.getSeesionId()));
            String oepnId =wxUserInfoVo.getOpenId();
            LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
            userLambdaQueryWrapper.eq(User::getOpenId,oepnId);
            userLambdaQueryWrapper.last("limit 1");
            User user = userMapper.selectOne(userLambdaQueryWrapper);
            UserDto userDto = new UserDto();
            userDto.from(wxUserInfoVo);

            if (user == null){
                //注册
                return register(userDto);
            }else{
                //登录
                userDto.setId(user.getId());
                return userLogin(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Result("发生错误");
    }

    @Override
    public Result userinfo(String token) {
        token = token.replace("Bearer ","");
        boolean verify = JWTUtils.verify(token);
        if (!verify){
            return new Result("未登录");
        }
        String userJson = redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
        if (StringUtils.isBlank(userJson)){
            return new Result("未登录");
        }
        UserDto userDto = JSON.parseObject(userJson, UserDto.class);
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setAvatarUrl(userDto.getPortrait());
        userInfoDto.setNickName(userDto.getNickname());
        return new Result(userInfoDto);
    }

    private Result userLogin(UserDto userDto) {
        String token = JWTUtils.sign(userDto.getId());
        userDto.setToken(token);
        userDto.setOpenId(null);
        userDto.setWxUnionId(null);
        redisTemplate.opsForValue().set(RedisKey.TOKEN+token,JSON.toJSONString(userDto),7,TimeUnit.DAYS);
        return new Result(userDto);
    }

    private Result register(UserDto userDto) {
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        userMapper.insert(user);
        userDto.setId(user.getId());
        return new Result(userDto);
    }

}
