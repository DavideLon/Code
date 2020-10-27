//#define NO_PI 5

#include <iostream>

#ifndef NO_PI
#include <wiringPi.h>
#endif

using namespace std;

const char a [] = "Hello";
const char b [] = "World";

int main()
{

//	cout << "Hello world!!" << endl;
	cout << a<< b<< endl;


return 0;
}
