import commandlineparser.Params;

import java.util.concurrent.*;

import static java.lang.Thread.MAX_PRIORITY;


/**
 * The type Thread dispatcher, which serves as the boss in boss-worker model.
 */
public class ThreadDispatcher implements Runnable {

  private final Params params;
  private final ActiveCount successCount = new ActiveCount();
  private final ActiveCount failCount = new ActiveCount();

  /**
   * Instantiates a new Thread dispatcher.
   *
   * @param params the params
   */
  public ThreadDispatcher(Params params) {
    this.params = params;
  }

  private void phaseStart(ExecutorService pool, int phaseThread, int skierRange, int startTime,
      int endTime, int numPost) throws InterruptedException {
    int currentStart = 1;
    // Use CountDownLatch to block the next phase until 10% of current phase has finished.
    final CountDownLatch phaseLatch = new CountDownLatch((int)Math.ceil(phaseThread / 10f));
    for (int i = 0; i < phaseThread; i++) {
      Runnable newThread = new SkiApiHandlerRunnable(phaseLatch, successCount, failCount,
          params.getAddress(), currentStart, currentStart + skierRange - 1, startTime, endTime,
          params.getSkiLifts(), numPost);
      pool.execute(newThread);
      currentStart += skierRange;
    }
    phaseLatch.await();
  }

  @Override
  public void run() {
    // Get the task start time.
    long taskStartTime = System.currentTimeMillis();
    // Calculate the number of thread of current phase so that we can determine the pool size.
    int phaseOneThreads = params.getMaxThreads() / 4;
    int phaseTwoThreads = params.getMaxThreads();
    int phaseThreeThreads = params.getMaxThreads() / 4;
    // Init the thread pool
    ExecutorService pool = Executors
        .newFixedThreadPool(phaseOneThreads + phaseTwoThreads + phaseThreeThreads);
    // Get phase attributes for phase one.
    int skierRange = params.getNumberSkiers() / phaseOneThreads;
    int startTime = 1;
    int endTime = 90;
    int numPost = params.getMeanLifts() / 10 * params.getNumberSkiers() / phaseOneThreads;

    // Start phase one
    try {
      phaseStart(pool, phaseOneThreads, skierRange, startTime, endTime, numPost);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Get phase attributes for phase two.
    startTime = 91;
    endTime = 360;
    skierRange = params.getNumberSkiers() / phaseTwoThreads;
    numPost = params.getMeanLifts() * 4 / 5 * params.getNumberSkiers() / phaseTwoThreads;

    // Start phase two.
    try {
      phaseStart(pool, phaseTwoThreads, skierRange, startTime, endTime, numPost);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Get phase attributes for phase three.
    startTime = 361;
    endTime = 420;
    numPost = params.getMeanLifts() / 10;
    skierRange = params.getNumberSkiers() / phaseThreeThreads;

    // Start phase three.
    try {
      phaseStart(pool, phaseThreeThreads, skierRange, startTime, endTime, numPost);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Wait for the finishing of all phases.
    pool.shutdown();
    while (!pool.isTerminated()) {
      try {
        pool.awaitTermination(MAX_PRIORITY, TimeUnit.HOURS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    // Calculate the wall time.
    long duration = System.currentTimeMillis() - taskStartTime;

    System.out.println("Finish " + successCount.getCount());
    System.out.println("Fails " + failCount.getCount());
    System.out.println("WallTime " + duration + "ms");

  }

}
