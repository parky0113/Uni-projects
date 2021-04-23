CREATE TABLE Car (
carMake TEXT,
carModel TEXT,
carYear TEXT,
dailyCost INTEGER,
kmCost REAL,
PRIMARY KEY (carMake, carModel, carYear)
);

CREATE TABLE Vehicle (
carMake TEXT,
carModel TEXT,
carYear TEXT,
VIN TEXT PRIMARY KEY
	CHECK(VIN glob '[0-9A-Z][0-9A-Z][X0-9][0-9A-Z][0-9A-Z]')
	CHECK(VIN glob '[^IOQ][^IOQ]?[^IOQ][^IOQ]'),
odometer INTEGER,
FOREIGN KEY (carMake, carModel, carYear) REFERENCES Car(carMake, carModel, carYear)
);

CREATE TABLE Customer (
id INTEGER PRIMARY KEY,
name TEXT,
email TEXT
);

CREATE TABLE rental (
customerId INTEGER,
VIN TEXT,
odo_out INTEGER,
odo_back INTEGER
	CHECK(odo_back >= odo_out),
date_out TEXT,
date_back TEXT
	CHECK(julianday(date_back) >= julianday(date_out)),
FOREIGN KEY (customerId) REFERENCES Customer(id)
	ON UPDATE CASCADE,
FOREIGN KEY (VIN) REFERENCES Vehicle(VIN)
);
