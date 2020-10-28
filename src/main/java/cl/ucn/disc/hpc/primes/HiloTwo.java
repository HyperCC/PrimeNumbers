package cl.ucn.disc.hpc.primes;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ExecutorService;

public class HiloTwo extends Thread {

    /**
     * Number to check if is prime
     */
    private final long possiblePrime;

    /**
     * The tester
     */
    private final AtomicLong current;

    /**
     * Counter of total primes
     */
    private static AtomicInteger cantPrimes = new AtomicInteger(0);

    /**
     * Constructor
     *
     * @param possiblePrime
     * @param current
     */
    public HiloTwo(long possiblePrime, AtomicLong current) {
        super();
        this.possiblePrime = possiblePrime;
        this.current = current;
    }

    /**
     *
     */
    @Override
    public void run() {
        for (; ; ) {

            // number to verify if is prime
            final long prime = this.current.getAndIncrement();

            // without numbers to verify
            if (prime > this.possiblePrime)
                return;

            if (isPrime(prime))
                cantPrimes.getAndIncrement();

        }
    }

    /**
     * Verify if the number provided is prime
     *
     * @param n
     * @return the result
     */
    public boolean isPrime(final long n) {

        if (n <= 0) {
            throw new IllegalArgumentException("Error in n: Can't process negative n");
        }

        // 1 isn't prime!
        if (n == 1) {
            return false;
        }

        // Testing primality from 2 to n-1
        for (long i = 2; i < n; i++) {

            // if the module ==0 -> not prime!
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }
}
