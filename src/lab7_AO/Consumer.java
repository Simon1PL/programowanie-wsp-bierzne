package lab7_AO;

import java.util.Random;

import static java.lang.Thread.sleep;
import static lab7_AO.Main.*;

public class Consumer implements Runnable {
    private static int consumerAmount = 1;
    private final int id;
    private final Proxy proxy;

    public Consumer(Proxy proxy) {
        this.proxy = proxy;
        this.id = consumerAmount++;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int amount = new Random().nextInt(MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION) + 1;
                int ADDITIONAL_WORK_TIME = amount * ADDITIONAL_WORK_TIME_PER_ITEM;
                Future future = proxy.takeItem(id, amount);
                int savedTime = 0;
                while (!future.hasResult()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    if (savedTime < ADDITIONAL_WORK_TIME) {
                        sleep(100);
                        savedTime += 100;
                    }
                }
                if (savedTime < ADDITIONAL_WORK_TIME) {
                    sleep(ADDITIONAL_WORK_TIME - savedTime);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
