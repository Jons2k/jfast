# Host: localhost  (Version: 5.5.29)
# Date: 2020-06-27 17:34:45
# Generator: MySQL-Front 5.3  (Build 4.234)

/*!40101 SET NAMES utf8 */;

#
# Structure for table "dev_chart"
#

DROP TABLE IF EXISTS `dev_chart`;
CREATE TABLE `dev_chart` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `cate_id` int(11) NOT NULL COMMENT '分类',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `title` varchar(100) NOT NULL COMMENT '名称',
  `ds_id` int(11) NOT NULL COMMENT '数据源',
  `type` varchar(20) NOT NULL COMMENT '类型',
  `comment` varchar(200) DEFAULT NULL COMMENT '说明',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Data for table "dev_chart"
#

/*!40000 ALTER TABLE `dev_chart` DISABLE KEYS */;
INSERT INTO `dev_chart` VALUES (1,1,'student_depart','按部门统计学生',3,'pie','按部门统计学生',1,'1'),(2,1,'student_depart2','学生部门排名',3,'bar','',2,'1'),(3,1,'student_depart3','学生部门拆线',3,'line','学生部门拆线',4,'1'),(4,1,'student_depart4','学生表格',3,'table','学生表格',5,'1');
/*!40000 ALTER TABLE `dev_chart` ENABLE KEYS */;

#
# Structure for table "dev_dataset"
#

DROP TABLE IF EXISTS `dev_dataset`;
CREATE TABLE `dev_dataset` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `cate_id` int(11) NOT NULL COMMENT '分类',
  `code` varchar(50) NOT NULL COMMENT '部门代码',
  `title` varchar(100) NOT NULL COMMENT '部门名称',
  `sqls` text NOT NULL COMMENT '查询SQL',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  `comment` varchar(200) DEFAULT NULL COMMENT '说明',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "dev_dataset"
#

/*!40000 ALTER TABLE `dev_dataset` DISABLE KEYS */;
INSERT INTO `dev_dataset` VALUES (2,1,'student_in_school','在校生','select u.nickname,d.title from sys_user u\nleft join sys_department d on u.depart_id=d.id\n#for(x : cond) \n  #(for.first ? \"WHERE\": \"AND\") #(x.key) #para(x.value) \n #end',1,'select','1'),(3,1,'student_depart','按部门统计学生','select count(u.id) value,d.title title \nfrom sys_user u\nleft join sys_department d on u.depart_id=d.id\ngroup by depart_id\n#for(x : cond) \n  #(for.first ? \"WHERE\": \"AND\") #(x.key) #para(x.value) \n #end',3,'按部门统计学生','1');
/*!40000 ALTER TABLE `dev_dataset` ENABLE KEYS */;

#
# Structure for table "dev_field"
#

DROP TABLE IF EXISTS `dev_field`;
CREATE TABLE `dev_field` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `model_id` int(11) NOT NULL COMMENT '所属模型',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `db_type` varchar(10) NOT NULL COMMENT '数据库类型',
  `type` varchar(10) NOT NULL COMMENT '业务类型',
  `len` int(6) NOT NULL COMMENT '长度',
  `scale` int(2) DEFAULT '0' COMMENT '缩放',
  `nullable` char(1) NOT NULL COMMENT '空值',
  `defaults` varchar(100) DEFAULT NULL COMMENT '默认值',
  `calculate` varchar(200) DEFAULT NULL COMMENT '自动计算公式',
  `param` varchar(200) DEFAULT NULL COMMENT '参数',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=180 DEFAULT CHARSET=utf8 COMMENT='字段';

#
# Data for table "dev_field"
#

