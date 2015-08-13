mysqladmin -P 7788 -u root proc status


http://dev.mysql.com/doc/refman/5.6/en/mysqladmin.html



mysql -P 7788 -u root
mysql>show databases;
mysql>use ihn;
mysql>show tables;
mysql>select * from user;
mysql>desc user;
mysql>insert user(username, password,role) values('su','su','admin');


SingleColumnRowMapper
ColumnMapRowMapper
BeanPropertyRowMapper

