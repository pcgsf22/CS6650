/**
 * The thread safe count.
 */
public class ActiveCount {
    private int activeThreadCount = 0;

    /**
     * Increment count by one.
     */
    public synchronized void incrementCount() {
        activeThreadCount++;
    }

    /**
     * Increment count by num.
     *
     * @param num the num
     */
    public synchronized void incrementBy(int num) {
        activeThreadCount+=num;
    }

    /**
     * Gets count.
     *
     * @return the count
     */
    public synchronized int getCount() {
        return activeThreadCount;
    }
}
