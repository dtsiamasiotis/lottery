import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Singleton
@Startup
public class JobManager {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private TimerService timerService;

    @PostConstruct
    public void init()
    {
        List<Cron> drawTimes = loadDrawTimes();
        for(Cron drawTime:drawTimes)
            createDrawTimers(drawTime);
    }

    public List<Cron> loadDrawTimes()
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM cron WHERE command=?",Cron.class);
        q.setParameter(1,"executeDraw");
        List<Cron> results = q.getResultList();
        return results;
    }

    public void createDrawTimers(Cron drawTime)
    {
        String expressionParts[] = drawTime.getExpression().split(" ");
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.second(expressionParts[0]).minute(expressionParts[1]).hour(expressionParts[2]);
        TimerConfig timerConfig = new TimerConfig();
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(scheduleExpression,timerConfig);
    }

    @Timeout
    public void timerTimeout(Timer timer)
    {
       
    }
}
