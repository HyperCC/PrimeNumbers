package cl.ucn.disc.hpc.primes;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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

        final long maxPrimes = 10000;

        final int runs = 3;

        final int maxCores = Runtime.getRuntime().availableProcessors();
        log.debug("Finding to {} with {} maxCores", maxPrimes, maxCores);


        for (int nConcurrentThreads = maxCores; nConcurrentThreads > 0; nConcurrentThreads--) {
            List<Long> times = new ArrayList<>();

            for (int i = 0; i <= runs; i++) {
                long ms = initCalculate(nConcurrentThreads, maxPrimes);
                times.add(ms);
            }

            for (Long time : times) {
                log.debug("Time: {}", time);
            }

            times.remove(Collections.min(times));
            times.remove(Collections.max(times));

            Double average = times.stream().mapToDouble(d -> d).average().orElse(0.0);
            log.debug("{} cores take an avegae of: {} ml", nConcurrentThreads, average);
        }
    }

    /**
     * @param nConcurrentThreads
     * @param maxPrimes
     * @return
     * @throws InterruptedException
     */
    public static long initCalculate(final int nConcurrentThreads, final long maxPrimes) throws InterruptedException {

        final ExecutorService executorService = Executors.newFixedThreadPool(nConcurrentThreads);

        // crear los hilos
        for (int i = 1; i <= nConcurrentThreads; i++) {
            executorService.submit(new HiloTwo(maxPrimes, new AtomicLong(nConcurrentThreads)));
        }

        // iniciar conteo temporal del procesamiento
        final StopWatch stopWatch = StopWatch.createStarted();
        executorService.shutdown();

        // verificar que se cumpla la busueda en menos de 5 min o lanzar InterruptedException
        if (executorService.awaitTermination(5, TimeUnit.MINUTES)) {
            log.debug("{} cores found {} primes in {}", nConcurrentThreads, Hilo.getPrimes(), stopWatch);
        } else {
            log.debug("Can't finish with {} cores in {}", nConcurrentThreads, stopWatch);
        }

        // devolver cantidad total de tiempo empleado
        return stopWatch.getTime(TimeUnit.MILLISECONDS);
    }

}













