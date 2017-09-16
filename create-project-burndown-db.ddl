create table PROJECT_BURN_PROJECT (PROJECT_ID int4 not null, primary key (PROJECT_ID))
create table PROJECT_BURN_SPRINT (SPRINT_ID int4 not null, SPRINT_NUMBER int4, POINTS_TOTAL int4, status varchar(255), PROJECT_ID int4, primary key (SPRINT_ID))
alter table PROJECT_BURN_SPRINT add constraint FK6dlnafbinfbui966l5c3oixkx foreign key (PROJECT_ID) references PROJECT_BURN_PROJECT
create table PROJECT_BURN_PROJECT (PROJECT_ID int4 not null, primary key (PROJECT_ID))
create table PROJECT_BURN_SPRINT (SPRINT_ID int4 not null, SPRINT_NUMBER int4, POINTS_TOTAL int4, status varchar(255), PROJECT_ID int4, primary key (SPRINT_ID))
alter table PROJECT_BURN_SPRINT add constraint FK6dlnafbinfbui966l5c3oixkx foreign key (PROJECT_ID) references PROJECT_BURN_PROJECT
create table PROJECT_BURN_PROJECT (PROJECT_ID int4 not null, TITLE varchar(255), primary key (PROJECT_ID))
create table PROJECT_BURN_SPRINT (SPRINT_ID int4 not null, SPRINT_NUMBER int4, POINTS_TOTAL int4, status varchar(255), PROJECT_ID int4, primary key (SPRINT_ID))
alter table PROJECT_BURN_SPRINT add constraint FK6dlnafbinfbui966l5c3oixkx foreign key (PROJECT_ID) references PROJECT_BURN_PROJECT
create table PROJECT_BURN_PROJECT (PROJECT_ID int4 not null, primary key (PROJECT_ID))
create table PROJECT_BURN_SPRINT (SPRINT_ID int4 not null, SPRINT_NUMBER int4, POINTS_TOTAL int4, status varchar(255), PROJECT_ID int4, primary key (SPRINT_ID))
alter table PROJECT_BURN_SPRINT add constraint FK6dlnafbinfbui966l5c3oixkx foreign key (PROJECT_ID) references PROJECT_BURN_PROJECT
