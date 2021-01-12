package vsLab7;

import java.util.Random;

import static java.lang.Thread.sleep;
import static lab7_AO.Main.ADDITIONAL_WORK_TIME_PER_ITEM;
import static vsLab7.Buffer.K;

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
            int amount = new Random().nextInt(K) + 1;
            try {
                this.buffer.addItem(this.id, amount);
                sleep(ADDITIONAL_WORK_TIME_PER_ITEM * amount);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
