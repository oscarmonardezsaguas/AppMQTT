
# Explicación detallada del código: MqttServicio
Este documento explica detalladamente el funcionamiento de la clase `MqttServicio`. El objetivo de esta clase es encapsular las operaciones relacionadas con el protocolo MQTT, como conectar a un broker, publicar mensajes y suscribirse a tópicos. Utiliza la biblioteca Eclipse Paho.


# 1. Importaciones

Las importaciones incluyen las clases necesarias de la biblioteca Eclipse Paho para manejar las operaciones MQTT:
- `MqttClient`: El cliente principal para interactuar con un broker MQTT.
- `MqttConnectOptions`: Configura las opciones de conexión (por ejemplo, sesiones limpias).
- `MqttMessage`: Representa los mensajes enviados o recibidos a través del protocolo MQTT.
- `MqttCallback`: Define métodos para manejar eventos como mensajes recibidos y conexión perdida.
- `MemoryPersistence`: Proporciona persistencia temporal en memoria para el cliente.



# 2. Atributos de la clase

- `client`: Instancia de `MqttClient` utilizada para establecer la conexión con el broker.
- `callback`: Instancia de `MqttCallback` que maneja eventos como mensajes recibidos o problemas de conexión.

# 3. Métodos principales
## 3.1 Método setCallback
Este método permite asignar un callback personalizado para manejar eventos MQTT. Si ya existe una instancia de cliente (`client`), el callback se aplica inmediatamente.
## 3.2 Método connect
Establece una conexión con un broker MQTT.
- Valida que los parámetros `brokerUrl` y `clientId` no sean nulos o vacíos.
- Configura opciones de conexión como sesiones limpias (`setCleanSession(true)`).
- Aplica el callback, si está definido.
- Llama al método `connect()` del cliente para establecer la conexión.

## 3.3 Método disconnect
Cierra la conexión con el broker MQTT.
- Verifica si el cliente está conectado.
- Llama al método `disconnect()`.
- Maneja excepciones en caso de errores durante la desconexión.

## 3.4 Método publish
Publica un mensaje en un tópico específico.
- Comprueba que el cliente esté conectado.
- Crea un objeto `MqttMessage` con el contenido del mensaje.
- Llama al método `publish()` del cliente, especificando el tópico y el mensaje.

## 3.5 Método subscribe
Se suscribe a un tópico para recibir mensajes.
- Comprueba que el cliente esté conectado.
- Llama al método `subscribe()` del cliente, especificando el tópico
