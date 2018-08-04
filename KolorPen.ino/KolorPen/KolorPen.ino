#include <SimpleTimer.h>
#include <TimerOne.h>

#define PEN 'P'
#define FREQUENCY 'F'
#define VOLUME 'O'
#define SECURITY 'S'
#define ERROR_OCCURED 'E'
#define BIG_NUMBER 1000000
#define SECURITY1_ALERT_DEFAULT LOW
#define SECURITY2_ALERT_DEFAULT LOW
#define SECURITY3_ALERT_DEFAULT LOW
#define SECURITY4_ALERT_DEFAULT LOW

enum EPinOutput
{
  ePIN_MAP_UNDEFINED = -1,
  ePIN_MAP_PEDAL = 2,
  ePIN_MAP_PEN_1_SELECT = 4,
  ePIN_MAP_PEN_2_SELECT = 5,
  ePIN_MAP_SECURITY1 = 3,
  ePIN_MAP_SECURITY2 = 7,
  ePIN_MAP_SECURITY3 = 8,
  ePIN_MAP_SECURITY4 = 11,
  ePIN_MAP_ERROR = 12,
  ePIN_MAP_VOLUME_ANALOG = 6, //9,
  ePIN_MAP_DPS = 10
};

// ------ PEDAL ------------------------------------------------------------------------------------
boolean ePedalState = false; // status of pedal
boolean ePedalStart = false;
unsigned long pedalPressStartTime = 0;
unsigned long pedalPressDuration = 0; // show time accumulator inmillisecond of pedal press
// ------ DEBOUNCE PEDAL ------------------------------------------------------------------------------------
int buttonState;                      // the current reading from the input pin
int lastButtonState = LOW;            // the previous reading from the input pin
unsigned long lastDebounceTime = 0; // the last time the output pin was toggled
unsigned long lastErrorOccured = 0;
unsigned long debounceDelay = 50;        // the debounce time; increase if the output flickers
// ------ PEN SELECTION ----------------------------------------------------------------------------
int ePenSelect = 1; // ePenSelect = 1, ePenSelect = 2

// ------ SECURITY ---------------------------------------------------------------------------------
boolean isValidSecurity = false;
boolean isDeviceBlocked = false;

// ------ Error ------------------------------------------------------------------------------------
boolean isError = false;
// -------------------------------------------------------------------------------------------------

// ------ Serial ------------------------------------------------------------------------------------
String inputString = "";        // a String to hold incoming data
boolean stringComplete = false; // whether the string is complete
boolean flag = false;
SimpleTimer timer;
int index = 0;
//---------------------------------------------------------------------------------------------------
long timerDPS = 1000000;

// a function to be executed periodically
void repeatMe() {
  char buffer[7];         //the ASCII of the integer will be stored in this char array
  itoa(index, buffer, 10); //(integer, yourBuffer, base)
  String s = buffer;
  //
  char bufferDuration[15];         //the ASCII of the integer will be stored in this char array
  itoa(pedalPressDuration, bufferDuration, 10); //(integer, yourBuffer, base)
  String duration = bufferDuration;
  //
  char bufferPen[2];
  itoa(ePenSelect, bufferPen, 10);
  String pen = bufferPen;
  //
  index++;
  writeToSerial("timer index: " + s + ", Press Duration: " + duration + ", Pen: " + pen + "\n"); 
}

void setup()
{
  Serial.begin(9600); //Sets the data rate in bits per second (baud) for serial data transmission
  setupPins();
  pinMode(LED_BUILTIN, OUTPUT);
  writeToSerial("KolorPen is ready\n");
  //
  timer.setInterval(1000, repeatMe);
  Timer1.initialize(timerDPS);
  //Timer1.stop();  // Ziv 2018/06/24
  Timer1.attachInterrupt( setToggle ); // attach the service routine here
  digitalWrite(ePIN_MAP_PEN_1_SELECT, ePenSelect); 
}

long calcDPS(int pulsePerSec) {
  long result = BIG_NUMBER / (pulsePerSec * 2);
  return result;
}

