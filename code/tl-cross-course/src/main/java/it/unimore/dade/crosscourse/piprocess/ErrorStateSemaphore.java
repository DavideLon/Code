package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorStateSemaphore implements Semaphore {

    private final static Logger logger = LoggerFactory.getLogger(ErrorStateSemaphore.class);

    protected SemaphoreStatusListener semaphoreStatusListener;

    private static final int MAX_ITERATIONS =20;

    private static final Integer LED_YELLOW = 1;

    private static final int TIMEOUT_ERROR_MS = 500;

    private static GpioController gpio = InitSemaphorePins.gpio;

    private static GpioPinDigitalOutput yellowLed = InitSemaphorePins.yellowLed;


    public ErrorStateSemaphore(GpioController externalGpio, GpioPinDigitalOutput yellow) {

        gpio= externalGpio;
        yellowLed= yellow;

        //initPin();
    }

    private static void initPin() {
        yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),
                "Blinking yellow error state",PinState.LOW);
        yellowLed.setShutdownOptions(true, PinState.LOW);
    }

    private static void blink(){
        if (yellowLed.isHigh()) {
            yellowLed.low();
        } else {
            yellowLed.high();
        }
    }

    public void run() {
        //int countIterations=0;
        //initPin();
        if(yellowLed.isMode(PinMode.DIGITAL_OUTPUT)) {

            try {
                while (true) {
                    logger.info("Switching yellow ---> ON");
                    blink();
                    Thread.sleep(TIMEOUT_ERROR_MS);
                    //countIterations++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        else{
            initPin();
            run();
        }
    }
}
