## 到云后端


<!-- vim-markdown-toc GFM -->

* [RESTful APIs](#restful-apis)
    * [认证](#认证)
        * [登录](#登录)
        * [登出](#登出)
        * [JWT](#jwt)
        * [用户注册](#用户注册)
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


### 认证


#### 登录    

用于获取 **refresh token**, **refresh token**可以用于获取 **JWT Token**

Method: **POST**  
URI: `/apis/auth/login`  
参数:
```json
{
  "userName": "string", 
  "password": "string"
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
URI: `/apis/auth/login`  
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
URI:`/apis/auth/signup`  
参数:
```json
{
  "userName": "string", 
  "password": "string",
  "phone": "string"
}
```


### 用户信息

#### 修改用户信息

Method: **PUT** :key:  
URI:`/apis/user`  
参数:
```json
{
  "userName": "string",
  "birthday": "number",
  "gender": "string",
  "studentTeacherId": "string",
  "shcool": "string",
  "college": "string",
  "major": "string"
}
```
上面参数都是可选的, 不过至少需要一个.


#### 修改用户信息(需要密码)

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
  "roles": ["string"]
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
  "value": "string"
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
  "value": "string"
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
  "value": "string"
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
    "value": "string"
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
  "size": "number"
}
```
返回值:
```json
{
  "total": "number",
  "pairs": [
    {
      "key": "string",
      "value": "string"
    }
  ]
}
```

Method: **

## 测试

在HTTP请求头中加入`X-IM-ADMIN: daoyun`, 可以调用需要鉴权的API, 配置见 [application.yaml](./src/main/resources/application.yaml#L32)

