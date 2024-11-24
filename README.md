1. Importaciones
El código importa clases de la biblioteca Eclipse Paho:

MqttClient: Cliente principal para interactuar con un broker MQTT.
MqttConnectOptions: Configura las opciones de conexión, como sesiones limpias, credenciales, etc.
MqttMessage: Representa mensajes MQTT.
MqttCallback: Define un conjunto de eventos para manejar conexión perdida, mensajes recibidos, etc.
MemoryPersistence: Manejador de persistencia en memoria para datos temporales del cliente.
2. Estructura de la clase
La clase MqttServicio tiene las siguientes responsabilidades:

Configurar y gestionar un cliente MQTT.
Publicar mensajes a tópicos específicos.
Suscribirse a tópicos para recibir mensajes.
Establecer un callback para manejar eventos MQTT.
3. Variables de la clase
client: Instancia de MqttClient que se conecta al broker MQTT.
callback: Instancia de MqttCallback para manejar eventos como conexión perdida o llegada de mensajes.
4. Métodos principales
4.1 setCallback
Este método configura un callback para manejar eventos MQTT.

Si ya existe una instancia del cliente (client), se establece el callback inmediatamente.
El callback puede manejar:
Conexión perdida.
Mensajes entrantes.
Confirmación de entrega.
4.2 connect
Este método conecta el cliente al broker MQTT:

Validación:
Comprueba que el brokerUrl y el clientId no sean nulos o vacíos.
Persistencia:
Utiliza MemoryPersistence para manejar datos temporales (id de cliente, etc.).
Inicialización:
Crea una instancia de MqttClient con el broker, el identificador de cliente y la persistencia.
Opciones de conexión:
Activa sesiones limpias (setCleanSession(true)), lo que asegura que no se recuperarán mensajes pendientes de una conexión anterior.
Conexión:
Se conecta al broker utilizando las opciones configuradas.
Callback:
Si se configuró un callback previamente, se aplica al cliente.
Si ocurre algún error durante el proceso, lanza una excepción para notificar al desarrollador.

4.3 disconnect
Este método desconecta al cliente del broker:

Comprueba si el cliente está inicializado y conectado.
Llama a client.disconnect() para cerrar la conexión.
Maneja excepciones en caso de errores y las encapsula en una excepción genérica.
4.4 publish
Este método publica un mensaje a un tópico específico:

Validación:
Comprueba que el cliente esté inicializado y conectado.
Construcción del mensaje:
Convierte el texto en un objeto MqttMessage con el payload adecuado (bytes del mensaje).
Publicación:
Usa client.publish(topic, mqttMessage) para enviar el mensaje al tópico.
Excepciones:
Si ocurre un error, lanza una excepción genérica.
4.5 subscribe
Este método se suscribe a un tópico específico:

Validación:
Asegura que el cliente esté conectado.
Suscripción:
Llama a client.subscribe(topic) para escuchar mensajes de ese tópico.
Excepciones:
Maneja errores con una excepción genérica.
5. Flujos clave
Conexión al broker
Se llama a connect(brokerUrl, clientId).
Se inicializa un cliente MQTT con las opciones de conexión.
Si se configuró un callback, este se registra.
El cliente se conecta al broker.
Publicar un mensaje
Verifica que el cliente esté conectado.
Construye un mensaje MQTT con el contenido proporcionado.
Publica el mensaje en el tópico especificado.
Suscribirse a un tópico
Verifica que el cliente esté conectado.
Se suscribe al tópico proporcionado.
Los mensajes recibidos en ese tópico activarán el callback.
Desconexión
Verifica que el cliente esté conectado.
Llama a disconnect() para finalizar la conexión.
6. Validación y manejo de errores
El código maneja errores de manera robusta:

Validación de entrada:
Los parámetros (brokerUrl, clientId) son validados antes de usarse.
Excepciones específicas de MQTT:
Errores de conexión, publicación o suscripción se manejan con MqttException.
Mensajes descriptivos:
Se proporcionan descripciones claras al lanzar excepciones, facilitando el diagnóstico.
