package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "outgoingrequestslog")
@Getter
@Setter
public class OutgoingRequestsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String outgoingRequest;
    private String response;
    private Date outgoingRequestTstamp;
    private Date responseTstamp;
}
