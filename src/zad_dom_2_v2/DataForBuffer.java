package zad_dom_2_v2;

/**
 * DataForBuffer - 2 fields: ID of sender, amount of items
 */
public class DataForBuffer {
    private final String senderID;
    private final int itemsAmount;

    public DataForBuffer(String senderID, int itemsAmount) {
        this.senderID = senderID;
        this.itemsAmount = itemsAmount;
    }

    public int itemsAmount() {
        return itemsAmount;
    }

    public String senderID() {
        return senderID;
    }
}
