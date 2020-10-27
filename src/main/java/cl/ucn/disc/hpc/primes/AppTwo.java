package cl.ucn.disc.hpc.primes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AppTwo {

    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {

        long timeactual = System.currentTimeMillis();
        int cont = 0;

        /*
        for (long i = 2; i < 100000; i++) {

            if (i == 2 || i == 3 || i == 5 || i == 7) {
                cont++;
                continue;
            }

            if (i % 2 == 0 || i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                continue;
            }
            if (isPrime(i)) {
                log.debug("numero primo {}", i);
                cont++;
            }
        }

         */

        List<Long> numPrimos = new ArrayList();

        for (long i = 2; i <= 100000; i++) {
            if (i == 2 || i == 3 || i == 5 || i == 7) {
                cont++;
                numPrimos.add(i);
                continue;
            }
            if (i % 2 == 0 || i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                continue;
            }

            for (long j = 2; j < i; j++) {
                if (i % j == 0) {
                    break;
                } else if (j == i - 1) {
                    cont++;
                    numPrimos.add(i);
                }
            }
        }

        long timefinal = System.currentTimeMillis();

        log.debug("la hora final es {} ", (timefinal - timeactual));
        log.debug("total de primos {}", cont);
        //log.debug("total de buces {}", bucles);

        timeactual = System.currentTimeMillis();
        int nuevoCont = 0;
        log.debug("LA SEGUNDA VEZ:");
        for (long i = 2; i <= 100000; i++) {
            if (numPrimos.contains(i)) {
                nuevoCont++;
            }
        }
        timefinal = System.currentTimeMillis();
        log.debug("OTRO CONTADOR {}", nuevoCont);
        log.debug("la hora final es {} ", (timefinal - timeactual));

         /*
        total tiempo 5594
        total primos 9592
        total bucles 455189149
         */

    }

    public static boolean isPrime(final long n) {

        if (n <= 0) {
            throw new IllegalArgumentException("Error in n: Can't process negative n");
        }

        // 1 isn't prime!
        if (n == 1) {
            return false;
        }

        // Testing primality from 2 to n-1
        for (long i = 2; i < n; i++) {

            if (i == 2 || i == 3 || i == 5 || i == 7) {
                continue;
            }

            if (i % 2 == 0 || i % 3 == 0 || i % 5 == 0 || i % 7 == 0) {
                continue;
            }

            // if the module ==0 -> not prime!
            if (n % i == 0) {
                return false;
            }
        }
        return true;

        /*
        6281
        5802
        5557
         */

        /*
        6572
        5777
        6045
         */
    }
}