/*!40000 ALTER TABLE `dev_field` DISABLE KEYS */;
INSERT INTO `dev_field` VALUES (1,2,'module_id','所属模块','number','number',11,0,'0','',NULL,'',1),(2,2,'tables','数据表','varchar','string',100,0,'0','',NULL,'',2),(3,3,'model_id','所属模型','number','number',11,0,'0','',NULL,'',1),(4,3,'code','代码','varchar','string',20,0,'0','',NULL,'',2),(5,3,'title','名称','varchar','string',50,0,'0','',NULL,'',3),(6,3,'db_type','数据库类型','varchar','string',10,0,'0','varchar',NULL,'db_type',4),(7,3,'type','业务类型','varchar','string',10,0,'0','string',NULL,'field_type',5),(8,3,'length','长度','number','number',6,0,'0','10',NULL,'',6),(9,3,'scale','缩放','number','number',2,0,'1','0',NULL,'',7),(10,3,'nullable','空值','varchar','string',1,0,'0','0',NULL,'',8),(11,3,'defaults','默认值','varchar','string',100,0,'1','',NULL,'',9),(12,3,'calculate','自动计算公式','varchar','string',200,0,'1','',NULL,'',10),(13,3,'param','参数','varchar','string',200,0,'1','',NULL,'',11),(14,3,'list_sort','排序','number','number',11,0,'0','9',NULL,'',12),(15,4,'title','规则名称','varchar','string',50,0,'0','','','',1),(16,4,'model_id','所属模型','varchar','relation',11,0,'0','','','dev_models',2),(17,4,'field_id','字段','varchar','relation',11,0,'0','','','dev_field',3),(18,4,'type','验证类型','varchar','dict',10,0,'0','','','validation_type',4),(19,4,'rule','验证规则','varchar','string',200,0,'1',NULL,NULL,NULL,5),(20,1,'code','代码','varchar','string',20,0,'0','',NULL,'',1),(21,1,'title','名称','varchar','string',50,0,'0','',NULL,'',2),(22,1,'status','状态','varchar','dict',1,0,'1','1',NULL,'status',3),(23,2,'type','类型','varchar','string',1,0,'0','c',NULL,'model_type',3),(24,2,'code','代码','varchar','string',20,0,'0','',NULL,'',4),(25,2,'title','名称','varchar','string',50,0,'0','',NULL,'',5),(26,2,'status','状态','varchar','dict',1,0,'1','1',NULL,'status',6),(27,5,'title','标题','varchar','string',100,0,'0','',NULL,'',1),(28,5,'content','内容','text','text',200,0,'0','',NULL,'',2),(29,5,'add_time','发布时间','datetime','datetime',20,0,'0','',NULL,'',3),(30,5,'add_user_id','发布人','number','number',11,0,'0','',NULL,'',4),(31,5,'read_count','阅读量','number','number',11,0,'0','0',NULL,'',5),(59,10,'title','应用名称','varchar','string',50,0,'0','',NULL,'',1),(60,10,'url','应用地址','varchar','string',20,0,'1','',NULL,'',2),(61,10,'type','应用类型','varchar','string',1,0,'0','i',NULL,'app_type',3),(62,10,'add_time','创建时间','datetime','datetime',20,0,'1','',NULL,'',4),(63,10,'comment','应用说明','text','text',11,0,'1','',NULL,'',5),(64,10,'list_sort','排序','number','number',11,0,'0','9',NULL,'',6),(65,10,'icon','图标','varchar','string',20,0,'1','',NULL,'',7),(66,10,'status','状态','varchar','dict',1,0,'1','1',NULL,'status',8),(67,11,'pid','上级','number','number',11,0,'0','0',NULL,'',1),(68,11,'type','类型','varchar','string',1,0,'0','',NULL,'dict_type',2),(69,11,'code','代码','varchar','string',20,0,'0','',NULL,'',3),(70,11,'title','名称','varchar','string',50,0,'0','',NULL,'',4),(71,11,'list_sort','排序','number','number',11,0,'0','9',NULL,'',5),(72,12,'pid','上级','number','number',11,0,'0','0',NULL,'',1),(73,12,'type','类型','varchar','string',1,0,'0','',NULL,'config_type',2),(74,12,'code','代码','varchar','string',20,0,'0','',NULL,'',3),(75,12,'title','名称','varchar','string',50,0,'0','',NULL,'',4),(76,12,'defaults','默认值','varchar','string',200,0,'0','',NULL,'',5),(77,12,'vals','设置值','varchar','string',200,0,'1','',NULL,'',6),(78,12,'list_sort','排序','number','number',11,0,'0','9',NULL,'',7),(79,13,'app_id','所属应用','number','number',11,0,'0','0',NULL,'',1),(80,13,'pid','上级菜单','number','number',11,0,'0','0',NULL,'',2),(81,13,'type','菜单类型','varchar','string',1,0,'0','',NULL,'menu_type',3),(82,13,'url','操作地址','varchar','string',200,0,'0','',NULL,'',4),(83,13,'title','菜单名称','varchar','string',50,0,'0','',NULL,'',5),(84,13,'list_sort','排序','number','number',11,0,'0','9',NULL,'',6),(85,13,'icon','图标','varchar','string',20,0,'1','',NULL,'',7),(86,13,'status','状态','varchar','dict',1,0,'1','1',NULL,'status',8),(87,14,'account','账号','varchar','string',50,0,'0','0',NULL,NULL,1),(88,14,'password','密码','varchar','string',32,0,'1',NULL,NULL,NULL,2),(89,14,'email','邮箱','varchar','string',50,0,'0',NULL,NULL,NULL,3),(90,14,'phone','手机号','varchar','string',20,0,'0',NULL,NULL,NULL,4),(91,14,'nickname','姓名','varchar','string',100,0,'0',NULL,NULL,NULL,5),(92,14,'create_time','创建时间','datetime','datetime',20,0,'1','',NULL,'',6),(93,14,'depart_id','所在部门','varchar','relation',11,0,'1',NULL,NULL,'sys_department',7),(94,14,'salt','盐值','varchar','string',6,0,'1',NULL,NULL,NULL,8),(95,14,'status','状态','varchar','dict',1,0,'0','1',NULL,'status',9),(96,15,'code','代码','varchar','string',20,0,'0','',NULL,'',1),(97,15,'title','名称','varchar','string',50,0,'0','',NULL,'',2),(98,15,'is_default','是否默认','varchar','dict',1,0,'1','0',NULL,'boolean',3),(99,15,'status','状态','varchar','dict',1,0,'1','1',NULL,'status',4),(104,18,'account','账号','varchar','string',50,0,'0','',NULL,'',1),(105,18,'user_id','用户','number','number',11,0,'1','0',NULL,'',2),(106,18,'ip','IP地址','varchar','string',50,0,'1','',NULL,'',3),(107,18,'session_id','Session','varchar','string',50,0,'1','',NULL,'',4),(108,18,'login_time','登录时间','datetime','datetime',20,0,'1','',NULL,'',5),(109,18,'logout_time','退出时间','datetime','datetime',20,0,'1','',NULL,'',6),(110,19,'code','代码','varchar','string',50,0,'0','',NULL,'',1),(111,19,'user_id','用户','number','number',11,0,'1','0',NULL,'',2),(112,19,'client_id','应用','number','number',11,0,'1','0',NULL,NULL,3),(113,19,'ip','IP地址','varchar','string',50,0,'1','',NULL,'',4),(114,19,'create_time','创建时间','datetime','datetime',20,0,'1','',NULL,'',5),(115,19,'expire_time','过期时间','datetime','datetime',20,0,'1','',NULL,'',6),(116,20,'pid','上级','number','number',11,0,'0','0',NULL,'',1),(117,20,'code','代码','varchar','string',20,0,'0','',NULL,'',3),(118,20,'title','名称','varchar','string',50,0,'0','',NULL,'',4),(119,20,'list_sort','排序','number','number',11,0,'0','9',NULL,'',5),(120,20,'status','状态','varchar','dict',1,0,'0','1',NULL,'status',6),(121,21,'name','文件名','varchar','string',100,0,'0','',NULL,'',1),(122,21,'exts','扩展名','varchar','string',10,0,'0','',NULL,'',2),(123,21,'md5','唯一标识','varchar','string',50,0,'0','',NULL,'',3),(124,21,'path','保存路径','varchar','string',200,0,'0','',NULL,'',4),(125,21,'upload_user_id','上传用户','number','number',11,0,'0','',NULL,'',5),(126,21,'upload_time','上传时间','datetime','datetime',20,0,'1','',NULL,'',6),(127,21,'size','文件大小','number','number',11,0,'1','',NULL,'',7),(128,21,'cover','封面','varchar','string',200,0,'1','',NULL,'',8),(129,21,'length','长度','number','number',11,0,'1','',NULL,'',9),(130,21,'pre_file','预览文件','varchar','file',200,0,'1','',NULL,'',10),(131,21,'status','状态','varchar','dict',1,0,'0','1',NULL,'status',11),(132,22,'user_id','操作用户','number','number',11,0,'0','0',NULL,'',1),(133,22,'action_id','操作动作','number','number',11,0,'0','0',NULL,'',2),(134,22,'action_time','上传时间','datetime','datetime',20,0,'0','now',NULL,NULL,3),(135,22,'param','参数','varchar','string',200,0,'0',NULL,NULL,NULL,4),(136,22,'ip','操作IP','varchar','string',50,0,'0','9',NULL,NULL,5),(137,23,'url','操作地址','varchar','string',50,0,'0',NULL,NULL,NULL,1),(138,23,'title','名称','varchar','string',50,0,'0',NULL,NULL,NULL,2),(139,23,'module_id','所属模块','varchar','relation',11,0,'0',NULL,NULL,'dev_module',3),(140,23,'model_id','所属模型','varchar','relation',11,0,'0',NULL,NULL,'dev_models',4),(141,24,'pid','上级部门','number','relation',11,0,'0','0',NULL,'sys_department',1),(142,24,'type','部门类型','varchar','dict',1,0,'0',NULL,NULL,'depart_type',2),(143,24,'code','部门代码','varchar','string',50,0,'0',NULL,NULL,NULL,3),(144,24,'title','部门名称','varchar','string',100,0,'0',NULL,NULL,NULL,4),(145,24,'list_sort','排序','number','number',11,0,'0','9',NULL,NULL,5),(146,24,'status','状态','varchar','dict',1,0,'1','1',NULL,'status',6),(147,25,'cate_id','分类','varchar','number',11,0,'1','0','','',1),(148,25,'code','数据集代码','varchar','string',50,0,'0','','','',2),(149,25,'title','数据集名称','varchar','string',100,0,'0','','','',3),(150,25,'sqls','查询SQL','text','text',500,0,'0','',NULL,'',4),(151,25,'list_sort','排序','varchar','number',11,0,'1','9','','',5),(152,25,'comment','说明','varchar','text',200,0,'1','','','',6),(153,25,'status','状态','varchar','dict',1,0,'1','1','','status',7),(154,26,'cate_id','分类','varchar','relation',11,0,'0','0','','sys_category',1),(155,26,'code','代码','varchar','string',50,0,'0','','','',2),(156,26,'title','名称','varchar','string',100,0,'0','','','',3),(157,26,'ds_id','数据源','varchar','relation',11,0,'0','','','dev_dataset',4),(158,26,'type','类型','varchar','dict',20,0,'0','bar','','chart_type',5),(160,26,'comment','说明','varchar','text',200,0,'1','','','',7),(161,26,'list_sort','排序','varchar','number',11,0,'0','9','','',8),(162,26,'status','状态','varchar','dict',1,0,'0','1','','status',9);
/*!40000 ALTER TABLE `dev_field` ENABLE KEYS */;

