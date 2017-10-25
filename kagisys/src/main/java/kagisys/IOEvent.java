package kagisys;

public abstract class IOEvent {

    public static final int RFID = 1;
    public static final int SERVO = 2;
    public static final int LED = 4;
    public static final int BUTTON_ZERO = 8;
    public static final int BUTTON_ONE = 16;

    public static final String LONG = "LONG";
    public static final String SHORT = "SHORT";

    abstract public void onEvent(int value, String data);
}
