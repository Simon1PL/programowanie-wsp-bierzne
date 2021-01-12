package lab3;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private final List<Integer> items = new ArrayList<>();

    private boolean isFull() {
        return items.size() >= 10;
    }

    public synchronized void addItem(int id) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.items.add(id);
        System.out.println("Item added " + id);
        notify();
    }

    public synchronized int takeItem() {
        while (items.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        int result = this.items.remove(0);
        System.out.println("Item taken " + result);
        notify();
        return result;
    }

}
