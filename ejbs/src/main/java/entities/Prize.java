package entities;

import javax.persistence.*;

@Entity
@Table(name = "prizes")
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long amount;
    private int numbersCount;
}
