package com.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//返回结果类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private Integer code;
    private String message;
    private Object data;

    public Result(Integer code , String message ){
        this.code = code;
        this.message = message;
    }

    public Result(Integer code , Object data ){
        this.code = code;
        this.data = data;
    }

    public Result(String message , Object data ){
        this.message = message;
        this.data = data;
    }

    public Result(Object data){
        this.data = data;
    }
}
