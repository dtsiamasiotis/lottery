package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "prizes")
@Getter
@Setter
public class Prize {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private long amount;
    private int numbersCount;
}
