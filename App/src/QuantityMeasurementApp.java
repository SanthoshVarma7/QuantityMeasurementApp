import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementApp {

    public enum LengthUnit {
        FEET(12.0),
        INCHES(1.0),
        YARDS(36.0),
        CENTIMETERS(0.393701);

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

        public double getValue() {
            return value;
        }

        public LengthUnit getUnit() {
            return unit;
        }

        private double convertToBaseUnit() {
            double rawValue = this.value * this.unit.getConversionFactor();
            return Math.round(rawValue * 1000000.0) / 1000000.0;
        }

        public static double convert(double value, LengthUnit fromUnit, LengthUnit toUnit) {
            if (!Double.isFinite(value)) return 0.0;
            double valueInInches = value * fromUnit.getConversionFactor();
            double convertedValue = valueInInches / toUnit.getConversionFactor();
            return Math.round(convertedValue * 1000000.0) / 1000000.0;
        }

        public static Length convert(Length length, LengthUnit toUnit) {
            double newValue = convert(length.getValue(), length.getUnit(), toUnit);
            return new Length(newValue, toUnit);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Length that = (Length) o;
            return Double.compare(this.convertToBaseUnit(), that.convertToBaseUnit()) == 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("1.0 FEET to INCHES: " + Length.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES));
        System.out.println("3.0 YARDS to FEET: " + Length.convert(3.0, LengthUnit.YARDS, LengthUnit.FEET));
        System.out.println("36.0 INCHES to YARDS: " + Length.convert(36.0, LengthUnit.INCHES, LengthUnit.YARDS));
        System.out.println("1.0 CM to INCHES: " + Length.convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES));
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testFeetToInchesConversion() {
        double result = QuantityMeasurementApp.Length.convert(1.0, QuantityMeasurementApp.LengthUnit.FEET, QuantityMeasurementApp.LengthUnit.INCHES);
        assertEquals(12.0, result);
    }

    @Test
    public void testYardsToFeetConversion() {
        double result = QuantityMeasurementApp.Length.convert(3.0, QuantityMeasurementApp.LengthUnit.YARDS, QuantityMeasurementApp.LengthUnit.FEET);
        assertEquals(9.0, result);
    }

    @Test
    public void testInchesToYardsConversion() {
        double result = QuantityMeasurementApp.Length.convert(36.0, QuantityMeasurementApp.LengthUnit.INCHES, QuantityMeasurementApp.LengthUnit.YARDS);
        assertEquals(1.0, result);
    }

    @Test
    public void testCentimetersToInchesConversion() {
        double result = QuantityMeasurementApp.Length.convert(1.0, QuantityMeasurementApp.LengthUnit.CENTIMETERS, QuantityMeasurementApp.LengthUnit.INCHES);
        assertEquals(0.393701, result);
    }

    @Test
    public void testZeroValueConversion() {
        double result = QuantityMeasurementApp.Length.convert(0.0, QuantityMeasurementApp.LengthUnit.FEET, QuantityMeasurementApp.LengthUnit.INCHES);
        assertEquals(0.0, result);
    }

    @Test
    public void testEqualityAfterConversion() {
        QuantityMeasurementApp.Length oneFoot = new QuantityMeasurementApp.Length(1.0, QuantityMeasurementApp.LengthUnit.FEET);
        QuantityMeasurementApp.Length convertedInches = QuantityMeasurementApp.Length.convert(oneFoot, QuantityMeasurementApp.LengthUnit.INCHES);
        assertTrue(oneFoot.equals(convertedInches));
    }
}