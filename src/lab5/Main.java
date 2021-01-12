package lab5;

public class Main {
    public static void main(String[] args) {

        Buffer buffer = new Buffer();

        Consumer[] consumers = new Consumer[5];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(buffer);
            new Thread(consumers[i]).start();
        }

        Producer[] producers = new Producer[5];
        for(int i = 0; i < producers.length; i++) {
            producers[i] = new Producer(buffer);
            new Thread(producers[i]).start();
        }

    }
}
