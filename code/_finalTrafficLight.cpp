/*
 * _finalTrafficLight.cpp
 *
 *  Created on: 28 ott 2020
 *      Author: dadon
 */

#include <iostream>
#include <vector>
#include <limits>

#include <wiringPi.h>

using namespace std;
//
// CONFIG
//
const int led_green=0;
const int led_yellow=1;
const int led_red=2;
const int led_blue=3;

const int timeoutErrorMS = 500;
const int timeoutOnMS= 2000;

//initialise states matrix

vector <vector <int>> matrice = {{led_red,led_yellow,led_blue,-1},
				{-1,1,led_blue,-1},
				{led_green,-1,-1,-1},
				{-1,-1,-1,-1}};
static int state =0;
static char hop;
//
// UTILS
//

//initialise Pi pins
void init()
{
	wiringPiSetup();

	pinMode(led_green, OUTPUT);
	pinMode(led_yellow, OUTPUT);
	pinMode(led_red, OUTPUT);
	pinMode(led_blue, OUTPUT);
}

//interact with led

void setLed(int whichLed, bool value)
{
	cout << "Setting led  '"<< whichLed << "' to : " << (value ? "ON" : "OFF") << endl;
	digitalWrite(whichLed, value);
}

//set/reset all leds

void setAll(bool value)
{
	for(int i=0; i<=3; i++)
	{
		digitalWrite(i, value);
	}
}

//mfn and fsn functions
//
int nextState(int state,char hop)
{
int futureState= matrice[state][(int) hop -'a'];

	if (futureState < -1 || futureState > 3)
	{
		return -2;
	}

return futureState;
}

void machine(int state)
{
	switch(state)
	{
	        case 0:
	                cout << "il tuo hop ti ha fatto entrare nello stato: " << state << endl;
	                setLed(led_green, true);
	                break;
	        case 1:
	                cout << "il tuo hop ti ha fatto entrare nello stato: " << state << endl;
	                setLed(led_yellow, true);
	                break;
	        case 2:
	                cout << "il tuo hop ti ha fatto entrare nello stato: " << state << endl;
	                setLed(led_red, true);
	        	break;
		case 3:
	                cout << "Congratulazioni! Hai raggiunto lo stato finale!" << endl;
	                setLed(led_blue,true);
			break;
		default: break;
	}

}
//////
////// Main
//////
int main()
{
init();
	cout << "Stato attuale: '" << state << "' " <<endl;
	setLed(led_green, true);

	while(1)
	{
		cout << "Inserisci il prossimo hop, 'x' per terminare l'esercitazione (hop legali: 'a','b','c')" << endl;
		cin >> hop;
		cin.clear();
		cin.ignore(numeric_limits<streamsize>::max(), '\n');
///exit char control
		if (hop=='x')
		{
			cout << "grazie e arrivederci" << endl;
			setAll(false);
			return 0;
		}
///computes next state only if hop is a letter
		else if (hop != 'x' && isalpha(hop))
		{
			state= nextState(state,hop);
//error control
			if (state<=-1)
			{
				goto error;
			}

			setAll(false);
			machine(state);
///end pgm and end loop
			if (state==3)
			{
				cout << "------------------------Fine programma!------------------------" << endl;
		                delay(timeoutOnMS);
			        setAll(false);
				return 0;
			}
		}

	}
	return 0;

error:
	if (state==-2)
	{
		cout << "Hop :'"<< hop << "' inserito errato (leciti solo: 'a','b','c'), programma terminato" << endl;
	}
	else if (state==-1)
	{
		cout << "L'hop inserito fa si che il linguaggio non sia legale per la nostra macchina a stati" << endl;
	}
	int flag=10;

	setAll(false);
	int onoff=1;
///error: every led blinks
	while(flag)
	{
		setAll(onoff);
		delay((timeoutErrorMS));
		onoff=!onoff;
		flag--;
	}
	setAll(false);

	return -1;
}
