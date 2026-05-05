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
    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }
    @Override public double getConversionFactor() { return factor; }
}

enum WeightUnit implements IMeasurable {
    GRAM(1.0), KILOGRAM(1000.0), POUND(453.592), TONNE(1000000.0);
    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }
    @Override public double getConversionFactor() { return factor; }
}

enum VolumeUnit implements IMeasurable {
    LITRE(1.0), // Base Unit
    MILLILITRE(0.001),
    GALLON(3.78541);

    private final double conversionFactor;
    VolumeUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }
    @Override public double getConversionFactor() { return conversionFactor; }
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

    private double getInBaseUnit() {
        double raw = unit.convertToBase(value);
        return Math.round(raw * 1000.0) / 1000.0;
    }

    public Quantity<U> add(Quantity<U> that, U targetUnit) {
        double totalBase = this.getInBaseUnit() + that.getInBaseUnit();
        double converted = targetUnit.fromBase(totalBase);
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
        Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litres = new Quantity<>(3.785, VolumeUnit.LITRE);
        System.out.println("1 Gallon == 3.785 Litres: " + gallon.equals(litres));

        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> l = new Quantity<>(1.0, VolumeUnit.LITRE);
        System.out.println("1000ml + 1L in Litres: " + ml.add(l, VolumeUnit.LITRE));
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testVolumeEquality() {
        Quantity<VolumeUnit> l = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        assertTrue(l.equals(ml));
    }

    @Test
    public void testGallonToLitreEquality() {
        Quantity<VolumeUnit> gal = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litre = new Quantity<>(3.785, VolumeUnit.LITRE);
        assertTrue(gal.equals(litre));
    }

    @Test
    public void testVolumeAddition() {
        Quantity<VolumeUnit> l = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> gal = new Quantity<>(1.0, VolumeUnit.GALLON); // 3.785L
        Quantity<VolumeUnit> result = l.add(gal, VolumeUnit.LITRE);
        assertEquals(4.785, result.getValue());
    }

    @Test
    public void testCrossCategorySafety() {
        Quantity<VolumeUnit> volume = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<WeightUnit> weight = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        assertFalse(volume.equals(weight));
    }
}