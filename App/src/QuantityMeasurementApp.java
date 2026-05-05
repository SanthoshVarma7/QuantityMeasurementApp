import org.junit.jupiter.api.Test; // [cite: 178]
import static org.junit.jupiter.api.Assertions.*; // [cite: 178]


public class QuantityMeasurementApp {


    public static class Feet {
        private final double value; // [cite: 38, 83]

        public Feet(double value) { // [cite: 40, 84]
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // 1. Reference Check: If both point to the same object [cite: 43, 87]
            if (this == obj) {
                return true;
            }

            // 2. Null Check: If the compared object is null [cite: 44, 88]
            // 3. Type Check: Ensure the object is of type Feet [cite: 44, 90]
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            // 4. Value Comparison: Use Double.compare() for precision [cite: 48, 92, 117]
            Feet feet = (Feet) obj; // [cite: 46]
            return Double.compare(feet.value, this.value) == 0;
        }
    }

    // Main method to demonstrate the equality check [cite: 109, 110]
    public static void main(String[] args) {
        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);

        boolean isEqual = feet1.equals(feet2);
        System.out.println("Input: 1.0 ft and 1.0 ft"); // [cite: 113]
        System.out.println("Output: Equal (" + isEqual + ")"); // [cite: 114]
    }
}


class QuantityMeasurementAppTest {

    @Test
    public void testFeetEquality_SameValue() { // [cite: 150, 178]
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(feet1.equals(feet2)); // [cite: 153]
    }

    @Test
    public void testFeetEquality_DifferentValue() { // [cite: 154, 178]
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        QuantityMeasurementApp.Feet feet2 = new QuantityMeasurementApp.Feet(2.0);
        assertFalse(feet1.equals(feet2)); // [cite: 157]
    }

    @Test
    public void testFeetEquality_NullComparison() { // [cite: 158, 178]
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(feet1.equals(null)); // [cite: 162]
    }

    @Test
    public void testFeetEquality_DifferentClass() { // [cite: 178]
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(feet1.equals(new Object())); // [cite: 138, 140]
    }

    @Test
    public void testFeetEquality_SameReference() { // [cite: 175, 178]
        QuantityMeasurementApp.Feet feet1 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(feet1.equals(feet1)); // [cite: 130]
    }
}