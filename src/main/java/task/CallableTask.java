package task;

import java.util.List;
import java.util.concurrent.Callable;

public class CallableTask implements Callable<Long> {
    private final List<Long> integers;

    public CallableTask(List<Long> integers) {
        this.integers = integers;
    }

    @Override
    public Long call() {
        return integers.stream()
                .reduce(0L, Long::sum);
    }
}
