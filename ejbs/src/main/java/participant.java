import javax.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name="participants")
@Getter
@Setter
public class participant{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long msisdn;
    private Date first_seen;
}