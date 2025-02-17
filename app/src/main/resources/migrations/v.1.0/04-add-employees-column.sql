alter table employee
add column email varchar(255) unique not null,
add column birthday date not null