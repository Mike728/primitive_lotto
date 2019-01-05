import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Main {
   public static final int MATCH = 6;

    public static void main(String[] args) {
        List<Integer> integerList = drawNumbers();
        int i = 0;

        do {
            i++;
            integerList = drawNumbers();
            System.out.println(i + " " + integerList);
        } while(!integerList.containsAll(Arrays.asList(49, 48, 47, 46, 45, 44)));

    }

    public static List<Integer> drawNumbers() {
        Random random = new Random();
        List<Integer> myList = new ArrayList<>();

        Stream<Integer> integerStream = Stream.iterate(1, x -> x + 1)
            .map(function -> random.nextInt(49) + 1)
            .distinct()
            .peek(v -> {
                myList.add(v);
            });

        takeWhile(integerStream, v -> myList.size() != MATCH).collect(Collectors.toList());
        myList.sort(Comparator.comparingInt(v -> v.intValue()));
        return myList;
    }

    static <T> Spliterator<T> takeWhile(
        Spliterator<T> spliterator, Predicate<? super T> predicate) {
        return new Spliterators.AbstractSpliterator<T>(spliterator.estimateSize(), 0) {
            boolean running = true;

            @Override
            public boolean tryAdvance(Consumer<? super T> consumer) {
                if (running) {
                    boolean hadNext = spliterator.tryAdvance(elem -> {
                        if (predicate.test(elem)) {
                            consumer.accept(elem);
                        } else {
                            running = false;
                        }
                    });
                    return hadNext && running;
                }
                return false;
            }
        };
    }

    static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
}
