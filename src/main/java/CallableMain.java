import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import task.CallableTask;

public class CallableMain {
    private static final int SIZE = 1_000_000;
    private static final int THREADS_AMOUNT = 10;

    public static void main(String[] args) throws InterruptedException {
        List<Long> list = LongStream.range(0, SIZE)
                .boxed()
                .collect(Collectors.toList());
        ExecutorService executorService = Executors.newFixedThreadPool(THREADS_AMOUNT);
        List<Callable<Long>> callableList = new ArrayList<>();
        int subListsNumber = list.size() / 10;
        for (int i = 0; i < 10; i++) {
            List<Long> subList = list.subList(i * subListsNumber, (i + 1) * subListsNumber);
            Callable<Long> subLists = new CallableTask(subList);
            callableList.add(subLists);
        }
        List<Future<Long>> futures = executorService.invokeAll(callableList);
        Long result = calculateListSum(futures);
        System.out.println(result);
        executorService.shutdown();
    }

    public static Long calculateListSum(List<Future<Long>> input) {
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
}
