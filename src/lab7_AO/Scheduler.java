package lab7_AO;

import java.util.LinkedList;
import java.util.List;

public class Scheduler implements Runnable {
    private final QueueMonitor queueMonitor;
    private final List<ConsumeMethodRequest> consumeRequests = new LinkedList<>();
    private final List<ProduceMethodRequest> produceRequests = new LinkedList<>();

    public Scheduler(QueueMonitor queueMonitor) {
        this.queueMonitor = queueMonitor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (consumeRequests.size() > 0 && consumeRequests.get(0).guard()) {
                    consumeRequests.remove(0).execute();
                } else if (produceRequests.size() > 0 && produceRequests.get(0).guard()) {
                    produceRequests.get(0).execute();
                } else {
                    Request request = queueMonitor.removeFromQueue();

                    if (request instanceof ConsumeMethodRequest) {
                        if (request.guard()) {
                            request.execute();
                        } else {
                            consumeRequests.add((ConsumeMethodRequest) request);
                        }
                    } else if (request instanceof ProduceMethodRequest) {
                        if (request.guard()) {
                            request.execute();
                        } else {
                            produceRequests.add((ProduceMethodRequest) request);
                        }
                    } else {
                        System.out.println("\u001B[36m" + "Something goes wrong, the request type is unavailable");
                    }
                }
            }
        } catch (InterruptedException e) {
            // e.printStackTrace();
        }
    }
}
