import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementApp {

    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0);

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
            return this.value * this.unit.getConversionFactor();
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

    public static boolean demonstrateLengthEquality(Length l1, Length l2) {
        return l1.equals(l2);
    }

    public static void demonstrateFeetEquality() {
        Length f1 = new Length(1.0, LengthUnit.FEET);
        Length f2 = new Length(1.0, LengthUnit.FEET);
        System.out.println("Feet Equality: " + f1.equals(f2));
    }

    public static void demonstrateInchesEquality() {
        Length i1 = new Length(1.0, LengthUnit.INCHES);
        Length i2 = new Length(1.0, LengthUnit.INCHES);
        System.out.println("Inches Equality: " + i1.equals(i2));
    }

    public static void demonstrateFeetInchesComparison() {
        Length f1 = new Length(1.0, LengthUnit.FEET);
        Length i1 = new Length(12.0, LengthUnit.INCHES);
        System.out.println("1.0 ft == 12.0 in: " + f1.equals(i1));
    }

    public static void main(String[] args) {
        demonstrateFeetEquality();
        demonstrateInchesEquality();
        demonstrateFeetInchesComparison();
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testFeetEquality() {
        QuantityMeasurementApp.Length f1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length f2 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(f1.equals(f2));
    }

    @Test
    public void testInchesEquality() {
        QuantityMeasurementApp.Length i1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.Length i2 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(i1.equals(i2));
    }

    @Test
    public void testFeetInchesComparison() {
        QuantityMeasurementApp.Length f1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length i1 = new QuantityMeasurementApp.Length(12.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(f1.equals(i1));
    }

    @Test
    public void testFeetInequality() {
        QuantityMeasurementApp.Length f1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length f2 = new QuantityMeasurementApp.Length(2.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(f1.equals(f2));
    }

    @Test
    public void testInchesInequality() {
        QuantityMeasurementApp.Length i1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        QuantityMeasurementApp.Length i2 = new QuantityMeasurementApp.Length(2.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertFalse(i1.equals(i2));
    }

    @Test
    public void testCrossUnitInequality() {
        QuantityMeasurementApp.Length f1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length i1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.INCHES);
        assertFalse(f1.equals(i1));
    }

    @Test
    public void testNullComparison() {
        QuantityMeasurementApp.Length f1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertFalse(f1.equals(null));
    }

    @Test
    public void testSameReference() {
        QuantityMeasurementApp.Length f1 = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        assertTrue(f1.equals(f1));
    }
}