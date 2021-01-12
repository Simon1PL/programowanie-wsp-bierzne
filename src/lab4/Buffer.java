package lab4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final List<Integer> items = new ArrayList<>();

    private boolean isFull() {
        return items.size() >= 10;
    }

    private final Lock lock = new ReentrantLock();
    private final Condition consumerCondition = lock.newCondition();
    private final Condition producerCondition = lock.newCondition();

    public void addItem(int id) {
        lock.lock();
        try {
            while (isFull()) {
                try {
                    producerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.items.add(id);
            System.out.println("Item added " + id);
            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public int takeItem() {
        lock.lock();
        try {
            while (items.isEmpty()) {
                try {
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int result = this.items.remove(0);
            System.out.println("Item taken " + result);
            producerCondition.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

}
