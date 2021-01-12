package vsLab7;

import java.util.Random;

import static java.lang.Thread.sleep;
import static lab7_AO.Main.ADDITIONAL_WORK_TIME_PER_ITEM;
import static vsLab7.Buffer.K;

public class Consumer implements Runnable {
    private static int consumerAmount = 1;
    private final int id;
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.id = consumerAmount++;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int amount = new Random().nextInt(K) + 1;
            try {
                buffer.takeItem(id, amount);
            } catch (InterruptedException e) {
                break;
            }
            try {
                sleep(ADDITIONAL_WORK_TIME_PER_ITEM * amount);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
