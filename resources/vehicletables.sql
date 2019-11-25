drop table returns;
drop table rent;
drop table reservation;
drop table vehicles;
drop table vtype;
drop table customers;

create table customers (
                           cellphone char(20),
                           name char(20),
                           address char(20),
                           dlicense char(20) not null,
                           primary key (dlicense)
);
grant select on customers to public;

create table vtype (
                       vtname char(20) not null,
                       features char(50),
                       wrate integer,
                       drate integer,
                       hrate integer,
                       wirate integer,
                       dirate integer,
                       hirate integer,
                       krate integer,
                       primary key (vtname)
);
grant select on vtype to public;

create table vehicles (
                          vlicense char(20) not null,
                          make char(20) not null,
                          model char(20) not null,
                          year integer not null,
                          colour char(10),
                          odometer integer,
                          status char(20) not null,
                          vtname char(20) not null,
                          location char(20) not null,
                          city char(20) not null,
                          primary key (vlicense),
                          foreign key (vtname) references vtype ON DELETE CASCADE
);
grant select on vehicles to public;

create table reservation (
                             confNo integer not null,
                             vtname char(20) not null,
                             dlicense char(20) not null,
                             fromDateTime timestamp,
                             toDateTime timestamp,
                             primary key (confNo),
                             foreign key (dlicense) references customers ON DELETE CASCADE,
                             foreign key (vtname) references vtype ON DELETE CASCADE
);
grant select on reservation to public;

create table rent (
                      rid integer not null,
                      confNo integer not null,
                      vlicense char(20) not null,
                      dlicense char(20) not null,
                      fromDateTime timestamp,
                      toDateTime timestamp,
                      odometer integer,
                      cardName char(20),
                      cardNo char(20),
                      expDate char(4),
                      primary key (rid),
                      foreign key (confNo) references RESERVATION,
                      foreign key (dlicense) references customers ON DELETE CASCADE,
                      foreign key (vlicense) references VEHICLES on delete cascade
);
grant select on rent to public;

create table returns(
                        rid integer NOT NULL,
                        datetime timestamp,
                        odometer integer,
                        fulltank number(1), /*since it doesn't support boolean type*/
                        CONSTRAINT boolean_constraint CHECK (fulltank IN (1,0)),
                        value integer,
                        foreign key (rid) references rent on delete cascade
);
grant select on returns to public;

insert into customers values('67738292736', 'Andy Smith', 'Vancouver', '50s8a2');
insert into customers values('87745334764', 'John Williams', 'Vancouver', '04d872');
insert into customers values('98304800402', 'Milly hosking', 'Burnaby', '93eb67');
insert into customers values('29443924998', 'Lennon White', 'Victoria', '6s3j72');
insert into customers values('49247927492', 'Eric Wong', 'Richmond', '05ejda');
insert into customers values('39294892849', 'Violet Johnson', 'Vancouver', '73hwc8');
insert into customers values('34204024042', 'Ricky Cheung', 'Burnaby', 'we35u7');
insert into customers values('34927424924', 'Harry Green', 'Vancouver', '83ry92');
insert into customers values('09583576448', 'Tom Smith', 'Richmond', 'r384wt');
insert into customers values('34294829489', 'Jerry Lam', 'Victora', 'a3jsd7');

insert into vtype values('Economy', 'Economy stuff', 7,6,5,4,3,2,1);
insert into vtype values('Compact', 'Compact stuff', 8,7,6,5,4,3,2);
insert into vtype values('Mid-size', 'Mid-size stuff', 9,7,6,5,5,4,3);
insert into vtype values('Standard', 'Standard stuff', 10,7,6,7,6,5,4);
insert into vtype values('Full-size', 'Full-size stuff', 8,7,5,4,4,3,2);
insert into vtype values('Truck', 'truck stuff', 10,9,8,7,6,5,4);
insert into vtype values('SUV', 'SUV stuff', 9,8,7,6,5,4,3);


