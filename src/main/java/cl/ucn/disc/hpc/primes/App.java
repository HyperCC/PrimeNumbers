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

        // number of Threads to use simultaneously
        final int nThreads = 1;
        // verify quantity of number primes to ..
        final long maybePrime = 100000;

        // Sequential execution time in milliseconds
        long Ts = getPrimesCant(maybePrime, 1).getTime(TimeUnit.MILLISECONDS);

        // Sequential processing
        log.debug("Time to process sequentially: {} milliseconds", Ts);
        stopWatch.reset();
        stopWatch.start();
        Hilo.restartCounter();


        // Time to execution with n Threads in milliseconds
        long Tn = getPrimesCant(maybePrime, nThreads).getTime(TimeUnit.MILLISECONDS);

        // Processing with Threads
        log.debug("Time to process with {} Threads: {} milliseconds", nThreads, Tn);

        log.debug("Speedup: {}", (Ts * 1.0 / Tn * 1.0));

    }

    /**
     * Process the number of prime numbers and time
     *
     * @return time to complete
     */
    public static StopWatch getPrimesCant(final long maybePrime, final int nThreads) throws InterruptedException {

        // quantity of primes up to ..
        //final long maybePrime = 1000;

        // number of Threads to use simultaneously
        //final int nThreads = 1;

        // The threads executor - use <n> threads to ..
        final ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (long i = 1; i < maybePrime; i++) {
            executorService.submit(new Hilo(i));
        }

        // Don't receive more tasks
        executorService.shutdown();

        // capure the time to process
        stopWatch.stop();

        if (executorService.awaitTermination(1, TimeUnit.HOURS)) {

            log.debug("Primes founded: {} in {} with {} threads.", Hilo.getPrimes(), stopWatch, nThreads);
        } else {

            // The calculate time
            log.info("Done in {} without primes founded", stopWatch);
        }

        return stopWatch;
    }
}
