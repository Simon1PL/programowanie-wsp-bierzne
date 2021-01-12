package lab7_v1;

public class Main {
    final static int TAKING_ONE_ITEM_TIME = 100; // milliseconds
    final static int ADDING_ONE_ITEM_TIME = 100; // milliseconds
    final static int PRODUCER_AMOUNT = 4;
    final static int CONSUMER_AMOUNT = 4;
    final static int MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION = 20;
    final static int ADDITIONAL_WORK_TIME = 10000; // milliseconds

    public static void main(String[] args) {
        Buffer buffer = new Buffer(TAKING_ONE_ITEM_TIME, ADDING_ONE_ITEM_TIME);
        QueueMonitor queueMonitor = new QueueMonitor();
        Scheduler scheduler = new Scheduler(queueMonitor);
        Proxy proxy = new Proxy(queueMonitor, buffer);

        Consumer[] consumers = new Consumer[CONSUMER_AMOUNT];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(proxy);
            new Thread(consumers[i]).start();
        }

        Producer[] producers = new Producer[PRODUCER_AMOUNT];
        for(int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(proxy);
            new Thread(producers[i]).start();
        }

        new Thread(scheduler).start();

    }
}
