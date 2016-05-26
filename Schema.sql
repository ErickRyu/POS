create table sale(
	day	date	not null,
	sales	integer	not null,
	primary key(day)
);

create table menu(
	id	integer		not null,
	name	varchar(30)	not null,
	price	integer		not null,
	cumulitive	integer	not null,
	primary key(id, name)
);

create table customer(
	id	integer		not null,
	name	varchar(20)	not null,	
	birth	varchar(4)	not null,
	phone	varchar(4)	not null,
	grade_name	varchar(10) 'Normal',
	total_purchase	integer	0,
	primary key(id, name),
	foreign key(grade)
);

create table staff(
	id	integer		not null,
	name	varchar(20)	not null,
	position	varchar(20)	not null,
	sales	integer,
	primary key(id, name)
);

create table grade(
	grade_name	varchar(20)	not null,
	threshold	integer		not null,
	discount float		not null,
	primary key(grade)
);