package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "winners")
@Getter
@Setter
public class Winner {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "draw_id")
    private long drawId;

    @Column(name = "winning_numbers")
    private String winningNumbers;

    @Column(name = "winning_numbers_count")
    private int winningNumbersCount;

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @OneToOne
    @JoinColumn(name = "prize_id")
    private Prize prize;
}
