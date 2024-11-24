package com.ashencostha.mqtt;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttServicio {

    private MqttClient client;
    private MqttCallback callback;

    public void setCallback(MqttCallback callback) {
        this.callback = callback;
        if (client != null) {
            client.setCallback(callback);
        }
    }

    public void connect(String brokerUrl, String clientId) throws IllegalArgumentException {
        if (brokerUrl == null || brokerUrl.isEmpty() || clientId == null || clientId.isEmpty()) {
            throw new IllegalArgumentException("Broker URL and Client ID must not be null or empty");
        }

        try {
            // Configurar la capa de persistencia
            MemoryPersistence persistence = new MemoryPersistence();

            // Inicializar el cliente MQTT
            client = new MqttClient(brokerUrl, clientId, persistence);

            // Configurar las opciones de conexión
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            // Establecer el callback si ya está configurado
            if (callback != null) {
                client.setCallback(callback);
            }

            // Conéctese al broker
            client.connect(connectOptions);

        } catch (MqttException e) {
            throw new RuntimeException("No se pudo conectar con broker MQTT", e);
        }
    }

    public void disconnect() {
        if (client != null && client.isConnected()) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                throw new RuntimeException("FNo se pudo desconectar del broker MQTT", e);
            }
        }
    }

    public void publish(String topic, String message) {
        if (client == null || !client.isConnected()) {
            throw new IllegalStateException("El cliente no está conectado");
        }

        try {
            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            throw new RuntimeException("No se pudo publicar el mensaje", e);
        }
    }

    public void subscribe(String topic) {
        if (client == null || !client.isConnected()) {
            throw new IllegalStateException("El cliente no está conectado");
        }

        try {
            client.subscribe(topic);
        } catch (MqttException e) {
            throw new RuntimeException("No se pudo suscribir al tema", e);
        }
    }
}