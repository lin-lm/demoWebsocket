# Getting Started

### Guides
The following guides illustrates how to use certain features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

### 本项目所包含的功能
## 目录
# 1.websocket前后端消息推送
# 2.页面导出为doc、pdf、html
# 3.springboot项目整合vue前端项目
# 4.springboot项目整合quartz定时任务，动态重置定时任务配置

## 说明
# 1.websocket前后端消息推送
* pom.xml中应引入的jar包：
    spring-boot-starter-web
    spring-boot-starter-websocket
    spring-boot-starter-test
  
* 用到的类
    com.javaxapi.demo.controller.SocketController
    com.javaxapi.demo.controller.WebSocketServer
    
* 测试页面
    index.html
    
* 辅助测试网站
    http://www.bejson.com/httputil/websocket/
    
# 2.页面导出为doc、pdf、html
* 本次导出以js形式

* 测试页面
    downloadDoc.html。需引入docDownload中的js文件
    downloadHtml.html。需引入jquery
    downloadPdf.html。需引入pdfDownload中的js文件
    
# 3.springboot项目整合vue前端项目
* 参考：https://segmentfault.com/a/1190000014211773
    搭建vue项目：https://www.cnblogs.com/hackyo/p/7988399.html

* 打包vue项目，生成dist目录

* 将dist目录下的static目录和index.html文件复制到springboot项目的resources/static/目录下。本项目中index_vue.html为原来vue项目的index.html文件

# 4.springboot项目整合quartz定时任务
* pom.xml中应引入的jar包：
    spring-context-support
    spring-tx
    quartz
    
* 用到的类
    com.javaxapi.demo.config.quartz.QuartzConfig    //定时任务配置类
    com.javaxapi.demo.schedule.job.ScheduledTaskJob  //定时任务：业务逻辑
    com.javaxapi.demo.config.quartz.ScheduleRefreshService  //动态重置定时任务配置

# 5.springboot2 http转https
https://blog.csdn.net/zsj777/article/details/81431017
* 生成证书，将证书放在resources目录下
    keytool -genkey -alias tomcat -keyalg RSA -validity 20000 -keystore keystore.p12
* 在application.properties中配置ssl相关信息
* 添加配置类
    com.javaxapi.demo.config.HttpsRequestConfig
