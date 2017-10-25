package kagisys.servo;

import com.pi4j.io.gpio.*;
import com.pi4j.util.CommandArgumentParser;
import com.pi4j.wiringpi.SoftPwm;
import kagisys.IOEvent;

/**
 * サーボモータの制御部分
 * キュート化導入してマルチタスクにすると他と干渉して上手く動かない事がある。
 * 具体的には連続してボタンを高速に押したときにPWM停止でフリーズする。
 */
public class Servo {
    private IOEvent ioEvent;

    private GpioController gpio = GpioFactory.getInstance();
    private Pin pin = CommandArgumentParser.getPin(
            RaspiPin.class,
            RaspiPin.GPIO_00);
    private GpioPinPwmOutput pwm;



    public Servo(IOEvent ioEvent) {
        this.ioEvent = ioEvent;
        pwm = gpio.provisionSoftPwmOutputPin(pin);
    }


    /**
     * 鍵の開け閉めを行う。元の状態次第で勝手に角度が変化します。
     */
    public synchronized void turnKey() {
        System.out.println("start pwm");
        pwm.setPwmRange(100);//出力時間を100分割
        pwm.setShutdownOptions(true, PinState.LOW);

        if (pwm.getPwm() < 18) {
            pwm.setPwm(25);//デューティー比25 180°
        } else {
            pwm.setPwm(6);//0°
        }
        System.out.println("pwm set");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("stopping pwm");
        SoftPwm.softPwmStop(0);
//        gpio.shutdown();
        ioEvent.onEvent(IOEvent.SERVO, pwm.getPwm() == 25 ? "closed" : "opened");

    }

}
