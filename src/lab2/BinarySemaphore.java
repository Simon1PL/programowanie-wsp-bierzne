package lab2;

public class BinarySemaphore {
    private boolean isBlock = false;

    public synchronized void P() { // acquire
        while (isBlock) { //while a nie if bo java czasem sama odblokowuje wątki
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isBlock = true;
    }

    public synchronized void V() { // release
        isBlock = false;
        notify();
    }

}