insert into vehicles values('1', 'Toyota', 'Corolla', 2019, 'black', 0, 'rented', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('2', 'Honda', 'Avancier', 2018, 'yellow', 0, 'rented', 'SUV', '1234 street', 'Richmond');
insert into vehicles values('3', 'Toyota', 'Avalon', 2019, 'yellow', 0, 'rented', 'Full-size', '5544 street', 'Burnaby');
insert into vehicles values('4', 'Honda', 'Avancier', 2019, 'black', 0, 'rented', 'SUV', '1234 street', 'Vancouver');
insert into vehicles values('5', 'BMW', 'M6 Gran Coupe', 2019, 'blue', 0, 'rented', 'Truck', '8080 street', 'Vancouver');
insert into vehicles values('6', 'Honda', 'Ballade', 2019, 'black', 0, 'In maintenance', 'Mid-size', '1234 street', 'Burnaby');
insert into vehicles values('7', 'Nissan', 'LEAF', 2018, 'black', 0, 'rented', 'Compact', '5544 street', 'Richmond');
insert into vehicles values('8', 'Nissan', 'Altima', 2019, 'orange', 0, 'rented', 'SUV', '2345 street', 'Vancouver');
insert into vehicles values('9', 'Nissan', 'Altima', 2019, 'orange', 0, 'available', 'SUV', '1234 street', 'Richmond');
insert into vehicles values('10', 'Nissan', 'LEAF', 2018, 'black', 0, 'rented', 'Compact', '5013 street', 'Burnaby');
insert into vehicles values('11', 'Toyota', 'Corolla', 2019, 'black', 0, 'available', 'Economy', '5013 street', 'Burnaby');
insert into vehicles values('12', 'Honda', 'Ballade', 2018, 'purple', 0, 'rented', 'Mid-size', '5013 street', 'Richmond');
insert into vehicles values('13', 'Volkswagen', 'Arteon', 2019, 'black', 0, 'available', 'Compact', '2345 street', 'Burnaby');
insert into vehicles values('14', 'Volkswagen', 'Arteon', 2018, 'black', 0, 'available', 'Compact', '1234 street', 'Vancouver');
insert into vehicles values('15', 'BMW', 'M6 Gran Coupe', 2019, 'black', 0, 'available', 'Truck', '5544 street', 'Vancouver');
insert into vehicles values('16', 'Honda', 'Ballade', 2019, 'red', 0, 'rented', 'SUV', '4399 street', 'Burnaby');
insert into vehicles values('17', 'Toyota', 'Avalon', 2019, 'black', 0, 'In maintenance', 'Full-size', '1234 street', 'Richmond');
insert into vehicles values('18', 'Toyota', 'Avalon', 2019, 'black', 0, 'available', 'Full-size', '3456 street', 'Vancouver');
insert into vehicles values('19', 'Volkswagen', 'Atlas', 2019, 'green', 0, 'available', 'Standard', '1010 street', 'Richmond');
insert into vehicles values('20', 'Volkswagen', 'Atlas', 2019, 'green', 0, 'available', 'Standard', '3456 street', 'Burnaby');
insert into vehicles values('21', 'Mercedes Benz', 'C300', 2019, 'black', 0, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('22', 'Mercedes Benz', 'C300', 2018, 'red', 1, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('23', 'Mercedes Benz', 'C300', 2017, 'silver', 5, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('24', 'Mercedes Benz', 'C300', 2016, 'blue', 6, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('25', 'Mercedes Benz', 'C300', 2015, 'dark blue', 3, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('26', 'Mercedes Benz', 'C300', 2014, 'white', 7, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('27', 'Mercedes Benz', 'C300', 2011, 'black', 0, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('28', 'Mercedes Benz', 'C300', 2018, 'red', 10, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('29', 'Mercedes Benz', 'C300', 2017, 'gold', 50000, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('30', 'Mercedes Benz', 'C300', 2016, 'black', 2243, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('31', 'Mercedes Benz', 'C300', 2015, 'dark blue', 0, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('32', 'Mercedes Benz', 'C300', 2014, 'grey', 21509, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('33', 'Mercedes Benz', 'C300', 2011, 'black', 0, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('34', 'Mercedes Benz', 'C300', 2018, 'red', 10, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('35', 'Mercedes Benz', 'C300', 2017, 'gold', 50000, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('36', 'Mercedes Benz', 'C300', 2016, 'black', 2243, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('37', 'Mercedes Benz', 'C300', 2015, 'dark blue', 0, 'available', 'Economy', '8080 street', 'Burnaby');
insert into vehicles values('38', 'Mercedes Benz', 'C300', 2014, 'grey', 21509, 'available', 'Economy', '8080 street', 'Burnaby');

insert into reservation values(1, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-04 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-04 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(2, 'SUV', '6s3j72', TO_TIMESTAMP('2019-09-05 23:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(3, 'Full-size', '04d872', TO_TIMESTAMP('2019-10-09 12:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-11-01 11:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(4, 'Compact', '05ejda', TO_TIMESTAMP('2019-01-09 20:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-01-20 10:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(5, 'Truck', 'a3jsd7', TO_TIMESTAMP('2019-05-23 14:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-01 20:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(6, 'Compact', 'we35u7', TO_TIMESTAMP('2019-03-11 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-04-01 15:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(7, 'SUV', '93eb67', TO_TIMESTAMP('2019-08-30 08:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-09-01 22:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(8, 'Mid-size', '73hwc8', TO_TIMESTAMP('2019-07-20 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(9, 'Compact', 'r384wt', TO_TIMESTAMP('2019-12-05 09:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-12-19 21:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(10, 'SUV', '83ry92', TO_TIMESTAMP('2019-09-15 10:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-09-17 22:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(11, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-04 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-04 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(12, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-05 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-05 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(13, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-06 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-06 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(14, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-07 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-07 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(15, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-08 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-08 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(16, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-09 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-09 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(17, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-10 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-10 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(18, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-11 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-11 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(19, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-12 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-12 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(20, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-13 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-13 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(21, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-14 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-14 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(22, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-15 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-15 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(23, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-16 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-16 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(24, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-17 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-17 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(25, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-18 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-18 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(26, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-19 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-19 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(27, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-20 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-20 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(28, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-21 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-21 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(29, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-20 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-20 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(30, 'Economy', '50s8a2', TO_TIMESTAMP('2019-09-21 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-21 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));

insert into rent values (1,1,'1','50s8a2', TO_TIMESTAMP('2019-09-04 05:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-10-04 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51135123','2004');
insert into rent values (2,3,'3','04d872', TO_TIMESTAMP('2019-10-09 12:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-11-01 11:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','42139823','2004');
insert into rent values (3,7,'4','93eb67', TO_TIMESTAMP('2019-08-30 08:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-01 22:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','43885123','2004');
insert into rent values (4,2,'2','6s3j72', TO_TIMESTAMP('2019-09-05 23:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','55178123','2004');
insert into rent values (5,4,'10','05ejda', TO_TIMESTAMP('2019-01-09 20:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-01-20 10:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','55135109','2004');
insert into rent values (6,5,'5','a3jsd7', TO_TIMESTAMP('2019-05-23 14:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-06-01 20:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','46775123','2004');
insert into rent values (7,8,'12','73hwc8', TO_TIMESTAMP('2019-07-20 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','49178123','2004');
insert into rent values (8,6,'7','we35u7', TO_TIMESTAMP('2019-03-11 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-04-01 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51189973','2004');
insert into rent values (9,10,'8','83ry92', TO_TIMESTAMP('2019-09-15 10:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-17 22:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51189973','2004');
insert into rent values (10,9,'16','r384wt', TO_TIMESTAMP('2019-09-15 09:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-12-19 21:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','48263942','2004');
insert into rent values (11,10,'1','50s8a2', TO_TIMESTAMP('2019-09-15 05:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-10-04 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51135123','2004');
insert into rent values (12,11,'21','50s8a2', TO_TIMESTAMP('2019-09-15 05:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-10-04 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51135123','2004');
insert into rent values (13,12,'22','50s8a2', TO_TIMESTAMP('2019-09-15 12:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-11-01 11:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','42139823','2004');
insert into rent values (14,13,'23','50s8a2', TO_TIMESTAMP('2019-09-15 08:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-01 22:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','43885123','2004');
insert into rent values (15,14,'24','50s8a2', TO_TIMESTAMP('2019-09-15 23:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','55178123','2004');
insert into rent values (16,15,'25','50s8a2', TO_TIMESTAMP('2019-09-15 20:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-01-20 10:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','55135109','2004');
insert into rent values (17,16,'26','50s8a2', TO_TIMESTAMP('2019-09-15 14:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-06-01 20:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','46775123','2004');
insert into rent values (18,17,'27','50s8a2', TO_TIMESTAMP('2019-09-15 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','49178123','2004');
insert into rent values (19,18,'28','50s8a2', TO_TIMESTAMP('2019-09-15 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-04-01 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51189973','2004');
insert into rent values (20,19,'29','50s8a2', TO_TIMESTAMP('2019-09-15 10:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-17 22:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51189973','2004');
insert into rent values (21,20,'30','50s8a2', TO_TIMESTAMP('2019-09-15 09:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-12-19 21:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','48263942','2004');
insert into rent values (22,21,'31','50s8a2', TO_TIMESTAMP('2019-09-15 05:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-10-04 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51135123','2004');
insert into rent values (23,22,'23','50s8a2', TO_TIMESTAMP('2019-09-15 08:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-01 22:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','43885123','2004');
insert into rent values (24,23,'24','50s8a2', TO_TIMESTAMP('2019-09-15 23:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','55178123','2004');
insert into rent values (25,24,'25','50s8a2', TO_TIMESTAMP('2019-09-15 20:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-01-20 10:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','55135109','2004');
insert into rent values (26,25,'26','50s8a2', TO_TIMESTAMP('2019-09-15 14:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-06-01 20:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','46775123','2004');
insert into rent values (27,26,'27','50s8a2', TO_TIMESTAMP('2019-09-15 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','49178123','2004');
insert into rent values (28,27,'28','50s8a2', TO_TIMESTAMP('2019-09-15 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-04-01 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51189973','2004');
insert into rent values (29,28,'29','50s8a2', TO_TIMESTAMP('2019-09-15 10:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-17 22:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51189973','2004');
insert into rent values (30,29,'30','50s8a2', TO_TIMESTAMP('2019-09-15 09:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-12-19 21:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','48263942','2004');
insert into rent values (31,30,'31','50s8a2', TO_TIMESTAMP('2019-09-15 05:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-10-04 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Mastercard','51135123','2004');

insert into returns values (1, TO_TIMESTAMP('2019-10-04 14:00:00','YYYY-MM-DD HH24:MI:SS'), 43493, 1, 132 ); /*value = wrate *4 weeks+ drate *2days + ... */
insert into returns values (2, TO_TIMESTAMP('2019-11-01 11:00:00','YYYY-MM-DD HH24:MI:SS'), 34944, 0, 231 );
insert into returns values (3, TO_TIMESTAMP('2019-09-01 22:00:00','YYYY-MM-DD HH24:MI:SS'), 34248, 1, 180 );
insert into returns values (4, TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'), 45353, 0, 326 );
insert into returns values (5, TO_TIMESTAMP('2019-01-20 10:00:00','YYYY-MM-DD HH24:MI:SS'), 95893, 1, 172 );
insert into returns values (6, TO_TIMESTAMP('2019-06-01 20:00:00','YYYY-MM-DD HH24:MI:SS'), 43358, 0, 202 );
insert into returns values (7, TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'), 45854, 1, 282 );
insert into returns values (8, TO_TIMESTAMP('2019-04-01 09:00:00','YYYY-MM-DD HH24:MI:SS'), 59648, 0, 42 );
insert into returns values (9, TO_TIMESTAMP('2019-09-17 22:00:00','YYYY-MM-DD HH24:MI:SS'), 44657, 1, 158 );
insert into returns values (10, TO_TIMESTAMP('2019-08-01 21:00:00','YYYY-MM-DD HH24:MI:SS'), 78647, 1, 134 );
insert into returns values (11, TO_TIMESTAMP('2019-08-01 14:00:00','YYYY-MM-DD HH24:MI:SS'), 43493, 1, 132 ); /*value = wrate *4 weeks+ drate *2days + ... */
insert into returns values (12, TO_TIMESTAMP('2019-08-01 11:00:00','YYYY-MM-DD HH24:MI:SS'), 34944, 0, 231 );
insert into returns values (13, TO_TIMESTAMP('2019-08-01 22:00:00','YYYY-MM-DD HH24:MI:SS'), 34248, 1, 180 );
insert into returns values (14, TO_TIMESTAMP('2019-08-01 20:00:00','YYYY-MM-DD HH24:MI:SS'), 45353, 0, 326 );
insert into returns values (15, TO_TIMESTAMP('2019-08-01 10:00:00','YYYY-MM-DD HH24:MI:SS'), 95893, 1, 172 );
insert into returns values (16, TO_TIMESTAMP('2019-08-01 20:00:00','YYYY-MM-DD HH24:MI:SS'), 43358, 0, 202 );
insert into returns values (17, TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'), 45854, 1, 282 );
insert into returns values (18, TO_TIMESTAMP('2019-08-01 09:00:00','YYYY-MM-DD HH24:MI:SS'), 59648, 0, 42 );
insert into returns values (19, TO_TIMESTAMP('2019-08-01 22:00:00','YYYY-MM-DD HH24:MI:SS'), 44657, 1, 158 );
insert into returns values (20, TO_TIMESTAMP('2019-08-01 21:00:00','YYYY-MM-DD HH24:MI:SS'), 78647, 1, 134 );
insert into returns values (21, TO_TIMESTAMP('2019-08-01 10:00:00','YYYY-MM-DD HH24:MI:SS'), 95893, 1, 172 );
insert into returns values (22, TO_TIMESTAMP('2019-08-01 20:00:00','YYYY-MM-DD HH24:MI:SS'), 43358, 0, 202 );
insert into returns values (23, TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'), 45854, 1, 282 );
insert into returns values (24, TO_TIMESTAMP('2019-08-01 09:00:00','YYYY-MM-DD HH24:MI:SS'), 59648, 0, 42 );
insert into returns values (25, TO_TIMESTAMP('2019-08-01 22:00:00','YYYY-MM-DD HH24:MI:SS'), 44657, 1, 158 );
insert into returns values (26, TO_TIMESTAMP('2019-08-01 21:00:00','YYYY-MM-DD HH24:MI:SS'), 78647, 1, 134 );