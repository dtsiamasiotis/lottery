import org.junit.jupiter.api.Test;
import utils.NumbersValidator;
import utils.NumbersValidatorResult;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumbersValidatorTest {

    @Test
    public void checkNumbersStringValidSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,4,5,6";
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(NumbersValidatorResult.VALID_SELECTION,numbersValidatorResult);
    }

    @Test
    public void checkNumbersStringInvalidNumbersOfSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,4,5";
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(NumbersValidatorResult.INVALID_NUMBER_OF_SELECTIONS,numbersValidatorResult);
    }

    @Test
    public void checkNumbersStringInvalidRangeOfSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,90,5,6";
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(NumbersValidatorResult.INVALID_RANGE_OF_SELECTIONS,numbersValidatorResult);
    }

    @Test
    public void checkNumbersStringDuplicateSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,3,3,5,6";
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(NumbersValidatorResult.DUPLICATE_SELECTION,numbersValidatorResult);
    }

    @Test
    public void checkNumbersStringAlphanumericSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,a3,b,5,6";
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(NumbersValidatorResult.ALPHANUMERIC_SELECTION,numbersValidatorResult);
    }

    @Test
    public void checkNumbersStringInvalidGenericSelectionTest()
    {
        NumbersValidator numbersValidator = new NumbersValidator();
        String numbersStr = "1,2,,3,4,5,6";
        NumbersValidatorResult numbersValidatorResult = numbersValidator.checkNumbersString(numbersStr,6);

        assertEquals(NumbersValidatorResult.INVALID_GENERIC_SELECTION,numbersValidatorResult);
    }
}
