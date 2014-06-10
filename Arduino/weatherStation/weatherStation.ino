#include <dht.h>
#define DHT11_PIN 5

dht DHT;

const int tempPin = A0;
const int lightPin = A1;
const int noisePin = A2;
char convert[20];

void setup(){
  //open a serial port
  Serial.begin(9600);
}

void loop(){
  //temperature
  int tempVal = analogRead(tempPin);
  //converte value to voltage
  float voltage = (tempVal/1024.0) * 5.0;
  //converte voltage to temperature
  float temperature =  (voltage - 0.5) * 100;
  delay(5);
  //light
  int lightVal = analogRead(lightPin);
  
  //humidity
  int humidityVal;
  //check humidity sensor error
  if (DHT.read11(DHT11_PIN) == DHTLIB_OK){
   humidityVal = DHT.humidity;
  }else{
  Serial.println("erreur");
  } 
 
  //noise
  int noiseVal = analogRead(noisePin);
  
  unsigned int frac;
  if(temperature >= 0)
        frac = (temperature - int(temperature)) * 100;
  else
        frac = (int(temperature)- temperature ) * 100;
  String toStockT = "temperature:" + (String)(int)temperature + '.' + (String) frac;
  String toStockH = ";humidity:" + (String)humidityVal;
  String toStockL =  ";light:" + (String)lightVal;
  String toStockN = ";noise:" + (String) noiseVal;
  String toStockP = ";pressure:test";
  String toStock = toStockT + toStockH + toStockL + toStockN +toStockP;
 Serial.println(toStock);
  toStock = "";
  delay(300000);
  
}
