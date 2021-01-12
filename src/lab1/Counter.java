package lab1;

public class Counter {
    private int value;

    public Counter() {}

    public void add() {
        this.value++;
    }

    public void subtract() {
        this.value--;
    }

    public int getValue() {
        return value;
    }
}
