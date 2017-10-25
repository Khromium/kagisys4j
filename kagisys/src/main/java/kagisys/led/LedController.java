package kagisys.led;

import com.pi4j.io.gpio.*;
import kagisys.IOEvent;

/**
 * マルチスレッドでledを制御するためのもの
 */
public class LedController {
    private IOEvent ioEvent;

    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin;

    private int onMilliSec = 1000;
    private int offMilliSec = 1000;
    private int count = -1;
    private Led led;


    public LedController(IOEvent ioEvent) {
        this.ioEvent = ioEvent;
        pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PILOT", PinState.HIGH);
        pin.low();
    }

    /**
     * 点滅の設定
     *
     * @param onMilliSec  点灯時間
     * @param offMilliSec 消灯時間
     * @param count       繰り返す回数。マイナスは無限
     */
    public void setTwinkState(int onMilliSec, int offMilliSec, int count) {
        this.onMilliSec = onMilliSec;
        this.onMilliSec = offMilliSec;
        this.count = count;
    }

    public void startTwinkle() {
        led = new Led(ioEvent, pin, onMilliSec, offMilliSec, count);
        led.start();
    }


    public void flash() {
        led = new Led(ioEvent, pin, onMilliSec, offMilliSec, count);
        setTwinkState(50, 50, 1);
        led.start();
    }

    public void stopTwinkle() {
        if (led != null) led.stopThread();
    }
}
