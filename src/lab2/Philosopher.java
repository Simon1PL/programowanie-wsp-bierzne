package lab2;

import java.util.Random;

import static java.lang.Thread.sleep;

public class Philosopher {
    private static int n = 1;
    private BinarySemaphore leftFork;
    private BinarySemaphore rightFork;
    public final int id;

    Philosopher(BinarySemaphore leftFork, BinarySemaphore rightFork) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.id = n++;
    }

    public synchronized void takeLeft() {
        this.leftFork.P();
//        System.out.println("Left " + id);
    }

    public synchronized void takeRight() {
        this.rightFork.P();
//        System.out.println("Right " + id);
    }

    public synchronized void takeBoth() {
        this.leftFork.P();
        this.rightFork.P();
        eat();
    }

    public synchronized void putAway() {
        this.leftFork.V();
        this.rightFork.V();
    }

    public void eat() {
        System.out.println("Philosoph " + id + " is eating");
        try {
            sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Philosoph " + id + " ends");
    }
}
