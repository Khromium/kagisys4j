package kagisys.led;

import com.pi4j.io.gpio.*;
import kagisys.IOEvent;

/**
 * LEDの制御部分
 * LEDの点滅処理を他と分けて行うためにThreadにしています。
 */
public class Led extends Thread {


    private boolean isTwinkling = true;
    private IOEvent ioEvent;
    private GpioPinDigitalOutput pin;

    private int onMilliSec;
    private int offMilliSec;
    private int count;


    public Led(IOEvent ioEvent, GpioPinDigitalOutput pin, int onMilliSec, int offMilliSec, int count) {
        this.ioEvent = ioEvent;
        this.pin = pin;
        this.onMilliSec = onMilliSec;
        this.offMilliSec = offMilliSec;
        this.count = count;
    }


    /**
     * スレッド停止用の変数。Thread.stopは非推奨。
     */
    public void stopThread() {
        this.isTwinkling = false;
    }

    @Override
    public void run() {
        pin.setShutdownOptions(true, PinState.LOW);
        while (isTwinkling) {
            try {
                pin.high();
                sleep(onMilliSec);
                pin.low();
                sleep(offMilliSec);

                if (count > 0) {
                    count--;
                } else if (count < 0) { //無限
                    continue;
                } else if (count == 0) { //回数が終わったら
                    isTwinkling = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pin.low();
        ioEvent.onEvent(IOEvent.LED, "led");
    }


}
