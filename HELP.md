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