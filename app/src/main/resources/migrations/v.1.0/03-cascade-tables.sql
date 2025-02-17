alter table employee
    drop constraint fk_department,
    add constraint fk_department
        foreign key (department_id)
        references department(id)
        on delete cascade;