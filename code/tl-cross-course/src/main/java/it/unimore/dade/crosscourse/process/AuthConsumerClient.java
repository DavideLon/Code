package it.unimore.dade.crosscourse.process;

import it.unimore.dade.crosscourse.piprocess.TrafficLightErrorState;
import it.unimore.dade.crosscourse.piprocess.TrafficLightMSF;
import it.unimore.dade.crosscourse.piprocess.TrafficLightOff;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Simple MQTT Consumer using the library Eclipse Paho
 * and authentication credentials
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project mqtt-playground
 * @created 14/10/2020 - 09:19
 */
public class AuthConsumerClient {

    private final static Logger logger = LoggerFactory.getLogger(AuthConsumerClient.class);

    //IP Address of the target MQTT Broker
    private static String BROKER_ADDRESS = "192.168.1.145";

    //PORT of the target MQTT Broker
    private static int BROKER_PORT = 1883;

    //MQTT account username to connect to the target broker
    private static final String MQTT_USERNAME = "000001";

    //MQTT account password to connect to the target broker
    private static final String MQTT_PASSWORD = "lgmoihgr";

    //Basic Topic used to consume generated demo data (the topic is associated to the user)
    private static final String MQTT_BASIC_TOPIC = "/iot/user/000001/";

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

            //Subscribe to the target topic #. In that case the consumer will receive (if authorized) all the message
            //passing through the broker
            client.subscribe(MQTT_BASIC_TOPIC + "#", (topic, msg) -> {
                //The topic variable contain the specific topic associated to the received message. Using MQTT wildcards
                //messaged from multiple and different topic can be received with the same subscription
                //The msg variable is a MqttMessage object containing all the information about the received message
            	byte[] payload = msg.getPayload();
                String command=new String(payload);
                logger.info("Message Received ({}) Message Received: {}", topic, command);

                if (command=="on") {
                    TrafficLightMSF trafficLightMSF = new TrafficLightMSF();
                }
                else if(command=="blink") {
                    TrafficLightErrorState trafficLightErrorState = new TrafficLightErrorState();
                }
                else {
                    TrafficLightOff trafficLightOff = new TrafficLightOff();
                }

            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}