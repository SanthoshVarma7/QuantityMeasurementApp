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
        if (unit == null || !Double.isFinite(value)) throw new IllegalArgumentException("Invalid input");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }

    private double getInBaseUnit() {
        return unit.convertToBase(value);
    }

    // --- UC13: CENTRALIZED ARITHMETIC LOGIC ---

    private void validate(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validate(other);
        double resultBase = this.getInBaseUnit() + other.getInBaseUnit();
        return new Quantity<>(Math.round(targetUnit.fromBase(resultBase) * 1000.0) / 1000.0, targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validate(other);
        double resultBase = this.getInBaseUnit() - other.getInBaseUnit();
        return new Quantity<>(Math.round(targetUnit.fromBase(resultBase) * 1000.0) / 1000.0, targetUnit);
    }

    public double divide(Quantity<U> other) {
        validate(other);
        double divisor = other.getInBaseUnit();
        if (divisor == 0) throw new ArithmeticException("Division by zero");
        return this.getInBaseUnit() / divisor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        if (!this.unit.getClass().equals(that.unit.getClass())) return false;
        return Double.compare(Math.round(this.getInBaseUnit() * 1000.0) / 1000.0,
                Math.round(that.getInBaseUnit() * 1000.0) / 1000.0) == 0;
    }

    @Override
    public String toString() { return value + " " + unit; }
}

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);

        System.out.println("Add: " + l1.add(l2, LengthUnit.FEET));       // 2.0 FEET
        System.out.println("Subtract: " + l1.subtract(l2, LengthUnit.YARDS)); // 0.0 YARDS
        System.out.println("Divide: " + l1.divide(l2));                 // 1.0
    }
}

class QuantityMeasurementAppTest {
    @Test
    public void testDryAddition() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(6.0, LengthUnit.INCHES);
        assertEquals(1.5, l1.add(l2, LengthUnit.FEET).getValue());
    }

    @Test
    public void testDrySubtraction() {
        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(500.0, WeightUnit.GRAM);
        assertEquals(0.5, w1.subtract(w2, WeightUnit.KILOGRAM).getValue());
    }

    @Test
    public void testDivisionResult() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> l2 = new Quantity<>(1.0, LengthUnit.FEET);
        assertEquals(3.0, l1.divide(l2));
    }
}