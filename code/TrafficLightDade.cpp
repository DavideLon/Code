#include <iostream>
#include <vector>
#include <limits>

#ifndef NO_PI
	#include <wiringPi.h>
#endif

using namespace std;
//
// CONFIG
//
const int led_blue=0;
const int led_red=1;
const int led_green=2;
const int led_yellow=3;

const int timeoutMS = 1000;

//
// UTILS
//
void init()
{
#ifndef NO_PI
	wiringPiSetup();

	pinMode(0, OUTPUT);
	pinMode(1, OUTPUT);
	pinMode(2, OUTPUT);
	pinMode(3, OUTPUT);
#endif
}

//function to interact with led

void setLed(int whichLed, bool value)
{
	cout << "Setting led  '"<< whichLed << "' to : " << (value ? "ON" : "OFF") << endl;
	digitalWrite(whichLed, value);
}

int main()
{
int flag =10;

	init();
// on inited, now let's light the led and make it blink


	while(flag)
		{
			setLed(led_yellow, false);
			setLed(led_red, true);
			delay (timeoutMS);
			setLed(led_red, false);
			setLed(led_green, true);
			delay (timeoutMS);
			setLed(led_green, false);
			setLed(led_yellow, true);
			delay (timeoutMS);
			flag = flag -1
		}

return 0;
}
