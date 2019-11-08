import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cron")
@Getter
@Setter
public class Cron {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String expression;

    private String command;
}
