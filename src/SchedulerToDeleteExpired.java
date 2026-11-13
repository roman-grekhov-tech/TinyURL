import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SchedulerToDeleteExpired {

    private final ScheduledExecutorService scheduler;

    public SchedulerToDeleteExpired(int initialDelay, int delay, Database db) {
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = db::deleteExpired;
        scheduler.scheduleWithFixedDelay(task, initialDelay, delay, TimeUnit.SECONDS);
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}
