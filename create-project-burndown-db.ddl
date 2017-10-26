create sequence PROJECT_BURNDOWN_SPRINT_ID_SEQ start 1 increment 1;
create table PROJECT_BURNDOWN (PROJECT_BURNDOWN_ID int4 not null, INITIAL_POINTS_TOTAL int4 not null, primary key (PROJECT_BURNDOWN_ID));
create table PROJECT_BURNDOWN_SPRINT (PROJECT_BURNDOWN_SPRINT_ID int4 not null, SPRINT_NUMBER int4 not null, POINTS_TOTAL int4, PROJECT_BURNDOWN_ID int4, primary key (PROJECT_BURNDOWN_SPRINT_ID));
alter table PROJECT_BURNDOWN_SPRINT add constraint FKn77qpxpo7dea3cplo0lap3vjh foreign key (PROJECT_BURNDOWN_ID) references PROJECT_BURNDOWN;
