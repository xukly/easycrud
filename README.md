# easycrud

spring-boot-starter项目，简单高效完成后台管理crud的工作(适用于SSM项目，mybatis集成mybatis-plus)。

#### 基础功能
---
1、反射实现基本crud：通过固定的entity、mapper命名,只需获得实体类名，就能通过spring获得对应的mapper实例，完成mybatis-plus内置的selectById、insert、updateById、deleteById等基础操作；
2、观察者模式实现crud的前后置操作：event listener事件监听实现crud操作的前后置逻辑（并未采取aop）；
3、基础的权限管理：spring security+jwt 的token权限验证形式；
4、注解实现用户操作日志管理：@EnableWriteLog注解切面实现日志记录管理；
