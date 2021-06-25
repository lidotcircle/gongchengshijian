## 到云后端


<!-- vim-markdown-toc GFM -->

* [环境变量](#环境变量)
* [RESTful APIs](#restful-apis)
    * [认证](#认证)
        * [获取短信](#获取短信)
        * [登录](#登录)
        * [短信登录](#短信登录)
        * [登出](#登出)
        * [JWT](#jwt)
        * [用户注册](#用户注册)
        * [重置密码请求](#重置密码请求)
        * [重置密码](#重置密码)
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
    * [数据字典](#数据字典)
        * [新建字典](#新建字典)
        * [删除字典](#删除字典)
        * [获取字典](#获取字典)
        * [分页获取字典](#分页获取字典)
        * [修改字典](#修改字典)
    * [数据字典项](#数据字典项)
        * [新建字典项](#新建字典项)
        * [删除字典项](#删除字典项)
        * [获取字典项](#获取字典项)
        * [分页获取字典项](#分页获取字典项)
        * [更新字典项](#更新字典项)
    * [班课](#班课)
        * [新建班课](#新建班课)
        * [获取班课](#获取班课)
        * [分页获取班课(教师或者老师)](#分页获取班课教师或者老师)
        * [分页获取班课(学生)](#分页获取班课学生)
        * [分页获取班课(教师)](#分页获取班课教师)
        * [分页获取班课(管理员)](#分页获取班课管理员)
        * [删除班课](#删除班课)
        * [更新班课](#更新班课)
    * [班课学生相关](#班课学生相关)
        * [邀请学生](#邀请学生)
        * [删除学生](#删除学生)
        * [加入班课](#加入班课)
        * [退出班课](#退出班课)
    * [班课任务与通知](#班课任务与通知)
        * [创建](#创建-1)
        * [删除](#删除-1)
        * [获取](#获取)
        * [修改](#修改-1)
        * [获取任务提交列表](#获取任务提交列表)
        * [学生获取提交的任务](#学生获取提交的任务)
    * [任务提交、修改相关](#任务提交修改相关)
        * [提交任务](#提交任务)
        * [删除](#删除-2)
        * [获取](#获取-1)
        * [修改](#修改-2)
    * [班课签到创建、修改相关](#班课签到创建修改相关)
        * [创建](#创建-2)
        * [删除](#删除-3)
        * [获取](#获取-2)
        * [修改](#修改-3)
        * [获取已签到列表](#获取已签到列表)
        * [学生获取签到记录](#学生获取签到记录)
    * [学生签到相关](#学生签到相关)
        * [学生签到](#学生签到)
        * [获取](#获取-3)
    * [短信验证码获取](#短信验证码获取)
    * [菜单管理](#菜单管理)
* [短信服务](#短信服务)
* [测试](#测试)

<!-- vim-markdown-toc -->

## 环境变量

```base
export PORT=
export DB_URL=
export DB_USER=
export DB_PASS=
export REDIS_HOST=
export REDIS_PORT=
export REDIS_PASS=
export JWT_SECRET=
export AliyunAccessKeyId=
export AliyunAccessKeySecret=
export ALIYUN_SMS_SIGN_NAME=
export SMS_LOGIN_TEMPLATE_CODE=
export SMS_SIGNUP_TEMPLATE_CODE=
export SMS_RESET_TEMPLATE_CODE=
export SMS_BIND_PHONE_TEMPLATE_CODE=
```


## RESTful APIs

使用**GET**和**DELETE**请求时, API的参数包含在URI的 *params* 中; 使用**POST** **PUT**请求时, 
API的参数序列化为JSON格式包含在 *body*中.

用户身份认证的 Token 由 **Refresh Token** 和 **JWT Token** 组成, 
前者是用户登录获得用于保存用户的登录状态, 
后者则用前者通过对应 API 获得一般用与API调用的身份认证.
**JWT Token**有效期比较短, 可以在会话结束丢弃.

对于需要身份认证的API, HTTP 请求头需要包含`Authorization: JWT`. 需要身份认证的API, 会以:key:进行标记.

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


#### 重置密码请求

Method: **POST**  
URI: `/apis/auth/password/reset-token`  
参数:
```typescript
{
  phone: string;
  messageCode: string;
  messageCodeToken: string;
}
```
返回:
```typescript
{
  resetToken: string;
}
```


#### 重置密码

Method: **PUT**  
URI: `/apis/auth/password`  
参数:
```typescript
{
  resetToken: string;
  password: string;
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


### 数据字典

#### 新建字典

Method: **POST** :key:  
URI: `/apis/datadict/type`  
参数: 
``` typescript
{
    typeCode: string;
    typeName: string;
    remark?: string;
}
```

#### 删除字典

Method: **DELETE** :key:  
URI: `/apis/datadict/type`  
参数: 
``` typescript
{
    typeCode: string;
}
```

#### 获取字典

Method: **GET** :key:  
URI: `/apis/datadict/type`  
参数: 
``` typescript
{
    typeCode: string;
}
```
返回值:
``` typescript
{
    typeCode: string;
    typeName: string;
    remark: string | null;
}
```


#### 分页获取字典

Method: **GET** :key:  
URI: `/apis/datadict/type/page`  
参数: 
``` typescript
{
    pageno: number;
    size: number;
    sortDir: "desc" | "asc";
    sortKey: string;
    searchWildcard： string; // Mysql 搜索语法
}
```
返回值:
``` typescript
{
    total: number; // 分页长度
    pairs: DictType[];
}

interface DictType {
    typeCode: string;
    typeName: string;
    remark: string;
}
```


#### 修改字典

Method: **PUT** :key:  
URI: `/apis/datadict/type`  
参数: 
``` typescript
{
    typeCode: string;
    typeName: string;
    remark?: string;
}
```


### 数据字典项

#### 新建字典项

Method: **POST** :key:  
URI: `/apis/datadict/data`  
参数: 
``` typescript
{
    typeCode: string;
    keyword: string;
    value: string;
    isDefault?: boolean;
    order?: number;
    remark?: string;
}
```

#### 删除字典项

Method: **DELETE** :key:  
URI: `/apis/datadict/data`  
参数: 
``` typescript
{
    typeCode: string;
    keyword: string;
}
```

#### 获取字典项

Method: **GET** :key:  
URI: `/apis/datadict/data`  
参数: 
``` typescript
{
    typeCode: string;
    keyword: string;
}
```
返回值:
``` typescript
{
    typeCode: string;
    keyword: string;
    value: string;
    isDefault?: boolean;
    order?: number;
    remark?: string;
}
```

#### 分页获取字典项

Method: **GET** :key:  
URI: `/apis/datadict/data/page`  
参数: 
``` typescript
{
    pageno: number;
    size: number;
    typeCode: stirng;
    sortDir?: "desc" | "asc";
    sortKey?: string;
    searchWildcard?: string;
}
```
返回值:
``` typescript
{
    total: number;
    pairs: DictData[];
}
interface DictData {
    typeCode: string;
    keyword: string;
    value: string;
    isDefault?: boolean;
    order?: number;
    remark?: string;
}
```

#### 更新字典项

Method: **PUT** :key:  
URI: `/apis/datadict/data`  
参数: 
``` typescript
{
    typeCode: string;
    keyword: string;
    value: string;
    isDefault?: boolean;
    order?: number;
    remark?: string;
}
```


### 班课

#### 新建班课

Method: **POST** :key:  
URI: `/apis/course`  
参数: 
``` typescript
{
    courseName: string;
    briefDescription?: string;
}
```
返回值:
``` typescript
    courseExId: string; // 10位数字
```

#### 获取班课

Method: **GET** :key:  
URI: `/apis/course`  
参数: 
``` typescript
{
    courseExId: string;
}
```
返回值:
``` typescript
{
    courseExId: string;
    courseName: string;
    briefDescription: string;
    teacher: Teacher;
    students: Student[];
    tasks: Task[];
}
interface Teacher {
    userName: string;
    name: string;
}
interface Student {
    userName: string;
    name: string;
    studentTeacherId: string;
    score: number;
}
interface Task {
    id: number;
    releasedate: Date; // 日期格式为 iso8601Format
    committable: boolean; // 该任务是否需要学生提交, 不需要提交的认为可以认为是通知
    taskTilte: string;
    content: string;
}
```

#### 分页获取班课(教师或者老师)

返回的班课中, 用户是班课的老师或者学生, 具体可以从返回值的班课中的教师字段判断

Method: **GET** :key:  
URI: `/apis/course/page`  
参数: 
``` typescript
{
    pageno: number;
    size: number;
    sortDir: "desc" | "asc";
    searchWildcard: string;
}
```
返回值:
``` typescript
{
    total: number;
    pairs: Course[];
}
interface Course {
    // 见获取班课的返回值
}
```

#### 分页获取班课(学生)

返回的班课中, 用户的角色都是学生

Method: **GET** :key:  
URI: `/apis/course/student/page`  
参数: 
``` typescript
{
    pageno: number;
    size: number;
    sortDir: "desc" | "asc";
    searchWildcard: string;
}
```
返回值:
``` typescript
{
    total: number;
    pairs: Course[];
}
interface Course {
    // 见获取班课的返回值
}
```

#### 分页获取班课(教师)

返回的班课中, 用户的角色都是教师

Method: **GET** :key:  
URI: `/apis/course/teacher/page`  
参数: 
``` typescript
{
    pageno: number;
    size: number;
    sortDir: "desc" | "asc";
    searchWildcard: string;
}
```
返回值:
``` typescript
{
    total: number;
    pairs: Course[];
}
interface Course {
    // 见获取班课的返回值
}
```

#### 分页获取班课(管理员)

返回系统中所有的班课

Method: **GET** :key:  
URI: `/apis/course/page/super`  
参数: 
``` typescript
{
    pageno: number;
    size: number;
    sortDir: "desc" | "asc";
    searchWildcard: string;
}
```
返回值:
``` typescript
{
    total: number;
    pairs: Course[];
}
interface Course {
    // 见获取班课的返回值
}
```

#### 删除班课

Method: **DELETE** :key:  
URI: `/apis/course`  
参数: 
``` typescript
{
    courseExId: string;
}
```

#### 更新班课

Method: **PUT** :key:  
URI: `/apis/course`  
参数: 
``` typescript
{
    courseExId: string;
    courseName: string;
    briefDescription?: string;
}
```


### 班课学生相关

#### 邀请学生

Method: **POST** :key:  
URI: `/apis/course/student`  
前置条件: 请求的用户是该课程的老师  
参数:
```typescript
{
    courseExId: string;
    studentName: string; // 学生用户名
}
```

#### 删除学生

Method: **DELETE** :key:  
URI: `/apis/course/student`  
前置条件: 请求的用户是该课程的老师  
参数:
```typescript
{
    courseExId: string;
    studentName: string;
}
```

#### 加入班课

Method: **POST** :key:  
URI: `/apis/course/student/me`  
前置条件: 课程的老师不可以加入自己的班课  
参数:
```typescript
{
    courseExId: string;
}
```


#### 退出班课

Method: **DELETE** :key:  
URI: `/apis/course/student/me`  
参数:
```typescript
{
    courseExId: string;
}
```


### 班课任务与通知

任务与通知以同样的API创建、修改、删除, 区别在于通知不可提交

#### 创建

Method: **POST** :key:  
URI: `/apis/course/task`  
参数:
```typescript
{
    courseExId: string;
    taskTitle: string;
    deadline: string; // "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", see ISO 8601
    committable?: boolean;
    content?: string;
}
```
返回值:
```typescript
{
    taskId: number;
}
```

#### 删除

Method: **DELETE** :key:  
URI: `/apis/course/task`  
参数:
```typescript
{
    taskId: number;
}
```

#### 获取

Method: **GET** :key:  
URI: `/apis/course/task`  
参数:
```typescript
{
    taskId: number;
}
```
返回值:
```typescript
{
    taskId: number;
    taskTilte: string;
    committable: boolean;
    ddeadline: string;
    content: string;
}
```

#### 修改

Method: **PUT** :key:  
URI: `/apis/course/task`  
参数:
```typescript
{
    taskId: number;
    taskTilte?: string;
    ddeadline?: string;
    content?: string;
}
```

#### 获取任务提交列表

Method: **GET** :key:  
URI: `/apis/course/task/anwser-list`  
参数:
```typescript
{
    taskId: number;
}
```
返回值:
```typescript
interface TaskAnwser{
    taskAnwserId: number;
    commitContent: string;
    teacherDoThis: string;
    grade: number;
    studentInfo: StudentInfo;
}

interface StudentInfo {
    name: string;
    userName: string;
    studentNo: string; // 学工号
}

type ReturnType = TaskAnwser[];
```

#### 学生获取提交的任务

Method: **GET** :key:  
URI: `/apis/course/task/anwser/me`  
参数:
```typescript
{
    taskId: number;
}
```
返回值:
```typescript
type ReturnType = TaskAnwser; // 见上面一个接口中的定义
```


### 任务提交、修改相关

#### 提交任务

Method: **POST** :key:  
URI: `/apis/course/task/anwser`  
参数:
```typescript
{
    taskId: number;
    commitContent: string;
}
```
返回值:
```typescript
{
    taskAnwserId: number;
}
```

#### 删除

Method: **DELETE** :key:  
URI: `/apis/course/task/anwser`  
参数:
```typescript
{
    taskAnwserId: number;
}
```

#### 获取

Method: **GET** :key:  
URI: `/apis/course/task/anwser`  
参数:
```typescript
{
    taskAnwserId: number;
}
```
返回值:
```typescript
{
    taskAnwserId: number;
    commitContent: string;
    grade: number;
    teacherDoThis: string;
}
```

#### 修改

Method: **PUT** :key:  
URI: `/apis/course/task/anwser`  
参数:
```typescript
{
    taskAnwserId: number;
    commitContent: string;
}
```

### 班课签到创建、修改相关

#### 创建

Method: **POST** :key:  
URI: `/apis/course/check-in`  
参数:
```typescript
{
    courseExId: string;
    jsonData: string;
    deadline: string; // "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", see ISO 8601
}
```
返回值:
```typescript
{
    checkinId: number;
}
```

#### 删除

Method: **DELETE** :key:  
URI: `/apis/course/check-in`  
参数:
```typescript
{
    checkinId: number;
}
```

#### 获取

Method: **GET** :key:  
URI: `/apis/course/check-in`  
参数:
```typescript
{
    checkId: number;
}
```
返回值:
```typescript
{
    checkinId: number;
    jsonData: string;
    deadline: string;
}
```

#### 修改

Method: **PUT** :key:  
URI: `/apis/course/check-in`  
参数:
```typescript
{
    checkinId: number;
    ddeadline?: string;
    jsonData?: string;
}
```

#### 获取已签到列表

Method: **GET** :key:  
URI: `/apis/course/check-in/anwser-list`  
参数:
```typescript
{
    checkinId: number;
}
```
返回值:
```typescript
interface CheckinAnwser{
    checkinAnwserId: number;
    studentInfo: StudentInfo;
    checkinJsonData: string;
}

interface StudentInfo {
    name: string;
    userName: string;
    studentNo: string; // 学工号
}

type ReturnType = CheckinAnwser[];
```

#### 学生获取签到记录

Method: **GET** :key:  
URI: `/apis/course/check-in/anwser/me`  
参数:
```typescript
{
    checkinId: number;
}
```
返回值:
```typescript
type ReturnType = CheckinAnwser; // 见上面一个接口中的定义
```


### 学生签到相关

#### 学生签到

Method: **POST** :key:  
URI: `/apis/course/check-in/anwser`  
参数:
```typescript
{
    taskId: number;
    checkinJsonData: string;
}
```
返回值:
```typescript
{
    checkinAnwserId: number;
}
```

#### 获取

Method: **GET** :key:  
URI: `/apis/course/check-in/anwser`  
参数:
```typescript
{
    checkinAnwserId: number;
}
```
返回值:
```typescript
{
    checkinAnwserId: number;
    checkinJsonData: string;
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


### 菜单管理


菜单的有三种类型`menu`, `page`, `button`, 唯一标识符为描述符`descriptor`.
`descriptor`的格式为`[a-zA-Z][a-zA-Z0-9_]+(\.[a-zA-Z][a-zA-Z0-9_]+)`, 如`user.login`.
每一个菜单对应有一个`link`, `link`用于表示`API`以及`HTTP Method`, 格式为`(POST|DELETE|POST|GET)[:](\/[a-zA-Z][a-zA-Z0-9@_-]+)+`.
菜单对象如下
```typescript
class PermMenu {
  descriptor: string;
  link: string;
  type: enum {"menu", "page", "button"};
  permEntryName: string;

  // 非创建字段
  children: PermMenu[];
  parent: PermMenu | null;    // 根节点此字段为空
}
```


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

