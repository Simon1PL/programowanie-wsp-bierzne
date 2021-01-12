package lab2;

public class Lackey {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(4);
        BinarySemaphore[] forks = new BinarySemaphore[5];
        Philosopher[] philosophers = new Philosopher[5];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new BinarySemaphore();
        }

        for (int i = 0; i < philosophers.length; i++) {
            philosophers[i] = new Philosopher(forks[i], forks[(i+1)%philosophers.length]);
        }


        for (Philosopher p : philosophers) {
            new Thread(() -> {
                semaphore.P();
                p.takeBoth();
                p.putAway();
                semaphore.V();
            }).start();
        }

    }
}
