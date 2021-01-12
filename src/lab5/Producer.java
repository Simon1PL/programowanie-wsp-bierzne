package lab5;

import java.util.Random;

import static lab5.Buffer.K;

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
            int amount = new Random().nextInt(K) + 1;
            this.buffer.addItem(this.id, amount);
        }
    }
}
