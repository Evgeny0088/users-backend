create table if not exists department(
    id varchar(100) not null primary key default generate_ulid(),
    dep_name varchar(100) not null,
    created_at date not null default now()
);