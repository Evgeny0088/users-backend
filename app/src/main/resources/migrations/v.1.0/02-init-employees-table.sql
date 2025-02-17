create table if not exists employee(
    id varchar(100) not null primary key,
    emp_name varchar(100) not null,
    last_name varchar(100) not null,
    salary integer,
    created_at timestamp not null default current_timestamp,
    department_id varchar(100) not null,
    constraint fk_department
        foreign key (department_id)
            references department(id)
);