#
# Structure for table "dev_models"
#

DROP TABLE IF EXISTS `dev_models`;
CREATE TABLE `dev_models` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `module_id` int(11) NOT NULL COMMENT '所属模块',
  `tables` varchar(100) NOT NULL COMMENT '数据表',
  `type` char(1) NOT NULL COMMENT '类型',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `pk_field` varchar(100) DEFAULT NULL COMMENT '主键字段',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COMMENT='模型';

#
# Data for table "dev_models"
#

/*!40000 ALTER TABLE `dev_models` DISABLE KEYS */;
INSERT INTO `dev_models` VALUES (1,1,'dev_module','c','module','模块',NULL,'1'),(2,1,'dev_models','c','models','模型',NULL,'1'),(3,1,'dev_field','c','field','字段',NULL,'1'),(4,1,'dev_validation','c','validation','验证',NULL,'1'),(5,2,'sys_notice','c','notice','公告',NULL,'1'),(10,2,'sys_application','c','application','应用',NULL,'1'),(11,2,'sys_dict','t','dict','字典',NULL,'1'),(12,2,'sys_config','t','config','配置',NULL,'1'),(13,2,'sys_menu','t','menu','菜单',NULL,'1'),(14,2,'sys_user','c','user','用户',NULL,'1'),(15,2,'sys_role','c','role','角色',NULL,'1'),(18,2,'sys_user_login','c','userLogin','登录日志',NULL,'1'),(19,2,'sys_token','c','token','令牌',NULL,'1'),(20,2,'sys_category','t','category','分类',NULL,'1'),(21,2,'sys_file','c','file','文件',NULL,'1'),(22,2,'sys_log','c','log','操作日志',NULL,'1'),(23,2,'sys_action','c','action','操作行为',NULL,'1'),(24,2,'sys_department','t','department','部门',NULL,'1'),(25,1,'dev_dataset','c','dataset','数据集',NULL,'1'),(26,1,'dev_chart','c','chart','报表',NULL,'1');
/*!40000 ALTER TABLE `dev_models` ENABLE KEYS */;

#
# Structure for table "dev_module"
#

DROP TABLE IF EXISTS `dev_module`;
CREATE TABLE `dev_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "dev_module"
#

/*!40000 ALTER TABLE `dev_module` DISABLE KEYS */;
INSERT INTO `dev_module` VALUES (1,'dev','开发','1'),(2,'sys','核心','1');
/*!40000 ALTER TABLE `dev_module` ENABLE KEYS */;

#
# Structure for table "dev_validation"
#

DROP TABLE IF EXISTS `dev_validation`;
CREATE TABLE `dev_validation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `title` varchar(50) NOT NULL COMMENT '规则名称',
  `model_id` int(11) NOT NULL COMMENT '所属模型',
  `field_id` int(11) NOT NULL COMMENT '字段',
  `type` varchar(10) NOT NULL COMMENT '验证类型',
  `rule` varchar(200) DEFAULT NULL COMMENT '验证规则',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111 DEFAULT CHARSET=utf8;

#
# Data for table "dev_validation"
#

