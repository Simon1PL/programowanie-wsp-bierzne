package lab7_v1;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class QueueMonitor {
    private final Queue<Request> allRequests = new LinkedList<>();

    private final ReentrantLock lock = new ReentrantLock();

    public void addToQueue(Request request) {
        lock.lock();
        allRequests.add(request);
        lock.unlock();
    }

    public Request removeFromQueue() {
        lock.lock();
        Request request = null;
        if (allRequests.size() > 0) {
            request = allRequests.remove();
        }
        lock.unlock();
        return request;
    }
}
