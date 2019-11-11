import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class RestRequestLogService {
    @Inject
    private RestRequestsLogDAO restRequestsLogDAO;

    public void saveRestRequestLog(RestRequestsLog restRequestsLog)
    {
        restRequestsLogDAO.saveRestRequest(restRequestsLog);
    }
}
