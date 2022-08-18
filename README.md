# Springboot微信小程序实现微信一键授权登录+图片上传功能（后端）

#### 介绍
Springboot微信小程序实现微信一键授权登录+图片上传功能（后端）



登录：
小程序调用wx.login()获得登录凭证code，把code发到后端，后端携带appid、appsecret、code向微信服务器调用换取openId和session_key，后端生成sessionId与换取的openid和session_key关联，将sessionId存入redis和小程序缓存
小程序调用wx.getUserProfile获取encryptedData、iv，从缓存获取sessionId，一起发送到后端，后端根据sessionId获取session_key，然后用微信小程序文档中的解密方法解密敏感数据。
判断openid是否存在数据库，若存在则直接登录，若不存在则往数据库插入用户信息，登录成功后将用户信息和token发送到小程序，小程序将token保存在缓存

图片上传：
用uploadFile方法，多张上传：每张图片执行一次上传请求，请求响应成功后再调用请求上传第二张图片，以此类推
