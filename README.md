# GraduationDesign
# 接口文档
## 登录接口文档
### 请求

|  名称 |  内容 | 描述 |
| :------------ | :------------ | :------------ |
|  操作名称 |  登录 | |
|  URI  |  /login | |
|  请求方式 | POST | |
|  发送参数1 |  username : [string]| 用户名 |
|  发送参数2 |  password : [string]| 密码 |
|  发送参数3 |  act : [int]|  登录角色 |

act说明

|  值 |  含义 |
| :------------ | :------------ |
|  1 |  学生  |
|  2 |  老师和专业负责人 |
|  3 |  管理员 |

请求示例：
```javascript
username=xxxxx&password=*******&act=0
```
### 响应

|  名称 |  内容 | 描述 |
| :------------ | :------------ | :------------ |
|  数据类型 | JSON | |
|  返回值1 |  status : [boolean]| 状态 true表示成功 |
|  返回值2 |  info : [string]| 错误原因 |

响应示例：
```javascript
{
	"status" : true
}
```
```javascript
{
	"status" : false,
	"info" : "密码错误"
}
```