/*!40000 ALTER TABLE `dev_validation` DISABLE KEYS */;
INSERT INTO `dev_validation` VALUES (1,'代码长度不能大于20',3,4,'length','20'),(2,'名称长度不能大于50',3,5,'length','50'),(3,'数据库类型长度不能大于10',3,6,'length','10'),(4,'业务类型长度不能大于10',3,7,'length','10'),(5,'空值长度不能大于1',3,10,'length','1'),(6,'默认值长度不能大于100',3,11,'length','100'),(7,'自动计算公式长度不能大于200',3,12,'length','200'),(8,'参数长度不能大于200',3,13,'length','200'),(9,'规则名称长度不能大于50',4,15,'length','50'),(10,'验证类型长度不能大于10',4,18,'length','10'),(11,'验证规则长度不能大于200',4,19,'length','200'),(12,'代码长度不能大于20',1,20,'length','20'),(13,'名称长度不能大于50',1,21,'length','50'),(14,'数据表长度不能大于100',2,2,'length','100'),(15,'类型长度不能大于1',2,23,'length','1'),(16,'代码长度不能大于20',2,24,'length','20'),(17,'名称长度不能大于50',2,25,'length','50'),(18,'标题长度不能大于100',5,27,'length','100'),(19,'发布时间必须是日期时间格式:2019-03-06 12:24:00',5,29,'regex','datetime'),(20,'阅读量必须是数字格式',5,31,'regex','number'),(41,'应用名称长度不能大于50',10,59,'length','50'),(43,'应用地址长度不能大于20',10,60,'length','20'),(44,'应用类型长度不能大于1',10,61,'length','1'),(45,'创建时间必须是日期时间格式:2019-03-06 12:24:00',10,62,'regex','datetime'),(46,'图标长度不能大于20',10,65,'length','20'),(47,'类型长度不能大于1',11,68,'length','1'),(48,'代码长度不能大于20',11,69,'length','20'),(49,'名称长度不能大于50',11,70,'length','50'),(50,'类型长度不能大于1',12,73,'length','1'),(51,'代码长度不能大于20',12,74,'length','20'),(52,'名称长度不能大于50',12,75,'length','50'),(53,'默认值长度不能大于200',12,76,'length','200'),(54,'设置值长度不能大于200',12,77,'length','200'),(55,'菜单类型长度不能大于1',13,81,'length','1'),(57,'操作地址长度不能大于200',13,82,'length','200'),(58,'菜单名称长度不能大于50',13,83,'length','50'),(59,'图标长度不能大于20',13,85,'length','20'),(60,'账号长度不能大于50',14,87,'length','50'),(61,'密码长度不能大于32',14,88,'length','32'),(62,'邮箱必须是邮箱格式:XXX@XX.com',14,89,'regex','email'),(63,'手机号必须是手机号码格式:1xxxxxxxxxx',14,90,'regex','phone'),(64,'手机号长度不能大于20',14,90,'length','20'),(65,'姓名长度不能大于100',14,91,'length','100'),(66,'创建时间必须是日期时间格式:2019-03-06 12:24:00',14,92,'regex','datetime'),(67,'盐值长度不能大于6',14,94,'length','6'),(68,'代码长度不能大于20',15,96,'length','20'),(69,'名称长度不能大于50',15,97,'length','50'),(70,'账号长度不能大于50',18,104,'length','50'),(71,'IP地址长度不能大于50',18,106,'length','50'),(72,'Session长度不能大于50',18,107,'length','50'),(73,'登录时间必须是日期时间格式:2019-03-06 12:24:00',18,108,'regex','datetime'),(74,'退出时间必须是日期时间格式:2019-03-06 12:24:00',18,109,'regex','datetime'),(75,'代码长度不能大于50',19,110,'length','50'),(76,'IP地址长度不能大于50',19,113,'length','50'),(77,'创建时间必须是日期时间格式:2019-03-06 12:24:00',19,114,'regex','datetime'),(78,'过期时间必须是日期时间格式:2019-03-06 12:24:00',19,115,'regex','datetime'),(79,'代码长度不能大于20',20,117,'length','20'),(80,'名称长度不能大于50',20,118,'length','50'),(81,'文件名长度不能大于100',21,121,'length','100'),(82,'扩展名长度不能大于10',21,122,'length','10'),(83,'唯一标识长度不能大于50',21,123,'length','50'),(84,'保存路径长度不能大于200',21,124,'length','200'),(85,'上传时间必须是日期时间格式:2019-03-06 12:24:00',21,126,'regex','datetime'),(86,'封面长度不能大于200',21,128,'length','200'),(87,'上传时间必须是日期时间格式:2019-03-06 12:24:00',22,134,'regex','datetime'),(88,'参数长度不能大于200',22,135,'length','200'),(89,'操作IP长度不能大于50',22,136,'length','50'),(91,'操作地址长度不能大于50',23,137,'length','50'),(92,'名称长度不能大于50',23,138,'length','50'),(93,'部门类型长度不能大于1',24,142,'length','1'),(94,'部门代码长度不能大于50',24,143,'length','50'),(95,'部门名称长度不能大于100',24,144,'length','100'),(96,'部门代码长度不能大于50',25,148,'length','50'),(97,'部门名称长度不能大于100',25,149,'length','100'),(98,'说明长度不能大于200',25,152,'length','200'),(99,'代码长度不能大于50',26,155,'length','50'),(100,'名称长度不能大于100',26,156,'length','100'),(101,'类型长度不能大于20',26,158,'length','20'),(102,'说明长度不能大于200',26,160,'length','200'),(107,'报表代码必须唯一',26,155,'unique','0');
/*!40000 ALTER TABLE `dev_validation` ENABLE KEYS */;

#
# Structure for table "sys_action"
#

