package lab7_AO;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Buffer {
    public static int takenItemsAmount = 0;
    public static int addedItemsAmount = 0;
    private final List<Integer> buffer = new LinkedList<>();

    private final int takingOneItemTime;
    private final int addingOneItemTime;

    public Buffer(int takingOneItemTime, int addingOneItemTime) {
        this.takingOneItemTime = takingOneItemTime;
        this.addingOneItemTime = addingOneItemTime;
    }

    public void addItem(int producerId, int amount) throws InterruptedException {
        sleep(addingOneItemTime * amount);
        for (int i = 0; i < amount; i++) {
            this.buffer.add(producerId);
        }
        addedItemsAmount += amount;
        System.out.println("\u001B[32m" + "Produced: " + amount + " by P" + producerId + ". In buffer: " + buffer.size());
    }

    public void takeItem(int consumerId, int amount) throws InterruptedException {
        int[] takenItems = new int[amount];
        sleep(takingOneItemTime * amount);
        for (int i = 0; i < amount; i++) {
            takenItems[i] = this.buffer.remove(0);
        }
        takenItemsAmount += amount;
        System.out.println("\u001B[31m" + "Consumed: " + amount + " by C" + consumerId + ". In buffer: " + buffer.size());
    }

    public int getSize() {
        return buffer.size();
    }
}
