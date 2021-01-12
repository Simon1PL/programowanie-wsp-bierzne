package lab4_zad2;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    private final Queue<Integer> fullTickets = new ArrayDeque<>();
    private final Queue<Integer> emptyTickets = new ArrayDeque<>();

    private final Lock lock = new ReentrantLock();
    private final Condition consumerCondition = lock.newCondition();
    private final Condition producerCondition = lock.newCondition();

    public Buffer() {
        for (int i = 0; i < 5; i++) {
            emptyTickets.add(i+1);
        }
    }

    public int producerTakeTicket() {
        lock.lock();
        try {
            while (emptyTickets.isEmpty()) {
                try {
                    producerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return emptyTickets.remove();
        } finally {
            lock.unlock();
        }
    }

    public void producerGiveBackTicket(int shelfNumber) {
        lock.lock();
        try {
            this.fullTickets.add(shelfNumber);
            consumerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public int consumerTakeTicket() {
        lock.lock();
        try {
            while (fullTickets.isEmpty()) {
                try {
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return fullTickets.remove();
        } finally {
            lock.unlock();
        }
    }

    public void consumerGiveBackTicket(int shelfNumber) {
        lock.lock();
        try {
            this.emptyTickets.add(shelfNumber);
            producerCondition.signal();
        } finally {
            lock.unlock();
        }
    }

}
