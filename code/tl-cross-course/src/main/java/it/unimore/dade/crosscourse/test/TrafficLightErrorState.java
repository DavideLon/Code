package it.unimore.dade.crosscourse.test;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrafficLightErrorState {

    private final static Logger logger = LoggerFactory.getLogger(TrafficLightErrorState.class);

    private static final int MAX_ITERATIONS =20;

    private static final Integer LED_YELLOW = 1;

    private static final int TIMEOUT_ERROR_MS = 500;

    private static final GpioController gpio = GpioFactory.getInstance();

    private static GpioPinDigitalOutput yellowBlinking=null;

    public TrafficLightErrorState() {
        initPin();
    }

    private static void initPin() {
        yellowBlinking = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),
                "Blinking yellow error state",PinState.LOW);
        yellowBlinking.setShutdownOptions(true, PinState.LOW);
    }

    private static void blink(){
        if (yellowBlinking.isHigh()) {
            yellowBlinking.low();
        } else {
            yellowBlinking.high();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int countIterations=0;
        initPin();
        try {
            while (countIterations < MAX_ITERATIONS) {
                logger.info("Switching yellow value {}", yellowBlinking.getState());
                blink();
                Thread.sleep(TIMEOUT_ERROR_MS);
                countIterations++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
