package utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WinnerRequest {
    private long msisdn;
    private String winningNumbers;
    private long drawId;
    private long prizeAmount;
    private long ticketId;
}
