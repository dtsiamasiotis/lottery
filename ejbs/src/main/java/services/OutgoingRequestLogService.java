package services;

import dao.OutgoingRequestsLogDAO;
import entities.OutgoingRequestsLog;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class OutgoingRequestLogService {
    @Inject
    private OutgoingRequestsLogDAO outgoingRequestsLogDAO;

    public void saveOutgoingRequestLog(OutgoingRequestsLog outgoingRequestsLog)
    {
        outgoingRequestsLogDAO.saveRestRequest(outgoingRequestsLog);
    }
}

