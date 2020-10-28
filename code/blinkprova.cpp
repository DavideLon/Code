#include <iostream>

#ifndef NO_PI
#include <wiringPi.h>
#endif

using namespace std;
//
// CONFIG
//
const int led_blue=0;
const int timeoutMS = 2000;
//
// UTILS
//
void init()
{
#ifndef NO_PI
	wiringPiSetup();
	pinMode(0, OUTPUT);
#endif
}

//function to interact with led

void setLed(int whichLed, bool value)
{
	cout << "Setting led  '"<< whichLed << "' to : " << (value ? "ON" : "OFF") << endl;
	digitalWrite(led_blue, value);
}

int main()
{
bool onoff= FALSE;
int flag=10;
	init();
// on inited, now let's light the led and make it blink
	while(flag)
	{
		setLed(led_blue, onoff);
		delay (timeoutMS);
		onoff = !onoff;
		flag--;
	}

return 0;
}
