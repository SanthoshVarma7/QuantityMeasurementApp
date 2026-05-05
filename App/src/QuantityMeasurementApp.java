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

        /**
         * UC6: Addition of two lengths.
         * Normalizes both to inches, adds them, and converts back to the unit of this instance.
         */
        public Length add(Length thatLength) {
            if (thatLength == null) {
                throw new IllegalArgumentException("Operand cannot be null"); //
            }
            double totalInches = (this.value * this.unit.getConversionFactor()) +
                    (thatLength.value * thatLength.unit.getConversionFactor()); //

            double convertedValue = totalInches / this.unit.getConversionFactor(); // Result in first operand's unit
            return new Length(Math.round(convertedValue * 100.0) / 100.0, this.unit); // Rounded to 2 decimal places
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length that = (Length) o;
            double thisInches = Math.round(this.value * this.unit.getConversionFactor() * 100.0) / 100.0;
            double thatInches = Math.round(that.value * that.unit.getConversionFactor() * 100.0) / 100.0;
            return Double.compare(thisInches, thatInches) == 0;
        }

        @Override
        public String toString() { return value + " " + unit; }
    }

    // API Helper for demonstration[cite: 6]
    public static Length demonstrateLengthAddition(Length l1, Length l2) {
        return l1.add(l2);
    }

    public static boolean demonstrateLengthEquality(Length l1, Length l2) {
        return l1.equals(l2);
    }
}

class QuantityMeasurementAppTest {
    @Test
    public void testAddFeetAndInches() {
        // 1.0 FEET + 12.0 INCHES = 2.0 FEET[cite: 6]
        QuantityMeasurementApp.Length length1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length length2 = new QuantityMeasurementApp.Length(12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        QuantityMeasurementApp.Length sum = QuantityMeasurementApp.demonstrateLengthAddition(length1, length2);
        QuantityMeasurementApp.Length expected = new QuantityMeasurementApp.Length(2.0, QuantityMeasurementApp.LengthUnit.FEET);

        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(sum, expected)); //[cite: 6]
    }

    @Test
    public void testAddInchesAndFeet() {
        // 12.0 INCHES + 1.0 FEET = 24.0 INCHES[cite: 6]
        QuantityMeasurementApp.Length length1 = new QuantityMeasurementApp.Length(12.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.Length length2 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);

        QuantityMeasurementApp.Length sum = length1.add(length2);
        assertEquals(24.0, sum.add(new QuantityMeasurementApp.Length(0, QuantityMeasurementApp.LengthUnit.INCHES)).add(new QuantityMeasurementApp.Length(0, QuantityMeasurementApp.LengthUnit.INCHES)).equals(new QuantityMeasurementApp.Length(24.0, QuantityMeasurementApp.LengthUnit.INCHES)) ? 24.0 : 0);
    }

    @Test
    public void testAdditionCommutativity() {
        // add(A, B) should represent the same physical length as add(B, A)[cite: 6]
        QuantityMeasurementApp.Length a = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length b = new QuantityMeasurementApp.Length(12.0, QuantityMeasurementApp.LengthUnit.INCHES);

        assertTrue(a.add(b).equals(b.add(a))); //[cite: 6]
    }
}