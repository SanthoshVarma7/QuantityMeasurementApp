import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementApp {

    public static class Feet {
        private final double value; [cite: 83]

        public Feet(double value) { [cite: 84]
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) { [cite: 101, 103]
            if (this == obj) return true; [cite: 87]
            if (obj == null || getClass() != obj.getClass()) return false; [cite: 88, 90]
            Feet feet = (Feet) obj; [cite: 46]
            return Double.compare(feet.value, value) == 0; [cite: 92, 117]
        }
    }

    public static class Inches {
        private final double value; [cite: 212]

        public Inches(double value) { [cite: 212]
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) { [cite: 212, 213]
            if (this == obj) return true; [cite: 284]
            if (obj == null || getClass() != obj.getClass()) return false; [cite: 284]
            Inches inches = (Inches) obj; [cite: 282]
            return Double.compare(inches.value, value) == 0; [cite: 282]
        }
    }

    public static void demonstrateFeetEquality() { [cite: 256]
        Feet feet1 = new Feet(1.0); [cite: 113]
        Feet feet2 = new Feet(1.0);
        System.out.println("Feet Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + feet1.equals(feet2) + ")"); [cite: 280]
    }

    public static void demonstrateInchesEquality() { [cite: 258]
        Inches inch1 = new Inches(1.0); [cite: 213]
        Inches inch2 = new Inches(1.0);
        System.out.println("Inches Input: 1.0 in and 1.0 in");
        System.out.println("Output: Equal (" + inch1.equals(inch2) + ")");
    }

    public static void main(String[] args) { [cite: 110, 263]
        demonstrateFeetEquality(); [cite: 265]
        demonstrateInchesEquality(); [cite: 267]
    }
}

class QuantityMeasurementAppTest {

    @Test
    public void testFeetEquality_SameValue() { [cite: 150, 336]
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0); [cite: 152]
        QuantityMeasurementApp.Feet f2 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(f1.equals(f2)); [cite: 153]
    }

    @Test
    public void testFeetEquality_DifferentValue() { [cite: 154, 338]
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0); [cite: 156]
        QuantityMeasurementApp.Feet f2 = new QuantityMeasurementApp.Feet(2.0);
        assertFalse(f1.equals(f2)); [cite: 157]
    }

    @Test
    public void testFeetEquality_NullComparison() { [cite: 158, 340]
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(f1.equals(null)); [cite: 162]
    }

    @Test
    public void testFeetEquality_DifferentClass() { [cite: 342]
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        assertFalse(f1.equals(new Object())); [cite: 139]
    }

    @Test
    public void testFeetEquality_SameReference() { [cite: 344]
        QuantityMeasurementApp.Feet f1 = new QuantityMeasurementApp.Feet(1.0);
        assertTrue(f1.equals(f1)); [cite: 175]
    }

    @Test
    public void testInchesEquality_SameValue() { [cite: 346]
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0); [cite: 213]
        QuantityMeasurementApp.Inches i2 = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(i1.equals(i2)); [cite: 213]
    }

    @Test
    public void testInchesEquality_DifferentValue() { [cite: 348]
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0); [cite: 214]
        QuantityMeasurementApp.Inches i2 = new QuantityMeasurementApp.Inches(2.0);
        assertFalse(i1.equals(i2)); [cite: 214]
    }

    @Test
    public void testInchesEquality_NullComparison() { [cite: 350]
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(i1.equals(null)); [cite: 287]
    }

    @Test
    public void testInchesEquality_DifferentClass() { [cite: 352]
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0);
        assertFalse(i1.equals(new Object())); [cite: 287]
    }

    @Test
    public void testInchesEquality_SameReference() { [cite: 354]
        QuantityMeasurementApp.Inches i1 = new QuantityMeasurementApp.Inches(1.0);
        assertTrue(i1.equals(i1)); [cite: 287]
    }
}