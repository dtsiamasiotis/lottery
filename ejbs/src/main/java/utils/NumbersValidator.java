package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumbersValidator {

    private String correctPattern = "([-]?[0-9]{1}[0-9]*,)*?[-]?[0-9]{1}[0-9]*";
    private String patternWithCharacters = "(.*)([A-Z]|[a-z])(.*)";
    private String patternWrongFormat = "([0-9]{1}[0-9]?[^,])+[0-9]{1}[0-9]?";

    public static final int VALID_SELECTION = 0;
    public static final int INVALID_NUMBER_OF_SELECTIONS = 1;
    public static final int INVALID_RANGE_OF_SELECTIONS = 2;
    public static final int DUPLICATE_SELECTION = 3;
    public static final int ALPHANUMERIC_SELECTION = 4;
    public static final int INVALID_GENERIC_SELECTION = 5;

    public static final int MIN_SELECTED_NUMBER = 1;
    public static final int MAX_SELECTED_NUMBER = 80;

    public String getcorrectPattern()
    {
        return correctPattern;
    }


    public int checkNumbersString(String numbersStr,int numbersPopulation)
    {
        String correctPattern = getcorrectPattern();

        if(numbersStr.matches(correctPattern))
        {

            String[] numbersSplit = numbersStr.split(",");
            List<Integer> numbersInt = new ArrayList<Integer>();

            if(numbersSplit.length != numbersPopulation)
                return INVALID_NUMBER_OF_SELECTIONS;

            for(int i=0;i<numbersSplit.length;i++)
            {
                int tempNumber = Integer.parseInt(numbersSplit[i]);
                numbersInt.add(tempNumber);
                if(tempNumber>MAX_SELECTED_NUMBER||tempNumber<MIN_SELECTED_NUMBER)
                    return INVALID_RANGE_OF_SELECTIONS;
            }

            Collections.sort(numbersInt);

            for(int i=0;i<numbersInt.size()-1;i++)
            {
                if(numbersInt.get(i)==numbersInt.get(i+1))
                {
                    return DUPLICATE_SELECTION;
                }
            }

            return VALID_SELECTION;
        }
        else if(numbersStr.matches(patternWithCharacters))
        {
            return ALPHANUMERIC_SELECTION;
        }
        else
        {
            return INVALID_GENERIC_SELECTION;
        }
    }
}
