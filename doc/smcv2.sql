
create table SMC_CFG_LEVEL
(
  ID          NUMBER,
  LEVELID     NUMBER,
  NAME        VARCHAR2(100),
  DESCRIPTION VARCHAR2(200)
)
;
create table SMC_CFG_SOURCE
(
  ID          NUMBER,
  NAME        VARCHAR2(50),
  USERNAME    VARCHAR2(50),
  PASSWORD    VARCHAR2(32),
  IP_LIST     VARCHAR2(500),
  DESCRIPTION VARCHAR2(200)
)
;


create table SMC_CFG_STRATEGY
(
  ID               NUMBER,
  SRC_ID           NUMBER,
  LEVEL_ID         NUMBER,
  TO_USERGROUP_ID  VARCHAR2(140),
  TTL              NUMBER,
  SEND_OFFSET_TIME NUMBER,
  SEND_TIMES       NUMBER,
  SEND_INTERVAL    NUMBER,
  SEND_WAY         NUMBER,
  RESEND_WHEN_FAIL NUMBER,
  TAKE_EFFECT      NUMBER
)
;


create table SMC_CFG_SYS
(
  ID                           NUMBER,
  SMTP_HOST                    VARCHAR2(100),
  MAIL_USER                    VARCHAR2(100),
  MAIL_PWD                     VARCHAR2(100),
  NODEID                       NUMBER,
  FEETYPE                      NUMBER,
  AGENTFLAG                    NUMBER,
  MORELATETOMTFLAG             NUMBER,
  PRIORITY                     NUMBER,
  REPORTFLAG                   NUMBER,
  TP_PID                       NUMBER,
  TP_UDHI                      NUMBER,
  MESSAGECODING                NUMBER,
  MESSAGETYPE                  NUMBER,
  SPNUMBER                     VARCHAR2(100),
  CHARGENUMBER                 VARCHAR2(100),
  CORPID                       VARCHAR2(100),
  SERVICETYPE                  VARCHAR2(100),
  FEEVALUE                     VARCHAR2(100),
  GIVENVALUE                   VARCHAR2(100),
  EXPIRETIME                   VARCHAR2(100),
  SCHEDULETIME                 VARCHAR2(100),
  SERVEIP                      VARCHAR2(100),
  SERVEPORT                    NUMBER,
  SMS_USERNAME                 VARCHAR2(100),
  SMS_USERPWD                  VARCHAR2(100),
  SERVER_RECEIVE_USERNAME      VARCHAR2(30),
  SERVER_RECEIVE_PWD           VARCHAR2(30),
  SERVER_RECEIVE_PORT          NUMBER,
  SECURITY_MAX_SENT_COUNT_DAY  NUMBER,
  SECURITY_MAX_SENT_COUNT_HOUR NUMBER,
  EXT_TABLE_DRIVER             VARCHAR2(100),
  EXT_TABLE_URL                VARCHAR2(100),
  EXT_TABLE_USER               VARCHAR2(100),
  EXT_TABLE_PWD                VARCHAR2(100),
  DESCRIPTION                  VARCHAR2(100),
  TAKE_EFFECT                  NUMBER
)
;


create table SMC_CFG_TOUSER
(
  ID          NUMBER,
  NAME        VARCHAR2(30),
  GROUP_ID    NUMBER,
  CELLPHONE   VARCHAR2(20),
  EMAIL       VARCHAR2(100),
  DESCRIPTION VARCHAR2(100)
)
;


create table SMC_CFG_TOUSERGROUP
(
  ID          NUMBER,
  NAME        VARCHAR2(100),
  DESCRIPTION VARCHAR2(100)
)
;


create table SMC_CFG_USER
(
  ID          NUMBER,
  NAME        VARCHAR2(32),
  PWD         VARCHAR2(32),
  DESCRIPTION VARCHAR2(100)
)
;


