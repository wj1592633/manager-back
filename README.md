# manager-back是一个简单的后台管理系统
基于springboot和vue构建的前后端分离工程。前端地址：https://github.com/wj1592633/manager-portal
后端使用了springboot、mybatis-plus、mysql、spring-security-oauth2、ehcache、swagger-ui等
* 系统模块：
  - 用户模块：SysUser ->实现了用户的增删查改
  - 角色模块：SysRole ->实现了角色的查询、创建、分配
  - 权限模块：SysMenu ->实现了权限的查询、分配
  - 日志模块：SysOperationLog ->使用了aop进行日志异步记录
