package lab2;

import static java.lang.Thread.sleep;

public class LeftFirst {
    public static void main(String[] args) {
        BinarySemaphore[] forks = new BinarySemaphore[5];
        Philosopher[] philosophers = new Philosopher[5];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new BinarySemaphore();
        }

        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(forks[i], forks[(i+1)%philosophers.length]);
        }



        for (Philosopher philosopher : philosophers) {
            new Thread(() -> {
                while (true) {
                    philosopher.takeLeft();
                    philosopher.takeRight();
                    philosopher.eat();
                    philosopher.putAway();
                }
            }).start();
        }

    }
}