DROP TABLE IF EXISTS `sys_action`;
CREATE TABLE `sys_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `url` varchar(50) NOT NULL COMMENT '操作地址',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `module_id` int(11) NOT NULL COMMENT '所属模块',
  `model_id` int(11) NOT NULL COMMENT '所属模型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=194 DEFAULT CHARSET=utf8;

#
# Data for table "sys_action"
#

/*!40000 ALTER TABLE `sys_action` DISABLE KEYS */;
INSERT INTO `sys_action` VALUES (2,'/dev/chart/lists','查分页',1,26),(3,'/dev/chart/slist','查全部',1,26),(4,'/dev/chart/save','新增',1,26),(5,'/dev/chart/update','修改',1,26),(6,'/dev/chart/delete','删除',1,26),(7,'/dev/chart/info','查看',1,26),(8,'/dev/chart/imports','导入',1,26),(9,'/dev/chart/exports','导出',1,26),(10,'/dev/dataset/lists','查分页',1,25),(11,'/dev/dataset/slist','查全部',1,25),(12,'/dev/dataset/save','新增',1,25),(13,'/dev/dataset/update','修改',1,25),(14,'/dev/dataset/delete','删除',1,25),(15,'/dev/dataset/info','查看',1,25),(16,'/dev/dataset/imports','导入',1,25),(17,'/dev/dataset/exports','导出',1,25),(18,'/dev/module/lists','查分页',1,1),(19,'/dev/module/slist','查全部',1,1),(20,'/dev/module/save','新增',1,1),(21,'/dev/module/update','修改',1,1),(22,'/dev/module/delete','删除',1,1),(23,'/dev/module/info','查看',1,1),(24,'/dev/module/imports','导入',1,1),(25,'/dev/module/exports','导出',1,1),(26,'/dev/models/lists','查分页',1,2),(27,'/dev/models/slist','查全部',1,2),(28,'/dev/models/save','新增',1,2),(29,'/dev/models/update','修改',1,2),(30,'/dev/models/delete','删除',1,2),(31,'/dev/models/info','查看',1,2),(32,'/dev/models/imports','导入',1,2),(33,'/dev/models/exports','导出',1,2),(34,'/dev/field/lists','查分页',1,3),(35,'/dev/field/slist','查全部',1,3),(36,'/dev/field/save','新增',1,3),(37,'/dev/field/update','修改',1,3),(38,'/dev/field/delete','删除',1,3),(39,'/dev/field/info','查看',1,3),(40,'/dev/field/imports','导入',1,3),(41,'/dev/field/exports','导出',1,3),(42,'/dev/validation/lists','查分页',1,4),(43,'/dev/validation/slist','查全部',1,4),(44,'/dev/validation/save','新增',1,4),(45,'/dev/validation/update','修改',1,4),(46,'/dev/validation/delete','删除',1,4),(47,'/dev/validation/info','查看',1,4),(48,'/dev/validation/imports','导入',1,4),(49,'/dev/validation/exports','导出',1,4),(50,'/sys/notice/lists','查分页',2,5),(51,'/sys/notice/slist','查全部',2,5),(52,'/sys/notice/save','新增',2,5),(53,'/sys/notice/update','修改',2,5),(54,'/sys/notice/delete','删除',2,5),(55,'/sys/notice/info','查看',2,5),(56,'/sys/notice/imports','导入',2,5),(57,'/sys/notice/exports','导出',2,5),(58,'/dev/client/lists','查分页',1,7),(59,'/dev/client/slist','查全部',1,7),(60,'/dev/client/save','新增',1,7),(61,'/dev/client/update','修改',1,7),(62,'/dev/client/delete','删除',1,7),(63,'/dev/client/info','查看',1,7),(64,'/dev/client/imports','导入',1,7),(65,'/dev/client/exports','导出',1,7),(66,'/dev/pipe/lists','查分页',1,8),(67,'/dev/pipe/slist','查全部',1,8),(68,'/dev/pipe/save','新增',1,8),(69,'/dev/pipe/update','修改',1,8),(70,'/dev/pipe/delete','删除',1,8),(71,'/dev/pipe/info','查看',1,8),(72,'/dev/pipe/imports','导入',1,8),(73,'/dev/pipe/exports','导出',1,8),(74,'/dev/logs/lists','查分页',1,9),(75,'/dev/logs/slist','查全部',1,9),(76,'/dev/logs/save','新增',1,9),(77,'/dev/logs/update','修改',1,9),(78,'/dev/logs/delete','删除',1,9),(79,'/dev/logs/info','查看',1,9),(80,'/dev/logs/imports','导入',1,9),(81,'/dev/logs/exports','导出',1,9),(82,'/sys/application/lists','查分页',2,10),(83,'/sys/application/slist','查全部',2,10),(84,'/sys/application/save','新增',2,10),(85,'/sys/application/update','修改',2,10),(86,'/sys/application/delete','删除',2,10),(87,'/sys/application/info','查看',2,10),(88,'/sys/application/imports','导入',2,10),(89,'/sys/application/exports','导出',2,10),(90,'/sys/dict/lists','查分页',2,11),(91,'/sys/dict/slist','查全部',2,11),(92,'/sys/dict/save','新增',2,11),(93,'/sys/dict/update','修改',2,11),(94,'/sys/dict/delete','删除',2,11),(95,'/sys/dict/info','查看',2,11),(96,'/sys/dict/imports','导入',2,11),(97,'/sys/dict/exports','导出',2,11),(98,'/sys/config/lists','查分页',2,12),(99,'/sys/config/slist','查全部',2,12),(100,'/sys/config/save','新增',2,12),(101,'/sys/config/update','修改',2,12),(102,'/sys/config/delete','删除',2,12),(103,'/sys/config/info','查看',2,12),(104,'/sys/config/imports','导入',2,12),(105,'/sys/config/exports','导出',2,12),(106,'/sys/menu/lists','查分页',2,13),(107,'/sys/menu/slist','查全部',2,13),(108,'/sys/menu/save','新增',2,13),(109,'/sys/menu/update','修改',2,13),(110,'/sys/menu/delete','删除',2,13),(111,'/sys/menu/info','查看',2,13),(112,'/sys/menu/imports','导入',2,13),(113,'/sys/menu/exports','导出',2,13),(114,'/sys/user/lists','查分页',2,14),(115,'/sys/user/slist','查全部',2,14),(116,'/sys/user/save','新增',2,14),(117,'/sys/user/update','修改',2,14),(118,'/sys/user/delete','删除',2,14),(119,'/sys/user/info','查看',2,14),(120,'/sys/user/imports','导入',2,14),(121,'/sys/user/exports','导出',2,14),(122,'/sys/role/lists','查分页',2,15),(123,'/sys/role/slist','查全部',2,15),(124,'/sys/role/save','新增',2,15),(125,'/sys/role/update','修改',2,15),(126,'/sys/role/delete','删除',2,15),(127,'/sys/role/info','查看',2,15),(128,'/sys/role/imports','导入',2,15),(129,'/sys/role/exports','导出',2,15),(130,'/sys/userlogin/lists','查分页',2,18),(131,'/sys/userlogin/slist','查全部',2,18),(132,'/sys/userlogin/save','新增',2,18),(133,'/sys/userlogin/update','修改',2,18),(134,'/sys/userlogin/delete','删除',2,18),(135,'/sys/userlogin/info','查看',2,18),(136,'/sys/userlogin/imports','导入',2,18),(137,'/sys/userlogin/exports','导出',2,18),(138,'/sys/token/lists','查分页',2,19),(139,'/sys/token/slist','查全部',2,19),(140,'/sys/token/save','新增',2,19),(141,'/sys/token/update','修改',2,19),(142,'/sys/token/delete','删除',2,19),(143,'/sys/token/info','查看',2,19),(144,'/sys/token/imports','导入',2,19),(145,'/sys/token/exports','导出',2,19),(146,'/sys/category/lists','查分页',2,20),(147,'/sys/category/slist','查全部',2,20),(148,'/sys/category/save','新增',2,20),(149,'/sys/category/update','修改',2,20),(150,'/sys/category/delete','删除',2,20),(151,'/sys/category/info','查看',2,20),(152,'/sys/category/imports','导入',2,20),(153,'/sys/category/exports','导出',2,20),(154,'/sys/file/lists','查分页',2,21),(155,'/sys/file/slist','查全部',2,21),(156,'/sys/file/save','新增',2,21),(157,'/sys/file/update','修改',2,21),(158,'/sys/file/delete','删除',2,21),(159,'/sys/file/info','查看',2,21),(160,'/sys/file/imports','导入',2,21),(161,'/sys/file/exports','导出',2,21),(162,'/sys/log/lists','查分页',2,22),(163,'/sys/log/slist','查全部',2,22),(164,'/sys/log/save','新增',2,22),(165,'/sys/log/update','修改',2,22),(166,'/sys/log/delete','删除',2,22),(167,'/sys/log/info','查看',2,22),(168,'/sys/log/imports','导入',2,22),(169,'/sys/log/exports','导出',2,22),(170,'/sys/action/lists','查分页',2,23),(171,'/sys/action/slist','查全部',2,23),(172,'/sys/action/save','新增',2,23),(173,'/sys/action/update','修改',2,23),(174,'/sys/action/delete','删除',2,23),(175,'/sys/action/info','查看',2,23),(176,'/sys/action/imports','导入',2,23),(177,'/sys/action/exports','导出',2,23),(178,'/sys/department/lists','查分页',2,24),(179,'/sys/department/slist','查全部',2,24),(180,'/sys/department/save','新增',2,24),(181,'/sys/department/update','修改',2,24),(182,'/sys/department/delete','删除',2,24),(183,'/sys/department/info','查看',2,24),(184,'/sys/department/imports','导入',2,24),(185,'/sys/department/exports','导出',2,24),(186,'/xdata/student/lists','查分页',3,27),(187,'/xdata/student/slist','查全部',3,27),(188,'/xdata/student/save','新增',3,27),(189,'/xdata/student/update','修改',3,27),(190,'/xdata/student/delete','删除',3,27),(191,'/xdata/student/info','查看',3,27),(192,'/xdata/student/imports','导入',3,27),(193,'/xdata/student/exports','导出',3,27);
/*!40000 ALTER TABLE `sys_action` ENABLE KEYS */;

#
# Structure for table "sys_application"
#

DROP TABLE IF EXISTS `sys_application`;
CREATE TABLE `sys_application` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `title` varchar(50) NOT NULL COMMENT '应用名称',
  `url` varchar(20) DEFAULT NULL COMMENT '应用地址',
  `type` char(1) NOT NULL COMMENT '应用类型',
  `add_time` datetime DEFAULT NULL COMMENT '创建时间',
  `comment` text COMMENT '应用说明',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "sys_application"
#

/*!40000 ALTER TABLE `sys_application` DISABLE KEYS */;
INSERT INTO `sys_application` VALUES (1,'开发','dev','i','2020-03-11 15:32:42','',8,'','1'),(2,'核心','sys','i','2020-03-11 15:32:42','',9,'','1');
/*!40000 ALTER TABLE `sys_application` ENABLE KEYS */;

#
# Structure for table "sys_category"
#

DROP TABLE IF EXISTS `sys_category`;
CREATE TABLE `sys_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `pid` int(11) NOT NULL COMMENT '上级',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  `status` char(1) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Data for table "sys_category"
#

