import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import task.RecursiveSumTask;

public class ForkJoinMain {
    private static final int SIZE = 1_000_000;

    public static void main(String[] args) {
        List<Long> list = LongStream.range(0, SIZE)
                .boxed()
                .collect(Collectors.toList());
        System.out.println(calculateListSum(list));
    }

    public static Long calculateListSum(List<Long> list) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        RecursiveSumTask recursiveSumTask = new RecursiveSumTask(0, list.size() - 1, list);
        return forkJoinPool.invoke(recursiveSumTask);
    }
}
