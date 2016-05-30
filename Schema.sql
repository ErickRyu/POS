create table sale(day	date	not null, sales	integer	default 0, primary key(day)) 
create table orderstatus(table_num	integer	not null, menu_name	varchar(20), customer_name varchar(20), sales integer, total_price	integer)
create table menu( id	integer		not null, name	varchar(30)	not null,price	integer		not null, cumulitive	integer	default 0,primary key(name))
create table customer(id	integer		not null, name	varchar(20)	not null, birth	char(4)	not null,phone	char(4)	not null, grade	varchar(10) default 'Normal',total_purchase	integer default 0, primary key(name))
create table staff(id	integer		not null, name	varchar(20)	not null, position	varchar(20)	not null,sales	integer default 0, primary key(name))
create table grade(grade_name	varchar(20)	not null, threshold integer not null, discount float	not null,primary key(grade_name))