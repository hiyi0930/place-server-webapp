

CREATE TABLE IF NOT EXISTS member
(   id identity not null primary key,
    name varchar(50),
    password varchar(100),
    status_cd varchar(4) not null,
    login_id varchar(50) not null unique,
    version int not null,
    created_date timestamp,
    modified_date timestamp
);
CREATE INDEX IDX_LOGIN_ID ON member(login_id);

CREATE TABLE IF NOT EXISTS keyword_search_count
(   id identity not null primary key,
    keyword varchar(200) not null unique,
    count bigint not null,
    created_date timestamp,
    modified_date timestamp
);
CREATE INDEX IDX_KEYWORD ON keyword_search_count(keyword);
CREATE INDEX IDX_COUNT_DESC ON keyword_search_count(count DESC);

CREATE TABLE IF NOT EXISTS member_keyword_history
(   id identity not null primary key,
    login_id varchar(50) not null,
    keyword varchar(200) not null,
    search_date varchar(10) not null,
    created_date timestamp,
    modified_date timestamp,
    unique key mbr_keyword_hist_uk (login_id, keyword, search_date)
);
CREATE INDEX IDX_LOGIND_ID_KEYWORD_DATE ON member_keyword_history(login_id, keyword, search_date);