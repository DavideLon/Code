package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Semaphore extends Runnable {
    /*
    public static final int LED_GREEN = 0;
    public static final int LED_YELLOW = 1;
    public static final int LED_RED = 2;

    private static final int TIMEOUT_ON_MS = 10;

    private static final GpioController gpio = GpioFactory.getInstance();
    private static GpioPinDigitalOutput greenLed = null;
    private static GpioPinDigitalOutput yellowLed = null;
    private static GpioPinDigitalOutput redLed = null;

    private final static Logger logger = LoggerFactory.getLogger(StartSemaphore.class);



    public static void initPins(){
        // provision gpio pins as output pins and make sure are set to LOW at startup
        greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN),   // PIN NUMBER
                "My Green LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        greenLed.setShutdownOptions(true, PinState.LOW);
        yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),   // PIN NUMBER
                "My Yellow LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        yellowLed.setShutdownOptions(true, PinState.LOW);
        redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_RED),   // PIN NUMBER
                "My Red LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        redLed.setShutdownOptions(true, PinState.LOW);
    }

*/
    @Override
    void run();
}
