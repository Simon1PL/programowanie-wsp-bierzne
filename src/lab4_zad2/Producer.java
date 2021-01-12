package lab4_zad2;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Producer implements Runnable {
    private static int producerAmount = 1;
    private final String id;
    private final Buffer buffer;

    public Producer(Buffer buffer) {
        this.id = "P" + producerAmount++;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            int shelfNumber = buffer.producerTakeTicket();
            try {
                sleep(new Random().nextInt(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("\033[0;35m" + "Item added on shelf: " + shelfNumber + " by producer: " + id);
            buffer.producerGiveBackTicket(shelfNumber);
        }
    }
}
