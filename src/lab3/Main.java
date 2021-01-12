package lab3;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) {

        Buffer buffer = new Buffer();

        Producer producer = new Producer(buffer);

        Runnable consumer = () -> {
            while (true) {
                try {
                    sleep(new Random().nextInt(500));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                buffer.takeItem();
            }
        };

        new Thread(producer).start();
        new Thread(consumer).start();


    }
}
