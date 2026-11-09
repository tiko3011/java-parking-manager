public class Car {
    private final Plate plate;
    private final boolean isVip;

    Car(Plate plate, boolean isVip){
        if (plate == null) {
            throw new IllegalArgumentException("Plate cannot be null");
        }
        this.plate = plate;
        this.isVip = isVip;
    }

    // Getters
    public Plate getPlate() {
        return plate;
    }
    public boolean isVip(){
        return isVip;
    }

    // Override Object methods
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Car otherCar) {
            return this.plate.equals(otherCar.plate) && this.isVip == otherCar.isVip;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return plate.hashCode() + (isVip ? 1 : 0);
    }
    @Override
    public String toString() {
        return "Plate: " + plate.getPlate() + " | Status: " + (isVip ? "VIP" : "Regular");
    }

    // Enum for action results
    public enum CarActionResult {
        SUCCESS,
        PARKING_FULL,
        ALREADY_PARKED,
        PARKED,
        NOT_PARKED,
        REGULAR_CAR_NOT_ALLOWED,
        VIP_CAR_NOT_ALLOWED,
        INVALID_PLATE,
        NULL_CAR,
        UNKNOWN_CAR
    }
}