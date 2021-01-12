package lab7_v1;

public class Future {
    private boolean hasResult = false;
    private int result;

    public Future() { }

    public boolean hasResult() {
        return hasResult;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
        this.hasResult = true;
    }
}
