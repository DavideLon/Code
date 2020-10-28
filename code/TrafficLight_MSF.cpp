/*
 * TrafficLight_MSF.cpp
 *
 *  Created on: 28 ott 2020
 *      Author: dadon
 */

#include <iostream>
#include <wiringPi.h>
#include <vector>
#include <limits>

#ifndef NO_PI
	#include <wiringPi.h>
#endif

using namespace std;
//
// CONFIG
//
const int led_blue=3;
const int led_red=2;
const int led_green=0;
const int led_yellow=1;

const int timeoutErrorMS = 500;
const int timeoutOnMS= 2000;

vector <vector <int>> matrice = {{2,1,3,-1},
								{-1,1,3,-1},
								{0,-1,-1,-1},
								{-1,-1,-1,-1}};

static int state =0;
static char hop;
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
	for(int i=1; i<=3; i++)
	{
		digitalWrite(i, value);
	}
}

///
/// Main
///
int main()
{

	while(1)
		{
			cout << "Stato attuale: '" << state << "' " <<endl;
			cout << "Inserisci il prossimo hop, 'x' per terminare l'esercitazione (hop legali: 'a','b','c')" << endl;
			cin >> hop;
			cin.clear();
			cin.ignore(numeric_limits<streamsize>::max(), '\n');

			if (hop=='x')
			{
				cout << "grazie e arrivederci" << endl;
				return 0;
			}
			else
			{
				state=matrice[state][(int)hop -'a'];
				if ((state < -1) || (state >3))
				{
					state= -2;
					//cout << "hop inserito errato!!"<<endl;
					goto error;
				}
				else if( state == -1)
				{
					goto error;
				}
				else if (state ==3 )
				{
					cout << "Congratulazioni! Hai raggiunto lo stato finale!" << endl;
					setLed(state,true);
					delay(timeoutOnMS);
					setAll(false);
					return 0;
				}
				else
				{
					cout << "il tuo hop ti ha fatto entrare nello stato: " << state << endl;
					setAll(false);
					setLed(state, true);
					delay(timeoutOnMS);
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
	int errFlag=10;

	setAll(false);
	int onoff=1;

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