void setupPins()
{
  pinMode(ePIN_MAP_PEDAL, INPUT_PULLUP);
  pinMode(ePIN_MAP_SECURITY1, INPUT);
  pinMode(ePIN_MAP_SECURITY2, INPUT);
  pinMode(ePIN_MAP_SECURITY3, INPUT);
  pinMode(ePIN_MAP_SECURITY4, INPUT);
  pinMode(ePIN_MAP_ERROR, INPUT);
  //pinMode(ePIN_MAP_SECURITY2, INPUT);
  //pinMode(ePIN_MAP_SECURITY3, INPUT);
  //pinMode(ePIN_MAP_SECURITY4, INPUT);
  pinMode(ePIN_MAP_PEN_1_SELECT, OUTPUT);
  pinMode(ePIN_MAP_PEN_2_SELECT, OUTPUT);
  pinMode(ePIN_MAP_VOLUME_ANALOG, OUTPUT);
  pinMode(ePIN_MAP_DPS, OUTPUT);
}

void treatCheckSecurity()
{
  securityViolation();
  
  isValidSecurity = true;
}

//void treatPenDebounce() {
//  int reading = digitalRead(ePIN_MAP_PEDAL);
//  if (reading != lastButtonState) {
//    lastDebounceTime = millis();
//  }
//  if ((millis() - lastDebounceTime) > debounceDelay) {if (reading != buttonState) {
//      buttonState = reading;
//      if (buttonState == LOW) {
//        ePedalState = HIGH;
//      } else {
//        ePedalState = LOW;
//      }
//    }    
//  }
//  lastButtonState = reading;
//}
void treatPenSelect() {
  
}

void treatPedal()
{
  treatPedalDebounce(); // check state of pedal and set the ePedalState parameter (iterate for debounceDelay until state changes)
  if (ePedalState == true)
  { // pedal is pressed
    digitalWrite(LED_BUILTIN, HIGH);
    setPedalPressed();
  }
  else
  { // pedal is unpressed
    digitalWrite(LED_BUILTIN, LOW);
    setPedalUnpressed();
  }
}

void writeToSerial(String message)
{
  for (int i = 0; i < message.length(); i++)
  {
    Serial.write(message[i]); // Push each char 1 by 1 on each loop pass
  }
}

void treatSerialInput()
{
  if (stringComplete)
  {
    treatCommand(inputString);
    resetSerialInput();
  }
}

void loop()
{
  timer.run();
  treatCheckSecurity();
  treatErrorOccured();
  if (isValidSecurity == true)
  { // device is good to go
    treatPenSelect();
    treatPedal();
    treatSerialInput();
  }
  else
  { // device is hacked!!!
//
    blockDevice();
  }
}

//String parseSerialInput(String inputString)
//{
//  // parse command from string;
//  String command = "";
//  command = "ziv";
//  return command;
//}

void turnOnPen(char penNum)
{
  if (penNum == '1')
  {
    ePenSelect = 1;
    digitalWrite(ePIN_MAP_PEN_1_SELECT, HIGH);
    digitalWrite(ePIN_MAP_PEN_2_SELECT, LOW);
    writeToSerial("Pen 1 selected\n");
  }
  else
  {
    ePenSelect = 2;
    digitalWrite(ePIN_MAP_PEN_1_SELECT, LOW);
    digitalWrite(ePIN_MAP_PEN_2_SELECT, HIGH);
    writeToSerial("Pen 2 selected\n");
  }
}

void commandPenSelected(String command)
{ // pen1 = ['P', ' ', '1', '/n'], pen2 = ['P', ' ', '2', '/n']
  if (command.length() == 4)
  {
    if ((command[2] == '1') || (command[2] == '2'))
    {
      turnOnPen(command[2]);
    }
  }
}

void commandDPS(String command) { // ['F', ' ', '0', '/n'] >> ['F', ' ', '1', '0', '0', '/n']
  String numberStr = command.substring(2, command.length() - 1);
  int number = numberStr.toInt();
  char buffer[7];         //the ASCII of the integer will be stored in this char array
  itoa(number, buffer, 10); //(integer, yourBuffer, base)
  String s = buffer;
  //analogWrite(ePIN_MAP_FREQ_ANALOG, number); // TODO
  writeToSerial("frequenscy: " + s + "\n");
  timerDPS = calcDPS(number);
  Timer1.stop();  // Ziv 2018/06/24
  Timer1.setPeriod(timerDPS); // Ziv 2018/06/24
  Timer1.start(); // Ziv 2018/06/24
  
}

void treatErrorOccured() {
  int err = digitalRead(ePIN_MAP_ERROR);
  if (err == HIGH) {
    writeToSerial("E 1\n");
    while (true) {
      
    }
  }
}

