package kagisys.tactSwitch;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import kagisys.IOEvent;

public class Switch extends Thread {
    private IOEvent ioEvent;
    private double timer0, timer1;


    public Switch(IOEvent ioEvent) {
        this.ioEvent = ioEvent;
    }

    @Override
    public void run() {
        switchZero();
        switchOne();
    }

    public void switchZero() {
        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.OFF);
        button.setShutdownOptions(true);

        button.addListener((GpioPinListenerDigital) pin_event -> {
            if (pin_event.getState().isHigh()) {
                timer(true, 0);
            } else if (pin_event.getState().isLow()) {
                timer(false, 0);
            }

        });

//        gpio.shutdown();
    }


    public void switchOne() {
        final GpioController gpio = GpioFactory.getInstance();

        final GpioPinDigitalInput button = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, PinPullResistance.OFF);
        button.setShutdownOptions(true);

        button.addListener((GpioPinListenerDigital) pin_event -> {
            if (pin_event.getState().isHigh()) {
                timer(true, 1);
            } else if (pin_event.getState().isLow()) {
                timer(false, 1);
            }

        });
//        gpio.shutdown();
    }

    private void timer(boolean isStart, int index) {
        switch (index) {
            case 0:
                if (isStart) {
                    timer0 = System.currentTimeMillis();
                } else {
                    if (System.currentTimeMillis() - timer0 > 2000) {
                        ioEvent.onEvent(IOEvent.BUTTON_ZERO, IOEvent.LONG);
                    } else {
                        ioEvent.onEvent(IOEvent.BUTTON_ZERO, IOEvent.SHORT);
                    }
                }
                break;
            case 1:
                if (isStart) {
                    timer1 = System.currentTimeMillis();
                } else {
                    if (System.currentTimeMillis() - timer1 > 2000) {
                        ioEvent.onEvent(IOEvent.BUTTON_ONE, IOEvent.LONG);
                    } else {
                        ioEvent.onEvent(IOEvent.BUTTON_ONE, IOEvent.SHORT);
                    }
                }
                break;
        }
    }
}

