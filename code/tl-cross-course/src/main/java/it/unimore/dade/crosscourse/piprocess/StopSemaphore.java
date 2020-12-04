package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopSemaphore implements Runnable {

    protected SemaphoreStatusListener semaphoreStatusListener;

    private final static Logger logger = LoggerFactory.getLogger(StopSemaphore.class);

    private static final int LED_GREEN = 0;
    private static final int LED_YELLOW = 1;
    private static final int LED_RED = 2;

    private static GpioController gpio = InitSemaphorePins.gpio;
    private static GpioPinDigitalOutput greenLed = InitSemaphorePins.greenLed;
    private static GpioPinDigitalOutput yellowLed =InitSemaphorePins.yellowLed;
    private static GpioPinDigitalOutput redLed = InitSemaphorePins.redLed;

    public static InitSemaphorePins initSemaphorePins = new InitSemaphorePins();

    public StopSemaphore(GpioController externalGpio, GpioPinDigitalOutput green,  GpioPinDigitalOutput yellow,  GpioPinDigitalOutput red) {

        gpio= externalGpio;
        greenLed=green;
        yellowLed= yellow;
        redLed= red;
        //initPins();
    }

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
        yellowLed.setShutdownOptions(true, PinState.LOW);
    }

    private static void pinsOff() {
        greenLed.low();
        yellowLed.low();
        redLed.low();
    }

    public void run() {
        if(yellowLed.isMode(PinMode.DIGITAL_OUTPUT)&&(greenLed.isMode(PinMode.DIGITAL_OUTPUT))&&redLed.isMode(PinMode.DIGITAL_OUTPUT)) {
            try {
                //initPins();
                logger.info("Switching Off all pins");
                pinsOff();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        else{
            initPins();
            run();
        }
    }

}
