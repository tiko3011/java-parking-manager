public class Plate {
    private final String plate;

    Plate(String plate) {
        if (isPlateValid(plate)){
            this.plate = plate.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid plate!");
        }
    }

    // Check if plate is valid
    public static boolean isPlateValid(String plate){
        if (plate == null || plate.isBlank()) return false;

        return plate.toUpperCase().matches("\\d{2}[A-Z]{2}\\d{3}")
                || plate.toUpperCase().matches("\\d{3}[A-Z]{2}\\d{2}");
    }

    // Getter
    public String getPlate() {
        return plate;
    }

    // Override Object methods
    @Override
    public int hashCode() {
        return plate.hashCode();
    }
    @Override
    public boolean equals(Object obj){
        if (obj instanceof Plate anotherPlate){
            return this.plate.equals(anotherPlate.plate);
        }
        return false;
    }
    @Override
    public String toString() {
        return plate;
    }
}
