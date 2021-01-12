package lab6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    public static int SIZE;
    private final List<Integer> empty = new ArrayList<>();
    private final List<Integer> busy = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition consumersCondition = lock.newCondition();
    private final Condition producersCondition = lock.newCondition();

    public Monitor(int size) {
        SIZE = size;
        for (int i = 0; i < size; i++) {
            empty.add(i);
        }
    }

    Integer start_production() {
        lock.lock();
        Integer current = null;
        try {
            while(empty.isEmpty()) {
                producersCondition.await();
            }
            current = empty.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return current;
    }

    void end_production(int current, int producerID) {
        lock.lock();
        busy.add(current);
        consumersCondition.signal();
        System.out.println("\u001B[34m" + "Produced " + current + " by " + producerID + ", active thread = " + (SIZE - empty.size() - busy.size()) + " empty slots: " + empty.size() + " busy slots: " + busy.size());
        lock.unlock();
    }

    Integer start_consumption() {
        lock.lock();
        Integer current = null;
        try {
            while(busy.isEmpty()) {
                consumersCondition.await();
            }
            current = busy.remove(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return current;
    }

    void end_consumption(int current, int consumerID) {
        lock.lock();
        empty.add(current);
        producersCondition.signal();
        System.out.println("\u001B[31m" + "Consumed " + current + " by " + consumerID + ", active thread = " + (SIZE - empty.size() - busy.size()) + " empty slots: " + empty.size() + " busy slots: " + busy.size());
        lock.unlock();
    }
}
