package entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import entities.Participant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String numbers;

    @Column(name = "date_played")
    private Date datePlayed;

    private boolean valid;

    @Column(name = "ticket_id")
    private long ticketId;

    @Column(name = "draw_id")
    private long drawId;

    @ManyToOne
    @JoinColumn(name="participant_id")
    private Participant participant;

    private boolean charged;

    @Transient
    private Winner winner;
}

