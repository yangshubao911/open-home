drop table IF EXISTS `tasks`;
create table if not exists `tasks`(
  id int(11)  AUTO_INCREMENT primary key,
  uid int(11) not null,
  title varchar(30) not null,
  content varchar(255) not null,
  create_time datetime not null
)engine InnoDB CHARACTER SET utf8;