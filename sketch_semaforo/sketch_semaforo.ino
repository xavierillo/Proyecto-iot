#include <SoftwareSerial.h>
#include <Servo.h>

// Pines del Bluetooth
SoftwareSerial BT(6, 5); // RX, TX

// Pines LEDs del semáforo
const int LED_ROJO = 8;
const int LED_AMARILLO = 9;
const int LED_VERDE = 10;

// Servo motor
Servo servo;
const int SERVO_PIN = 11;

// Tiempos por defecto (en milisegundos)
int tiempoRojo = 3000;
int tiempoVerde = 2000;
int tiempoAmarillo = 1000;

// Buffer de entrada Bluetooth
String comando = "";

void setup() {
  Serial.begin(9600);
  BT.begin(9600);

  pinMode(LED_ROJO, OUTPUT);
  pinMode(LED_AMARILLO, OUTPUT);
  pinMode(LED_VERDE, OUTPUT);

  servo.attach(SERVO_PIN);
  servo.write(0); // Cerrado inicialmente

  Serial.println("Sistema de semáforo listo");
}

void loop() {
  // Leer comandos desde Bluetooth
  while (BT.available()) {
    char c = BT.read();
    if (c == '\n' || c == '\r') {
      if (comando.length() > 0) {
        procesarComando(comando);
        comando = "";
      }
    } else {
      comando += c;
    }
  }
}

void procesarComando(String cmd) {
  cmd.trim();
  Serial.print("Comando recibido: ");
  Serial.println(cmd);

  if (cmd.startsWith("SET_RED=")) {
    tiempoRojo = cmd.substring(8).toInt();
    Serial.println("Nuevo tiempo rojo: " + String(tiempoRojo));
  } else if (cmd.startsWith("SET_GREEN=")) {
    tiempoVerde = cmd.substring(10).toInt();
    Serial.println("Nuevo tiempo verde: " + String(tiempoVerde));
  } else if (cmd.startsWith("SET_YELLOW=")) {
    tiempoAmarillo = cmd.substring(11).toInt();
    Serial.println("Nuevo tiempo amarillo: " + String(tiempoAmarillo));
  } else if (cmd == "RUN") {
    ejecutarSecuenciaSemaforoYPuerta();
  }
}

void ejecutarSecuenciaSemaforoYPuerta() {
  Serial.println("Ejecutando secuencia...");

  // LED Rojo
  digitalWrite(LED_ROJO, HIGH);
  delay(tiempoRojo);
  digitalWrite(LED_ROJO, LOW);

  // LED Verde
  digitalWrite(LED_VERDE, HIGH);
  delay(tiempoVerde);
  digitalWrite(LED_VERDE, LOW);

  // LED Amarillo
  digitalWrite(LED_AMARILLO, HIGH);
  delay(tiempoAmarillo);
  digitalWrite(LED_AMARILLO, LOW);

  // Activar motor (abrir puerta)
  Serial.println("Abriendo puerta...");
  servo.write(90); // Abre
  delay(2000);     // Mantiene abierto 2 segundos
  servo.write(0);  // Cierra
  Serial.println("Puerta cerrada.");
}
