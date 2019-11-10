import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
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

    @EJB
    private DrawService drawService;

    @PostConstruct
    public void init()
    {
        List<Cron> cronCommands = loadCronCommands();
        for(Cron cronCommand:cronCommands)
            createTimers(cronCommand);
        System.out.println("init:"+this.toString());
    }

    public List<Cron> loadCronCommands()
    {
        Query q = entityManager.createNativeQuery("SELECT * FROM cron",Cron.class);
        List<Cron> results = q.getResultList();
        return results;
    }

    public void createTimers(Cron cronCommand)
    {
        String expressionParts[] = cronCommand.getExpression().split(" ");
        ScheduleExpression scheduleExpression = new ScheduleExpression();
        scheduleExpression.second(expressionParts[0]).minute(expressionParts[1]).hour(expressionParts[2]);
        TimerConfig timerConfig = new TimerConfig();
        TimerType timerType = new TimerType();
        timerType.setType(cronCommand.getCommand());
        timerConfig.setInfo(timerType);
        timerConfig.setPersistent(false);
        timerService.createCalendarTimer(scheduleExpression,timerConfig);
    }

    @Timeout
    public void timerTimeout(Timer timer)
    {
        TimerType timerType = (TimerType)timer.getInfo();
        if(timerType.getType().equals("performDraw"))
            drawService.performDraw();
        else if(timerType.getType().equals("createDraw"))
            drawService.createDraw();
        else if(timerType.getType().equals("findWinners"))
            drawService.findWinners();

        System.out.println(timerType.getType());
    }
}
