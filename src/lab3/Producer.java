package lab3;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Producer implements Runnable {
    private static int producerAmount = 1;
    private final int id;
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.id = producerAmount++;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
//            try {
//                sleep(new Random().nextInt(500));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            this.buffer.addItem(this.id);
        }
    }
}
