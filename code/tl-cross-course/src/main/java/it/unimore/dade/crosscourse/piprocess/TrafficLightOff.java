package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;

public class TrafficLightOff {

    private static final int LED_GREEN = 0;
    private static final int LED_YELLOW = 1;
    private static final int LED_RED = 2;

    private static final GpioController gpio = GpioFactory.getInstance();
    private static GpioPinDigitalOutput greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN),
            PinState.LOW);      // PIN STARTUP STATE
    private static GpioPinDigitalOutput yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),
            PinState.LOW);      // PIN STARTUP STATE
    private static GpioPinDigitalOutput redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_RED),
            PinState.LOW);      // PIN STARTUP STATE

     private static void pinsOff() {

        greenLed.low();
        yellowLed.low();
        redLed.low();
    }

    public static void main(String[] args) {
        pinsOff();
    }

}
