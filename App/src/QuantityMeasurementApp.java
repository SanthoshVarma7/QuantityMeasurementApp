import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

interface IMeasurable {
    double getConversionFactor();

    default double convertToBase(double value) {
        return value * getConversionFactor();
    }

    default double fromBase(double valueInBase) {
        return valueInBase / getConversionFactor();
    }
}

enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);

    private final double conversionFactor;
    LengthUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

    @Override
    public double getConversionFactor() { return conversionFactor; }
}

enum WeightUnit implements IMeasurable {
    GRAM(1.0), KILOGRAM(1000.0), POUND(453.592), TONNE(1000000.0);

    private final double conversionFactor;
    WeightUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

    @Override
    public double getConversionFactor() { return conversionFactor; }
}

class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException();
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public U getUnit() { return unit; }

    private double getInBaseUnit() {
        double raw = unit.convertToBase(value);
        return Math.round(raw * 1000.0) / 1000.0;
    }

    public Quantity<U> add(Quantity<U> that, U targetUnit) {
        double sumInBase = this.getInBaseUnit() + that.getInBaseUnit();
        double converted = targetUnit.fromBase(sumInBase);
        return new Quantity<>(Math.round(converted * 1000.0) / 1000.0, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        if (!this.unit.getClass().equals(that.unit.getClass())) return false;
        return Double.compare(this.getInBaseUnit(), that.getInBaseUnit()) == 0;
    }

    @Override
    public String toString() { return value + " " + unit; }
}

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Quantity<LengthUnit> ft = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> in = new Quantity<>(12.0, LengthUnit.INCHES);
        System.out.println(ft.equals(in));

        Quantity<WeightUnit> kg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> lb = new Quantity<>(2.205, WeightUnit.POUND);
        System.out.println(kg.equals(lb));
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testLengthEquality() {
        Quantity<LengthUnit> f1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> i1 = new Quantity<>(12.0, LengthUnit.INCHES);
        assertTrue(f1.equals(i1));
    }

    @Test
    public void testWeightEquality() {
        Quantity<WeightUnit> kg = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> grams = new Quantity<>(1000.0, WeightUnit.GRAM);
        assertTrue(kg.equals(grams));
    }

    @Test
    public void testAddition() {
        Quantity<LengthUnit> l1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(12.0, LengthUnit.INCHES);
        Quantity<LengthUnit> result = l1.add(l2, LengthUnit.FEET);
        assertEquals(2.0, result.getValue());
    }

    @Test
    public void testTypeSafety() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(length.equals(weight));
    }
}