server side subsystem:
1, RESTful web service that offers services to web browser.
2, MySQL to store data
3, Spring, JDBC, baidu open API, property asset integration...
4, layers: web services layer -> business services layer + common model-> DAO layer


dependency:
0, java8/maven
1, mysql server (c3p0 connection pool)
2, mysql JDBC driver
3, JMS (optional)
4, Spring, logback,...
5, baidu map api
6, twaver 3d api
7, jquery, requirejs, backbond
8, jetty, jersey restful web service, jackson to handle json

StartServer --> start mysql/jetty  -> init spring(core, sm and parking) -> start modules



