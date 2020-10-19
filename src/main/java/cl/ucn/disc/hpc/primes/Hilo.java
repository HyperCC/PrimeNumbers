package cl.ucn.disc.hpc.primes;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Principal Thread implemented
 */
public class Hilo extends Thread {

    /**
     * Number to check
     */
    private long possiblePrime;

    /**
     * Counter of total primes
     */
    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Partial constructor
     *
     * @param num
     */
    public Hilo(long num) {
        super();
        this.possiblePrime = num;
    }

    /**
     * Execute this run() if the Thread is started
     */
    @Override
    public void run() {

        if (isPrime(this.possiblePrime)) {
            counter.getAndIncrement();

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

    /**
     * The total primes counter
     *
     * @return
     */
    public static int getPrimes() {

        return counter.get();
    }

}
