package lab2;

public class Semaphore {
    private final int N;
    private int free;

    Semaphore(int n) {
        this.N = n;
        this.free = N;
    }

    public synchronized void P() { // acquire
        while (free == 0) { //while a nie if bo java czasem sama odblokowuje wątki
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        free--;
    }

    public synchronized void V() { // release
        free = (free + 1) % N;
        notify();
    }

}
