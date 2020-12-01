package it.unimore.dade.crosscourse.piprocess;

import java.util.ArrayList;
import com.pi4j.io.gpio.*;

public class TrafficLightMSF {

    private static final int LED_GREEN = 0;
    private static final int LED_YELLOW = 1;
    private static final int LED_RED = 2;

    public static int count = 0;
    public static int state = 0;
    public static boolean switched = false;

    public static Integer timers []= {4000,5000,4000};

    private static int NUM_STATES = 3;

    private static final int TIMEOUT_ON_MS = 1000;

    private static final GpioController gpio = GpioFactory.getInstance();
    private static ArrayList<Integer[]> stateMatrix = new ArrayList<Integer[]>();

    public TrafficLightMSF() {
        initPins();
    }

    public void initPins(){
        // provision gpio pins as output pins and make sure are set to LOW at startup
        GpioPinDigitalOutput greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN),   // PIN NUMBER
                "My Green LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        GpioPinDigitalOutput yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_YELLOW),   // PIN NUMBER
                "My Yellow LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
        GpioPinDigitalOutput redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_RED),   // PIN NUMBER
                "My Red LED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)
    }

    private static Integer startSemaphore() {
            switch (state){
                case LED_GREEN :
                    if(count <  timers[state]) {
                        switched=false;
                        count++;
                        return state;
                    }
                    else
                        switched=true;
                        return LED_YELLOW;
                case LED_YELLOW:
                    if(count <  timers[state]) {
                        switched=false;
                        count++;
                        return state;
                    }
                    else
                        switched=true;
                        return LED_RED;
                case LED_RED:
                    if(count <  timers[state]) {
                        switched=false;
                        count++;
                        return state;
                    }
                    else
                        switched=true;
                        return LED_GREEN;
            }
            return -1;
    }

    private static void switchLed() {
        GpioPinDigitalOutput greenLed;
        GpioPinDigitalOutput yellowLed;
        GpioPinDigitalOutput redLed;
        if(switched) {
            switch (state) {
                case LED_GREEN:
                    redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN), PinState.LOW);
                    greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN), PinState.HIGH);
                case LED_YELLOW:
                    greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN), PinState.LOW);
                    yellowLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN), PinState.HIGH);
                case LED_RED:
                    greenLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN), PinState.LOW);
                    redLed = gpio.provisionDigitalOutputPin(RaspiPin.getPinByAddress(LED_GREEN), PinState.HIGH);
            }
        }
    }

    public static void main(String[] args) {
        try {
            while (true) {
                state = startSemaphore();
                switchLed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}