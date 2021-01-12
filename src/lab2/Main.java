package lab2;

import lab1.Counter;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) {

        BinarySemaphore semaphore = new BinarySemaphore();
        Counter counter = new Counter();
        List<Thread> threads = new ArrayList<>();

        Runnable r1 = () -> {
            semaphore.P();
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.add();
            semaphore.V();
        };

        Runnable r2 = () -> {
            semaphore.P();
            try {
                sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            counter.subtract();
            semaphore.V();
        };

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(r1);
            thread.start();
            threads.add(thread);
            thread = new Thread(r2);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(counter.getValue());

    }
}
