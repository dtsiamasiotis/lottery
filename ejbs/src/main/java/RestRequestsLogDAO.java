import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class RestRequestsLogDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void saveRestRequest(RestRequestsLog restRequestsLog)
    {
        entityManager.persist(restRequestsLog);
    }
}
