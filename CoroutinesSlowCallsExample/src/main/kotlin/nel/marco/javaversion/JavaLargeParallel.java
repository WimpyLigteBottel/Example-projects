package nel.marco.javaversion;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class JavaLargeParallel {


    WebClient client = WebClient.create("http://localhost:8080");

    public String exampleWrongSetup(Integer callAmount) {

        Instant before = Instant.now();

        var responses = IntStream.rangeClosed(0, callAmount)
                .parallel()
                .mapToObj(item -> retrieveUsers())
                .toList();


        var duration = Duration.between(before, Instant.now()).toMillis();
        return "JavaParallel: java parrallel stream [duration=" + duration + "]";
    }

    @Nullable
    private String retrieveUsers() {
        try {
            Thread.sleep(1000);
            return "DONE";
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    public String example(int callAmount) {
        var executors = Executors.newVirtualThreadPerTaskExecutor();
        var startTime = Instant.now();

        var listOfTasks = IntStream.range(0, callAmount).boxed().toList();

        List<CompletableFuture<String>> futures = listOfTasks
                .stream()
                .map(i -> CompletableFuture.supplyAsync(this::retrieveUsers, executors))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[callAmount])).join();

        long total = Duration.between(startTime,Instant.now()).toMillis();
        return getClass().getSimpleName() + ": example = " + total + " ms";
    }

}
