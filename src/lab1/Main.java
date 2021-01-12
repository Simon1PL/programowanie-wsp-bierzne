package lab1;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class Main {

    public static void main(String[] args) {

        Counter counter = new Counter();
        List<Thread> threads = new ArrayList<>();

        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter.add();
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter.subtract();
            }
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
