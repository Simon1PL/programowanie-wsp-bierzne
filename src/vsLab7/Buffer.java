package vsLab7;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;
import static lab7_AO.Main.*;

public class Buffer {
    public static int takenItemsAmount = 0;
    public static int addedItemsAmount = 0;
    public static final int K = MAX_PRODUCT_AMOUNT_ON_ONE_OPERATION;
    private final List<Integer> items = new LinkedList<>();
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

    public void addItem(int id, int amount) throws InterruptedException {
        lock.lock();
        try {
            while (isFirstProducer) {
                producersCondition.await();
            }
            while (isNotEnoughSpace(amount)) {
                isFirstProducer = true;
                firstProducerCondition.await();
            }
            sleep(ADDING_ONE_ITEM_TIME * amount);
            for (int i = 0; i < amount; i++) {
                this.items.add(id);
            }
            addedItemsAmount += amount;
            System.out.println("\u001B[32m" + "Produced: " + amount + " by P" + id + ". In buffer: " + items.size());
            isFirstProducer = false;
            firstConsumerCondition.signal();
            producersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void takeItem(int id, int amount) throws InterruptedException {
        lock.lock();
        try {
            while (isFirstConsumer) {
                consumersCondition.await();
            }
            while (isNotEnoughElements(amount)) {
                isFirstConsumer = true;
                firstConsumerCondition.await();
            }
            sleep(TAKING_ONE_ITEM_TIME * amount);
            this.items.subList(0, amount).clear();
            takenItemsAmount += amount;
            System.out.println("\u001B[31m" + "Consumed: " + amount + " by C" + id + ". In buffer: " + items.size());
            isFirstConsumer = false;
            firstProducerCondition.signal();
            consumersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

}
