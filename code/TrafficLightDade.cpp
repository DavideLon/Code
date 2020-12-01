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
const int led_red=0;
const int led_green=1;
const int led_yellow=2;
const int led_blue=3;

const int timeoutRedGreenMS = 60;
const int timeoutYellowMS = 30;
//const int timeoutGreenMS = 1000;

const int iterations= 20;

static int counter=0;
static int state= 0;
static int countRed=0;
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

void setAll(bool value)
{
	for(int i=0; i<3; i++)
	{
		digitalWrite(i, value);
	}
}

void tl()
{
	setLed(led_red, true);
	countRed++;

	while(countRed<10)
	{
		//TLon();
		//transition(timer);
		if (counter ==iterations)
		{
			counter=0;
			state++;
			switch (state)
			{
				case led_red:
					setLed(led_yellow, false);
					setLed(led_red, true);
					countRed++;
					break;
				case led_green:
					setLed(led_red, false);
					setLed(led_green, true);
					break;
				case led_yellow:
					setLed(led_green, false);
					setLed(led_yellow, true);
					state=led_red-1;
					break;
			}
		}
		else
		{
			counter ++;
			if (state==led_red-1)
			{
				delay(timeoutYellowMS);
			}
			else
			{
				delay(timeoutRedGreenMS);
			}
		}
	}
}

//blinking yellow for error state

void blinkingYellow()
{
int flag = 10;
int onoff=1;

	setAll(false);

	while(flag)
	{
		setLed(led_yellow,onoff);
		delay(timeoutYellowMS*10);
		onoff=!onoff;
		flag--;
	}
}

int main()
{

	init();
	setAll(false);
/*
// on inited, now let's light the led and make it blink


	while(flag)
	{
			setLed(led_yellow, false);
			setLed(led_red, true);
			delay (timeoutRedMS);
			setLed(led_red, false);
			setLed(led_green, true);
			delay (timeoutGreenMS);
			setLed(led_green, false);
			setLed(led_yellow, true);
			delay (timeoutYellowMS);
			flag--;
	}
	flag=5;
*/

//same, but with functions
	tl();

	blinkingYellow();

	setAll(false);

return 0;
}
