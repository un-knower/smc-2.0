
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
  is '���';
comment on column SMC_DATA.SRC_ID
  is '��ϢԴ���';
comment on column SMC_DATA.LEVEL_ID
  is '��Ϣ����';
comment on column SMC_DATA.TO_USERS
  is '������';
comment on column SMC_DATA.SEND_WAY
  is '���ͷ�ʽ 1:���� 2���ʼ� 3:�ʼ������';
comment on column SMC_DATA.OCCUR_TIME
  is '��Ϣ����ʱ��';
comment on column SMC_DATA.CONTENT
  is '����';
comment on column SMC_DATA.SEND_TIME
  is '����ʱ��   Ϊ�գ���������  ��Ϊ�գ� ��ʱ����';
comment on column SMC_DATA.SEND_TIME_EXCLUDE
  is '���ĸ�ʱ��˲����Ͷ�Ϣ,����Ϊ��';
comment on column SMC_DATA.STAMPTIME
  is '���ʱ��';
comment on column SMC_DATA.USER_NAME
  is '�����û���';
comment on column SMC_DATA.PASSWORD
  is '���ŷ�������';
comment on column SMC_DATA.SENT_OK_TIMES
  is '���ʹ���';
comment on column SMC_DATA.SUBJECT
  is '�ʼ�����';
comment on column SMC_DATA.TYPE
  is '����1:webservice���� 2. ��Χϵͳ����';
comment on column SMC_DATA.ATTACHMENTFILE
  is '�ʼ�������(����type=1)';


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
  is 'Ͷ����ʷ��¼��';
comment on column SMC_DATA_RECEIVE_HISTORY.ID
  is '���';
comment on column SMC_DATA_RECEIVE_HISTORY.FROM_USER
  is '�û�����';
comment on column SMC_DATA_RECEIVE_HISTORY.RECEIVE_TIME
  is '����ʱ��';
comment on column SMC_DATA_RECEIVE_HISTORY.CONTENT
  is '��������';
comment on column SMC_DATA_RECEIVE_HISTORY.BUSINESSID
  is 'ҵ����';
comment on column SMC_DATA_RECEIVE_HISTORY.ISEFFECT
  is '�����Ƿ���Ч ,1����Ч ��0 ��Ч';
comment on column SMC_DATA_RECEIVE_HISTORY.ISPARSEOK
  is '�Ƿ������';
comment on column SMC_DATA_RECEIVE_HISTORY.REMARK
  is '��ע';
comment on column SMC_DATA_RECEIVE_HISTORY.SPNUMBER
  is '���ؽ����';
comment on column SMC_DATA_RECEIVE_HISTORY.STAMPTIME
  is '�������ʱ��';
comment on column SMC_DATA_RECEIVE_HISTORY.RESULT
  is '���';
