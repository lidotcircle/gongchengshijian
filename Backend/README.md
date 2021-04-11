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
  "student_teacher_id": "string",
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
  "student_teacher_id": "string",
  "school": "string",
  "college": "string",
  "major": "string",
  "phone": "string",
  "email": "string",
  "roles": ["string"]
}
```

## 测试

在HTTP请求头中加入`X-IM-ADMIN: daoyun`, 可以调用需要鉴权的API, 配置见 [application.yaml](./src/main/resources/application.yaml#L32)

