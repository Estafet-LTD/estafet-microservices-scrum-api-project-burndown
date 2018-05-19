alter table PROJECT_BURNDOWN_SPRINT drop constraint FKn77qpxpo7dea3cplo0lap3vjh;
alter table PROJECT_BURNDOWN_STORY drop constraint FK9qnyfpkfqdxhupt17x4e5j9o5;
drop table if exists MESSAGE_EVENT cascade;
drop table if exists PROJECT_BURNDOWN cascade;
drop table if exists PROJECT_BURNDOWN_SPRINT cascade;
drop table if exists PROJECT_BURNDOWN_STORY cascade;
drop sequence PROJECT_BURNDOWN_SPRINT_ID_SEQ;
