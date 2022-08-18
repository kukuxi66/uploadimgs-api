package com.dao.vo;

import lombok.Data;

@Data
public class UserVo {

    private String seesionId;

    private String encryptedData;

    private String iv;

}
