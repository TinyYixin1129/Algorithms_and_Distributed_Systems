package main

import (
	"flag"
	"fmt"
	"sync"
)

func filter(thisPrime int, in <-chan int, wg *sync.WaitGroup, results chan<- int) {
	defer wg.Done()
	out := make(chan int)
	nextPrime := 0
	var nextWg sync.WaitGroup

	for n := range in {
		if n%thisPrime != 0 {
			if nextPrime == 0 { //the first is the prime
				nextPrime = n
				results <- nextPrime
				nextWg.Add(1)
				go filter(nextPrime, out, &nextWg, results)
			} else { //keep the rest
				out <- n
			}
		}
	}
	close(out)
	nextWg.Wait() //wait the next filter finish
}

func main() {
	maxi := flag.Int("n", 100, "length")
	flag.Parse()
	max := *maxi

	if max < 2 {
		fmt.Printf("Invalid number\n")
		return
	}

	c := make(chan int)
	results := make(chan int, max)
	results <- 2

	var wg sync.WaitGroup

	wg.Add(1)
	go filter(2, c, &wg, results)

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
