package lab7_v1;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Buffer {
    private final List<Integer> buffer = new LinkedList<>();

    private final int takingOneItemTime;
    private final int addingOneItemTime;

    public Buffer(int takingOneItemTime, int addingOneItemTime) {
        this.takingOneItemTime = takingOneItemTime;
        this.addingOneItemTime = addingOneItemTime;
    }

    public void addItem(int producerId, int amount) {
        try {
            sleep(addingOneItemTime * amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < amount; i++) {
            this.buffer.add(producerId);
        }
        System.out.println("\u001B[32m" + "Produced: " + amount + " by P" + producerId + ". In buffer: " + buffer.size());
    }

    public void takeItem(int consumerId, int amount) {
        int[] takenItems = new int[amount];
        try {
            sleep(takingOneItemTime * amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < amount; i++) {
            takenItems[i] = this.buffer.remove(0);
        }
        System.out.println("\u001B[31m" + "Consumed: " + amount + " by C" + consumerId + /*" " + Arrays.toString(takenItems) + */". In buffer: " + buffer.size());
    }

    public int getSize() {
        return buffer.size();
    }
}
