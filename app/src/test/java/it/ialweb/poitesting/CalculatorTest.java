package it.ialweb.poitesting;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    private Calculator calculator;

    @Before
    public void setup() {
        calculator = new Calculator();
    }

    @Test
    public void testSum0() {
        int result = calculator.sum(2, 0);
        assertEquals(3, result);
    }

    @Test
    public void testDiv() {
        calculator.div(2, 0);
    }

    @Test
    public void testSum() {
        int result = calculator.sum(2, 3);
        assertEquals(5, result);
    }
}
