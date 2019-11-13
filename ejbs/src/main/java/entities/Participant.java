package entities;

import javax.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="participants")
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long msisdn;
    private Date first_seen;

    @OneToMany(mappedBy = "participant",cascade=CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Ticket> tickets = new HashSet<Ticket>();

    public void addTicket(Ticket ticket)
    {
        tickets.add(ticket);
        ticket.setParticipant(this);
    }
}