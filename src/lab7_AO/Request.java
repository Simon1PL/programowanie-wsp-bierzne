package lab7_AO;

public interface Request {
    void execute() throws InterruptedException;
    boolean guard();
}
