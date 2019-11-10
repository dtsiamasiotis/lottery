import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class DrawDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveDraw(Draw draw)
    {
        entityManager.persist(draw);
    }
    public void updateDraw(Draw draw){entityManager.merge(draw);}
}
