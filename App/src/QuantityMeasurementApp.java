import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

interface IMeasurable {
    double getConversionFactor();
    default double convertToBase(double v) { return v * getConversionFactor(); }
    default double fromBase(double v) { return v / getConversionFactor(); }
}

enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0), YARDS(36.0);
    private final double factor;
    LengthUnit(double f) { this.factor = f; }
    @Override public double getConversionFactor() { return factor; }
}

enum WeightUnit implements IMeasurable {
    GRAM(1.0), KILOGRAM(1000.0);
    private final double factor;
    WeightUnit(double f) { this.factor = f; }
    @Override public double getConversionFactor() { return factor; }
}

class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }

    private double getInBaseUnit() {
        return Math.round(unit.convertToBase(value) * 1000.0) / 1000.0;
    }

    public Quantity<U> subtract(Quantity<U> that, U targetUnit) {
        double diffInBase = this.getInBaseUnit() - that.getInBaseUnit();
        double converted = targetUnit.fromBase(diffInBase);
        return new Quantity<>(Math.round(converted * 1000.0) / 1000.0, targetUnit);
    }

    public double divide(Quantity<U> that) {
        double divisor = that.getInBaseUnit();
        if (divisor == 0) throw new IllegalArgumentException("Division by zero");
        return this.getInBaseUnit() / divisor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        return Double.compare(this.getInBaseUnit(), that.getInBaseUnit()) == 0;
    }

    @Override
    public String toString() { return value + " " + unit; }
}

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Quantity<LengthUnit> tenFeet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> sixInches = new Quantity<>(6.0, LengthUnit.INCHES);

        Quantity<LengthUnit> diff = tenFeet.subtract(sixInches, LengthUnit.FEET);
        System.out.println("10ft - 6in = " + diff); // 9.5 FEET

        Quantity<WeightUnit> tenKg = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> fiveKg = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        System.out.println("10kg / 5kg = " + tenKg.divide(fiveKg)); // 2.0
    }
}

class QuantityMeasurementAppTest {
    @Test
    public void testSubtraction() {
        Quantity<LengthUnit> l1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(6.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = l1.subtract(l2, LengthUnit.FEET);
        assertEquals(9.5, result.getValue());
    }

    @Test
    public void testDivision() {
        Quantity<WeightUnit> w1 = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        assertEquals(2.0, w1.divide(w2));
    }

    @Test
    public void testDivisionByZero() {
        Quantity<WeightUnit> w1 = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(0.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> w1.divide(w2));
    }
}