package it.unimore.dade.crosscourse.process;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import it.unimore.dade.crosscourse.piprocess.*;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;


public class AuthConsumerServer {

    private final static Logger logger = LoggerFactory.getLogger(AuthConsumerServer.class);

    //static Executor executor = Executors.newSingleThreadExecutor();

    //IP Address of the target MQTT Broker
    private static final String BROKER_ADDRESS = "192.168.1.145";

    //PORT of the target MQTT Broker
    private static final int BROKER_PORT = 1883;

    //MQTT account username to connect to the target broker
    private static final String MQTT_USERNAME = "000001";

    //MQTT account password to connect to the target broker
    private static final String MQTT_PASSWORD = "lgmoihgr";

    //Basic Topic used to consume generated demo data (the topic is associated to the user)
    private static final String MQTT_BASIC_TOPIC = "/iot/user/000001/";

    private static final String ON = "on";
    private static final String OFF = "off";
    private static final String ERROR = "error";

    public static boolean initedPins;

    private static GpioController gpio = null;
    private static GpioPinDigitalOutput greenLed = null;
    private static GpioPinDigitalOutput yellowLed = null;
    private static GpioPinDigitalOutput redLed = null;

    static InitSemaphorePins initSemaphorePins = new InitSemaphorePins();

    public static void initLocalPins(){
        gpio=initSemaphorePins.getGpio();
        greenLed=initSemaphorePins.getGreenLed();
        yellowLed= initSemaphorePins.getYellowLed();
        redLed=initSemaphorePins.getRedLed();
    }



    public static void main(String [ ] args) {

    	logger.info("MQTT Auth Consumer Tester Started ...");

        try{

            //Generate a random MQTT client ID using the UUID class
            String clientId = UUID.randomUUID().toString();

            //Represents a persistent data store, used to store outbound and inbound messages while they
            //are in flight, enabling delivery to the QoS specified. In that case use a memory persistence.
            //When the application stops all the temporary data will be deleted.
            MqttClientPersistence persistence = new MemoryPersistence();

            //The the persistence is not passed to the constructor the default file persistence is used.
            //In case of a file-based storage the same MQTT client UUID should be used
            IMqttClient client = new MqttClient(
                    String.format("tcp://%s:%d", BROKER_ADDRESS, BROKER_PORT),
                    clientId,
                    persistence);

            //Define MQTT Connection Options such as reconnection, persistent/clean session and connection timeout
            //Authentication option can be added -> See AuthProducer example
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(MQTT_USERNAME);
            options.setPassword(new String(MQTT_PASSWORD).toCharArray());
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);

            //Connect to the target broker
            client.connect(options);

            logger.info("Connected !");

            initSemaphorePins.init();

            initLocalPins();

            StartSemaphore startRunnable = new StartSemaphore(gpio, greenLed, yellowLed, redLed);
            StopSemaphore stopRunnable = new StopSemaphore(gpio, greenLed, yellowLed, redLed);
            ErrorStateSemaphore errorRunnable = new ErrorStateSemaphore(gpio, yellowLed);

            Thread startSemaphore = new Thread(startRunnable);
            Thread stopSemaphore = new Thread(stopRunnable);
            Thread errorStateSemaphore = new Thread(errorRunnable);

            //status listener, does it really works?
            SemaphoreStatusListener semaphoreStatusListener = command1 -> {
                //TODO insert switch case

                if (!startSemaphore.isAlive())
                    startSemaphore.setName("Start Thread");
                if (!stopSemaphore.isAlive())
                    stopSemaphore.setName("Stop Thread");
                if (!errorStateSemaphore.isAlive())
                    errorStateSemaphore.setName("Error Thread");

                if (command1.toLowerCase().equals(ON)) {
                    logger.info("Telling RPI to switch semaphore ON");
                    logger.info("-----------------{}-----------------", startSemaphore.getState());
                    logger.info("-----------------{}-----------------", stopSemaphore.getState());
                    logger.info("-----------------{}-----------------", errorStateSemaphore.getState());

                    if (stopSemaphore.isAlive()) {
                        stopSemaphore.wait();
                    }
                    if (errorStateSemaphore.isAlive()) {
                        errorStateSemaphore.wait();
                    }
                    if (startSemaphore.isAlive()){
                        logger.debug("DEBUG ON");
                        startSemaphore.wait();
                        startSemaphore.notify();
                    }
                    startSemaphore.start();

                }
                else if(command1.toLowerCase().equals(OFF)) {
                    logger.info("Telling RPI to switch semaphore OFF");
                    logger.info("-----------------{}-----------------",startSemaphore.getState());
                    logger.info("-----------------{}-----------------",stopSemaphore.getState());
                    logger.info("-----------------{}-----------------",errorStateSemaphore.getState());
                    if (startSemaphore.isAlive())
                        startSemaphore.wait();
                    if(errorStateSemaphore.isAlive())
                        errorStateSemaphore.wait();
                    logger.debug("DEBUG OFF");
                    stopSemaphore.start();
                }
                else {
                    logger.info("Telling RPI to go in blinking yellow error state");
                    logger.info("-----------------{}-----------------",startSemaphore.getState());
                    logger.info("-----------------{}-----------------",stopSemaphore.getState());
                    logger.info("-----------------{}-----------------",errorStateSemaphore.getState());
                    if (startSemaphore.isAlive())
                        startSemaphore.wait();
                    if(stopSemaphore.isAlive())
                        stopSemaphore.wait();
                    if (errorStateSemaphore.isAlive()){
                        logger.debug("DEBUG ON");
                        errorStateSemaphore.wait();
                        errorStateSemaphore.notify();
                    }
                    logger.debug("DEBUG ERROR");
                    errorStateSemaphore.start();
                }
            };

            //Subscribe to the target topic #. In that case the consumer will receive (if authorized) all the message
            //passing through the broker
            client.subscribe(MQTT_BASIC_TOPIC + "#", (topic, msg) -> {
                //The topic variable contain the specific topic associated to the received message. Using MQTT wildcards
                //messaged from multiple and different topic can be received with the same subscription
                //The msg variable is a MqttMessage object containing all the information about the received message
            	byte[] payload = msg.getPayload();
                String command=new String(payload);
                logger.info("On topic : ({}) Message Received: ({})", topic, command);

                semaphoreStatusListener.onStatusChanged(command);

            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
