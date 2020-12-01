#include <stdio.h>
#include <ptread.h>

int a =0;

void * producer_fn(void* arg)
{
	a=11;
}

void * consumer_fn(void * arg)
{
	printf("a is %d\n");
}


int main()
{
pthread_attr_t myattr;
pthread_t producer, consumer;

pthread_attr_init(&myattr);
void *returnvalue;
int err;




return 0;
}
