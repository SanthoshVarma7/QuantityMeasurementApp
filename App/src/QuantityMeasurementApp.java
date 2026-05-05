import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

enum LengthUnit {
    FEET(12.0),
    INCHES(1.0),
    YARDS(36.0),
    CENTIMETERS(0.393701);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double convertToBase(double value) {
        return value * this.conversionFactor;
    }

    public double fromBase(double valueInBase) {
        return valueInBase / this.conversionFactor;
    }
}

class QuantityLength {
    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    public double getInBaseUnit() {
        return unit.convertToBase(value);
    }

    public static QuantityLength add(QuantityLength l1, QuantityLength l2, LengthUnit targetUnit) {
        if (l1 == null || l2 == null || targetUnit == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        double sumInBase = l1.getInBaseUnit() + l2.getInBaseUnit();
        double convertedValue = targetUnit.fromBase(sumInBase);
        return new QuantityLength(Math.round(convertedValue * 1000.0) / 1000.0, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuantityLength that = (QuantityLength) o;
        return Double.compare(Math.round(this.getInBaseUnit() * 1000.0) / 1000.0,
                Math.round(that.getInBaseUnit() * 1000.0) / 1000.0) == 0;
    }

    @Override
    public String toString() { return value + " " + unit; }
}

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(12.0, LengthUnit.INCHES);

        System.out.println("Equality: " + feet.equals(inches));
        System.out.println("Addition (Result in Yards): " + QuantityLength.add(feet, inches, LengthUnit.YARDS));
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testDelegatedConversion() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertEquals(36.0, yard.getInBaseUnit());
    }

    @Test
    public void testAdditionWithTargetUnit() {
        QuantityLength l1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength l2 = new QuantityLength(12.0, LengthUnit.INCHES);
        QuantityLength result = QuantityLength.add(l1, l2, LengthUnit.YARDS);

        // 1ft + 12in = 24 inches = 0.667 Yards
        assertEquals(0.667, result.getValue());
        assertEquals(LengthUnit.YARDS, result.getUnit());
    }

    @Test
    public void testEqualityAcrossUnits() {
        QuantityLength cm = new QuantityLength(30.48, LengthUnit.CENTIMETERS);
        QuantityLength foot = new QuantityLength(1.0, LengthUnit.FEET);
        assertTrue(cm.equals(foot));
    }
}