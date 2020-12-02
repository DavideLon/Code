package it.unimore.dade.crosscourse.process;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Simple MQTT Producer using the library Eclipse Paho
 * and authentication credentials
 *
 * @author Marco Picone, Ph.D. - picone.m@gmail.com
 * @project mqtt-playground
 * @created 14/10/2020 - 09:19
 */
public class AuthProducerClient {

    private final static Logger logger = LoggerFactory.getLogger(AuthProducerClient.class);

    //BROKER URL
    private static String BROKER_URL = "tcp://192.168.1.145:1883";

    //MQTT account username to connect to the target broker
    private static final String MQTT_USERNAME = "000001";

    //MQTT account password to connect to the target broker
    private static final String MQTT_PASSWORD = "lgmoihgr";

    //Basic Topic used to publish generated demo data (the topic is associated to the user)
    private static final String MQTT_BASIC_TOPIC = "/iot/user/000001/";

    //Additional Topic structure used to publish generated demo data. It is merged with the Basic Topic to obtain
    //the final used topic
    private static final String TOPIC = "tl/status";

    public static void main(String[] args) {

        logger.info("Auth SimpleProducer started ...");

        try{

            String publisherId = UUID.randomUUID().toString();

            MqttClientPersistence persistence = new MemoryPersistence();

            IMqttClient client = new MqttClient(BROKER_URL, publisherId, persistence);

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


            //Decide led's working mode: ------on/error/off------

            String status = "on";  //message sent

            //Internal Method to publish MQTT data using the created MQTT Client
            //The final topic is obtained merging the MQTT_BASIC_TOPIC and TOPIC in order to send the messages
            //to the correct topic root associated to the authenticated user
            //Eg. /iot/user/000001/tl/status
            publishData(client, MQTT_BASIC_TOPIC + TOPIC, status);


            //Disconnect from the broker and close the connection
            client.disconnect();
            client.close();

            logger.info("Disconnected !");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Send a target String Payload to the specified MQTT topic
     *
     * @param mqttClient
     * @param topic
     * @param msgString
     * @throws MqttException
     */
    public static void publishData(IMqttClient mqttClient, String topic, String msgString) throws MqttException {

        logger.debug("Publishing to Topic: {} Data: {}", topic, msgString);

        if (mqttClient.isConnected() && msgString != null && topic != null) {
        	
            MqttMessage msg = new MqttMessage(msgString.getBytes());
            msg.setQos(2);
            msg.setRetained(false);
            mqttClient.publish(topic,msg);
            
            logger.debug("(If Authorized by Broker ACL) Data Correctly Published !");
        }
        else{
            logger.error("Error: Topic or Msg = Null or MQTT Client is not Connected !");
        }

    }



}
