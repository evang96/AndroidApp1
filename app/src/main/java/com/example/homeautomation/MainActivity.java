//Evan Garvey
//Home automation system FYP - CloudHome
//G00322695

package com.example.homeautomation;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.VideoView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {
    MqttAndroidClient client;
    boolean login_done = false;
    String ipaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch onoff = findViewById(R.id.onoff);
        Switch leftright = findViewById(R.id.leftright);
        Switch lamp = findViewById(R.id.lamp);
        final WebView video = findViewById(R.id.videoview);
        Button preview = findViewById(R.id.prview);
        final TextView temp = findViewById(R.id.temp);
        final TextView hum = findViewById(R.id.hum);
        final TextView pres = findViewById(R.id.press);
        final TextView alltitude = findViewById(R.id.allt);
        final TextView kitchen = findViewById(R.id.kitchen);
        final TextView living_room = findViewById(R.id.living);
        final TextView hall = findViewById(R.id.hall);
        final TextView stairs = findViewById(R.id.stair);

        //while(login_done == false) {
            Login2();
        //}

        String clientId = MqttClient.generateClientId();

        client =
                new MqttAndroidClient(this.getApplicationContext(), "tcp://mqtt.eclipse.org:1883",
                        clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("TAG", "onSuccess");
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("TAG", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { //when switch pressed func called. 2 state on off. subscribe to home automation in pi code. checked or unchecked
                if (isChecked) {
                    String topic = "Home_Automation";
                    String payload = "on off switch 1";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
//unchecked
                } else {
                    // The toggle is disabled
                    String topic = "Home_Automation";
                    String payload = "on off switch 0"; // unchecked process message
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //2 state checked unchecked. if checked left and unchecked right. process message in code. left is reverse and right is forward.
        leftright.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String topic = "Home_Automation";
                    String payload = "left";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                } else {
                    // The toggle is disabled
                    String topic = "Home_Automation";
                    String payload = "right";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        //led on off checked and unchecked
        lamp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String topic = "Home_Automation";
                    String payload = "lamp 1";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                } else {
                    // The toggle is disabled
                    String topic = "Home_Automation";
                    String payload = "lamp 0";
                    byte[] encodedPayload = new byte[0];
                    try {
                        encodedPayload = payload.getBytes(StandardCharsets.UTF_8);
                        MqttMessage message = new MqttMessage(encodedPayload);
                        client.publish(topic, message);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
            // camera pi video same ip address as phone pi - vmedia wifi
        preview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                // The toggle is disabled
                String topic = "Home_Automation";
                String payload = "preview start";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
                */
                video.loadUrl("http://192.168.0.39:8000");
            }
        });


        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                //txt_msg.setText(new String(mqttMessage.getPayload()));
                String message = new String(mqttMessage.getPayload());

                if(message.indexOf("pir 1")>-1) { kitchen.setText(message);

               // if(message.indexOf("No")>-1) kitchen.setTextColor(0xFFFF0000);
                //else kitchen.setTextColor(0xFFFFFFFF);
                }
                //if(message.equals("kitchen 0")) { kitchen.setText("Kitchen : No Intruder");}

                if(message.indexOf("pir 2")>-1) { living_room.setText(message);}
                //if(message.equals("livingR 0")) { living_room.setText("Living Room : No Intruder");}

                if(message.indexOf("pir 3")>-1) { hall.setText(message);}
                //if(message.equals("hall 0")) { hall.setText("Hall Room : No Intruder");}

                if(message.indexOf("pir 4")>-1) { stairs.setText(message);}
                //if(message.equals("stairs 0")) { stairs.setText("Stairs : No Intruder");}

                if(message.indexOf("temperature")>-1){temp.setText(message); }
                if(message.indexOf("humidity")>-1){hum.setText(message); }
                if(message.indexOf("pressure")>-1){pres.setText(message); }
                if(message.indexOf("alltitude")>-1){alltitude.setText(message); }

            }



            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

/*
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http//:" + "192.168.0.39" + "8000");
                video.setVideoURI(uri);
                video.start();
            }
        });
*/
    }


void  subscribe(){

    String topic = "Home_automation_client";
    int qos = 1;
    try {
        IMqttToken subToken = client.subscribe(topic, qos);
        subToken.setActionCallback(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                // The message was published
                Log.d("TAG", "Subscribed");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken,
                                  Throwable exception) {
                // The subscription could not be performed, maybe the user was not
                // authorized to subscribe on the specified topic e.g. using wildcards
                Log.d("TAG", "Not Subscribed");
            }
        });
    } catch (MqttException e) {
        e.printStackTrace();
    }
}
// ip address for fingerprint sensor. not used
void Login2(){

    //while(login_done == false) {
        Login();
        //if (login_done == false) Login2();
    //}

}
    void Login(){

        final LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.login, null);
        final EditText user = linearLayout.findViewById(R.id.user);
        final EditText passwd = linearLayout.findViewById(R.id.password);
        //final EditText ipadd = (EditText) linearLayout.findViewById(R.id.ip);
        //final Button login = (Button) linearLayout.findViewById(R.id.login);
        final AlertDialog builder = new AlertDialog.Builder(this)
                .setPositiveButton("Cancel", null)
                .setNegativeButton("Login", null)
                .setView(linearLayout)
                .setCancelable(false)
                .create();
        builder.show();

        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user_in = user.getText().toString();
                String pass_in = passwd.getText().toString();
                //ipaddress = ipadd.getText().toString();

                if((user_in.equals("evan"))&&(pass_in.equals("evan"))) {login_done = true;
                return ;
                }
                else {
                    login_done = false;
                return;
                }
                }
        });
    }

}
