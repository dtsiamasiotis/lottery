import org.junit.jupiter.api.Test;
import utils.NumbersValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersValidatorTest {

    @Test
    public void checkNumbersStringValidSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,4,5,6";
        int result = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(0,result);
    }

    @Test
    public void checkNumbersStringInvalidNumbersOfSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,4,5";
        int result = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(1,result);
    }

    @Test
    public void checkNumbersStringInvalidRangeOfSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,90,5,6";
        int result = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(2,result);
    }

    @Test
    public void checkNumbersStringDuplicateSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,3,5,6";
        int result = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(3,result);
    }

    @Test
    public void checkNumbersStringAlphanumericSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,a3,b,5,6";
        int result = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(4,result);
    }

    @Test
    public void checkNumbersStringInvalidGenericSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,,3,4,5,6";
        int result = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(5,result);
    }
}
