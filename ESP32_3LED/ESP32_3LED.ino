#include <WiFi.h>

// WiFi credentials
const char* ssid     = "Your WIFI ssid";
const char* password = "Your WIFI password";

// Create an HTTP server on port 80
WiFiServer server(80);

const int ledY =  14;
const int ledG =  12;
const int ledR =  32;

// Buffer for incoming HTTP request lines
char linebuf[80]; // Buffer to store client requests
int charcount=0;  // Character counter for the buffer

void setup() {
  pinMode(ledR, OUTPUT);
  pinMode(ledY, OUTPUT);
  pinMode(ledG, OUTPUT);

  Serial.begin(115200);
  while(!Serial) {
    // Wait for Serial Monitor to open
  }
 
  
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  // Connect to WiFi network
  WiFi.begin(ssid, password);

  // Wait for the connection to establish
  while(WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("");
  Serial.println("WiFi connected");                                     
  Serial.println("IP address: ");    
  Serial.println(WiFi.localIP()); // Display ESP32's IP address

  // Start the HTTP server
  server.begin();
}

void loop() {
  // Check if a client has connected
  WiFiClient client = server.available();

 
  if (client) {
    Serial.println("New client"); // Notify of new connection
    memset(linebuf,0,sizeof(linebuf));  // Clear buffer
    charcount=0;
    boolean currentLineIsBlank = true;  // Track blank lines in HTTP headers
    
    while (client.connected()) {
      if (client.available()) {
        char c = client.read(); // Read a character from the client
        Serial.write(c);
        linebuf[charcount]=c; // Add to buffer
        // Avoid overflow
        if (charcount<sizeof(linebuf)-1){
          charcount++;
        }
        // Detect end of HTTP headers
        if (c == '\n' && currentLineIsBlank) {
          // Send HTTP response headers
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: text/html");  
          client.println("Connection: close"); 
          client.println();

          // Send HTML content
          client.println("<!DOCTYPE HTML><html><head>");
          client.println("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"></head>");
          client.println("<p><font size=\"15\">Press to off <a href=\"OFF\"><button style=\" background-color: black; color: white; border-radius: 15px; height: 90px; width: 150px; padding: 10px;\"><font size=\"12\">all</font></button></a> the leds</font></p>");
          client.println("<p><font size=\"15\">Press to on an <a href=\"Y\"><button style=\" background-color: #B8860B; color: white; border-radius: 15px; height: 90px; width: 150px; padding: 10px;\"><font size=\"12\">yellow</button></a> led</font></font></p>");
          client.println("<p><font size=\"15\">Press to on a <a href=\"R\"><button style=\" background-color: #8B0000; color: white; border-radius: 15px; height: 90px; width: 150px; padding: 10px;\"><font size=\"12\">red</button></a> led</font></font></p>");
          client.println("<p><font size=\"15\">Press to on a <a href=\"G\"><button style=\" background-color: #556B2F; color: white; border-radius: 15px; height: 90px; width: 150px; padding: 10px;\"><font size=\"12\">green</button></a> led</font></font></p>");
          client.println("</html>");
          break;
        }
        // Process each line of the request
        if (c == '\n') {
          currentLineIsBlank = true;
          // Check for specific requests and toggle LEDs accordingly
          if (strstr(linebuf,"GET /OFF") > 0){
            Serial.println("OFF ALL");
            digitalWrite(ledY, 0);
            digitalWrite(ledG, 0);
            digitalWrite(ledR, 0);
          }
          else if (strstr(linebuf,"GET /Y") > 0){
            Serial.println("ON YELLOW");
            digitalWrite(ledY, 1);
            digitalWrite(ledG, 0);
            digitalWrite(ledR, 0);
          }
          else if (strstr(linebuf,"GET /R") > 0){
            Serial.println("ON YELLOW");
            digitalWrite(ledY, 0);
            digitalWrite(ledG, 0);
            digitalWrite(ledR, 1);
          }
          else if (strstr(linebuf,"GET /G") > 0){
            Serial.println("ON YELLOW");
            digitalWrite(ledY, 0);
            digitalWrite(ledG, 1);
            digitalWrite(ledR, 0);
          }
          currentLineIsBlank = true;  // Reset for next line
          memset(linebuf,0,sizeof(linebuf));  // Clear buffer
          charcount=0;
        } else if (c != '\r') {
          currentLineIsBlank = false; // Not a blank line
        }
      }
    }
    delay(1); // Give client time to process

    client.stop();  // Disconnect the client
    Serial.println("client disconnected");
  }
}
