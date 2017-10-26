alter table PROJECT_BURNDOWN_SPRINT drop constraint FKn77qpxpo7dea3cplo0lap3vjh;
drop table if exists PROJECT_BURNDOWN cascade;
drop table if exists PROJECT_BURNDOWN_SPRINT cascade;
drop sequence PROJECT_BURNDOWN_SPRINT_ID_SEQ;
