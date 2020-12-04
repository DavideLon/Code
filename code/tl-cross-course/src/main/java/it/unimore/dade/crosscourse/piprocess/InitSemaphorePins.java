package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitSemaphorePins {

    private final static Logger logger = LoggerFactory.getLogger(InitSemaphorePins.class);

    private static final int LED_GREEN = 0;
    private static final int LED_YELLOW = 1;
    private static final int LED_RED = 2;

    public static final GpioController gpio = GpioFactory.getInstance();
    static protected GpioPinDigitalOutput greenLed = null;
    protected static GpioPinDigitalOutput yellowLed = null;
    protected static GpioPinDigitalOutput redLed = null;

    static protected boolean inited= false;

    public InitSemaphorePins() {
    }

    public static GpioController getGpio() {
        return gpio;
    }

    public static GpioPinDigitalOutput getGreenLed() {
        return greenLed;
    }

    public static GpioPinDigitalOutput getYellowLed() {
        return yellowLed;
    }

    public static GpioPinDigitalOutput getRedLed() {
        return redLed;
    }

    public static boolean isInited() {
        return inited;
    }

    public static void init() {

        // provision gpio pins as output pins and make sure are set to LOW at startup
        greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN),   // PIN NUMBER
                "My Green LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        greenLed.setShutdownOptions(true, PinState.LOW);

        logger.info("Led properties"+ greenLed.getProperties());

        yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),   // PIN NUMBER
                "My Yellow LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        yellowLed.setShutdownOptions(true, PinState.LOW);

        logger.info("Led properties"+ yellowLed.getProperties());

        redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_RED),   // PIN NUMBER
                "My Red LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        redLed.setShutdownOptions(true, PinState.LOW);

        logger.info("Led properties"+ redLed.getProperties());

        inited= true;

    }
}
