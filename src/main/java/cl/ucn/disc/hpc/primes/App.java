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
     * Principal main method
     *
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {

        log.debug("Initializing the App class ..");

        // The Chrono - to make snapshot of time
        final StopWatch stopWatch = StopWatch.createStarted();

        // quantity of primes up to ..
        final long maybePrime = 1000;
        // number of Threads to use simultaneously
        final int nThreads = 1;

        // The threads executor - use <n> threads to ..
        final ExecutorService executorService = Executors.newFixedThreadPool(nThreads);

        for (long i = 1; i < maybePrime; i++) {
            executorService.submit(new Hilo(i));
        }

        // Don't receive more tasks
        executorService.shutdown();

        if (executorService.awaitTermination(1, TimeUnit.HOURS)) {

            log.debug("Primes founded: {} in {}.", Hilo.getPrimes(), stopWatch);
        } else {

            // The calculate time
            log.info("Done in {} without primes founded", stopWatch);
        }
    }
}
