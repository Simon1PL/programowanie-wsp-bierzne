package lab5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
    public static final int K = 5;
    private final List<Integer> items = new ArrayList<>();
    private boolean isFirstConsumer = false;
    private boolean isFirstProducer = false;

    private boolean isNotEnoughSpace(int amount) {
        return items.size() + amount > 2 * K;
    }

    private boolean isNotEnoughElements(int amount) {
        return items.size() - amount < 0;
    }

    private final ReentrantLock lock = new ReentrantLock();
    private final Condition consumersCondition = lock.newCondition();
    private final Condition firstConsumerCondition = lock.newCondition();
    private final Condition producersCondition = lock.newCondition();
    private final Condition firstProducerCondition = lock.newCondition();

    public void addItem(int id, int amount) {
        lock.lock();
        //lock.hasWaiters()... z ksiazki jest z hasWaiters, pokazac zakleszczenie
        //tutaj tez mogloby wystapic zakleszczenie/zaglodzenie/poprostu 2 watki w first jesli akurat java obudzi losowo watek gdy juz jeden watek bedzie w momencie wyjscia z petli isFirstProducer a przed zmianą isFirstProducer na true???
        try {
            while (isFirstProducer) {
                try {
                    System.out.println("\u001B[35mP" + id + ": " + amount + " wait in producersCondition\u001B[0m");
                    producersCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (isNotEnoughSpace(amount)) {
                try {
                    isFirstProducer = true;
                    System.out.println("\u001B[31mP" + id + ": " + amount + " wait in firstProducerCondition\u001B[0m");
                    firstProducerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < amount; i++) {
                this.items.add(id);
            }
            System.out.println(amount + " items added by P" + id);
            isFirstProducer = false;
            firstConsumerCondition.signal();
            producersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void takeItem(int id, int amount) {
        lock.lock();
        try {
            while (isFirstConsumer) {
                try {
                    System.out.println("\u001B[33mC" + id + ": " + amount + " wait in consumersCondition\u001B[0m");
                    consumersCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (isNotEnoughElements(amount)) {
                try {
                    isFirstConsumer = true;
                    System.out.println("\u001B[32mC" + id + ": " + amount + " wait in firstConsumerCondition\u001B[0m");
                    firstConsumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < amount; i++) {
                this.items.remove(0);
            }
            System.out.println(amount + " items removed by C" + id);
            isFirstConsumer = false;
            firstProducerCondition.signal();
            consumersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

}
