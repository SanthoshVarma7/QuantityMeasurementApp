import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementApp {

    public enum LengthUnit {
        FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);

        private final double conversionFactor;
        LengthUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }
        public double getConversionFactor() { return conversionFactor; }
    }

    public static class Length {
        private final double value;
        private final LengthUnit unit;

        public Length(double value, LengthUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        public double getValue() { return value; }
        public LengthUnit getUnit() { return unit; }

        /**
         * UC7: Addition with Explicit Target Unit.
         * Adds two lengths and converts the sum to the specified target unit.
         */
        public static Length add(Length l1, Length l2, LengthUnit targetUnit) {
            if (l1 == null || l2 == null || targetUnit == null) {
                throw new IllegalArgumentException("Inputs and Target Unit cannot be null");
            }

            // Step 1: Convert both to base unit (Inches)
            double sumInInches = (l1.value * l1.unit.getConversionFactor()) +
                    (l2.value * l2.unit.getConversionFactor());

            // Step 2: Convert sum to target unit
            double convertedValue = sumInInches / targetUnit.getConversionFactor();

            // Step 3: Round to 3 decimal places for precision handling
            double roundedValue = Math.round(convertedValue * 1000.0) / 1000.0;
            return new Length(roundedValue, targetUnit);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length that = (Length) o;
            double thisInches = Math.round(this.value * this.unit.getConversionFactor() * 1000.0) / 1000.0;
            double thatInches = Math.round(that.value * that.unit.getConversionFactor() * 1000.0) / 1000.0;
            return Double.compare(thisInches, thatInches) == 0;
        }

        @Override
        public String toString() { return value + " " + unit; }
    }

    public static void main(String[] args) {
        Length ft = new Length(1.0, LengthUnit.FEET);
        Length in = new Length(12.0, LengthUnit.INCHES);

        // Example: 1ft + 12in with target unit YARDS
        Length result = Length.add(ft, in, LengthUnit.YARDS);
        System.out.println("Result: " + result); // Output: ~0.667 YARDS
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        // Add (1000.0 ft, 500.0 ft) with target unit INCHES -> 18000.0 INCHES
        QuantityMeasurementApp.Length l1 = new QuantityMeasurementApp.Length(1000.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length l2 = new QuantityMeasurementApp.Length(500.0, QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length result = QuantityMeasurementApp.Length.add(l1, l2, QuantityMeasurementApp.LengthUnit.INCHES);
        assertEquals(18000.0, result.getValue());
        assertEquals(QuantityMeasurementApp.LengthUnit.INCHES, result.getUnit());
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        // Add (12.0 in, 12.0 in) with target unit YARDS -> ~0.667 YARDS
        QuantityMeasurementApp.Length l1 = new QuantityMeasurementApp.Length(12.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.Length l2 = new QuantityMeasurementApp.Length(12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        QuantityMeasurementApp.Length result = QuantityMeasurementApp.Length.add(l1, l2, QuantityMeasurementApp.LengthUnit.YARDS);
        assertEquals(0.667, result.getValue());
    }

    @Test
    public void testAddition_WithCmToFeet() {
        // 30.48 cm + 30.48 cm = 60.96 cm. Result in FEET -> 2.0 FEET
        QuantityMeasurementApp.Length l1 = new QuantityMeasurementApp.Length(30.48, QuantityMeasurementApp.LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.Length l2 = new QuantityMeasurementApp.Length(30.48, QuantityMeasurementApp.LengthUnit.CENTIMETERS);

        QuantityMeasurementApp.Length result = QuantityMeasurementApp.Length.add(l1, l2, QuantityMeasurementApp.LengthUnit.FEET);
        assertEquals(2.0, result.getValue());
    }

    @Test
    public void testAddition_NullCheck() {
        assertThrows(IllegalArgumentException.class, () -> {
            QuantityMeasurementApp.Length.add(null, null, QuantityMeasurementApp.LengthUnit.FEET);
        });
    }
}