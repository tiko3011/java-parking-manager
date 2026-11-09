import java.util.*;

public class Parking {
    private final Map<Plate, Car> plateToCarMap;
    private final int maxCapacityRegular;
    private final int maxCapacityVip;
    private int availableSpacesRegular;
    private int availableSpacesVip;

    public Parking(int maxCapacityRegular, int maxCapacityVip){
        if (maxCapacityRegular <= 0 || maxCapacityVip <= 0) {
            throw new IllegalArgumentException("Max capacity must be greater than 0");
        }

        plateToCarMap = new HashMap<>();
        this.maxCapacityRegular = maxCapacityRegular;
        this.maxCapacityVip = maxCapacityVip;
        availableSpacesRegular = maxCapacityRegular;
        availableSpacesVip = maxCapacityVip;
    }

    // 1. Add a car to the parking lot
    public Car.CarActionResult addCarRegular(Car car){
        if (checkEdgeCasesAdding(car) != Car.CarActionResult.SUCCESS) return checkEdgeCasesAdding(car);
        if (car.isVip()) return Car.CarActionResult.VIP_CAR_NOT_ALLOWED;

        if (isCarParked(car) == Car.CarActionResult.NOT_PARKED){
            plateToCarMap.put(car.getPlate(), car);
            availableSpacesRegular--;
            return Car.CarActionResult.SUCCESS;
        }

        return Car.CarActionResult.ALREADY_PARKED;
    }
    public Car.CarActionResult addCarVip(Car car){
        if (checkEdgeCasesAdding(car) != Car.CarActionResult.SUCCESS) return checkEdgeCasesAdding(car);
        if (!car.isVip()) return Car.CarActionResult.REGULAR_CAR_NOT_ALLOWED;

        if (isCarParked(car) == Car.CarActionResult.NOT_PARKED){
            plateToCarMap.put(car.getPlate(), car);
            availableSpacesVip--;
            return Car.CarActionResult.SUCCESS;
        }

        return Car.CarActionResult.ALREADY_PARKED;
    }

    // 2. Remove a car from the parking lot
    public Car.CarActionResult removeCarRegular(Car car){
        if (checkEdgeCasesRemoving(car) != Car.CarActionResult.SUCCESS) return checkEdgeCasesRemoving(car);
        if (car.isVip()) return Car.CarActionResult.VIP_CAR_NOT_ALLOWED;

        Car.CarActionResult actionResult = isCarParked(car);
        if (actionResult == Car.CarActionResult.PARKED){
            plateToCarMap.remove(car.getPlate());
            availableSpacesRegular++;
            return Car.CarActionResult.SUCCESS;
        }

        return actionResult;
    }
    public Car.CarActionResult removeCarVip(Car car){
        if (checkEdgeCasesRemoving(car) != Car.CarActionResult.SUCCESS) return checkEdgeCasesRemoving(car);
        if (!car.isVip()) return Car.CarActionResult.REGULAR_CAR_NOT_ALLOWED;

        Car.CarActionResult actionResult = isCarParked(car);
        if (actionResult == Car.CarActionResult.PARKED){
            plateToCarMap.remove(car.getPlate());
            availableSpacesVip++;
            return Car.CarActionResult.SUCCESS;
        }

        return actionResult;
    }

    // 3. Display all currently parked cars
    public List<Car> getParkedCars(){
        return new ArrayList<>(plateToCarMap.values());
    }

    // Helper method to get a car by its plate
    public Car getCarByPlate(Plate plate){
        if (plate == null) return null;
        return plateToCarMap.get(plate);
    }

    // Check if a car is already parked
    public Car.CarActionResult isCarParked(Car car) {
        if (car == null) return Car.CarActionResult.NULL_CAR;
        if (plateToCarMap.containsValue(car)) return Car.CarActionResult.PARKED;
        if (plateToCarMap.containsKey(car.getPlate())) return Car.CarActionResult.UNKNOWN_CAR;

        return Car.CarActionResult.NOT_PARKED;
    }

    // Check edge cases
    public Car.CarActionResult checkEdgeCasesAdding(Car car){
        if (car == null) return Car.CarActionResult.NULL_CAR;
        if (plateToCarMap.containsKey(car.getPlate())) return Car.CarActionResult.ALREADY_PARKED;
        if (car.isVip() && availableSpacesVip <= 0) return Car.CarActionResult.PARKING_FULL;
        if (!car.isVip() && availableSpacesRegular <= 0) return Car.CarActionResult.PARKING_FULL;

        return Car.CarActionResult.SUCCESS;
    }
    public Car.CarActionResult checkEdgeCasesRemoving(Car car){
        if (car == null) return Car.CarActionResult.NULL_CAR;
        if (!plateToCarMap.containsKey(car.getPlate())) return Car.CarActionResult.NOT_PARKED;
        if (!plateToCarMap.containsValue(car)) return Car.CarActionResult.UNKNOWN_CAR;

        return Car.CarActionResult.SUCCESS;
    }

    // Getters Regular
    public int getMaxCapacityRegular() {
        return maxCapacityRegular;
    }
    public int getOccupiedSpacesRegular() {
        return maxCapacityRegular - availableSpacesRegular;
    }
    public int getAvailableSpacesRegular() {
        return availableSpacesRegular;
    }

    // Getters VIP
    public int getMaxCapacityVip() {
        return maxCapacityVip;
    }
    public int getAvailableSpacesVip() {
        return availableSpacesVip;
    }
    public int getOccupiedSpacesVip() {
        return maxCapacityVip - availableSpacesVip;
    }
}