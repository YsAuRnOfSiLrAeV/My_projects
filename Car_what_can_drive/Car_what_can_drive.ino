#include <Servo.h>

Servo s3;

int trigPin = 12;    
int echoPin = 13;    

long duration, cm;

void setup() {
  s3.attach(3);
  pinMode(6,OUTPUT);
  pinMode(11,OUTPUT);
  pinMode(9,OUTPUT);
  pinMode(8,OUTPUT);
  pinMode(7,OUTPUT);
  pinMode(5,OUTPUT);
  Serial.begin(9600);
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);
}


void loop(){

  digitalWrite(trigPin, LOW);
  delayMicroseconds(5);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  
  pinMode(echoPin, INPUT);
  
  updateDistance();
  
  Serial.print(cm);
  Serial.print("cm");
  Serial.println();
  delay(250);
  
  if (cm <= 20)
  {
      Stop();
      delay(50);
      s3.write(179);
      delay(50);
      updateDistance();
      
      if (cm <= 20) {
          s3.write(1);
          delay(50);
          updateDistance();
          
          if (cm <= 20) {
              s3.write(90);
              right();
          } else {
              left();
          }
      }
  } else {
      forward();
      s3.write(90);
  }
}

void updateDistance() {
    digitalWrite(trigPin, LOW);
    delayMicroseconds(5);
    digitalWrite(trigPin, HIGH);
    delayMicroseconds(10);
    digitalWrite(trigPin, LOW);

    duration = pulseIn(echoPin, HIGH);
    cm = (duration / 2) / 29.1;
}

void forward() {
digitalWrite(11,1);
digitalWrite(8,0);

digitalWrite(6,1);
digitalWrite(9,0);

digitalWrite(5,1);
digitalWrite(7,1);

}

void right() {
digitalWrite(11,0);
digitalWrite(8,0);

digitalWrite(6,1);
digitalWrite(9,1);

digitalWrite(5,1);
digitalWrite(7,1);
}

void backward() {
digitalWrite(11,0);
digitalWrite(8,1);

digitalWrite(6,1);
digitalWrite(9,1);

digitalWrite(5,1);
digitalWrite(7,0);
}

void left() {
digitalWrite(11,1);
digitalWrite(8,1);

digitalWrite(6,1);
digitalWrite(9,0);

digitalWrite(5,1);
digitalWrite(7,0);
}

void Stop() {
digitalWrite(11,1);
digitalWrite(8,1);

digitalWrite(6,1);
digitalWrite(9,1);

digitalWrite(5,1);
digitalWrite(7,1);
}
