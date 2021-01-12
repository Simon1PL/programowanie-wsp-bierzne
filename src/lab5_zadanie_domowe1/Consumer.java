package lab5_zadanie_domowe1;

import java.util.Random;

import static java.lang.Thread.sleep;
import static lab5.Buffer.K;

public class Consumer implements Runnable {
    private static int consumerAmount = 1;
    private final int id;
    private final Buffer2 buffer;

    public Consumer(Buffer2 buffer) {
        this.id = consumerAmount++;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            try {
                sleep(new Random().nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int amount = new Random().nextInt(K) + 1;
            buffer.takeItem(id, amount);
        }
    }
}
