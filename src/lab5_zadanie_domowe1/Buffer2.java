package lab5_zadanie_domowe1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer2 {
    public static final int K = 5;
    private final List<Integer> items = new ArrayList<>();

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
        try {
            while (lock.hasWaiters(firstProducerCondition)) {
                // nie dziala, bo: moze zostac obudzony watek w kolejce first, przez co kolejka first nie bedzie miala waitersow, ale nastepnie okaze sie ze nie ma dosc surowcow i watek wroci do kolejki first(otrzymamy 2 watki w kolejce first)
                // koncówka: P1 jest w kolejce firstProducer, C1 w firstConsumer chce 5, P2 produkuje 4 element(było 0), próbuje wznowić firstConsumer, nie może bo nie ma dostepnych 5, próbuje wznowić producers, nie może bo już jakiś czeka w first. Nie wznawia nic...
                try {
                    System.out.println("\u001B[35mP" + id + ": " + amount + " wait in producersCondition\u001B[0m");
                    producersCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (isNotEnoughSpace(amount)) {
                try {
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
            firstConsumerCondition.signal();
            producersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void takeItem(int id, int amount) {
        lock.lock();
        try {
            while (lock.hasWaiters(firstConsumerCondition)) {
                try {
                    System.out.println("\u001B[33mC" + id + ": " + amount + " wait in consumersCondition\u001B[0m");
                    consumersCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (isNotEnoughElements(amount)) {
                try {
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
            firstProducerCondition.signal();
            consumersCondition.signal();
        } finally {
            lock.unlock();
        }
    }

}