void securityViolation() {
  if ( (SECURITY1_ALERT_DEFAULT != digitalRead(ePIN_MAP_SECURITY1)) || 
       (SECURITY2_ALERT_DEFAULT != digitalRead(ePIN_MAP_SECURITY2)) ||
       (SECURITY3_ALERT_DEFAULT != digitalRead(ePIN_MAP_SECURITY3)) ||
       (SECURITY4_ALERT_DEFAULT != digitalRead(ePIN_MAP_SECURITY4)) )
 {
    writeToSerial("S 1\n");
    digitalWrite(ePIN_MAP_PEN_1_SELECT, LOW);
    digitalWrite(ePIN_MAP_PEN_2_SELECT, LOW);
    while (true) {
    }
  }
}

void commandVolume(String command) { // ['F', ' ', '0', '/n'] >> ['F', ' ', '1', '0', '0', '/n']
  String numberStr = command.substring(2, command.length() - 1);
  int number = numberStr.toInt();
  char buffer[7];         //the ASCII of the integer will be stored in this char array
  itoa(number, buffer, 10); //(integer, yourBuffer, base)
  String s = buffer;

  // map it to the range of the analog out:
  int outputValue = map(number, 0, 1023, 0, 255);
  // change the analog out value:
  analogWrite(ePIN_MAP_VOLUME_ANALOG, outputValue); // TODO
  itoa(outputValue, buffer, 10); //(integer, yourBuffer, base)
  String s1 = buffer;
  writeToSerial("Volume: num " + s + " analog = " + s1 + "\n");
}

void treatCommand(String command)
{
  writeToSerial("command from Serial: " + command + "\n");
  switch (command[0])
  {
  case PEN:
    commandPenSelected(command);
    break;
  case FREQUENCY:
    commandDPS(command);
    // digitalWrite(ePIN_MAP_PEN_SELECT, HIGH);
    break;
  case VOLUME:
    commandVolume(command);
    break;
//  case SECURITY:
//    commandSecurityViolation(command)
//    break;

  default:
    // statements
    break;
  }
}

void resetSerialInput()
{ // clear the input string from serial
  inputString = "";
  stringComplete = false;
}

void treatPedalDebounce()
{ // https://www.arduino.cc/en/Tutorial/Debounce
  int reading = digitalRead(ePIN_MAP_PEDAL);
  // pressed = LOW, unpressed = HIGH
  if (reading != lastButtonState) {
    lastDebounceTime = millis();
  }
  if ((millis() - lastDebounceTime) > debounceDelay) {
    if (reading != buttonState) {
      buttonState = reading;
      if (buttonState == LOW) {
        ePedalState = HIGH;
      } else {
        ePedalState = LOW;
      }
    }
  }
  lastButtonState = reading;
}

void setToggle() {
//  int freqStatus = digitalRead(ePIN_MAP_DPS);
//  if (freqStatus^1) {
//    writeToSerial("ON\n");
//  } else {
//    writeToSerial("OFF\n");
//  }
  digitalWrite(ePIN_MAP_DPS, digitalRead(ePIN_MAP_DPS) ^ 1);
}

void setPedalPressed()
{
  if (ePedalStart == false)
  { // pedal pressed for the first time
    ePedalStart = true;
    pedalPressStartTime = millis();
    pedalPressDuration = 0;
    writeToSerial("p 1\n" );
    //
//    Timer1.stop();
//    Timer1.setPeriod(timerDPS);
//    Timer1.start();
  }
  else
  {
    // do nothing
  }
}

void dpsAbsuloteStop() {
  digitalWrite(ePIN_MAP_DPS, LOW);
}

void setPedalUnpressed()
{
  if (ePedalStart == true)
  {
    pedalPressDuration = millis() - pedalPressStartTime;
    writeToSerial("p 0\n" );
    ePedalStart = false;
    //Timer1.stop(); // Ziv 2018/06/24
    dpsAbsuloteStop();
  }
  else
  {
    // do nothing
  }
}

void blockDevice()
{
  isDeviceBlocked = true;
  // send something serial to android
  // set led/blink
  while (isDeviceBlocked)
  {
    // do something the change the state isDeviceBlocked = false;
  }
}

void serialEvent()
{
  while (Serial.available())
  {
    // get the new byte:
    char inChar = (char)Serial.read();
    // add it to the inputString:
    inputString += inChar;
    // if the incoming character is a newline, set a flag so the main loop can
    // do something about it:
    if ((inChar == '\n') || (inChar == '\r'))
    {
      stringComplete = true;
      // TODO: Parse command from input serial
    }
  }
}
