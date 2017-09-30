create table PROJECT (PROJECT_ID int4 not null, INITIAL_POINTS_TOTAL int4 not null, primary key (PROJECT_ID));
create table SPRINT (SPRINT_ID int4 not null, SPRINT_NUMBER int4 not null, POINTS_TOTAL int4 not null, projectId int4, status varchar(255), PROJECT_ID int4, primary key (SPRINT_ID));
alter table SPRINT add constraint FK7pee0qu96unu7n0k20rdvfuog foreign key (PROJECT_ID) references PROJECT;
