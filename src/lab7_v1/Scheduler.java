package lab7_v1;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Scheduler implements Runnable {
    private final QueueMonitor queueMonitor;
    private final List<ConsumeMethodRequest> consumeRequests = new LinkedList<>();
    private final List<ProduceMethodRequest> produceRequests = new LinkedList<>();

    public Scheduler(QueueMonitor queueMonitor) {
        this.queueMonitor = queueMonitor;
    }

    @Override
    public void run() {
        while (true) {
            if (consumeRequests.size() > 0 && consumeRequests.get(0).guard()) {
                consumeRequests.remove(0).execute();
            }
            else if (produceRequests.size() > 0 && produceRequests.get(0).guard()){
                produceRequests.get(0).execute();
            }
            else {
                Request request = queueMonitor.removeFromQueue();

                if (request instanceof ConsumeMethodRequest) {
                    if (request.guard()) {
                        request.execute();
                    }
                    else {
                        consumeRequests.add((ConsumeMethodRequest) request);
                    }
                }
                else if (request instanceof ProduceMethodRequest) {
                    if (true) {
                        request.execute();
                    }
                    else {
                        produceRequests.add((ProduceMethodRequest) request);
                    }
                }
                else if (request == null) {
                    System.out.println("No requests in queue");
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("Something goes wrong, the request type is unavailable");
                }
            }
        }
    }
}
