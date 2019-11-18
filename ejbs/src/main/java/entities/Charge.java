package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "charges")
@Getter
@Setter
public class Charge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String msisdn;
    private String amount;

    @OneToOne
    @JoinColumn(name="ticket_id")
    private Ticket ticket;
}
