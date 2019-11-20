insert into vtype values('Car', 'car stuff', 1,1,1,1,1,1,1);
insert into vtype values('Truck', 'truck stuff', 2,2,2,2,2,2,2);
insert into vtype values('SUV', 'suv stuff', 3,3,3,3,3,3,3);


insert into vehicles values('1', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '1234 street', 'Burnaby');
insert into vehicles values('2', 'Honda', 'Something', 2018, 'black', 0, 'In maintenance', 'Truck', '1234 street', 'Richmond');
insert into vehicles values('3', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '1234 street', 'Burnaby');
insert into vehicles values('4', 'Honda', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '1234 street', 'Vancouver');
insert into vehicles values('5', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'Car', '1234 street', 'Vancouver');
insert into vehicles values('6', 'Honda', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '1234 street', 'Burnaby');
insert into vehicles values('7', 'Toyota', 'Something', 2018, 'black', 0, 'In maintenance', 'Car', '2345 street', 'Richmond');
insert into vehicles values('8', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '2345 street', 'Vancouver');
insert into vehicles values('9', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '1234 street', 'Richmond');
insert into vehicles values('10', 'Honda', 'Something', 2018, 'black', 0, 'In maintenance', 'Truck', '1234 street', 'Burnaby');
insert into vehicles values('11', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '1234 street', 'Burnaby');
insert into vehicles values('12', 'Honda', 'Something', 2018, 'black', 0, 'In maintenance', 'Truck', '1234 street', 'Richmond');
insert into vehicles values('13', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '2345 street', 'Burnaby');
insert into vehicles values('14', 'Honda', 'Something', 2018, 'black', 0, 'In maintenance', 'Car', '1234 street', 'Vancouver');
insert into vehicles values('15', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'Car', '1234 street', 'Vancouver');
insert into vehicles values('16', 'Honda', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '2345 street', 'Burnaby');
insert into vehicles values('17', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'Truck', '1234 street', 'Richmond');
insert into vehicles values('18', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'SUV', '3456 street', 'Vancouver');
insert into vehicles values('19', 'Toyota', 'Something', 2019, 'black', 0, 'In maintenance', 'Car', '3456 street', 'Richmond');
insert into vehicles values('20', 'Honda', 'Something', 2019, 'black', 0, 'In maintenance', 'Truck', '3456 street', 'Burnaby');


insert into reservation values(1, 'SUV', '50s8a2', TO_TIMESTAMP('2019-09-04 05:00:00', 'YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-10-04 15:00:00', 'YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(2, 'Truck', '6s3j72', TO_TIMESTAMP('2019-09-05 23:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(3, 'SUV', '04d872', TO_TIMESTAMP('2019-10-09 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-11-01 09:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(4, 'Truck', '04d872', TO_TIMESTAMP('2019-01-09 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-01-20 09:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(5, 'Car', '04d872', TO_TIMESTAMP('2019-05-23 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-06-01 09:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(6, 'Car', '04d872', TO_TIMESTAMP('2019-03-11 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-04-01 09:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(7, 'SUV', '04d872', TO_TIMESTAMP('2019-08-30 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-09-01 09:00:00','YYYY-MM-DD HH24:MI:SS'));
insert into reservation values(8, 'Truck', '04d872', TO_TIMESTAMP('2019-07-20 15:00:00','YYYY-MM-DD HH24:MI:SS'), TO_TIMESTAMP('2019-08-01 13:00:00','YYYY-MM-DD HH24:MI:SS'));


insert into rent values (1,1,'1','50s8a2', TO_TIMESTAMP('2019-09-04 05:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-10-04 15:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','54135123','2004');
insert into rent values (2,3,'3','04d872', TO_TIMESTAMP('2019-10-09 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-11-01 09:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','54135123','2004');
insert into rent values (3,7,'4','04d872', TO_TIMESTAMP('2019-08-30 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-01 09:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','54135123','2004');
insert into rent values (4,2,'2','6s3j72', TO_TIMESTAMP('2019-09-05 23:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-09-25 20:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','54135123','2004');
insert into rent values (5,4,'10','04d872', TO_TIMESTAMP('2019-01-09 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-01-20 09:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','54135123','2004');
insert into rent values (6,5,'5','04d872', TO_TIMESTAMP('2019-05-23 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-05-25 09:00:00','YYYY-MM-DD HH24:MI:SS'),0,'Visa','54135123','2004');
insert into rent values (7,8,'12','04d872', TO_TIMESTAMP('2019-05-23 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-05-25 09:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','54135123','2004');
insert into rent values (8,6,'7','6s3j72', TO_TIMESTAMP('2019-05-23 15:00:00','YYYY-MM-DD HH24:MI:SS'),
                         TO_TIMESTAMP('2019-05-25 09:00:00','YYYY-MM-DD HH24:MI:SS'),1,'Visa','54135123','2004');