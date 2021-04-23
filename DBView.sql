CREATE VIEW CustomerSummary AS
SELECT rental.customerId, date_out as rental_date_out, date_back as rental_date_back,
((julianday(date_back)-julianday(date_out)+1)*dailyCost)+((odo_back-odo_out)*kmCost) as rental_cost
FROM rental, Car, Vehicle
WHERE rental.VIN = Vehicle.VIN
AND (Vehicle.carMake,Vehicle.carModel,Vehicle.carYear)
= (Car.carMake,Car.carModel,Car.carYear)
AND date_back is not NULL;