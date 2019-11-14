package utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WinnerRestModel {
    private long msisdn;
    private String winningNumbers;
    private long drawId;
    private long prizeAmount;
    private long ticketId;
}
