CREATE SEQUENCE If NOT EXISTS upload.logging_event_id_seq MINVALUE 1 START 1;

CREATE SEQUENCE If NOT EXISTS upload.hibernate_sequence START 1;


CREATE TABLE If NOT EXISTS upload.logging_event
(
    timestmp          BIGINT       NOT NULL,
    formatted_message TEXT         NOT NULL,
    logger_name       VARCHAR(254) NOT NULL,
    level_string      VARCHAR(254) NOT NULL,
    thread_name       VARCHAR(254),
    reference_flag    SMALLINT,
    arg0              VARCHAR(254),
    arg1              VARCHAR(254),
    arg2              VARCHAR(254),
    arg3              VARCHAR(254),
    caller_filename   VARCHAR(254) NOT NULL,
    caller_class      VARCHAR(254) NOT NULL,
    caller_method     VARCHAR(254) NOT NULL,
    caller_line       CHAR(4)      NOT NULL,
    event_id          BIGINT DEFAULT nextval('logging_event_id_seq') PRIMARY KEY
);

CREATE TABLE If NOT EXISTS upload.logging_event_property
(
    event_id     BIGINT       NOT NULL,
    mapped_key   VARCHAR(254) NOT NULL,
    mapped_value VARCHAR(1024),
    PRIMARY KEY (event_id, mapped_key),
    FOREIGN KEY (event_id) REFERENCES upload.logging_event (event_id)
);

CREATE TABLE If NOT EXISTS upload.logging_event_exception
(
    event_id   BIGINT       NOT NULL,
    i          SMALLINT     NOT NULL,
    trace_line VARCHAR(254) NOT NULL,
    PRIMARY KEY (event_id, i),
    FOREIGN KEY (event_id) REFERENCES upload.logging_event (event_id)
);

CREATE TABLE If NOT EXISTS upload.user_groups
(
    id BIGINT auto_increment NOT NULL,
    group_name VARCHAR(100),
    CONSTRAINT user_groups_pkey PRIMARY KEY (id),
    CONSTRAINT user_groups_group_name_key UNIQUE (group_name)
);


CREATE TABLE If NOT EXISTS upload.participant
(
    id        BIGINT auto_increment NOT NULL,
    username  VARCHAR(100),
    full_name VARCHAR(244),
    group_id  BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT "FK_participant_user_groups" FOREIGN KEY (group_id) REFERENCES upload.user_groups (id) ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE If NOT EXISTS upload.access_user_list
(
    id          IDENTITY NOT NULL PRIMARY KEY,
    user_id     bigint   NOT NULL,
    create_date TIMESTAMP default CURRENT_TIMESTAMP,
    date_from   TIMESTAMP default CURRENT_TIMESTAMP,
    date_to     TIMESTAMP default CURRENT_TIMESTAMP,
    is_native   BOOLEAN   default false,
    role        VARCHAR(100),
    added_by    VARCHAR(255)
);

CREATE TABLE If NOT EXISTS upload.file_owner
(
    id       IDENTITY NOT NULL PRIMARY KEY,
    username VARCHAR(100),
    path     VARCHAR(100)
);