create table SMC_DATA
(
  ID                NUMBER not null,
  SRC_ID            NUMBER not null,
  LEVEL_ID          NUMBER not null,
  TO_USERS          VARCHAR2(500),
  SEND_WAY          NUMBER not null,
  OCCUR_TIME        DATE,
  CONTENT           VARCHAR2(800),
  SEND_TIME         DATE,
  SEND_TIME_EXCLUDE VARCHAR2(800),
  STAMPTIME         DATE default sysdate,
  USER_NAME         VARCHAR2(10),
  PASSWORD          VARCHAR2(32),
  SENT_OK_TIMES     NUMBER default 0,
  SUBJECT           VARCHAR2(500),
  TYPE              NUMBER default 1,
  ATTACHMENTFILE    VARCHAR2(200),
  COUNTER           NUMBER
)
;
comment on column SMC_DATA.ID
  is '编号';
comment on column SMC_DATA.SRC_ID
  is '消息源编号';
comment on column SMC_DATA.LEVEL_ID
  is '消息级别';
comment on column SMC_DATA.TO_USERS
  is '接收人';
comment on column SMC_DATA.SEND_WAY
  is '发送方式 1:短信 2：邮件 3:邮件与短信';
comment on column SMC_DATA.OCCUR_TIME
  is '消息产生时间';
comment on column SMC_DATA.CONTENT
  is '内容';
comment on column SMC_DATA.SEND_TIME
  is '发送时间   为空：立即发送  不为空： 定时发送';
comment on column SMC_DATA.SEND_TIME_EXCLUDE
  is '在哪个时间端不发送短息,可以为空';
comment on column SMC_DATA.STAMPTIME
  is '入库时间';
comment on column SMC_DATA.USER_NAME
  is '短信用户名';
comment on column SMC_DATA.PASSWORD
  is '短信发送密码';
comment on column SMC_DATA.SENT_OK_TIMES
  is '发送次数';
comment on column SMC_DATA.SUBJECT
  is '邮件主题';
comment on column SMC_DATA.TYPE
  is '类型1:webservice插入 2. 外围系统插入';
comment on column SMC_DATA.ATTACHMENTFILE
  is '邮件附件名(仅限type=1)';


create table SMC_DATA_FORBIDPHONE
(
  TELEPHONE  VARCHAR2(50),
  BUSINESSID NUMBER
)
;


create table SMC_DATA_HISTORY
(
  ID          NUMBER not null,
  SRC_ID      NUMBER,
  LEVEL_ID    NUMBER,
  OCCUR_TIME  DATE,
  CONTENT     VARCHAR2(800),
  SEND_RESULT NUMBER,
  STAMPTIME   DATE,
  CAUSE       VARCHAR2(140)
)
;
alter table SMC_DATA_HISTORY
  add constraint PK_SMC_DATA_HISTORY primary key (ID);


create table SMC_DATA_RECEIVE
(
  ID           NUMBER not null,
  FROM_USER    VARCHAR2(20),
  RECEIVE_TIME DATE,
  CONTENT      VARCHAR2(800),
  BUSINESSID   VARCHAR2(20),
  ISEFFECT     NUMBER,
  ISPARSEOK    NUMBER,
  REMARK       VARCHAR2(500),
  SPNUMBER     VARCHAR2(30)
)
;
alter table SMC_DATA_RECEIVE
  add constraint PK_SMC_DATA_RECEIVE primary key (ID);


create table SMC_DATA_RECEIVE_HISTORY
(
  ID           NUMBER not null,
  FROM_USER    VARCHAR2(100),
  RECEIVE_TIME DATE,
  CONTENT      VARCHAR2(800),
  BUSINESSID   VARCHAR2(20),
  ISEFFECT     NUMBER,
  ISPARSEOK    NUMBER,
  REMARK       VARCHAR2(500),
  SPNUMBER     VARCHAR2(30),
  STAMPTIME    DATE,
  RESULT       NUMBER,
  CAUSE        VARCHAR2(800),
  NETTYPE      NUMBER default 1
)
;
comment on table SMC_DATA_RECEIVE_HISTORY
  is '投诉历史记录表';
comment on column SMC_DATA_RECEIVE_HISTORY.ID
  is '编号';
comment on column SMC_DATA_RECEIVE_HISTORY.FROM_USER
  is '用户号码';
comment on column SMC_DATA_RECEIVE_HISTORY.RECEIVE_TIME
  is '接收时间';
