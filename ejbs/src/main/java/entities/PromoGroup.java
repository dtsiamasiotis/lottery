package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "promogroups")
@Getter
@Setter
public class PromoGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String sqlQuery;
}
