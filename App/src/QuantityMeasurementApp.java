import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// --- WEIGHT CATEGORY ---

enum WeightUnit {
    MILLIGRAM(0.001),
    GRAM(1.0), // Base Unit
    KILOGRAM(1000.0),
    POUND(453.592),
    TONNE(1000000.0);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double convertToBase(double value) {
        return value * this.conversionFactor;
    }

    public double fromBase(double valueInBase) {
        return valueInBase / this.conversionFactor;
    }
}

class Weight {
    private final double value;
    private final WeightUnit unit;

    public Weight(double value, WeightUnit unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public WeightUnit getUnit() { return unit; }

    private double convertToBaseUnit() {
        double raw = unit.convertToBase(value);
        return Math.round(raw * 100.0) / 100.0;
    }

    public Weight add(Weight that, WeightUnit targetUnit) {
        double totalBase = this.convertToBaseUnit() + that.convertToBaseUnit();
        double converted = targetUnit.fromBase(totalBase);
        return new Weight(Math.round(converted * 100.0) / 100.0, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weight that = (Weight) o;
        return Double.compare(this.convertToBaseUnit(), that.convertToBaseUnit()) == 0;
    }

    @Override
    public String toString() { return value + " " + unit; }
}

// --- LENGTH CATEGORY (From UC8) ---

enum LengthUnit {
    FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);
    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }
    public double convertToBase(double v) { return v * factor; }
    public double fromBase(double v) { return v / factor; }
}

class Length {
    private final double value;
    private final LengthUnit unit;
    public Length(double v, LengthUnit u) { this.value = v; this.unit = u; }
    private double toBase() { return Math.round(unit.convertToBase(value) * 100.0) / 100.0; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length that = (Length) o;
        return Double.compare(this.toBase(), that.toBase()) == 0;
    }
}

// --- MAIN APPLICATION ---

public class QuantityMeasurementApp {
    public static void main(String[] args) {
        Weight kg = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight grams = new Weight(1000.0, WeightUnit.GRAM);
        System.out.println("1kg == 1000g: " + kg.equals(grams));

        Weight lb = new Weight(1.0, WeightUnit.POUND);
        Weight sum = kg.add(lb, WeightUnit.GRAM);
        System.out.println("1kg + 1lb in Grams: " + sum);
    }
}

// --- TEST SUITE ---

class QuantityMeasurementAppTest {

    @Test
    public void testWeightEquality_SameUnit() {
        Weight w1 = new Weight(10.0, WeightUnit.GRAM);
        Weight w2 = new Weight(10.0, WeightUnit.GRAM);
        assertEquals(w1, w2);
    }

    @Test
    public void testWeightEquality_DifferentUnit() {
        Weight kg = new Weight(1.0, WeightUnit.KILOGRAM);
        Weight grams = new Weight(1000.0, WeightUnit.GRAM);
        assertTrue(kg.equals(grams));
    }

    @Test
    public void testWeightAddition() {
        Weight kg = new Weight(1.0, WeightUnit.KILOGRAM); // 1000g
        Weight grams = new Weight(500.0, WeightUnit.GRAM); // 500g
        Weight result = kg.add(grams, WeightUnit.KILOGRAM);
        assertEquals(1.5, result.getValue());
    }

    @Test
    public void testPoundToGramEquality() {
        Weight lb = new Weight(1.0, WeightUnit.POUND);
        Weight grams = new Weight(453.59, WeightUnit.GRAM);
        assertTrue(lb.equals(grams));
    }

    @Test
    public void testTypeSafety_WeightNotEqualToLength() {
        Weight weight = new Weight(1.0, WeightUnit.KILOGRAM);
        Length length = new Length(1.0, LengthUnit.FEET);
        // This will return false due to getClass() check in equals()
        assertFalse(weight.equals(length));
    }
}