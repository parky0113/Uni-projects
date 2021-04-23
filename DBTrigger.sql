CREATE TRIGGER out_odoLogger 
AFTER INSERT ON rental
FOR EACH ROW
BEGIN
UPDATE rental SET odo_out = ((SELECT odometer FROM Vehicle,rental WHERE rental.VIN = NEW.VIN
							AND NEW.VIN = Vehicle.VIN)) WHERE odo_out IS NULL;
END;

CREATE TRIGGER back_odoLogger
AFTER UPDATE OF odo_back ON rental
FOR EACH ROW
WHEN OLD.odo_back is NULL
BEGIN
UPDATE Vehicle SET odometer = NEW.odo_back WHERE VIN = NEW.VIN;
END;
