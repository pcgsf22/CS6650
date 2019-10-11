
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import org.apache.log4j.Logger;

/**
 * The type Ski api handler runnable.
 */
public class SkiApiHandlerRunnable implements Runnable {

  private boolean running = true;
  private final Logger logger = Logger.getLogger(SkiApiHandlerRunnable.class);

  private final CountDownLatch finishCount;
  private final ActiveCount successCount;
  private final ActiveCount failCount;

  private SkiersApi apiInstance;
  private ApiClient client;
  private int startIDRange;
  private int endIDRange;
  private int startTime;
  private int endTime;
  private int liftRange;
  private int numPost;
  private final Random random = new Random();

  /**
   * Instantiates a new Ski api handler runnable.
   *
   * @param finishCount the finish count
   * @param successCount the success count
   * @param failCount the fail count
   * @param urlBase the url base
   * @param startIDRange the start id range
   * @param endIDRange the end id range
   * @param startTime the start time
   * @param endTime the end time
   * @param liftRange the lift range
   * @param numPost the num post
   */
  public SkiApiHandlerRunnable(CountDownLatch finishCount, ActiveCount successCount,
      ActiveCount failCount, String urlBase, int startIDRange, int endIDRange, int startTime,
      int endTime, int liftRange, int numPost) {
    this.finishCount = finishCount;
    this.successCount = successCount;
    this.failCount = failCount;
    this.startIDRange = startIDRange;
    this.endIDRange = endIDRange;
    this.startTime = startTime;
    this.endTime = endTime;
    this.liftRange = liftRange;
    this.numPost = numPost;
    this.apiInstance = new SkiersApi();
    this.client = apiInstance.getApiClient();
    client.setBasePath(urlBase);
  }


  @Override
  public void run() {
    int localSuccessCount = 0;
    int localFailCount = 0;

    for (int i = 0; i < numPost; i++) {
      int id = random.ints(startIDRange, endIDRange + 1).findAny().getAsInt();
      int time = random.ints(startTime, endTime + 1).findAny().getAsInt();
      int lift = random.ints(1, liftRange + 1).findAny().getAsInt();
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(lift);
      liftRide.setTime(time);
      try {
        apiInstance.writeNewLiftRide(liftRide, 0, "2019", "29", id);
        localSuccessCount++;
      } catch (ApiException e) {
        localFailCount++;
        logger.trace(e);
        e.printStackTrace();
      }
    }
    // Synchronize local status to global status
    successCount.incrementBy(localSuccessCount);
    failCount.incrementBy(localFailCount);
    finishCount.countDown();
  }

}
