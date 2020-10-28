package cl.ucn.disc.hpc.primes;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
     * The complete list of primes
     */
    private final List<Long> allPrimes;

    /**
     * Counter of total primes
     */
    private static AtomicInteger cantPrimes = new AtomicInteger(0);

    /**
     * Constructor
     *
     * @param possiblePrime
     * @param current
     * @param allPrimes
     */
    public HiloTwo(long possiblePrime, AtomicLong current, List<Long> allPrimes) {
        super();
        this.possiblePrime = possiblePrime;
        this.current = current;
        this.allPrimes = allPrimes;
    }

    /**
     * the run from Thread
     */
    @Override
    public void run() {
        for (; ; ) {

            // number to verify if is prime
            final long prime = this.current.getAndIncrement();

            // without numbers to verify
            if (prime > this.possiblePrime)
                return;

            //if (isPrime(prime))
            if (isPrimeSecondVersion(prime))
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

        // the principal primes
        if (n < 6)
            if (n == 2 || n == 3 || n == 5)
                return true;

        // 1 isn't prime! - the multiples of 2,3,5 aren't primes
        if (n == 1 || n % 2 == 0 || n % 3 == 0 || n % 5 == 0)
            return false;

        // Testing primality from 2 to n-1
        for (long i = 3; (i * i) <= n; i += 2) {

            // if the module ==0 -> not prime!
            if (n % i == 0)
                return false;

        }
        return true;
    }

    /**
     * Verify if the number provided is prime checking the list
     *
     * @param n
     * @return
     */
    public boolean isPrimeSecondVersion(final long n) {

        return (this.allPrimes.contains(n)) ? true : false;
    }

    /**
     * The total primes counter
     *
     * @return
     */
    public static int getPrimes() {

        return cantPrimes.get();
    }

    /**
     * Restart the counter to 0 to a new iteration with different cores cant
     */
    public static void restartCounter() {

        cantPrimes = new AtomicInteger(0);
    }
}
