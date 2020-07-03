package task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

public class SumService {
    private static final int THREADS_AMOUNT = 10;

    public Long getSumWithCallable(List<Long> input) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_AMOUNT);
        List<Callable<Long>> callableList = new ArrayList<>();
        int subListsNumber = input.size() / 10;
        for (int i = 0; i < 10; i++) {
            List<Long> subList = input.subList(i * subListsNumber, (i + 1) * subListsNumber);
            Callable<Long> subLists = new CallableTask(subList);
            callableList.add(subLists);
        }
        List<Future<Long>> futures = executorService.invokeAll(callableList);
        Long result = calculateListSum(futures);
        executorService.shutdown();
        return result;
    }

    private Long calculateListSum(List<Future<Long>> input) {
        return input.stream()
                .mapToLong(longFuture -> {
                    try {
                        return longFuture.get();
                    } catch (InterruptedException | ExecutionException exception) {
                        throw new RuntimeException(exception);
                    }
                })
                .sum();
    }

    public Long getSumWithForkJoin(List<Long> input) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        RecursiveSumTask recursiveSumTask =
                new RecursiveSumTask(0, input.size() - 1, input);
        return forkJoinPool.invoke(recursiveSumTask);
    }
}

