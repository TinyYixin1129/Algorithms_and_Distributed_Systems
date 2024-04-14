package main

import (
	"flag"
	"fmt"
	"sync"
)

//recursion algorithm
func filter(thisPrime int, in <-chan int, wg *sync.WaitGroup, results chan<- int) {
	defer wg.Done()
	out := make(chan int)				//channel sending the non-filtered numbers
	nextPrime := 0
	var nextWg sync.WaitGroup

	for n := range in {
		if n%thisPrime != 0 {
			if nextPrime == 0 { 		//the first is the prime
				nextPrime = n			//set the next prime
				results <- nextPrime
				nextWg.Add(1)
				go filter(nextPrime, out, &nextWg, results)
			} else { //keep the rest and sent to the next filter
				out <- n
			}
		}
	}
	close(out)
	nextWg.Wait() //wait the next filter finish
}

func main() {
	//default until 100
	maxi := flag.Int("n", 100, "length")
	flag.Parse()
	max := *maxi

	if max < 2 {
		fmt.Printf("Invalid number\n")
		return
	}


	c := make(chan int)
	results := make(chan int, max)
	results <- 2	//set 2 the first prime

	var wg sync.WaitGroup

	wg.Add(1)
	//begin the recursion
	go filter(2, c, &wg, results)
	
	//send all the numbers to the first filter
	for i := 3; i <= max; i++ {
		c <- i
	}
	close(c) //close before range

	wg.Wait()

	close(results)
	fmt.Printf("List of prime number until %d :\n", max)
	for prime := range results {
		fmt.Printf("%d  ", prime)
	}

}