/*!40000 ALTER TABLE `sys_category` DISABLE KEYS */;
INSERT INTO `sys_category` VALUES (1,0,'student','学生',1,'1'),(2,0,'teacher','教师',9,'1');
/*!40000 ALTER TABLE `sys_category` ENABLE KEYS */;

#
# Structure for table "sys_config"
#

DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `pid` int(11) NOT NULL COMMENT '上级',
  `type` char(1) NOT NULL COMMENT '类型',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `defaults` varchar(200) DEFAULT NULL COMMENT '默认值',
  `vals` varchar(200) DEFAULT NULL COMMENT '设置值',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Data for table "sys_config"
#

/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,0,'g','core','核心配置',NULL,NULL,1),(2,0,'g','sys','系统配置',NULL,NULL,92),(3,2,'t','title','系统名称','管理平台','管理平台 ',1),(4,1,'n','file_size','附件大小','10240','1024',1),(5,2,'t','role','默认角色','default',NULL,9);
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;

#
# Structure for table "sys_department"
#

DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `pid` int(11) NOT NULL COMMENT '上级部门',
  `type` char(1) NOT NULL COMMENT '部门类型',
  `code` varchar(50) NOT NULL COMMENT '部门代码',
  `title` varchar(100) NOT NULL COMMENT '部门名称',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Data for table "sys_department"
#

/*!40000 ALTER TABLE `sys_department` DISABLE KEYS */;
INSERT INTO `sys_department` VALUES (1,0,'0','10','教务',1,'1'),(2,0,'0','21','财务',2,'1'),(3,0,'0','BSC','办事处',9,'1'),(4,3,'0','BJ','北京',1,'1'),(5,3,'0','FJ','福建',2,'1');
/*!40000 ALTER TABLE `sys_department` ENABLE KEYS */;

#
# Structure for table "sys_dict"
#

DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `pid` int(11) NOT NULL COMMENT '上级',
  `type` char(1) NOT NULL COMMENT '类型',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8;

#
# Data for table "sys_dict"
#

/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1,0,'d','model_type','模型类型',99),(2,1,'v','c','通用',1),(3,1,'v','t','树形',2),(4,1,'v','d','数据',3),(7,0,'d','db_type','数据类型',99),(8,7,'v','varchar','文本',1),(9,7,'v','text','长文本',2),(10,7,'v','number','数字',3),(11,7,'v','date','日期',4),(12,7,'v','datetime','日期时间',5),(13,0,'d','field_type','字段类型',99),(14,13,'v','string','文本',1),(15,13,'v','text','长文本',2),(16,13,'v','number','数字',3),(17,13,'v','html','富文本',4),(18,13,'v','file','文件',5),(19,13,'v','dict','字典',6),(20,13,'v','date','日期',7),(21,13,'v','datetime','日期时间',8),(23,0,'d','dict_type','字典类型',99),(24,23,'v','c','分类',1),(25,23,'v','d','字典',2),(26,23,'v','v','值',3),(27,0,'d','config_type','配置类型',99),(28,27,'v','g','分组',1),(29,27,'v','n','数字',2),(30,27,'v','t','文本',3),(31,27,'v','p','图片',4),(32,27,'v','d','日期',5),(33,0,'d','menu_type','菜单类型',99),(34,33,'v','0','分组',1),(35,33,'v','1','内部功能',2),(36,33,'v','2','外部链接',3),(37,0,'d','course_type','分类类型',99),(38,37,'v','1','点播课程',1),(39,37,'v','2','网络课堂',2),(40,0,'d','app_type','应用类型',99),(41,40,'v','i','内置模块',1),(42,40,'v','d','桌面应用',2),(43,40,'v','m','移动应用',3),(44,40,'v','h','HTML5',4),(45,40,'v','w','WEB应用',5),(46,0,'d','depart_type','部门类型',99),(47,46,'v','0','普通部门',1),(48,0,'d','chart_type','图表类型',99),(49,48,'v','table','数据表格',1),(50,48,'v','line','折线图',2),(51,48,'v','bar','柱状图',3),(52,48,'v','pie','饼图',4),(53,48,'v','gauge','仪表盘',5),(54,0,'d','msg_type','消息类型',99),(55,54,'v','1','点对点',1),(56,54,'v','2','一对多',2),(57,0,'d','pipe_mode','数据流类型',99),(58,57,'v','r','只读',1),(59,57,'v','w','只写',2),(61,0,'d','validation_type','验证规则类型',99),(62,61,'v','regex','正则匹配',1),(63,61,'v','length','长度限制',2),(64,61,'v','between','数字范围',3),(65,61,'v','in','枚举',4),(66,61,'v','sql','数据库查询',6),(67,0,'d','status','状态',99),(68,67,'v','1','启用',1),(69,67,'v','0','禁用',2),(70,0,'d','boolean','是否',9),(71,70,'v','1','是',1),(72,70,'v','0','否',2),(73,13,'v','relation','关联',9),(74,0,'d','jdbc_type','数据库类型',4),(75,74,'v','mysql','MySQL',1),(76,74,'v','sqlserver','SQL Server',2),(77,74,'v','oracle','ORACLE',3),(78,74,'v','pg','PostgreSQL',4),(79,74,'v','mongodb','MongoDB',5),(80,61,'v','unique','唯一',5);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;

