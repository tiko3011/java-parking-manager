import java.util.InputMismatchException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("\n### Parking Garage Manager ###");

            // Input max capacity for regular and VIP cars
            System.out.print("Enter the maximum capacity for regular cars: ");
            int maxCapacityRegular = inputMaxCapacity(scanner);
            System.out.print("Enter the maximum capacity for VIP cars: ");
            int maxCapacityVip = inputMaxCapacity(scanner);

            // Create a parking and start the menu loop
            Parking parking = new Parking(maxCapacityRegular, maxCapacityVip);
            System.out.println("Parking Garage Created!");
            showMenu();
            int choice;

            while ((choice = makeChoice(scanner)) != 5) {
                switch (choice) {
                    case 1:
                        // Add a car
                        addCar(scanner, parking);
                        break;
                    case 2:
                        // Remove a car
                        removeCar(scanner, parking);
                        break;
                    case 3:
                        // Display all parked cars
                        displayParkedCars(parking);
                        break;
                    case 4:
                        // Display parking lot status
                        displayParkingStatus(parking);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                showMenu();
            }

            System.out.println("Exiting the Parking Garage Manager. Goodbye!");
        } catch (NoSuchElementException e) {
            System.out.println("Error: No input provided. Exiting the program.");
        }
    }

    // For creating a parking
    public static int inputMaxCapacity(Scanner scanner){
        int maxCapacity = 0;
        boolean invalidInput = true;
        while (invalidInput) {
            try {
                maxCapacity = scanner.nextInt();
                if (maxCapacity <= 0) {
                    System.out.print("Invalid number!\nEnter a valid max capacity: ");
                } else {
                    invalidInput = false;
                    return maxCapacity;
                }
            } catch (InputMismatchException e) {
                System.out.print("Invalid input!\nEnter a valid max capacity: ");
                scanner.next(); // Clear the invalid input
            } finally {
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
        }

        return maxCapacity;
    }

    // For creating a car
    public static Car createCar(Scanner scanner){
        Plate plate = inputCarPlate(scanner);
        boolean isVip = inputCarVipStatus(scanner);
        return new Car(plate, isVip);
    }
    public static Plate inputCarPlate(Scanner scanner){
        System.out.print("Enter the plate of the car: ");

        while (true){
            String plate = scanner.nextLine();
            if(Plate.isPlateValid(plate)){
                return new Plate(plate);
            } else {
                System.out.print("Invalid plate!\nEnter a valid plate: ");
            }
        }
    }
    public static boolean inputCarVipStatus(Scanner scanner){
        System.out.print("Is the car VIP? (yes/no): ");

        while (true){
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes")) {
                return true;
            } else if (input.equals("no")) {
                return false;
            } else {
                System.out.print("Invalid input! Please enter 'yes' or 'no': ");
            }
        }
    }

    // Show menu and make choice
    public static void showMenu(){
        System.out.print
                ("""
                        \n### Menu ###
                        1. Add a car
                        2. Remove a car
                        3. Display all parked cars
                        4. Display parking lot status
                        5. Exit
                        \nChoose an option:\s""");
    }
    public static int makeChoice(Scanner scanner){
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            return -1;
        } finally {
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
        }
    }

    // Add car to parking lot
    public static void addCar(Scanner scanner, Parking parking){
        Car car = createCar(scanner);
        Car.CarActionResult isActionCompleted = car.isVip() ? parking.addCarVip(car) : parking.addCarRegular(car);

        switch (isActionCompleted) {
            case SUCCESS -> System.out.println("Car with plate " + car.getPlate() +
                    "\nhas been added to the parking lot.");
            case PARKING_FULL -> System.out.println("Parking lot is full for " +
                    (car.isVip() ? "VIP" : "regular") + " cars.");
            case ALREADY_PARKED -> System.out.println("Car with plate " + car.getPlate() +
                    "\nis already parked in the parking lot.");
            case REGULAR_CAR_NOT_ALLOWED -> System.out.println("Car with plate " + car.getPlate() +
                    "\nis a regular car and cannot be added to the VIP section.");
            case VIP_CAR_NOT_ALLOWED -> System.out.println("Car with plate " + car.getPlate() +
                    "\nis a VIP car and cannot be added to the regular section.");
            case NULL_CAR -> System.out.println("Car cannot be null.");
            default -> System.out.println("Failed to add car with plate " + car.getPlate());
        }
    }

    // Remove car from parking lot
    public static void removeCar(Scanner scanner, Parking parking){
        Plate plate = inputCarPlate(scanner);
        Car car = parking.getCarByPlate(plate);
        if (car == null) {
            System.out.println("Car with plate " + plate + " is not parked in the parking lot.");
            return;
        }

        Car.CarActionResult actionResult = car.isVip() ? parking.removeCarVip(car) : parking.removeCarRegular(car);

        switch (actionResult){
            case SUCCESS -> System.out.println("Car with plate " + car.getPlate() +
                    "\nhas been removed from the parking lot.");
            case NOT_PARKED -> System.out.println("Car with plate " + car.getPlate() +
                    "\nis not parked in the parking lot.");
            case REGULAR_CAR_NOT_ALLOWED -> System.out.println("Car with plate " + car.getPlate() +
                    "\nis a regular car and cannot be removed from the VIP section.");
            case VIP_CAR_NOT_ALLOWED -> System.out.println("Car with plate " + car.getPlate() +
                    "\nis a VIP car and cannot be removed from the regular section.");
            case NULL_CAR -> System.out.println("Car cannot be null.");
            default -> System.out.println("Failed to remove car with plate " + car.getPlate() +
                    "\nIt may not be parked or there was an error.");
        }
    }

    // Display all parked cars
    public static void displayParkedCars(Parking parking){
        List<Car> cars = parking.getParkedCars();
        if (cars == null || cars.isEmpty()){
            System.out.println("No cars are currently parked.");
        } else {
            System.out.println("\n###################");
            System.out.println("Currently parked cars:");
            for (Car car: cars) {
                System.out.println(car);
            }
            System.out.println("###################");
        }
    }

    // Display parking lot status
    public static void displayParkingStatus(Parking parking){
        System.out.println(
                "##############################" +
                "\n##### Parking Lot Status #####" +
                "\nTotal Capacity: " + (parking.getMaxCapacityRegular() + parking.getMaxCapacityVip()) +
                "\nTotal Available Spaces: " + (parking.getAvailableSpacesRegular() + parking.getAvailableSpacesVip()) +
                "\nTotal Occupied Spaces: " + (parking.getOccupiedSpacesRegular() + parking.getOccupiedSpacesVip()) +
                "\n\nMax Capacity (Regular): " + parking.getMaxCapacityRegular() +
                "\nAvailable Spaces (Regular): " + parking.getAvailableSpacesRegular() +
                "\nOccupied Spaces (Regular): " + parking.getOccupiedSpacesRegular() +
                "\n\nMax Capacity (VIP): " + parking.getMaxCapacityVip() +
                "\nAvailable Spaces (VIP): " + parking.getAvailableSpacesVip() +
                "\nOccupied Spaces (VIP): " + parking.getOccupiedSpacesVip() +
                "##############################");

    }
}
