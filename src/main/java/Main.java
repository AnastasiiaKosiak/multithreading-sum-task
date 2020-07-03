import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import task.SumService;

public class Main {
    private static final int SIZE = 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        List<Long> list = LongStream.range(0, SIZE)
                .boxed()
                .collect(Collectors.toList());
        SumService sumService = new SumService();
        Long callableSum = sumService.getSumWithCallable(list);
        System.out.println(callableSum);
        Long joinSum = sumService.getSumWithForkJoin(list);
        System.out.println(joinSum);
    }
}
