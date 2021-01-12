package lab6;

public class Main {
    final static int BUFFER_SIZE = 10;
    final static int PRODUCER_AMOUNT = 4;
    final static int CONSUMER_AMOUNT = 4;

    public static void main(String[] args) {

        Monitor monitor = new Monitor(BUFFER_SIZE);
        Buffer buffer = new Buffer();

        Consumer[] consumers = new Consumer[CONSUMER_AMOUNT];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(monitor, buffer);
            new Thread(consumers[i]).start();
        }

        Producer[] producers = new Producer[PRODUCER_AMOUNT];
        for(int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(monitor, buffer);
            new Thread(producers[i]).start();
        }

    }
}
