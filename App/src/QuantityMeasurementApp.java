import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementApp {

    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
        YARDS(36.0), // 1 yard = 3 feet = 36 inches
        CENTIMETERS(0.393701); // 1 cm = 0.393701 inches

        private final double conversionFactor;

        LengthUnit(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    public static class Length {
        private final double value;
        private final LengthUnit unit;

        public Length(double value, LengthUnit unit) {
            this.value = value;
            this.unit = unit;
        }

        private double convertToBaseUnit() {
            // Convert to base unit (inches) for comparison
            double rawValue = this.value * this.unit.getConversionFactor();
            // Rounding to handle floating-point precision in cross-unit comparisons
            return Math.round(rawValue * 1000000.0) / 1000000.0;
        }

        public boolean compare(Length thatLength) {
            return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length that = (Length) o;
            return this.compare(that);
        }
    }

    public static boolean demonstrateLengthComparison(double v1, LengthUnit u1, double v2, LengthUnit u2) {
        Length l1 = new Length(v1, u1);
        Length l2 = new Length(v2, u2);
        boolean result = l1.equals(l2);
        System.out.println("Quantity(" + v1 + ", " + u1 + ") and Quantity(" + v2 + ", " + u2 + ")");
        System.out.println("Output: Equal (" + result + ")");
        return result;
    }

    public static void main(String[] args) {
        demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
        demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES); //
        demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701, LengthUnit.INCHES); //[cite: 4]
        demonstrateLengthComparison(3.0, LengthUnit.FEET, 1.0, LengthUnit.YARDS); //[cite: 4]
        demonstrateLengthComparison(30.48, LengthUnit.CENTIMETERS, 1.0, LengthUnit.FEET); //[cite: 4]
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void yardEquals36Inches() {
        QuantityMeasurementApp.Length yard = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.Length inches = new QuantityMeasurementApp.Length(36.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(yard.equals(inches)); //[cite: 4]
    }

    @Test
    public void threeFeetEqualsOneYard() {
        QuantityMeasurementApp.Length feet = new QuantityMeasurementApp.Length(3.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length yard = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        assertTrue(feet.equals(yard)); //[cite: 4]
    }

    @Test
    public void centimeterEquals0Point393701Inches() {
        QuantityMeasurementApp.Length cm = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.Length inches = new QuantityMeasurementApp.Length(0.393701, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(cm.equals(inches)); //[cite: 4]
    }

    @Test
    public void thirtyPoint48CmEqualsOneFoot() {
        // 30.48 * 0.393701 ≈ 11.9999... which rounds to 12.0 inches (1 foot)
        QuantityMeasurementApp.Length cm = new QuantityMeasurementApp.Length(30.48, QuantityMeasurementApp.LengthUnit.CENTIMETERS);
        QuantityMeasurementApp.Length foot = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(cm.equals(foot)); //[cite: 4]
    }

    @Test
    public void reflexiveSymmetricAndTransitiveProperty() {
        QuantityMeasurementApp.Length yard = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.YARDS);
        QuantityMeasurementApp.Length feet = new QuantityMeasurementApp.Length(3.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length inches = new QuantityMeasurementApp.Length(36.0, QuantityMeasurementApp.LengthUnit.INCHES);

        // Reflexive[cite: 4]
        assertTrue(yard.equals(yard));
        // Symmetric[cite: 4]
        assertTrue(yard.equals(feet) && feet.equals(yard));
        // Transitive[cite: 4]
        if (yard.equals(feet) && feet.equals(inches)) {
            assertTrue(yard.equals(inches));
        }
    }
}