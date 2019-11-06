import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String numbers;

    @ManyToOne
    @JoinColumn(name="participant_id")
    private Participant participant;

}

