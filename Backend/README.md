## 到云后端


### mysql & redis

mysql 和 redis 使用 **docker** 容器, 配置见[docker-compose.yaml](./docker-compose.yaml)


### RESTful APIs

使用**GET**和**DELETE**请求时, API的参数包含在URI的 *params* 中; 使用**POST** **PUT**请求时, 
API的参数序列化为JSON格式包含在 *body*中.


#### 认证


##### 登录    

用于获取 **refresh token**, **refresh token**可以用于获取 **JWT Token**

Method: **POST**  
URI: `/apis/auth/login`  
参数:
```json
{
  "userName": <string>, 
  "password": <string>
}
```


##### 登出

删除对应的**refresh token**  

Method: **DELETE**
URI: `/apis/auth/login`  
参数:
```json
{
  "token": <string>
}```


##### JWT

获取**JWT Token**

Method: **GET**   
URI: `/apis/auth/jwt`
参数: 
```json
{
  "token": <string>
}```


##### 用户注册

Method: **POST**  
URI:`/apis/auth/signup`
参数:
```json
{
  "userName": <string>, 
  "password": <string>,
  "phone": <string>,
}```

### 测试

在HTTP请求头中加入`X-IM-ADMIN: daoyun`, 可以调用需要鉴权的API, 配置见 [application.yaml](./src/main/resources/application.yaml#L32)

