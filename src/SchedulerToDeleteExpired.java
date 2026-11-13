import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerToDeleteExpired {

    private static final int DELAY = Integer.parseInt(Config.get("schedulerToDeleteExpired.delayInSeconds"));
    private static final int INITIAL_DELAY = 30;

    private final ScheduledExecutorService scheduler;

    public SchedulerToDeleteExpired(Database db) {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = db::deleteExpired;
        scheduler.scheduleWithFixedDelay(task, INITIAL_DELAY, DELAY, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
