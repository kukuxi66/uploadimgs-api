package com.Service.impl;

import com.Service.FileService;
import com.alibaba.fastjson.JSON;
import com.dao.dto.UserDto;
import com.dao.po.User;
import com.utils.JWTUtils;
import com.utils.RedisKey;
import com.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Result getFile(MultipartFile fileImage , String token) {
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
        final String imagePathRoot =  "E:\\图片\\test-image\\" + getDate() + "\\";
        File file = new File(imagePathRoot);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileName = fileImage.getOriginalFilename();
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String imageFilePath = imagePathRoot  +  userDto.getNickname() + "_" + uuid + fileType;
        try {
            fileImage.transferTo(new File(imageFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result("操作成功",imageFilePath);
    }

    private String getDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //时间转换
        String nowDate = new SimpleDateFormat("yyyy-MM").format(new Date()).toString();
        return nowDate;
    }

}
