package lab4_zad2;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Consumer implements Runnable {
    private static int consumerAmount = 1;
    private final String id;
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.id = "C" + consumerAmount++;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int shelfNumber = buffer.consumerTakeTicket();
            try {
                sleep(new Random().nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\033[0;33m" + "Item taken from shelf: " + shelfNumber + " by customer: " + id);
            buffer.consumerGiveBackTicket(shelfNumber);
        }
    }
}
