package it.unimore.dade.crosscourse.piprocess;

import com.pi4j.io.gpio.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class StartSemaphore implements Runnable {

    protected SemaphoreStatusListener semaphoreStatusListener = new SemaphoreStatusListener() {
        @Override
        public void onStatusChanged(String updatedStatus) throws InterruptedException {
            shutdown();
        }
    };

    public String msg= null;

    volatile boolean shutdown = false;

    private static final int LED_GREEN = 0;
    private static final int LED_YELLOW = 1;
    private static final int LED_RED = 2;
    public boolean inited=false;

    public static int count = 0;
    public static int state = 0;
    public static boolean switched = false;

    public static Integer timers []= {200,100,200};

    //public static final int MAX_ITERATIONS = 20;

    private static int NUM_STATES = 3;

    public static InitSemaphorePins initSemaphorePins = new InitSemaphorePins();

    private static final int TIMEOUT_ON_MS = 10;

    private static final GpioController gpio = initSemaphorePins.getGpio();
    private static GpioPinDigitalOutput greenLed = initSemaphorePins.getGreenLed();
    private static GpioPinDigitalOutput yellowLed = initSemaphorePins.getYellowLed();
    private static GpioPinDigitalOutput redLed = initSemaphorePins.getRedLed();
/*

    private static final GpioController gpio = GpioFactory.getInstance();
    private static GpioPinDigitalOutput greenLed = null;
    private static GpioPinDigitalOutput yellowLed =null;
    private static GpioPinDigitalOutput redLed = null;
*/
    private final static Logger logger = LoggerFactory.getLogger(StartSemaphore.class);

    //private static ArrayList<Integer[]> stateMatrix = new ArrayList<Integer[]>();

    public void shutdown(){
        shutdown=!shutdown;
    }

    public StartSemaphore() {
        /*if(!isInited())
            initPins();*/
        //this.semaphoreStatusListenerList = new ArrayList<>();
        //addDataListener(semaphoreStatusListener);
        //notifyUpdatedStatus("on");
    }
    public StartSemaphore(String message){
        /*if(!isInited())
            initPins();*/
        this.msg= message;
    }
/*
    public void addDataListener(SemaphoreStatusListener semaphoreStatusListener) {
        if(this.semaphoreStatusListenerList!= null)
            this.semaphoreStatusListenerList.add(semaphoreStatusListener);
    }

    public void removeDataListener(SemaphoreStatusListener semaphoreStatusListener) {
        if(this.semaphoreStatusListenerList!= null && this.semaphoreStatusListenerList.contains(semaphoreStatusListener))
            this.semaphoreStatusListenerList.remove(semaphoreStatusListener);
    }

    //notifies the status change to all the listeners in the list
    //every listener is linked to different parts in the code

    protected void notifyUpdatedStatus(String status) {
        if(this.semaphoreStatusListenerList!= null && this.semaphoreStatusListenerList.size()>0)
            this.semaphoreStatusListenerList.forEach(semaphoreStatusListener -> {
               if (semaphoreStatusListener!= null) {
                   try {
                       semaphoreStatusListener.onStatusChanged(status);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
            });
        else
            logger.error("Empty list or Null Status Listener! Nothing to notify");
    }
*/
    public void initPins(){
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
        this.inited=true;
    }

    private static Integer startSemaphore() throws InterruptedException {
        switch (state){
            case LED_GREEN :
                if(count < timers[state]) {
                    switched=false;
                    count++;
                    Thread.sleep(TIMEOUT_ON_MS);
                    return state;
                }
                else{
                    switched=true;
                    count=0;
                    logger.info("Switching yellow ON");
                    return LED_YELLOW;
                }
            case LED_YELLOW:
                if(count < timers[state]) {
                    switched=false;
                    count++;
                    Thread.sleep(TIMEOUT_ON_MS);
                    return state;
                }
                else {
                    switched = true;
                    count=0;
                    logger.info("Switching red ON");
                    return LED_RED;
                }
            case LED_RED:
                if(count < timers[state]) {
                    switched=false;
                    count++;
                    Thread.sleep(TIMEOUT_ON_MS);
                    return state;
                }
                else {
                    switched = true;
                    count=0;
                    logger.info("Switching green ON");
                    return LED_GREEN;
                }
        }
        return -1;
    }

    private static void switchLed() {
        if(switched) {
            switch (state) {
                case LED_GREEN:
                    redLed.low();
                    greenLed.high();
                    break;
                case LED_YELLOW:
                    greenLed.low();
                    yellowLed.high();
                    break;
                case LED_RED:
                    yellowLed.low();
                    redLed.high();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + state);
            }
        }
    }

    //execute local on the RPI from command line
    //mvn -U clean install
    //mvn exec:java -Dexec.mainClass="<Your Main Class>"
    //mvn exec:java -Dexec.mainClass="it.unimore.dade.crosscourse.test.TrafficLightMSF"

    public boolean isInited()
    {
        return this.inited;
    }

    public void run() {
        //int countIterations=0;
        //if(yellowLed.isMode(PinMode.DIGITAL_OUTPUT)&&(greenLed.isMode(PinMode.DIGITAL_OUTPUT))&&redLed.isMode(PinMode.DIGITAL_OUTPUT)) {
         if (initSemaphorePins.isInited())
         {

            logger.info("Starting TL, green on");
            greenLed.high();
            try {
                while (!shutdown) {
                    //semaphoreStatusListener.onStatusChanged(msg);
                    state = startSemaphore();
                    switchLed();
                    //countIterations ++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        else{
            initSemaphorePins.init();
            run();
       }
    }

}
