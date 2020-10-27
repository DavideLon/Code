#include <iostream>
//#ifndef NO_PI
#include <wiringPi.h>

using namespace std;

///config

const int led_Blue =0;
const int timeoutMs = 1000;

///utils

void init()
{
#ifndef NO_PI
	wiringPiSetup();
	pinMode(led_Blue, OUTPUT);
#endif
}

void setLed(int ledNumber, bool value)
{
	cout << "Setting Led" << ledNumber << "to" << (value ? "ON" : "OFF") << endl;
#ifndef NO_PI
	digitalWrite(ledNumber, value);
#endif

}

int main ()
{
	init();

	//On, Inited...

	//digitalWrite(led_Blue, true);   ///messo nella funzione
	bool onoff = true;

	while(1)
	{
		setLed(led_Blue, onoff);
		onoff= !onoff;

#ifndef NO_PI
		delay(timeoutMs);
//#else
		//TODO else delay (sleep?)
#endif
	}

	setLed(led_Blue, false);
	cout << "provaprova" << endl;
	return 0;
}

