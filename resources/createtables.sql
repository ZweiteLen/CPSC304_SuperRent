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
    foreign key (vlicense) references VEHICLES on delete cascade
);
grant select on rent to public;

create table returns(
                        rid char(20),
                        datetime timestamp,
                        odometer integer,
                        fulltank number(1), /*since it doesn't support boolean type*/
                        CONSTRAINT boolean_constraint CHECK (fulltank IN (1,0)),
                        value integer,
                        foreign key (rid) references rent on delete cascade
);
grant select on returns to public;