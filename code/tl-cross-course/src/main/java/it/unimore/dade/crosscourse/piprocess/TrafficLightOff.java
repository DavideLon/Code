package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;

public class TrafficLightOff {

    private static final int LED_GREEN = 0;
    private static final int LED_YELLOW = 1;
    private static final int LED_RED = 2;

    private static final GpioController gpio = GpioFactory.getInstance();

    public TrafficLightOff() {
    }

    private static void pinsOff() {
        GpioPinDigitalOutput greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN),
                PinState.LOW);      // PIN STARTUP STATE
        GpioPinDigitalOutput yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),
                PinState.LOW);      // PIN STARTUP STATE
        GpioPinDigitalOutput redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_RED),
                PinState.LOW);      // PIN STARTUP STATE
    }

    public static void main(String[] args) {
        pinsOff();
    }

}
