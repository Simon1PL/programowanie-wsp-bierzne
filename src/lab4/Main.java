package lab4;

public class Main {
    public static void main(String[] args) {

        Buffer buffer = new Buffer();

        Producer producer = new Producer(buffer);

        Consumer[] consumers = new Consumer[1000];
        for(int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer(buffer);
            new Thread(consumers[i]).start();
        }

        new Thread(producer).start();

    }
}
