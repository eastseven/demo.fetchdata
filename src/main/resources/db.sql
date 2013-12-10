
create table jd_menu (
	menu_id int,
	menu_pid int,
	menu_seq int,
	menu_level int,
	menu_name varchar(32),
	menu_url varchar(1024)
) DEFAULT CHARSET=utf8;