comment on column SMC_DATA_RECEIVE_HISTORY.CAUSE
  is 'ԭ��';
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
values (10, 10, '����0', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (1, 1, '����1', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (2, 2, '����2', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (3, 3, '����3', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (4, 4, '����4', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (5, 5, '����5', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (6, 6, '����6', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (7, 7, '����7', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (8, 8, '����8', null);
insert into SMC_CFG_LEVEL (ID, LEVELID, NAME, DESCRIPTION)
values (9, 9, '����9', null);
commit;

insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3, 'IGPv1', '1', '1', null, 'IGPv1');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1102, '�������ܸ澯', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1, 'NBI', '1', '1', null, 'NBI');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (4, 'OSP', '1', '1', null, 'osp');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (6, 'M1', '1', '1', null, 'm1');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (8, 'IGPV3', '1', '1', null, 'IGPV3');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2101, 'Ӳ���澯', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2102, '���ܸ澯', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2103, '����', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2104, '��ҵ�ƻ�', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2105, '������������', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2106, '��С��', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (2107, 'MR����С��', '1', '1', null, 'GSM����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3102, 'WCDMA���ܸ澯', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (4000, '����', '1', '1', null, 'CDMA��ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3101, 'Ӳ���澯', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3102, '���ܸ澯', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3103, '����', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3104, '��ҵ�ƻ�', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3105, '������������', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3106, '��С��', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (3107, 'MR����С��', '1', '1', null, 'WCDMA����ص�ҵ��');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1101, 'Ӳ���澯', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1102, '���ܸ澯', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1103, '����', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1104, '��ҵ�ƻ�', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1105, '������������', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1106, '��С��', '1', '1', null, '�������ܸ澯');
insert into SMC_CFG_SOURCE (ID, NAME, USERNAME, PASSWORD, IP_LIST, DESCRIPTION)
values (1107, 'MR����С��', '1', '1', null, '�������ܸ澯');
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
values (1, '�﹤', 1, '18600000000', 'tianjing@uway.cn', '���� ');
insert into SMC_CFG_TOUSER (ID, NAME, GROUP_ID, CELLPHONE, EMAIL, DESCRIPTION)
values (2, '����', 1, '18600000000', 'liangww@uway.cn', '����˵��');
commit;


insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (8, 'donet��', 'donet��');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (7, 'c++', '1');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (6, 'java��', 'java��');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (1, '��Ʒ��', '1');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (2, 'C#��', '2');
insert into SMC_CFG_TOUSERGROUP (ID, NAME, DESCRIPTION)
values (9, '������', '����������');
commit;


insert into SMC_CFG_USER (ID, NAME, PWD, DESCRIPTION)
values (1, '1', 'c4ca4238a0b923820dcc509a6f75849b', '��½�û���');
insert into SMC_CFG_USER (ID, NAME, PWD, DESCRIPTION)
values (6, '1', '1', '1');
commit;




insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('����ҵ��������', 2100, '2100#?', '���ö���ҵ��������', 1, 0, '���ö���ҵ��������');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('����ҵ��������', 2101, '2101#?', '����Ӳ���澯��ص�ҵ��������', 1, 0, '����Ӳ���澯��ص�ҵ��������');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('����ҵ��������', 2102, '2102#?', '�������ܸ澯��ص�ҵ��������', 1, 0, '�������ܸ澯��ص�ҵ��������');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('����ҵ��������', 2103, '2103#?', '���ù�����ص�ҵ��������', 1, 0, '���ù�����ص�ҵ��������');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('ֹͣ����֪ͨ', 2100, '2100#1', 'ֹͣһ�����ڱ��˶���֪ͨ(���ܸ澯,Ӳ���澯�������߰�)��', 1, 0, 'ֹͣһ�����ڱ��˶���֪ͨ(���ܸ澯,Ӳ���澯�������߰�)��');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('ֹͣ����֪ͨ', 2101, '2101#1', 'ֹֻͣ���ڱ���Ӳ���澯֪ͨ', 1, 0, 'ֹֻͣ���ڱ���Ӳ���澯֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('ֹͣ����֪ͨ', 2102, '2102#1', 'ֹֻͣ���ڱ���Ӳ���澯֪ͨ', 1, 1, 'ֹֻͣ���ڱ���Ӳ���澯֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('ֹͣ����֪ͨ', 2103, '2103#1', 'ֹֻͣ���ڱ��˹����߰�֪ͨ', 1, 0, 'ֹֻͣ���ڱ��˹����߰�֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('�ָ�����֪ͨ', 2100, '2100#2', '�ָ�һ�����ڱ��˶���֪ͨ(���ܸ澯,Ӳ���澯�������߰�)��', 1, 0, '�ָ�һ�����ڱ��˶���֪ͨ(���ܸ澯,Ӳ���澯�������߰�)��');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('�ָ�����֪ͨ', 2101, '2101#2', '�ָ����͸�����Ӳ���澯֪ͨ', 1, 0, '�ָ����͸�����Ӳ���澯֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('�ָ�����֪ͨ', 2102, '2102#2', '�ָ����͸��������ܸ澯֪ͨ', 1, 2, '�ָ����͸��������ܸ澯֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('�ָ�����֪ͨ', 2103, '2103#2', '�ָ����͸����˹����߰�֪ͨ', 1, 0, '�ָ����͸����˹����߰�֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('ҵ�����ѯ', 2103, '2103#Q1', '��ѯ���������������', 2, 0, '��ѯ���������������');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('ֹͣ���ж���֪ͨ', 0, '0000#1', 'ֹͣ���ж���֪ͨ', 1, 0, 'ֹͣ���ж���֪ͨ');
insert into SMC_QUERY_RECEIVE (SENDSMSTYPE, BUSINESSID, SENDSMSCONTENT, CONTENTEXPLANATION, PROCESSTYPE, ISNEEDREBACKSMS, REBACKSMSCONTENT)
values ('�ָ����ж���֪ͨ', 0, '0000#2', '�ָ����ж���֪ͨ', 1, 1, '�ָ����ж���֪ͨ');
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
  is '���';
comment on column SMC_EXPRESS_DATA_HISTORY.SRC_ID
  is '��ϢԴ����';
comment on column SMC_EXPRESS_DATA_HISTORY.LEVEL_ID
  is '��Ϣ����';
comment on column SMC_EXPRESS_DATA_HISTORY.TO_USERS
  is '������';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_WAY
  is '���ͷ�ʽ 1������ 2���ʼ� 3�����ʼ�';
comment on column SMC_EXPRESS_DATA_HISTORY.OCCUR_TIME
  is '����ʱ��';
comment on column SMC_EXPRESS_DATA_HISTORY.CONTENT
  is '��������';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_TIME
  is '����ʱ��';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_TIME_EXCLUDE
  is '�����ų�ʱ��';
comment on column SMC_EXPRESS_DATA_HISTORY.SEND_RESULT
  is '���ͽ��  0�ɹ�  -1 ʧ�� -2 ��Ϣ����';
comment on column SMC_EXPRESS_DATA_HISTORY.STAMPTIME
  is '���ʱ��';
comment on column SMC_EXPRESS_DATA_HISTORY.CAUSE
  is 'ʧ��ԭ��';
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
  
