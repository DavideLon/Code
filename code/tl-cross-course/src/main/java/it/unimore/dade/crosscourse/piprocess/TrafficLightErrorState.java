package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;

public class TrafficLightErrorState {

    private static final Integer LED_YELLOW = 1;

    private static final int TIMEOUT_ERROR_MS = 500;

    private static final GpioController gpio = GpioFactory.getInstance();

    private static GpioPinDigitalOutput yellowBlinking=null;

    public TrafficLightErrorState() {
        initPin();
    }

    private void initPin() {
        yellowBlinking = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),
                "Blinking yellow error state",PinState.LOW);
    }

    private static void blink(){
        if (yellowBlinking.isHigh()) {
            yellowBlinking.low();
        } else {
            yellowBlinking.high();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try {
            while (true) {
                blink();
                Thread.sleep(TIMEOUT_ERROR_MS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