comment on column SMC_DATA_RECEIVE_HISTORY.CONTENT
  is '接收内容';
comment on column SMC_DATA_RECEIVE_HISTORY.BUSINESSID
  is '业务编号';
comment on column SMC_DATA_RECEIVE_HISTORY.ISEFFECT
  is '短信是否有效 ,1：有效 ，0 无效';
comment on column SMC_DATA_RECEIVE_HISTORY.ISPARSEOK
  is '是否解析好';
comment on column SMC_DATA_RECEIVE_HISTORY.REMARK
  is '备注';
comment on column SMC_DATA_RECEIVE_HISTORY.SPNUMBER
  is '网关接入号';
comment on column SMC_DATA_RECEIVE_HISTORY.STAMPTIME
  is '短信入库时间';
comment on column SMC_DATA_RECEIVE_HISTORY.RESULT
  is '结果';
comment on column SMC_DATA_RECEIVE_HISTORY.CAUSE
  is '原因';
alter table SMC_DATA_RECEIVE_HISTORY
  add constraint PK_SMC_DATA_RECEIVE_HISTORY primary key (ID);

create table SMC_EXPRESS_DATA
(
  ID                NUMBER,
  SRC_ID            NUMBER,
  LEVEL_ID          NUMBER,
  TO_USERS          VARCHAR2(500),
  SEND_WAY          NUMBER,
  OCCUR_TIME        DATE,
  CONTENT           VARCHAR2(800),
  SEND_TIME         DATE,
  SEND_TIME_EXCLUDE VARCHAR2(800)
)
;


create table SMC_QUERY_RECEIVE
(
  SENDSMSTYPE        VARCHAR2(50),
  BUSINESSID         NUMBER,
  SENDSMSCONTENT     VARCHAR2(800),
  CONTENTEXPLANATION VARCHAR2(200),
  PROCESSTYPE        NUMBER,
  ISNEEDREBACKSMS    NUMBER,
  REBACKSMSCONTENT   VARCHAR2(800)
)
;


insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (10, 10, '级别0', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (1, 1, '级别1', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (2, 2, '级别2', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (3, 3, '级别3', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (4, 4, '级别4', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (5, 5, '级别5', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (6, 6, '级别6', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (7, 7, '级别7', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (8, 8, '级别8', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (9, 9, '级别9', null);
commit;

insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3, 'IGPv1', '1', '1', null, 'IGPv1');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1102, '电信性能告警', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1, 'NBI', '1', '1', null, 'NBI');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (4, 'OSP', '1', '1', null, 'osp');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (6, 'M1', '1', '1', null, 'm1');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (8, 'IGPV3', '1', '1', null, 'IGPV3');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2101, '硬件告警', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2102, '性能告警', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2103, '工单', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2104, '作业计划', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2105, '备份数据提醒', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2106, '坏小区', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2107, 'MR问题小区', '1', '1', null, 'GSM网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3102, 'WCDMA性能告警', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (4000, '报表', '1', '1', null, 'CDMA相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3101, '硬件告警', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3102, '性能告警', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3103, '工单', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3104, '作业计划', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3105, '备份数据提醒', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3106, '坏小区', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3107, 'MR问题小区', '1', '1', null, 'WCDMA网相关的业务');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1101, '硬件告警', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1102, '性能告警', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1103, '工单', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1104, '作业计划', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1105, '备份数据提醒', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1106, '坏小区', '1', '1', null, '电信性能告警');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1107, 'MR问题小区', '1', '1', null, '电信性能告警');
commit;

insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (1009, 1102, 2, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (314, 3105, 2, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (3, 3, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (4, 4, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (1, 1, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (5, 5, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (9, 9, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (100, 1102, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (101, 1103, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (103, 1104, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (104, 1105, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (105, 1106, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (106, 1107, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (200, 2102, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (201, 2103, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (203, 2104, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (204, 2105, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (205, 2106, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (206, 2107, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (300, 3102, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (301, 3103, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (303, 3104, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (304, 3105, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (305, 3106, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (306, 3107, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (307, 4000, 1, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (308, 4000, 2, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (225, 2106, 2, '1', 1440, 1, 1, 1, 1, 1, 1);
insert into SMC_CFG_STRATEGY (ID, SRC_ID, LEVEL_ID, TO_USERGROUP_ID, TTL, SEND_OFFSET_TIME, SEND_TIMES, SEND_INTERVAL, SEND_WAY, RESEND_WHEN_FAIL, TAKE_EFFECT)
values (324, 3105, 2, '1', 1440, 1, 1, 1, 1, 1, 1);
commit;

insert into SMC_CFG_SYS (ID, SMTP_HOST, MAIL_USER, MAIL_PWD, NODEID, FEETYPE, AGENTFLAG, MORELATETOMTFLAG, PRIORITY, REPORTFLAG, TP_PID, TP_UDHI, MESSAGECODING, MESSAGETYPE, SPNUMBER, CHARGENUMBER, CORPID, SERVICETYPE, FEEVALUE, GIVENVALUE, EXPIRETIME, SCHEDULETIME, SERVEIP, SERVEPORT, SMS_USERNAME, SMS_USERPWD, SERVER_RECEIVE_USERNAME, SERVER_RECEIVE_PWD, SERVER_RECEIVE_PORT, SECURITY_MAX_SENT_COUNT_DAY, SECURITY_MAX_SENT_COUNT_HOUR, EXT_TABLE_DRIVER, EXT_TABLE_URL, EXT_TABLE_USER, EXT_TABLE_PWD, DESCRIPTION, TAKE_EFFECT)
values (1, 'smtp.uway.cn ', 'lihao@uway.cn ', 'myjava522 ', -1257824640, 1, 0, 2, 0, 1, 1, 1, 15, 2, '106557540 ', '000000000000000000000 ', '42656 ', 'wypt ', '0 ', '0 ', ' ', ' ', '61.158.140.134 ', 8801, 'wypt ', 'wypt ', 'wypt ', 'wypt ', 9988, 10000, 5000, '1 ', '1 ', '1 ', '1 ', '1 ', 1);
commit;


insert into SMC_CFG_TOUSER (ID, NAME, GROUP_ID, CELLPHONE, EMAIL, DESCRIPTION)
values (1, '田工', 1, '18600000000', 'tianjing@uway.cn', '描述 ');
insert into SMC_CFG_TOUSER (ID, NAME, GROUP_ID, CELLPHONE, EMAIL, DESCRIPTION)
values (2, '梁工', 1, '18600000000', 'liangww@uway.cn', '描述说明');
commit;


insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (8, 'donet组', 'donet组');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (7, 'c++', '1');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (6, 'java组', 'java组');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (1, '产品组', '1');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (2, 'C#组', '2');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (9, '测试组', '测试组描述');
commit;


insert into SMC_CFG_USER (ID, NAME, PWD, DESCRIPTION)
values (1, '1', 'c4ca4238a0b923820dcc509a6f75849b', '登陆用户名');
insert into SMC_CFG_USER (ID, NAME, PWD, DESCRIPTION)
values (6, '1', '1', '1');
commit;




insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('短信业务编码查找', 2100, '2100#?', '常用短信业务代码查找', 1, 0, '常用短信业务代码查找');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('短信业务编码查找', 2101, '2101#?', '常用硬件告警相关的业务代码查找', 1, 0, '常用硬件告警相关的业务代码查找');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('短信业务编码查找', 2102, '2102#?', '常用性能告警相关的业务代码查找', 1, 0, '常用性能告警相关的业务代码查找');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('短信业务编码查找', 2103, '2103#?', '常用工单相关的业务代码查找', 1, 0, '常用工单相关的业务代码查找');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('停止短信通知', 2100, '2100#1', '停止一切属于本人短信通知(性能告警,硬件告警，工单催办)等', 1, 0, '停止一切属于本人短信通知(性能告警,硬件告警，工单催办)等');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('停止短信通知', 2101, '2101#1', '停止只属于本人硬件告警通知', 1, 0, '停止只属于本人硬件告警通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('停止短信通知', 2102, '2102#1', '停止只属于本人硬件告警通知', 1, 1, '停止只属于本人硬件告警通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('停止短信通知', 2103, '2103#1', '停止只属于本人工单催办通知', 1, 0, '停止只属于本人工单催办通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('恢复短信通知', 2100, '2100#2', '恢复一切属于本人短信通知(性能告警,硬件告警，工单催办)等', 1, 0, '恢复一切属于本人短信通知(性能告警,硬件告警，工单催办)等');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('恢复短信通知', 2101, '2101#2', '恢复发送给本人硬件告警通知', 1, 0, '恢复发送给本人硬件告警通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('恢复短信通知', 2102, '2102#2', '恢复发送给本人性能告警通知', 1, 2, '恢复发送给本人性能告警通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('恢复短信通知', 2103, '2103#2', '恢复发送给本人工单催办通知', 1, 0, '恢复发送给本人工单催办通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('业务类查询', 2103, '2103#Q1', '查询当天待处理工单个数', 2, 0, '查询当天待处理工单个数');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('停止所有短信通知', 0, '0000#1', '停止所有短信通知', 1, 0, '停止所有短信通知');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('恢复所有短信通知', 0, '0000#2', '恢复所有短信通知', 1, 1, '恢复所有短信通知');
commit;



create table SMC_EXPRESS_DATA_HISTORY
(
  ID                NUMBER not null,
  SRC_ID            NUMBER,
  LEVEL_ID          NUMBER,
  TO_USERS          VARCHAR2(500),
  SEND_WAY          NUMBER,
  OCCUR_TIME        DATE,
  CONTENT           VARCHAR2(800),
  SEND_TIME         DATE,
  SEND_TIME_EXCLUDE VARCHAR2(800),
  SEND_RESULT       NUMBER,
  STAMPTIME         DATE,
  CAUSE             VARCHAR2(140),
  ATTACHMENTFILE    VARCHAR2(100),
  COUNTER           NUMBER default 1
);
-- Add comments to the columns 
comment on column SMC_EXPRESS_DATA_HISTORY.ID
  is '编号';
comment on column SMC_EXPRESS_DATA_HISTORY.SRC_ID
  is '消息源编码';
comment on column SMC_EXPRESS_DATA_HISTORY.LEVEL_ID
  is '消息级别';
comment on column SMC_EXPRESS_DATA_HISTORY.TO_USERS
  is '接收人';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_WAY
  is '发送方式 1：短信 2：邮件 3短信邮件';
comment on column SMC_EXPRESS_DATA_HISTORY.OCCUR_TIME
  is '发生时间';
comment on column SMC_EXPRESS_DATA_HISTORY.CONTENT
  is '发送内容';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_TIME
  is '发送时间';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_TIME_EXCLUDE
  is '发送排除时间';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_RESULT
  is '发送结果  0成功  -1 失败 -2 消息过期';
comment on column SMC_EXPRESS_DATA_HISTORY.STAMPTIME
  is '入库时间';
comment on column SMC_EXPRESS_DATA_HISTORY.CAUSE
  is '失败原因';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SMC_EXPRESS_DATA_HISTORY
  add constraint PK_SMC_EXPRESS_DATA_HISTORY primary key (ID) ;
  
  
  -- Create sequence 
create sequence SEQ_EXPRESS
minvalue 1
maxvalue 9999999999999999
start with 12304
increment by 1
cache 4;


-- Create sequence 
create sequence SEQ_SMC_CFG_SOURCE
minvalue 1
maxvalue 99999999999999999999999
start with 1
increment by 1
cache 4;

-- Create sequence 
create sequence SEQ_SMC_CFG_STRATEGY
minvalue 1
maxvalue 99999999999999999999999
start with 13
increment by 1
cache 4;

-- Create sequence 
create sequence SEQ_SMC_CFG_TOUSER
minvalue 1
maxvalue 99999999999999999999999
start with 5
increment by 1
cache 4;

-- Create sequence 
create sequence SEQ_SMC_CFG_TOUSERGROUP
minvalue 1
maxvalue 99999999999999999999999
start with 13
increment by 1
cache 4;


-- Create sequence 
create sequence SEQ_SMC_CFG_USER
minvalue 1
maxvalue 99999999999999999999999
start with 9
increment by 1
cache 4;
  
