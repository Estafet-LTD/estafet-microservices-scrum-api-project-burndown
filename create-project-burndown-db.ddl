create sequence PROJECT_BURNDOWN_SPRINT_ID_SEQ start 1 increment 1;
create table MESSAGE_EVENT (TOPIC_ID varchar(255) not null, MESSAGE_REFERENCE varchar(255) not null, VERSION int4, primary key (TOPIC_ID));
create table PROJECT_BURNDOWN (PROJECT_BURNDOWN_ID int4 not null, INITIAL_POINTS_TOTAL int4 not null, TITLE varchar(255) not null, primary key (PROJECT_BURNDOWN_ID));
create table PROJECT_BURNDOWN_SPRINT (PROJECT_BURNDOWN_SPRINT_ID int4 not null, END_DATE varchar(255) not null, SPRINT_NUMBER int4 not null, POINTS_TOTAL int4 not null, START_DATE varchar(255) not null, STATUS varchar(255) not null, PROJECT_BURNDOWN_ID int4, primary key (PROJECT_BURNDOWN_SPRINT_ID));
create table PROJECT_BURNDOWN_STORY (PROJECT_BURNDOWN_STORY_ID int4 not null, STATUS varchar(255) not null, STORY_POINTS int4 not null, PROJECT_BURNDOWN_ID int4, primary key (PROJECT_BURNDOWN_STORY_ID));
alter table PROJECT_BURNDOWN_SPRINT add constraint FKn77qpxpo7dea3cplo0lap3vjh foreign key (PROJECT_BURNDOWN_ID) references PROJECT_BURNDOWN;
alter table PROJECT_BURNDOWN_STORY add constraint FK9qnyfpkfqdxhupt17x4e5j9o5 foreign key (PROJECT_BURNDOWN_ID) references PROJECT_BURNDOWN;
