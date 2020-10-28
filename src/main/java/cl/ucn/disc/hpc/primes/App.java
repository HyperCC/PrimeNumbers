package cl.ucn.disc.hpc.primes;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public final class App {

    /**
     * The Logger
     */
    private static final Logger log = LoggerFactory.getLogger(App.class);

    /**
     * The Chrono - to make snapshot of time
     */
    private static final StopWatch stopWatch = StopWatch.createStarted();

    /**
     * Principal main method
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        log.debug("Initializing the App class ..");

        // verify quantity of number primes to ..
        final long maybePrime = 10000;

        // Sequential execution time in milliseconds
        long Ts = getPrimesCant(maybePrime, 1).getTime(TimeUnit.MILLISECONDS);
        log.debug("Time to process sequentially: {} milliseconds", Ts);

        log.debug("Speedup: 1 with 1 Thread");
        log.debug("Efficiency: 1 with 1 Thread");
        log.debug("**********************************************************\n");

        // number of Threads to use simultaneously from 1 to 16
        for (int nThreads = 2; nThreads <= 16; nThreads++) {

            // restart the time
            stopWatch.reset();
            stopWatch.start();
            // restart the primes counter
            Hilo.restartCounter();

            // Time to execution with n Threads in milliseconds
            long Tn = getPrimesCant(maybePrime, nThreads).getTime(TimeUnit.MILLISECONDS);
            log.debug("Time to process with {} Threads: {} milliseconds", nThreads, Tn);

            double speedup = (Ts * 1.0 / Tn * 1.0);
            log.debug("Speedup: {} with {} Threads", speedup, nThreads);
            log.debug("Efficiency: {} with {} Threads", (speedup / nThreads * 1.0), nThreads);

            log.debug("**********************************************************\n");
        }

        log.debug("Ending the application..");
    }

    /**
     * Process the number of prime numbers and time
     *
     * @return time to complete
     */
    public static StopWatch getPrimesCant(final long maybePrime, final int nThreads) throws InterruptedException {

        // The threads executor - use <n> threads to ..
        final ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (long i = 1; i < maybePrime; i++) {

            executorService.submit(new Hilo(i));
        }

        // Don't receive more tasks
        executorService.shutdown();

        if (executorService.awaitTermination(1, TimeUnit.HOURS)) {

            log.info("Primes founded: {} in {} with {} threads.", Hilo.getPrimes(), stopWatch, nThreads);
        } else {

            // The calculate time
            log.info("Done in {} without primes founded", stopWatch);
        }

        return stopWatch;
    }
}
