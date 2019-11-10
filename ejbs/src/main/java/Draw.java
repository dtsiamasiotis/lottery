import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "draws")
@Getter
@Setter
public class Draw {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private Date drawTime;

    @Column(name = "winning_numbers")
    private String winningNumbers;

    public Draw(Date drawTime, String winningNumbers){
        this.drawTime = drawTime;
        this.winningNumbers = winningNumbers;
    }

    public Draw(){}
}
