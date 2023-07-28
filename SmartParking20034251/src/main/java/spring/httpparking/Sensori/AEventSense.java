package spring.httpparking.Sensori;

import org.eclipse.paho.client.mqttv3.*;

import java.awt.*;
import java.awt.event.*;

public class AEventSense extends Frame {
    int state = 0;
    Button entrataButton;
    Button uscitaButton;
    Button pagamentoButton;
    TextField tf;
    MqttClient c;
    String topic;
    //se è presente un ticket di uno specifico parcheggio una macchina può uscire dal parcheggio, rimuovi ticket 
    //aggiungiamo lista parcheggi per sceglierne uno e far funzionare i button su quello specifico parcheggio

    AEventSense(MqttClient cli, String topic, String topic2, String topic3) {
    //create components
        this.topic=topic;
        c = cli;
        tf = new TextField();
        tf.setBounds(60, 50, 170, 20);
        tf.setText("state: red=off, green=on");
        entrataButton = new Button("Entrata");
        entrataButton.setBounds(100, 100, 80, 30);
        entrataButton.setBackground(Color.green);
        uscitaButton = new Button("Uscita");
        uscitaButton.setBounds(100, 150, 80, 30);
        uscitaButton.setBackground(Color.green);
        pagamentoButton = new Button("Pagamento");
        pagamentoButton.setBounds(100, 200, 80, 30);
        pagamentoButton.setBackground(Color.green);
//register listener
        OuterSense o = new OuterSense(this,topic);
        entrataButton.addActionListener(o);
        OuterSense o2 = new OuterSense(this,topic2);
        uscitaButton.addActionListener(o2);
        OuterSense o3 = new OuterSense(this,topic3);
        pagamentoButton.addActionListener(o3);//passing outer class instance
//add components and set size, layout and visibility
        add(entrataButton);
        add(uscitaButton);
        add(pagamentoButton);
        add(tf);
        setSize(300, 300);
        setLayout(null);
        setVisible(true);
    }
}

class OuterSense implements ActionListener{
    //private static final String TOPIC_SWITCH = "home/2/switch";
    AEventSense obj;
    String topic;
    OuterSense(AEventSense obj, String topic){
        this.obj=obj;
        this.topic=topic;
    }

    public void actionPerformed(ActionEvent e){

        // get current state from color
        //se state=0 allora parcheggio è chiuso
        int a,b,c;
        if (obj.entrataButton.getBackground()==Color.green) a=1; else a=0;
        if (obj.uscitaButton.getBackground()==Color.green) b=1; else b=0;
        if (obj.pagamentoButton.getBackground()==Color.green) c=1; else c=0;
        
        // pubblica valori di umidità estremi per forzare accensione -0- o spegnimento -99-
       try{
        String[] componentiTopic = topic.split("/");
        System.out.println(topic);
            switch (componentiTopic[2]) {
                case "entrata":
                if(a==0) System.out.println("opzione non disponibile");
                else obj.c.publish(topic,("macchina in entrata, stampo ticket").getBytes(), 1, true);
                    break;
                case "uscita":
                if(b==0) System.out.println("opzione non disponibile");
                else obj.c.publish(topic,("macchina in uscita").getBytes(), 1, true);
                    break;
                case "pagamento":
                if(c==0) System.out.println("opzione non disponibile");
                else obj.c.publish(topic,("ticket pagato, uscita permessa").getBytes(), 1, true);
                    break;
                default:
                    break;
                }
            
        } catch (MqttException ee) {
        ee.printStackTrace();
        }
    }
}


