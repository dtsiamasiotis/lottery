package utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PromoRequest {
    private long msisdn;
    private String message;
}
