-- All tables created in my-sql in reverse file order.
-- Previous names of tables are grouped together with the new name.

DROP TABLE IF EXISTS log_reference;
DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS project_post_comment;
DROP TABLE IF EXISTS project_input_value;
DROP TABLE IF EXISTS portfolio;
DROP TABLE IF EXISTS project_post;
DROP TABLE IF EXISTS project_input_category;
DROP TABLE IF EXISTS assignment__project_definition;

DROP TABLE IF EXISTS knowledge_and_skill_assignment; -- Renamed
DROP TABLE IF EXISTS knowledge_and_skill__assignment; -- Renamed
DROP TABLE IF EXISTS assignment__knowledge_and_skill;
DROP TABLE IF EXISTS class_x__knowledge_and_skill;

DROP TABLE IF EXISTS project_cycle_step; -- Renamed.
DROP TABLE IF EXISTS project_cycle; -- Renamed.
DROP TABLE IF EXISTS project_milestone_step;
DROP TABLE IF EXISTS project_milestone;

DROP TABLE IF EXISTS project__image;
DROP TABLE IF EXISTS motivation;
DROP TABLE IF EXISTS knowledge_and_skill;
DROP TABLE IF EXISTS project;

DROP TABLE IF EXISTS ikigai_input;
DROP TABLE IF EXISTS project_input;
DROP TABLE IF EXISTS project_definition_category;
DROP TABLE IF EXISTS assignment;
DROP TABLE IF EXISTS project_definition_category_type;
DROP TABLE IF EXISTS project_definition;

DROP TABLE IF EXISTS image; -- Renamed.
DROP TABLE IF EXISTS file_x;

DROP TABLE IF EXISTS user; -- Renamed.
DROP TABLE IF EXISTS user_x;

DROP TABLE IF EXISTS teacher_school; -- Renamed.
DROP TABLE IF EXISTS teacher__school;

DROP TABLE IF EXISTS teacher_class; -- Renamed.
DROP TABLE IF EXISTS teacher__class_x;

DROP TABLE IF EXISTS student__school;

DROP TABLE IF EXISTS student_class; -- Renamed.
DROP TABLE IF EXISTS student__class_x;

DROP TABLE IF EXISTS class; -- Renamed.
DROP TABLE IF EXISTS class_x;

DROP TABLE IF EXISTS teacher;
DROP TABLE IF EXISTS student;

DROP TABLE IF EXISTS admin; -- Renamed.
DROP TABLE IF EXISTS admin_x;

DROP TABLE IF EXISTS school;
DROP TABLE IF EXISTS district;

-- DROP TABLE IF EXISTS interest;
