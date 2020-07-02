package task;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class RecursiveSumTask extends RecursiveTask<Long> {
    private static final int MAX_SIZE = 100_000;
    private final int low;
    private final int high;
    private final List<Long> integers;

    public RecursiveSumTask(int low, int high, List<Long> integers) {
        this.low = low;
        this.high = high;
        this.integers = integers;
    }

    @Override
    protected Long compute() {
        long sum = 0L;
        if ((high - low) < MAX_SIZE) {
            return integers.stream()
                    .mapToLong(num -> num)
                    .sum();
        } else {
            int middle = low + (high - low) / 2;
            RecursiveTask<Long> left = new RecursiveSumTask(low, middle, integers);
            RecursiveTask<Long> right = new RecursiveSumTask(middle, high, integers);
            left.fork();
            right.fork();
            sum += left.join() + right.join();
        }
        return sum;
    }
}
