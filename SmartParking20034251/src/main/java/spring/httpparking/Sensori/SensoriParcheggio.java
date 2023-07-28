package spring.httpparking.Sensori;

import org.eclipse.paho.client.mqttv3.*;

import spring.httpparking.database.Database;
import spring.httpparking.model.Parcheggio;
import spring.httpparking.model.Ticket;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 *implementa un pannello di controllo che in base a bottoni selezionati dall'utente 
 *pubblica su determinati topic i messaggi appropriati per entrare uscire parcheggio pagare ticket .
 */ 
public class SensoriParcheggio implements MqttCallback {

    // the MQTT topics of this example
    private String topicEntrata;
    private String topicUscita;
    private String topicPagamento;
    private String topicLog;
    private Integer parkingid;
    private String clientId;
    private static Database db;
    private ArrayList<Ticket> tickets;

    
    // the broker URL
    private static final String brokerURL = "tcp://127.0.0.1:1883";
    // init the client
    private MqttClient client;
    private AEventSense visualize;
    /**
     * genera client id e istanzia mqtt client 
     * trova parcheggio più vicino 
     */
    public SensoriParcheggio() {
        db=new Database();
        int b=db.NearestParcheggioID();
        inizializza(b);

        // random client identifier
        clientId = MqttClient.generateClientId();
        // crea  il client MQTT
        try {
             client = new MqttClient(brokerURL, clientId);

        } catch (MqttException e) {
            e.printStackTrace();
        }
        visualize = new AEventSense(client,topicEntrata, topicUscita,topicPagamento);
        start();
    }
    /** MQTTCallBack Interface methods
     *
     */
    @Override
    public void connectionLost(Throwable cause) {
        // what happens when the connection is lost. We could reconnect here, for example.
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // quando arriva un nuovo messaggio in questo caso lo stampiamo
        String strmsg = new String(message.getPayload(), StandardCharsets.UTF_8);
        System.out.println("Message arrived for the topic '" + topic + "': " + strmsg);
        gestisciAE();
        System.out.println("received message "+strmsg+" on topic "+topic);
             
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // called when delivery for a message has been completed, and all acknowledgments have been received
        // no-op, here
    }

    private void inizializza(int parkingId){
        this.parkingid=parkingId;
        this.topicEntrata = "parking/" + parkingId + "/entrata";
		this.topicUscita = "parking/" + parkingId + "/uscita";
		this.topicPagamento = "parking/" + parkingId + "/pagamento";
        this.topicLog= "parking/" + parkingId + "/log";
    }

    private void gestisciAE(){
        tickets=db.getAllTicketparcheggio(parkingid);
        Parcheggio parcheggio= db.getParcheggioById(parkingid);
                if(parcheggio.isAvailable()==false){
                    System.out.println("(Parcheggi Pieno");
                    visualize.entrataButton.setBackground(Color.red);
                }
                else{
                    visualize.entrataButton.setBackground(Color.green);
                    System.out.println("posti disponibili: "+parcheggio.getNumPostiAvailable());
                } 
                if(parcheggio.getNumPostiAvailable()==parcheggio.getNumPostiTot()){
                    visualize.uscitaButton.setBackground(Color.red);
                    visualize.pagamentoButton.setBackground(Color.red);
                }
                else{
                    if(tickets.isEmpty()==true){
                        visualize.pagamentoButton.setBackground(Color.green);
                        visualize.uscitaButton.setBackground(Color.red);
                    }
                    else{
                        visualize.pagamentoButton.setBackground(Color.green);
                        visualize.uscitaButton.setBackground(Color.green);
                    }
                }
    }

    /**
     * The method to start the publisher. Currently, it sets a Last Will and Testament
     * message, open a non persistent connection, and publish a temperature value
     */
    public void start() {
        try {
            Parcheggio parcheggio= db.getParcheggioById(parkingid);
            String password = "pwd1";
            char pwd[] = password.toCharArray();
            visualize.entrataButton.setBackground(Color.red);
            visualize.uscitaButton.setBackground(Color.red);
            gestisciAE();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ui00");
            options.setPassword(pwd);
            // persistent, durable connection
            options.setCleanSession(true);
            client.connect(options);
            System.out.println("SUBSCRIBE "+topicLog);
            // si mette in ascolto su topiclog per ricevere messaggi errore o ok
            client.subscribe(topicLog, 2);
            System.out.print("(Parcheggi Liberi: "+parcheggio.getNumPostiAvailable()+" utilizzare i bottoni per accedere alle funzionalita' del parcheggio\n");
            System.out.print("si può uscire da un parcheggio solo dopo aver pagato il ticket\n");
            client.setCallback(this);
               
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }



    /**
     * The main
     */
    public static void main(String[] args) {
        SensoriParcheggio pannello = new SensoriParcheggio();

    }
}
