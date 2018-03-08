# 新增题目接口文档
## 新增操作
请求

| 名称   | 内容           | 描述   |
| :--- | :----------- | :--- |
| 操作名称 | 新增操作         |      |
| URI  | /problem/add |      |
| 请求方式 | POST         |      |
| 发送参数 | info:[array] | 数据   |

array内容
```javascript
  [
    name[string], mid[int], is_new[int], type[int], 
    source[int], nature[int], way[int](为0表示盲选，否则是学生ID),
    introduction[string], requirement[string],
  ]
```

响应

| 名称   | 内容               | 描述     |
| :--- | :--------------- | :----- |
| 响应类型 | JSON             |        |
| 返回值1 | status:[boolean] | 是否插入成功 |
| 返回值2 | info:[string]    | 错误原因   |

响应示例
```javascript
{
  'status' : true
}
{
  'status' : false,
  'info' : '学生已经被指定',
}
```

## 指定学生操作
请求

| 名称   | 内容              | 描述        |
| :--- | :-------------- | :-------- |
| 操作名称 | 查询学生信息          | 用于绑定学生和题目 |
| URI  | /student/info   |           |
| 请求方式 | POST            |           |
| 发送参数 | stu_id:[string] | 学号、账号     |

请求示例
`stu_id=12345678`

响应

| 名称   | 内容                | 描述         |
| :--- | :---------------- | :--------- |
| 响应类型 | JSON              |            |
| 返回值1 | status:[boolean]  | 查询是否合法     |
| 返回值2 | id:[int]          | 用户的ID，不是学号 |
| 返回值3 | stu_name:[string] | 学生姓名       |

响应示例
```javascript
{
  'status' : true,
  'id' : 1234,
  'stu_name' : '小明',
}
```
