package lab7_v1;

import java.util.Random;

import static java.lang.Thread.sleep;
import static lab7_v1.Main.*;

public class Producer implements Runnable {
    private static int producerAmount = 1;
    private final int id;
    private final Proxy proxy;

    public Producer(Proxy proxy) {
        this.id = producerAmount++;
        this.proxy = proxy;
    }

    @Override
    public void run() {
        while (true) {
            try {
                int amount = new Random().nextInt(MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION) + 1;
                Future future = proxy.addItem(id, amount);
                int savedTime = - amount * ADDING_ONE_ITEM_TIME;
                while (!future.hasResult()) {
                    sleep(100);
                    savedTime += 100;
                }
                System.out.println("\u001B[34m" + "Produced: " + future.getResult() + " by P" + id + " saved time " + savedTime);
                if (savedTime < ADDITIONAL_WORK_TIME) {
                    sleep(ADDITIONAL_WORK_TIME - savedTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
