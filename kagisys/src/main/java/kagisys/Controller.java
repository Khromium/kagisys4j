package kagisys;

import kagisys.led.LedController;
import kagisys.rc522.ReadRFID;
import kagisys.servo.Servo;
import kagisys.sql.SQLite;
import kagisys.tactSwitch.Switch;

import java.io.File;


public class Controller extends IOEvent {
    private Servo servo;
    private LedController ledController;
    private Switch tactSwitch;
    private ReadRFID readRFID;
    private boolean isRegister = false;
    private File dbFile = new File("./ids.db");

    public static void main(String[] args) {
        new Controller().start();
    }

    public void start() {
        readRFID = new ReadRFID(this);
        readRFID.start();
        ledController = new LedController(this);
        tactSwitch = new Switch(this);
        tactSwitch.start();
        servo = new Servo(this);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public synchronized void onEvent(int value, String data) {
        ledController.stopTwinkle();
        switch (value) {
            case IOEvent.LED:
                break;
            case IOEvent.SERVO:
                break;
            case IOEvent.BUTTON_ZERO:
                ledController.flash();
                servo.turnKey();
                break;
            case IOEvent.BUTTON_ONE:
                if (data.equals(IOEvent.LONG)) {
                    //登録モード
                    ledController.setTwinkState(1000, 1000, -1);
                    ledController.startTwinkle();
                    //                    led = new Led(this);
//                    led.setTwinkState(1000, 1000, -1);
//                    led.start();
                    isRegister = true;
                    System.out.println("touroku mode");
                }
                break;
            case IOEvent.RFID:
                if (isRegister) {
                    SQLite.putId(dbFile, data);
                    System.out.println(data + "was registered");
                    ledController.stopTwinkle();
                    isRegister = false;
                } else {
                    if (SQLite.isExist(dbFile, data)) {
                        System.out.println("verified id :" + data);
                        servo.turnKey();
                    } else {
                        System.out.println("invalid id :" + data);
                    }
                }
                ledController.flash();
                break;
        }
        System.out.println("value=" + value + "\n" + "args=" + data);
    }
}
