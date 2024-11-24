package com.ashencostha.mqtt;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "21beb8861bf446b59d440a117181100c";
    private MqttServicio mqttServicio;
    private EditText topicEditText;
    private EditText messageEditText;
    private TextView messagesTextView;
    private Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mqttServicio = new MqttServicio();
        getLifecycle().addObserver(new MqttLifecycleObserver());

        topicEditText = findViewById(R.id.topicEditText);
        messageEditText = findViewById(R.id.messageEditText);
        messagesTextView = findViewById(R.id.messagesTextView);
        Button publishButton = findViewById(R.id.publishButton);
        Button subscribeButton = findViewById(R.id.subscribeButton);

        mainHandler = new Handler(Looper.getMainLooper());

        mqttServicio.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                mainHandler.post(() -> Toast.makeText(MainActivity.this, "Conexión perdida: " + cause.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                mainHandler.post(() -> {
                    String receivedMessage = "Mensaje recibido en tópico " + topic + ": " + new String(message.getPayload());
                    messagesTextView.append(receivedMessage + "\n");
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                mainHandler.post(() -> Toast.makeText(MainActivity.this, "Entrega completa", Toast.LENGTH_SHORT).show());
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = topicEditText.getText().toString();
                String message = messageEditText.getText().toString();
                publicandoMensaje(topic, message);
            }
        });

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topic = topicEditText.getText().toString();
                subscribiendoTopico(topic);
            }
        });
    }

    @Override
    protected void onDestroy() {
        mqttServicio.disconnect();
        super.onDestroy();
    }

    private void publicandoMensaje(String topic, String message) {
        Toast.makeText(this, "Publicando mensaje: " + message, Toast.LENGTH_SHORT).show();
        mqttServicio.publish(topic, message);
    }

    private void subscribiendoTopico(String topic) {
        Toast.makeText(this, "Suscribiendo tópico: " + topic, Toast.LENGTH_SHORT).show();
        mqttServicio.subscribe(topic);
    }

    private class MqttLifecycleObserver implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void connect() {
            mqttServicio.connect(BROKER_URL, CLIENT_ID);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void disconnect() {
            mqttServicio.disconnect();
        }
    }
}