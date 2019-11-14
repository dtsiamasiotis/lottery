package services;

import dao.IncomingRequestsLogDAO;
import entities.IncomingRequestsLog;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class IncomingRequestLogService {
    @Inject
    private IncomingRequestsLogDAO incomingRequestsLogDAO;

    public void saveIncomingRequestLog(IncomingRequestsLog incomingRequestsLog)
    {
        incomingRequestsLogDAO.saveRestRequest(incomingRequestsLog);
    }
}
