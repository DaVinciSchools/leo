-- All tables created in my-sql in reverse file order.
-- Previous names of tables are grouped together with the new name.

ALTER TABLE project_input
    DROP CONSTRAINT project_input__existing_project_id;

DROP TABLE IF EXISTS log_reference;
DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS deadline_status;
DROP TABLE IF EXISTS deadline;
DROP TABLE IF EXISTS deadline_source;
DROP TABLE IF EXISTS comment_x;
DROP TABLE IF EXISTS post;
DROP TABLE IF EXISTS project_post_rating;
DROP TABLE IF EXISTS project_input_fulfillment;
DROP TABLE IF EXISTS project_post_comment;
DROP TABLE IF EXISTS project_input_value;
DROP TABLE IF EXISTS portfolio;
DROP TABLE IF EXISTS assignment_project_status;
DROP TABLE IF EXISTS project_post;
DROP TABLE IF EXISTS project_input_category; -- Renamed
DROP TABLE IF EXISTS assignment__project_definition;

DROP TABLE IF EXISTS knowledge_and_skill_assignment; -- Renamed
DROP TABLE IF EXISTS knowledge_and_skill__assignment; -- Renamed
DROP TABLE IF EXISTS assignment__knowledge_and_skill;
DROP TABLE IF EXISTS class_x__knowledge_and_skill;

DROP TABLE IF EXISTS project_cycle_step; -- Renamed.
DROP TABLE IF EXISTS project_cycle; -- Renamed.
DROP TABLE IF EXISTS project_milestone_step;
DROP TABLE IF EXISTS project_milestone;
DROP TABLE IF EXISTS project_input__assignment;
DROP TABLE IF EXISTS project_comment;
DROP TABLE IF EXISTS project__image;
DROP TABLE IF EXISTS project__assignment;
DROP TABLE IF EXISTS motivation;
DROP TABLE IF EXISTS knowledge_and_skill;
DROP TABLE IF EXISTS project;

DROP TABLE IF EXISTS ikigai_input; -- Renamed
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

DROP TABLE IF EXISTS interest;
