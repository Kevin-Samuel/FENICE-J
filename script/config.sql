create database fenice;
USE fenice;
create table `t_account`
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Faccount` VARCHAR(255) NOT NULL UNIQUE,
	`Fname` VARCHAR(255) NOT NULL,
	`Fpasswd` VARCHAR(255) NOT NULL,
	`Faccount_type` VARCHAR(64) NOT NULL,
	`Fbalance` DOUBLE(25, 2) NOT NULL default 0,
	PRIMARY KEY (`Fid`, `Faccount`),
	INDEX (Faccount)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000000;

insert into `t_account` (`Faccount`, `Fname`, `Fpasswd`, `Faccount_type`, `Fbalance`) values ('000001', 'fenicesun', '930709', 'ALL', '1000000');

create table `t_security_holding`
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Faccount` VARCHAR(128) NOT NULL,
	`Fsecurity_id` VARCHAR(128) NOT NULL,
	`Fquantity` INT NOT NULL default 0,
	PRIMARY KEY (`Fid`),
	UNIQUE(`Faccount`, `Fsecurity_id`),
	INDEX (`Faccount`),
	INDEX (`Fsecurity_id`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000000;

insert into `t_security_holding` (`Faccount`, `Fsecurity_id`, `Fquantity`) values('000001', 'FENICE', '100000');

create table `t_order` 
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Forder_id` VARCHAR(255) NOT NULL UNIQUE,
	`Fsecurity_id` VARCHAR(255) NOT NULL,
	`Fentrust_type` VARCHAR(64) NOT NULL default 'LIMIT',
	`Ftrade_direction` VARCHAR(64) NOT NULL,
	`Fprice` DOUBLE(10, 2) default '0',
	`Fquantity` INT NOT NULL default '0',
	`Forder_time` BIGINT default null,
	`Fstatus` VARCHAR(64) NOT NULL default 'delegating',
	PRIMARY KEY (`Fid`, `Forder_id`),
	INDEX (`Forder_id`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000000;

#status: agreed, delegating, canceled, invalid

create table `t_account_order_relation` 
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Faccount` VARCHAR(255) NOT NULL,
	`Forder_id` VARCHAR(255) NOT NULL,
	PRIMARY KEY(`Fid`),
	INDEX (`Faccount`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000000;

create table `t_security`
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Fsecurity_id` VARCHAR(255) NOT NULL UNIQUE,
	`Fsecurity_name` VARCHAR(255) NOT NULL UNIQUE,
	`Fshares` INT NOT NULL default 0,
	`Fvalue` DOUBLE(25, 2) NOT NULL default 0,
	`Fprice` DOUBLE(10, 2) NOT NULL default 0,
	`Fnet_asset` DOUBLE(10, 2) NOT NULL default 0,
	PRIMARY KEY (`Fid`, `Fsecurity_id`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000;

INSERT INTO `t_security`(`Fsecurity_id`, `Fsecurity_name`, `Fshares`, `Fvalue`, `Fprice`, `Fnet_asset`) VALUES('MORGAN', 'MORGANSTANLEY', 250000000, 2500000000, 10, 5.6);

create table `t_clear_and_delivery`
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Fbid_order_id` VARCHAR(255) NOT NULL,
	`Fask_order_id` VARCHAR(255) NOT NULL,
	`Fstatus`VARCHAR(64) NOT NULL default 'agreed',
	`Fdeal_time` BIGINT default null,
	`Fprice` DOUBLE(10, 2) NOT NULL default 0,
	`Fquantity` DOUBLE(10, 2) NOT NULL default 0,
	PRIMARY KEY(`Fid`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000000;

create table `t_deal_record` 
(
	`Fid` INT NOT NULL UNIQUE AUTO_INCREMENT,
	`Faccount` VARCHAR(255) NOT NULL,
	`Ftrade_direction` VARCHAR(64) NOT NULL,
	`Fsecurity_id` VARCHAR(255) NOT NULL,
	`Fprice` DOUBLE(10, 2) NOT NULL default 0,
	`Fquantity` DOUBLE(10, 2) NOT NULL default 0,
	`Fdeal_time` BIGINT default null,
	`Fstatus`VARCHAR(64) NOT NULL default 'agreed',
	PRIMARY KEY(`Fid`)
)ENGINE=MyISAM DEFAULT CHARSET=utf8 AUTO_INCREMENT=10000000;