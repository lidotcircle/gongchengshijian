## 到云后端


<!-- vim-markdown-toc GFM -->

* [RESTful APIs](#restful-apis)
    * [认证](#认证)
        * [获取短信](#获取短信)
        * [登录](#登录)
        * [短信登录](#短信登录)
        * [登出](#登出)
        * [JWT](#jwt)
        * [用户注册](#用户注册)
        * [快速注册](#快速注册)
    * [用户信息](#用户信息)
        * [修改用户信息](#修改用户信息)
        * [修改用户信息(需要密码)](#修改用户信息需要密码)
        * [获取用户信息](#获取用户信息)
    * [系统参数](#系统参数)
        * [创建](#创建)
        * [查询](#查询)
        * [修改](#修改)
        * [删除](#删除)
        * [获取全部](#获取全部)
        * [分页获取](#分页获取)
    * [短信验证码获取](#短信验证码获取)
* [短信服务](#短信服务)
* [测试](#测试)

<!-- vim-markdown-toc -->


## RESTful APIs

使用**GET**和**DELETE**请求时, API的参数包含在URI的 *params* 中; 使用**POST** **PUT**请求时, 
API的参数序列化为JSON格式包含在 *body*中.

用户身份认证的 Token 由 **Refresh Token** 和 **JWT Token** 组成, 
前者是用户登录获得用于保存用户的登录状态, 
后者则用前者通过对应 API 获得一般用与API调用的身份认证.
**JWT Token**有效期比较短, 可以在会话结束丢弃.

对于需要身份认证的API, HTTP 请求头需要包含`Authentication: JWT`. 需要身份认证的API, 会以:key:进行标记.

**IMPORTAN**: 
**用户名登录**, **获取短信验证码**失败超过3次(以后不确定)之后需要 **hcaptcha** 人机验证, 对应请求需要加入**captcha**字段.
对于因未加**captcha**字段失败的请求, 返回**Http Status 403 Forbidden**, 并返回以下错误
```json
"error": {
  "reason": "require captcha"
}
```
即一开始**captcha**字段可以为空, 再获取到**Http Status 403 Forbidden**并且异常为上面对象时, 需要进行**hcaptcha**人机验证

**对于时间相关数据的格式**
使用*ISO-8601*格式的子集, 通用`yyyy-mm-ddThh:MM:SS.sZ`, 如`2021-04-25T09:20:11.000Z`表示 **2021年4月25日 早上9点20分11秒0毫秒, 0时区**

### 认证

**IMPORTAN**: 
+ *captcha* 实现, 使用**hcaptcha**, 客户端需要使用hcaptcha提供的*API*, *sitekey*
+ *messageCode* 实现, `messageCode`短信验证码, `messageCodeToken`短信验证码的验证Token, 由服务器生成在短信请求返回给客户端
+ *分页获取* 分页获取中的`searchWildcard`字段使用*Mysql*数据库的通配符语法, `sortKey`为属性名, `sortDir`为`DESC`或`ASC`

#### 获取短信

Method: **POST**
URI: `/apis/message`
```json
{
  "phone": "string",
  "type": "string",
  "captcha": "string"
}
```
返回值:
```json
{
  "codeToken": "string"
}
```
*type* 的合法值 `login signup reset`

#### 登录    

用于获取 **refresh token**, **refresh token**可以用于获取 **JWT Token**

Method: **POST**  
URI: `/apis/auth/refresh-token`  
参数:
```json
{
  "userName": "string", 
  "password": "string",
  "captcha": "sring"
}
```
返回值:
```json
{
  "token": "string"
}
```


#### 短信登录

Method: **POST**  
URI: `/apis/auth/refresh-token/message`  
参数:
```json
{
  "phone": "string",
  "messageCode": "string",
  "messageCodeToken": "string"
}
```
返回值:
```json
{
  "token": "string"
}
```


#### 登出

删除对应的**refresh token**

Method: **DELETE**  
URI: `/apis/auth/refresh-token`  
参数:
```json
{
  "token": "string"
}
```


#### JWT

获取**JWT Token**

Method: **GET**   
URI: `/apis/auth/jwt`  
参数: 
```json
{
  "token": "string"
}
```
返回值:
```json
{
  "jwtToken": "string"
}
```


#### 用户注册

Method: **POST**  
URI:`/apis/auth/user`  
参数:
```json
{
  "userName": "string", 
  "password": "string",
  "phone": "string",
  "messsageCode": "string",
  "messsageCodeToken": "string",

  "以下为可选属性": "",
  "trueName": "string",
  "school": "string",
  "college": "string",
  "studentTeacherId": "string",
  "major": "string",
  "birthday": "string",
  "gender": "string"
}
```


#### 快速注册

最为注册以及短信登录的接口, 如果手机号码未注册, 则注册, 否则登录.

Method: **POST**  
URI: `/apis/auth/refresh-token/quick`  
参数:
```json
{
  "phone": "string",
  "messageCode": "string",
  "messageCodeToken": "string"
}
```
返回值:
``` json
{
  "refresh-token": "string"
}
```
快速注册所用的Message type为`login`


### 用户信息

#### 修改用户信息

Method: **PUT** :key:  
URI:`/apis/user`  
参数:
```json
{
  "name": "string",
  "birthday": "number",
  "gender": "string",
  "photo": "string",
  "studentTeacherId": "string",
  "shcool": "string",
  "college": "string",
  "major": "string"
}
```
上面参数都是可选的, 不过至少需要一个.


#### 修改用户信息(需要密码)

`photo`字段推荐base64, 格式如下`data:image/png;base64,...`, 也可以用图片的超链接

Method: **PUT** :key:  
URI:`/apis/user/privileged`  
参数:
```json
{
  "requiredPassword": "string",
  "password": "number",
  "phone": "string",
  "email": "email"
}
```
除了`requiredPassword`是必选的, 上面参数都是可选的, 不过至少需要一个.

#### 获取用户信息

Method: **GET** :key:  
URI: `/apis/user`  
参数: 无  
返回值: 
```json
{
  "userName": "string",
  "name": "string",
  "birthday": "string",
  "gender": "string",
  "studentTeacherId": "string",
  "school": "string",
  "college": "string",
  "major": "string",
  "phone": "string",
  "email": "string",
  "roles": ["string"],
  "photo": "string"
}
```


### 系统参数

*key* 的长度在2到48之间

#### 创建

Method: **POST** :key:  
URI:`/apis/sysparam'`  
条件: 对应 *key*没被使用  
参数:
```json
{
  "key": "string",
  "value": "string",
  "remark": "string"
}
```

#### 查询

Method: **GET** :key:  
URI:`/apis/sysparam`  
条件: 对应 *key*存在  
参数:
```json
{
  "key": "string"
}
```
返回值:
```json
{
  "key": "string",
  "value": "string",
  "remark": "string"
}
```

#### 修改

Method: **PUT** :key:  
URI:`/apis/sysparam`  
条件: 对应 *key*存在  
参数:
```json
{
  "key": "string",
  "value": "string",
  "remark": "string"
}
```

#### 删除

Method: **DELETE** :key:  
URI:`/apis/sysparam`  
参数:
```json
{
  "key": "string"
}
```

#### 获取全部

Method: **GET** :key:  
URI: `/apis/sysparam/all-key`  
返回值:
```json
[
  {
    "key": "string",
    "value": "string",
    "remark": "string"
  }
]
```

#### 分页获取

Method: **GET** :key:  
URI: `/apis/sysparam/page`  
参数: 
```json
{
  "pageno": "number",
  "size": "number",
  "sortDir": "string",
  "sortKey": "string",
  "searchWildcard": "string"
}
```
返回值:
```json
{
  "total": "number",
  "pairs": [
    {
      "key": "string",
      "value": "string",
      "remark": "string"
    }
  ]
}
```


### 短信验证码获取

Method: **POST** :key:  
URI: `/apis/message`  
参数:
```json
{
  "phone": "string",
  "type": "string",
  "captcha": "string"
}
```
返回值:
```json
{
  "codeToken": "string"
}
```
以上`type`字段为`login`, `signup`, `reset`对应不同的请求


## 短信服务

短信服务默认关闭, 所有短信验证码都为`666666`. 
在[application.yaml](./src/main/resources/application.yaml#L32)将`daoyun.message.enable`设为`true`
可以开启阿里云的短信服务, 需要对应的配置环境变量
```bash
export AliyunAccessKeyId=
export AliyunAccessKeySecret=
export AliyunSmsSignName=
```


## 测试

在HTTP请求头中加入`X-IM-ADMIN: daoyun`, 可以调用需要鉴权的API, 配置见 [application.yaml](./src/main/resources/application.yaml#L32)