#
# Structure for table "sys_file"
#

DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `name` varchar(100) NOT NULL COMMENT '文件名',
  `exts` varchar(10) NOT NULL COMMENT '扩展名',
  `md5` varchar(50) NOT NULL COMMENT '唯一标识',
  `path` varchar(200) NOT NULL COMMENT '保存路径',
  `upload_user_id` int(11) NOT NULL COMMENT '上传用户',
  `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
  `size` int(11) DEFAULT NULL COMMENT '文件大小',
  `cover` varchar(200) DEFAULT NULL COMMENT '封面',
  `length` int(11) DEFAULT NULL COMMENT '长度',
  `pre_file` varchar(200) DEFAULT NULL COMMENT '预览文件',
  `status` char(1) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "sys_file"
#

/*!40000 ALTER TABLE `sys_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_file` ENABLE KEYS */;

#
# Structure for table "sys_log"
#

DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `user_id` int(11) NOT NULL COMMENT '操作用户',
  `action_id` int(11) NOT NULL COMMENT '操作动作',
  `action_time` datetime NOT NULL COMMENT '上传时间',
  `param` text COMMENT '参数',
  `ip` varchar(50) NOT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=287 DEFAULT CHARSET=utf8;

#
# Data for table "sys_log"
#

/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;

#
# Structure for table "sys_menu"
#

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `app_id` int(11) NOT NULL COMMENT '所属应用',
  `pid` int(11) NOT NULL COMMENT '上级菜单',
  `type` char(1) NOT NULL COMMENT '菜单类型',
  `url` varchar(200) NOT NULL COMMENT '操作地址',
  `title` varchar(50) NOT NULL COMMENT '菜单名称',
  `list_sort` int(11) NOT NULL COMMENT '排序',
  `icon` varchar(20) DEFAULT NULL COMMENT '图标',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

#
# Data for table "sys_menu"
#

/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,1,11,'1','dev/module/list.html','模块管理',9,NULL,'1'),(2,1,11,'1','dev/models/list.html','模型管理',9,NULL,'1'),(3,1,11,'1','dev/field/list.html','字段管理',9,NULL,'1'),(4,1,11,'1','dev/validation/list.html','数据验证',9,NULL,'1'),(5,2,12,'1','sys/application/list.html','应用管理',9,NULL,'1'),(6,2,12,'1','sys/dict/list.html','字典管理',9,NULL,'1'),(7,2,12,'1','sys/config/list.html','配置管理',9,NULL,'1'),(8,2,12,'1','sys/menu/list.html','菜单管理',9,NULL,'1'),(9,2,24,'1','sys/user/list.html','用户管理',9,NULL,'1'),(10,2,24,'1','sys/role/list.html','角色管理',9,NULL,'1'),(11,1,0,'0','#','开发工具',1,NULL,'1'),(12,2,0,'0','#','系统管理',1,NULL,'1'),(13,1,30,'1','dev/database/query','数据查询',4,'database','1'),(14,2,29,'1','sys/token/list.html','令牌管理',9,NULL,'1'),(15,2,24,'1','sys/category/list.html','分类管理',9,NULL,'1'),(16,2,29,'1','sys/file/list.html','文件管理',9,NULL,'1'),(17,2,29,'1','sys/log/list.html','日志管理',9,NULL,'1'),(18,2,12,'1','sys/action/list.html','行为管理',9,NULL,'1'),(19,2,24,'1','sys/department/list.html','部门管理',9,NULL,'1'),(20,1,30,'1','dev/dataset/list.html','数据集',9,NULL,'1'),(21,1,30,'1','dev/chart/list.html','报表管理',9,NULL,'1'),(23,2,29,'1','sys/notice/list.html','公告管理',9,NULL,'1'),(24,2,0,'0','#','用户权限',2,NULL,'1'),(29,2,0,'0','#','运维管理',0,NULL,'1'),(30,1,0,'0','#','数据报表',2,NULL,'1'),(31,2,29,'1','sys/config/set','系统配置',4,'cogs','1'),(33,2,29,'1','sys/userlogin/list.html','登录日志',9,NULL,'1');
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;

#
# Structure for table "sys_menu_action"
#

DROP TABLE IF EXISTS `sys_menu_action`;
CREATE TABLE `sys_menu_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单',
  `action_id` int(11) NOT NULL COMMENT '行为',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8;

#
# Data for table "sys_menu_action"
#

