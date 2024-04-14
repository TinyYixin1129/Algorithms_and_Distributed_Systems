package main

import (
	"flag"
	"fmt"
	"runtime"
	"sync"
)

//calculat the somme of a table and put the answer into a channel
func sommer(table []int, c chan<- int) {
	sum := 0
	for _, val := range table {
		sum += val
	}
	c <- sum
}

func main() {
	//default 100 numbers
	len := flag.Int("n", 100, "length")
	flag.Parse()
	length := *len

	if length < 2 {
		println("Size invalid")
		return
	}

	//initialize the table
	numbers := make([]int, length)
	for i := 0; i < length; i++ {
		numbers[i] = i + 1
		//numbers[i] = rand.Int()
	}

	var nbcpu = runtime.NumCPU()

	//ex: 2 CPU with 7 Numbers, 7/2=3..1, we need 1 more CPU
	//So we choose 3+1=4, CPU1 carry 4 and CPU2 carry 3
	slice_size := length / nbcpu
	if length%nbcpu != 0 {
		slice_size++
	}

	ans := make(chan int, nbcpu)

	var wg sync.WaitGroup

	for i := 0; i < length; i += slice_size {
		end := i + slice_size
		if end > length { //the last slice
			end = length
		}
		wg.Add(1)
		go func(table []int) {
			defer wg.Done()
			sommer(table, ans)
		}(numbers[i:end])
	}

	go func() { //wait all goroutines are done
		wg.Wait()
		close(ans)
	}()

	result := 0
	for somme := range ans { //need to close chan
		result += somme
	}

	fmt.Printf("The somme is : %d\n", result)

}
