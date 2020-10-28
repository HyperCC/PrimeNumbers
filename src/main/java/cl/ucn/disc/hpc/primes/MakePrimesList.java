package cl.ucn.disc.hpc.primes;

import java.util.ArrayList;
import java.util.List;

public final class MakePrimesList {

    /**
     * The list with the primes required
     */
    private static List<Long> numPrimos = new ArrayList();

    private final long maxNumber;

    public MakePrimesList(long maxNumber) {
        this.maxNumber = maxNumber;
    }

    public void calculatePrimes() {

        for (long i = 2; i < this.maxNumber; i++) {

            // the principal primes
            if (i < 6) {
                if (i == 2 || i == 3 || i == 5) {
                    numPrimos.add(i);
                    continue;
                }
            }

            // 1 isn't prime! - the multiples of 2,3,5 aren't primes
            if (i == 1 || i % 2 == 0 || i % 3 == 0 || i % 5 == 0) {
                continue;
            }

            boolean isPrime = true;

            for (long j = 3; j < i; j += 2) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                numPrimos.add(i);
            }
        }
    }

    public List<Long> getNumPrimos() {
        return numPrimos;
    }
}