/*!40000 ALTER TABLE `sys_menu_action` DISABLE KEYS */;
INSERT INTO `sys_menu_action` VALUES (21,19,179),(22,19,181),(23,19,180),(24,19,183),(25,19,182),(26,19,185),(27,19,184),(28,19,178),(37,21,2),(38,21,9),(39,21,7),(40,21,8),(41,21,5),(42,21,6),(43,21,3),(44,21,4),(45,20,16),(46,20,17),(47,20,12),(48,20,13),(49,20,14),(50,20,15),(51,20,10),(52,20,11),(53,33,136),(54,33,135),(55,33,137),(56,33,130),(57,33,132),(58,33,131),(59,33,134),(60,33,133),(61,31,103),(62,31,102),(63,31,105),(64,31,104),(65,31,98),(66,31,99),(67,31,101),(68,31,100),(69,23,56),(70,23,57),(71,23,52),(72,23,53),(73,23,54),(74,23,55),(75,23,50),(76,23,51),(77,17,169),(78,17,168),(79,17,163),(80,17,162),(81,17,165),(82,17,164),(83,17,167),(84,17,166),(85,16,158),(86,16,157),(87,16,159),(88,16,161),(89,16,160),(90,16,154),(91,16,156),(92,16,155),(93,14,138),(94,14,139),(95,14,141),(96,14,140),(97,14,143),(98,14,142),(99,14,145),(100,14,144),(101,15,147),(102,15,146),(103,15,149),(104,15,148),(105,15,150),(106,15,152),(107,15,151),(108,15,153),(109,10,125),(110,10,124),(111,10,127),(112,10,126),(113,10,129),(114,10,128),(115,10,123),(116,10,122),(117,9,114),(118,9,116),(119,9,115),(120,9,118),(121,9,117),(122,9,119),(123,9,121),(124,9,120),(125,18,170),(126,18,172),(127,18,171),(128,18,174),(129,18,173),(130,18,176),(131,18,175),(132,18,177),(133,8,113),(134,8,107),(135,8,106),(136,8,109),(137,8,108),(138,8,110),(139,8,112),(140,8,111),(141,7,103),(142,7,102),(143,7,105),(144,7,104),(145,7,98),(146,7,99),(147,7,101),(148,7,100),(149,6,90),(150,6,91),(151,6,96),(152,6,97),(153,6,92),(154,6,93),(155,6,94),(156,6,95),(157,5,89),(158,5,85),(159,5,86),(160,5,87),(161,5,88),(162,5,82),(163,5,83),(164,5,84),(165,4,49),(166,4,45),(167,4,46),(168,4,47),(169,4,48),(170,4,42),(171,4,43),(172,4,44),(173,3,38),(174,3,39),(175,3,34),(176,3,35),(177,3,36),(178,3,37),(179,3,41),(180,3,40),(181,2,27),(182,2,28),(183,2,29),(184,2,26),(185,2,30),(186,2,31),(187,2,32),(188,2,33),(189,1,18),(190,1,19),(191,1,23),(192,1,24),(193,1,25),(194,1,20),(195,1,21),(196,1,22);
/*!40000 ALTER TABLE `sys_menu_action` ENABLE KEYS */;

#
# Structure for table "sys_notice"
#

DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `add_time` datetime NOT NULL COMMENT '发布时间',
  `add_user_id` int(11) NOT NULL COMMENT '发布人',
  `read_count` int(11) NOT NULL COMMENT '阅读量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "sys_notice"
#

/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;

#
# Structure for table "sys_notice_read"
#

DROP TABLE IF EXISTS `sys_notice_read`;
CREATE TABLE `sys_notice_read` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户',
  `notice_id` int(11) NOT NULL COMMENT '公告',
  `read_time` datetime NOT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "sys_notice_read"
#

/*!40000 ALTER TABLE `sys_notice_read` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice_read` ENABLE KEYS */;

#
# Structure for table "sys_role"
#

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `code` varchar(20) NOT NULL COMMENT '代码',
  `title` varchar(50) NOT NULL COMMENT '名称',
  `is_default` char(1) DEFAULT '0' COMMENT '是否默认',
  `status` char(1) DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "sys_role"
#

/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'test','测试角色','0','1');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;

#
# Structure for table "sys_role_action"
#

DROP TABLE IF EXISTS `sys_role_action`;
CREATE TABLE `sys_role_action` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `role_id` int(11) NOT NULL COMMENT '角色',
  `action_id` int(11) NOT NULL COMMENT '行为',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

#
# Data for table "sys_role_action"
#

/*!40000 ALTER TABLE `sys_role_action` DISABLE KEYS */;
INSERT INTO `sys_role_action` VALUES (1,1,29),(2,1,31),(3,1,17),(4,1,16),(5,1,23),(6,1,15),(7,1,14),(8,1,12),(9,1,18),(10,1,8),(11,1,7),(12,1,6),(13,1,5),(14,1,24),(15,1,10),(16,1,19),(17,1,9),(18,1,30),(19,1,20),(20,1,21);
/*!40000 ALTER TABLE `sys_role_action` ENABLE KEYS */;

#
# Structure for table "sys_role_menu"
#

DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `role_id` int(11) NOT NULL COMMENT '角色',
  `menu_id` int(11) NOT NULL COMMENT '菜单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

#
# Data for table "sys_role_menu"
#

/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (1,1,29),(2,1,31),(3,1,17),(4,1,16),(5,1,23),(6,1,15),(7,1,14),(8,1,12),(9,1,18),(10,1,8),(11,1,7),(12,1,6),(13,1,5),(14,1,24),(15,1,10),(16,1,19),(17,1,9),(18,1,30),(19,1,20),(20,1,21);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;

#
# Structure for table "sys_token"
#

DROP TABLE IF EXISTS `sys_token`;
CREATE TABLE `sys_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `code` varchar(50) NOT NULL COMMENT '代码',
  `user_id` int(11) DEFAULT '0' COMMENT '用户',
  `client_id` int(11) DEFAULT '0' COMMENT '应用',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "sys_token"
#

/*!40000 ALTER TABLE `sys_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_token` ENABLE KEYS */;

#
# Structure for table "sys_user"
#

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `account` varchar(50) NOT NULL COMMENT '账号',
  `password` char(32) NOT NULL COMMENT '密码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(100) DEFAULT NULL COMMENT '姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `depart_id` int(11) DEFAULT '9' COMMENT '所在部门',
  `salt` char(6) NOT NULL COMMENT '盐值',
  `status` char(1) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "sys_user"
#

/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','ab711479b07a51ec99b31daec72225b8','admin@qq.com','13866666666','管理员','2020-03-16 20:39:42',1,'321445','1'),(2,'test','a2991761c6be0a10c4d62e2d0deb406e','test@qq.com','13988888888','test','2020-03-16 20:50:12',2,'245245','1'),(3,'test2','db96f43b75aa45b6f8c117264ba660f8','test2@qq.com','18912345678','test2','2020-04-21 15:24:50',1,'1gks3u','1');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;

#
# Structure for table "sys_user_login"
#

DROP TABLE IF EXISTS `sys_user_login`;
CREATE TABLE `sys_user_login` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `account` varchar(50) NOT NULL COMMENT '账号',
  `user_id` int(11) DEFAULT '0' COMMENT '用户',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `session_id` varchar(50) DEFAULT NULL COMMENT 'Session',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `logout_time` datetime DEFAULT NULL COMMENT '退出时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "sys_user_login"
#

/*!40000 ALTER TABLE `sys_user_login` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_login` ENABLE KEYS */;

#
# Structure for table "sys_user_role"
#

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增长主键ID',
  `user_id` int(11) NOT NULL COMMENT '用户',
  `role_id` int(11) NOT NULL COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Data for table "sys_user_role"
#

/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (2,3,1),(3,2,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
