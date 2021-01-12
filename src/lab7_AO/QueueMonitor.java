package lab7_AO;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class QueueMonitor {
    private final Queue<Request> allRequests = new LinkedList<>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition emptyQueue = lock.newCondition();

    public void addToQueue(Request request) {
        lock.lock();
        allRequests.add(request);
        emptyQueue.signal();
        lock.unlock();
    }

    public Request removeFromQueue() throws InterruptedException {
        lock.lock();
        while (allRequests.size() == 0) {
            System.out.println("\u001B[36m" + "No requests in queue");
            emptyQueue.await();
        }
        Request request = allRequests.remove();
        lock.unlock();
        return request;
    }
}
