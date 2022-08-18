package com.dao.vo;

import lombok.Data;

@Data
public class WxUserInfoVo {

    private String openId;

    private String nickName;

    private String gender;

    private String city;

    private String province;

    private String country;

    private String avatarUrl;

    private String unionId;
}