#include <SimpleTimer.h>

#define PEN 'P'
#define FREQUENCY 'F'
#define POWER 'O'

enum EPinOutput
{
  ePIN_MAP_UNDEFINED = -1,
  ePIN_MAP_PEDAL = 2,
  ePIN_MAP_PEN_1_SELECT = 3,
  ePIN_MAP_PEN_2_SELECT = 4,
  ePIN_MAP_SECURITY1 = 5,
  ePIN_MAP_SECURITY2 = 6,
  ePIN_MAP_SECURITY3 = 7,
  ePIN_MAP_SECURITY4 = 8,
  ePIN_MAP_ERROR = 9,
  ePIN_MAP_FREQ_ANALOG = 23,
  ePIN_MAP_POWER_ANALOG = 24,
};

// ------ PEDAL ------------------------------------------------------------------------------------
boolean ePedalState = false; // status of pedal
boolean ePedalStart = false;
unsigned long pedalPressStartTime = 0;
unsigned long pedalPressDuration = 0; // show time accumulator inmillisecond of pedal press
// ------ DEBOUNCE PEDAL ------------------------------------------------------------------------------------
// int ePedalReadingState = HIGH;        // the current state of the output pin
int buttonState;                      // the current reading from the input pin
int lastButtonState = LOW;            // the previous reading from the input pin
unsigned long lastDebounceTime = 0; // the last time the output pin was toggled
unsigned long debounceDelay = 50;        // the debounce time; increase if the output flickers
// ------ PEN SELECTION ----------------------------------------------------------------------------
int ePenSelect = 1; // pen_1 = 1, pen_2 = 2

// ------ SECURITY ---------------------------------------------------------------------------------
boolean isValidSecurity = false;
boolean isDeviceBlocked = false;

// ------ Frequency --------------------------------------------------------------------------------
const int analogFrequencyPin = 3; // the number of the frequency pin - OUTPUT

// ------ Power ------------------------------------------------------------------------------------
const int analogPowerPin = 3; // the number of the power pin - OUTPUT

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
  index++;
  writeToSerial("timer index: " + s + ", pedalPressDuration: " + duration + "\n"); 
}

void setup()
{
  Serial.begin(9600); //Sets the data rate in bits per second (baud) for serial data transmission
  setupPins();
  writeToSerial("KolorPen is ready\n");
  pinMode(LED_BUILTIN, OUTPUT);
  //
  timer.setInterval(1000, repeatMe);
}

void setupPins()
{
  pinMode(ePIN_MAP_PEDAL, INPUT_PULLUP);
  pinMode(ePIN_MAP_SECURITY1, INPUT);
  pinMode(ePIN_MAP_SECURITY2, INPUT);
  pinMode(ePIN_MAP_SECURITY3, INPUT);
  pinMode(ePIN_MAP_SECURITY4, INPUT);
  pinMode(ePIN_MAP_PEN_1_SELECT, OUTPUT);
  pinMode(ePIN_MAP_PEN_2_SELECT, OUTPUT);
}

void treatCheckSecurity()
{
  int sec1 = digitalRead(ePIN_MAP_SECURITY1);
  int sec2 = digitalRead(ePIN_MAP_SECURITY2);
  int sec3 = digitalRead(ePIN_MAP_SECURITY3);
  int sec4 = digitalRead(ePIN_MAP_SECURITY4);

  // TODO: algoritem for security validation
  isValidSecurity = true;
}

void treatPedal()
{
  treatPedalDebounce(); // check state of pedal and set the ePedalState parameter (iterate for debounceDelay until state changes)
  if (ePedalState == true)
  { // pedal is pressed
    setPedalPressed();
  }
  else
  { // pedal is unpressed
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
  if (digitalRead(ePIN_MAP_PEDAL) == HIGH) {
    digitalWrite(LED_BUILTIN, LOW);
  } else {
    digitalWrite(LED_BUILTIN, HIGH);
  }
  treatCheckSecurity();
  if (isValidSecurity == true)
  { // device is good to go
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
    digitalWrite(ePIN_MAP_PEN_1_SELECT, HIGH);
    digitalWrite(ePIN_MAP_PEN_2_SELECT, LOW);
    writeToSerial("Pen 1 selected\n");
  }
  else
  {
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

void commandFrequency(String command) { // ['F', ' ', '0', '/n'] >> ['F', ' ', '1', '0', '0', '/n']
  String numberStr = command.substring(2, command.length() - 1);
  int number = numberStr.toInt();
  char buffer[7];         //the ASCII of the integer will be stored in this char array
  itoa(number, buffer, 10); //(integer, yourBuffer, base)
  String s = buffer;
  analogWrite(ePIN_MAP_FREQ_ANALOG, number); // TODO
  writeToSerial("frequenscy: " + s + "\n");
}

void commandPower(String command) { // ['F', ' ', '0', '/n'] >> ['F', ' ', '1', '0', '0', '/n']
  String numberStr = command.substring(2, command.length() - 1);
  int number = numberStr.toInt();
  char buffer[7];         //the ASCII of the integer will be stored in this char array
  itoa(number, buffer, 10); //(integer, yourBuffer, base)
  String s = buffer;
  analogWrite(ePIN_MAP_POWER_ANALOG, number); // TODO
  writeToSerial("power: " + s + "\n");
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
    commandFrequency(command);
    // digitalWrite(ePIN_MAP_PEN_SELECT, HIGH);
    break;
  case POWER:
    commandPower(command);
    break;
    //  case 4:
    //    analogWrite(ePIN_MAP_POWER_ANALOG, value);  // TODO value
    //    break;
    //  case 5:
    // reset treatment state ??

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

void setPedalPressed()
{
  if (ePedalStart == false)
  { // pedal pressed for the first time
    ePedalStart = true;
    pedalPressStartTime = millis();
    pedalPressDuration = 0;
    writeToSerial("pedal is pressed\n" );
    writeToSerial("p 1\n" );
  }
  else
  {
    // do nothing
  }
}

void setPedalUnpressed()
{
  if (ePedalStart == true)
  {
    pedalPressDuration = millis() - pedalPressStartTime;
    writeToSerial("Pedal Unpressed\n");
    writeToSerial("p 0\n" );
    ePedalStart = false;
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
    Serial.write('3');
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
