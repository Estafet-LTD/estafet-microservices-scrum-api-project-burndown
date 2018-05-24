alter table PROJECT_BURNDOWN_SPRINT drop constraint PB_SPRINT_TO_PROJECT_BURNDOWN_FK;
alter table PROJECT_BURNDOWN_STORY drop constraint PB_STORY_TO_PROJECT_BURNDOWN_FK;
drop table if exists MESSAGE_EVENT cascade;
drop table if exists PROJECT_BURNDOWN cascade;
drop table if exists PROJECT_BURNDOWN_SPRINT cascade;
drop table if exists PROJECT_BURNDOWN_STORY cascade;
drop sequence PROJECT_BURNDOWN_SPRINT_ID_SEQ;
