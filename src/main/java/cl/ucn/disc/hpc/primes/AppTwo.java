package cl.ucn.disc.hpc.primes;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Second version to find primes with N cores according mi PC
 */
public class AppTwo {

    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * the main
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        log.debug("Start the AppTwo..");

        // num max to find primes
        final long maxPrimes = 10000;


        // initialize the MakePrimesList
        log.debug("creando lista completa de primos");
        MakePrimesList losPrimos = new MakePrimesList(maxPrimes);

        // calculate the list of primes
        losPrimos.calculatePrimes();
        log.debug("lista completa de primos obtenida");

        // get the list complete of primes
        List<Long> listaPrimos = losPrimos.getNumPrimos();
        log.debug("fin lista primos total de {}", listaPrimos.size());


        // cant of loop for each test with N cores
        final int runs = 8;

        // cant of cores
        final int maxCores = Runtime.getRuntime().availableProcessors();
        log.debug("Finding primes numbers to {} with {} maxCores", maxPrimes, maxCores);

        // code initiator
        for (int nConcurrentThreads = maxCores; nConcurrentThreads > 0; nConcurrentThreads--) {

            // records to each iteration
            List<Long> times = new ArrayList<>();

            // run the App to 1-N cores
            for (int i = 1; i <= runs; i++) {
                long ms = initCalculate(nConcurrentThreads, maxPrimes, listaPrimos);
                times.add(ms / nConcurrentThreads);

                // restart the primes counter
                HiloTwo.restartCounter();
            }

            // print all times saved to N cores
            for (Long time : times) {
                log.debug("Time: {}", time);
            }

            // delete minim and maxim to avoid bias
            times.remove(Collections.min(times));
            times.remove(Collections.max(times));

            // results for iteration
            Double average = times.stream().mapToDouble(d -> d).average().orElse(0.0);
            log.debug("{} cores take an average of: {} ml", nConcurrentThreads, average);
        }

        log.debug("Ending the AppTwo..");
    }

    /**
     * Initialize the HiloTwo creation
     *
     * @param nConcurrentThreads
     * @param maxPrimes
     * @param allPrimes
     * @return
     * @throws InterruptedException
     */
    public static long initCalculate(final int nConcurrentThreads, final long maxPrimes, List<Long> allPrimes) throws InterruptedException {

        final ExecutorService executorService = Executors.newFixedThreadPool(nConcurrentThreads);

        // crear los hilos
        for (int i = 1; i <= nConcurrentThreads; i++)
            executorService.submit(new HiloTwo(maxPrimes, new AtomicLong(1), allPrimes));

        // iniciar conteo temporal del procesamiento
        final StopWatch stopWatch = StopWatch.createStarted();
        executorService.shutdown();

        // verificar que se cumpla la busueda en menos de 5 min o lanzar InterruptedException
        if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
            log.debug("{} cores found {} primes in {} milliseconds", nConcurrentThreads, HiloTwo.getPrimes() / nConcurrentThreads, stopWatch.getTime(TimeUnit.MILLISECONDS) / nConcurrentThreads);

        } else {
            log.debug("Can't finish with {} cores in {} milliseconds", nConcurrentThreads, stopWatch.getTime(TimeUnit.MILLISECONDS) / nConcurrentThreads);

        }

        // devolver cantidad total de tiempo empleado
        return stopWatch.getTime(TimeUnit.MILLISECONDS);
    }

}













