package com.stream.demo;

import com.stream.demo.domain.MaleStudent;
import com.stream.demo.domain.Member;
import com.stream.demo.domain.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void test_iterator() {

        List<String> list = Arrays.asList("John", "Simons", "Andy");
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            String name = iterator.next();
            System.out.println(name);
        }
    }

    @Test
    public void test_stream() {
        List<String> list = Arrays.asList("John", "Simons", "Andy");
        list.stream().forEach(System.out::println);
        list.stream().forEach(name -> System.out.println(name));
    }

    @Test
    public void tests_class_stream() {
        List<Student> list = Arrays.asList(new Student("John Smith", 91, Student.Sex.MALE), new Student("Andy Knight", 93, Student.Sex.MALE));
        Stream<Student> stream = list.stream();
        stream.forEach(s -> {
                    String name = s.getName();
                    int score = s.getScore();
                    System.out.println(name + "-" + score);
                }
        );

        list.stream().forEach(s -> System.out.println(s.getName()));
    }

    @Test
    public void tests_class_process() {
        List<Student> list = Arrays.asList(
                new Student("John", 76, Student.Sex.MALE),
                new Student("Jack", 88, Student.Sex.MALE),
                new Student("Smith", 100, Student.Sex.MALE));

        double avg = list.stream().mapToInt(Student::getScore).average().getAsDouble();
        System.out.println("평균 점수: " + avg);
    }

    @Test
    public void stream_from_array_of_String() {
        String[] strArr = {"Kim", "Lee", "Park"};
        Stream<String> stream1 = Stream.of(strArr); // return Stream
        Stream<String> stream2 = Arrays.stream(strArr); // return Stream<String> when array of String, if array of int return IntStream

        stream1.forEach(e -> System.out.println(e));
        stream2.forEach(e -> System.out.println(e));
    }

    @Test
    public void stream_from_array_of_int() {
        int[] intArr = {10, 20, 30, 40, 50};
        IntStream intStream1 = Arrays.stream(intArr);
        Stream<int[]> intStream2 = Stream.of(intArr);

        intStream1.forEach(e -> System.out.println(e));
        intStream2.forEach(e -> System.out.println(e + " ")); // why??
    }

    static int sum = 0;

    @Test
    public void stream_range() {

        IntStream stream = IntStream.rangeClosed(1, 100);
        stream.forEach(e -> sum += e);

        System.out.println(sum);

    }

    @Test
    public void stream_of_file() throws IOException {
        Path path = Paths.get("tmp_data.txt");
        Stream<String> stream;

        // Files.lines() 메소드 이용
        stream = Files.lines(path, Charset.defaultCharset());
        stream.forEach(System.out::println);
        System.out.println();

        // BufferedReader 의 lines() 메소드 이용
        File file = path.toFile();
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        stream = br.lines();
        stream.forEach(System.out::println);

        stream.close();
        br.close();
    }

    @Test
    public void stream_of_directory() throws IOException {
        Path path = Paths.get(".");
        Stream<Path> stream = Files.list(path);
        stream.forEach(p -> System.out.println(p.getFileName()));
    }

    @Test
    public void stream_pipeline() {
        List<Member> list = Arrays.asList(
                new Member("Kush", Member.MALE, 40),
                new Member("Pierre", Member.MALE, 22),
                new Member("Jolie", Member.FEMALE, 18),
                new Member("Sozi", Member.FEMALE, 22)
        );

        double ageAvg = list.stream()
                .filter(m -> m.getSex() == Member.MALE)
                .mapToInt(Member::getAge)
                .average()
                .getAsDouble();

        System.out.println("남자 평균 나이: " + ageAvg);
    }

    @Test
    public void stream_filter() {
        List<String> names = Arrays.asList(
                "Jack Daniel",
                "Andy Smith",
                "Demian Rice",
                "Mike Tomson",
                "Jack Daniel",
                "Jolie Martonne"

        );

        names.stream()
                .distinct()
                .forEach(n -> System.out.println(n));
        System.out.println();

        names.stream()
                .filter(n -> n.startsWith("J"))
                .forEach(n -> System.out.println(n));
        System.out.println();

        names.stream()
                .distinct()
                .filter(n -> n.startsWith("J"))
                .forEach(n -> System.out.println(n));
    }

    @Test
    public void flatMapExam() {
        List<String> inputList1 = Arrays.asList("java8 lamda", "Stream mapping");
        inputList1.stream()
                .flatMap(data -> Arrays.stream(data.split(" ")))
                .forEach(word -> System.out.println(word));

        System.out.println();

        List<String> inputList2 = Arrays.asList("10, 20, 30", "40, 50, 60");
        inputList2.stream()
                .flatMapToInt(data -> {
                    String[] strArr = data.split(",");
                    int[] intArr = new int[strArr.length];

                    for (int i = 0; i < strArr.length; i++) {
                        intArr[i] = Integer.parseInt(strArr[i].trim());
                    }

                    return Arrays.stream(intArr);
                })
                .forEach(number -> System.out.println(number));
    }

    @Test
    public void mapExam() {
        List<Student> student = Arrays.asList(
                new Student("Jack", 10, Student.Sex.MALE),
                new Student("Jolie", 20, Student.Sex.FEMALE),
                new Student("Smith", 30, Student.Sex.MALE)
        );

        student.stream()
                .mapToInt(Student::getScore)
                .forEach(score -> System.out.println(score));

    }

    @Test
    public void asDoubleStreamExam() {

        int[] intArr = {10, 20, 30, 40, 50, 60};

        IntStream intStream = Arrays.stream(intArr);
        intStream.asDoubleStream()
                .forEach(d -> System.out.println(d));

        System.out.println();

        intStream = Arrays.stream(intArr);
        intStream.boxed()
                .forEach(obj -> System.out.println(obj.intValue())); // Stream<Integer>
    }

    @Test
    public void stream_sort() {

        IntStream intStream = Arrays.stream(new int[]{9, 6, 11, 2, 3});
        intStream
                .sorted()
                .forEach(n -> System.out.print(n + ", "));

        System.out.println();

        List<Student> studentList = Arrays.asList(
                new Student("Jack", 80, Student.Sex.MALE),
                new Student("John", 100, Student.Sex.MALE),
                new Student("Andy", 99, Student.Sex.MALE),
                new Student("Jolie", 70, Student.Sex.FEMALE)
        );

        studentList.stream()
                .sorted()
                .forEach(s -> System.out.print(s.getScore() + ", "));

        System.out.println();

        studentList.stream()
                .sorted(Comparator.reverseOrder())
                .forEach(s -> System.out.print(s.getScore() + ", "));
    }

    @Test
    public void stream_loop() {
        int[] intArr = {5, 4, 3, 2, 1};

        System.out.println("[peek()를 마지막에 호출한 경우]");
        Arrays.stream(intArr)
                .filter(a -> a % 2 == 0)
                .peek(n -> System.out.println(n));    // 동작하지 않음

        System.out.println("[최종 처리 메소드를 마지막에 호출한 경우]");
        int total = Arrays.stream(intArr)
                .filter(a -> a % 2 == 0)
                .peek(n -> System.out.println(n))    // 동작함
                .sum();
        System.out.println("총합: " + total);

        System.out.println("[forEach를 마지막에 호출한 경우]");
        Arrays.stream(intArr)
                .filter(a -> a % 2 == 0)
                .forEach(n -> System.out.println(n)); // 최종 메소드로 동작함
    }

    @Test
    public void stream_match_exam() {

        int[] intArr = {2, 4, 6, 8, 10, 12};

        boolean result = Arrays.stream(intArr)
                .allMatch(a -> a % 2 == 0);
        System.out.println("모두 2의 배수 인가 ? " + result);

        result = Arrays.stream(intArr)
                .anyMatch(a -> a % 3 == 0);
        System.out.println("하나라도 3의 배수가 있는가 ? " + result);

        result = Arrays.stream(intArr)
                .noneMatch(a -> a % 5 == 0);
        System.out.println("5의 배수가 없는가 ? " + result);
    }

    @Test
    public void steam_aggregate() {

        int[] intArr = {5, 8, 11, 13, 19, 20, 24};
        long count = Arrays.stream(intArr)
                .filter(n -> n % 2 == 0)
                .count();
        System.out.println("2의 배수 개수: " + count);

        long sum = Arrays.stream(intArr)
                .filter(n -> n % 2 == 0)
                .sum();
        System.out.println("2의 배수의 합: " + sum);

        double avg = Arrays.stream(intArr)
                .average()
                .getAsDouble();
        System.out.println("배열의 평균; " + avg);

        int third = Arrays.stream(intArr)
                .filter(n -> n % 3 == 0)
                .findFirst()
                .getAsInt();
        System.out.println("3의 배수: " + third);

    }

    @Test(expected = NoSuchElementException.class)
    public void optional_test1() {
        List<Integer> list = new ArrayList<>();
        double avg = list.stream()
                .mapToInt(Integer::intValue)
                .average()
                .getAsDouble();

        System.out.println("평균: " + avg);
    }

    @Test
    public void optional_test2() {
        List<Integer> list = new ArrayList<>();
        double avg = list.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        System.out.println("평균: " + avg);
    }

    @Test
    public void optional_test3() {
        List<Integer> list = new ArrayList<>();
        OptionalDouble avg = list.stream()
                .mapToInt(Integer::intValue)
                .average();

        if (avg.isPresent()) {
            System.out.println("평균: " + avg.getAsDouble());
        } else {
            System.out.println("평균 : " + 0.0);
        }
    }

    @Test
    public void optional_test4() {
        List<Integer> list = new ArrayList<>();
        list.stream()
                .mapToInt(Integer::intValue)
                .average()
                .ifPresent(a -> System.out.println("평균: " + a));
    }

    @Test
    public void reduce_test() {

        List<Student> studentList = Arrays.asList(
                new Student("Jack", 10, Student.Sex.MALE),
                new Student("Jolie", 20, Student.Sex.FEMALE),
                new Student("Smith", 30, Student.Sex.MALE)
        );

        List<Student> studentList1 = new ArrayList<>();

        int sum = studentList.stream()
                .map(Student::getScore)
                .reduce((a, b) -> a + b)
                .get();

        int sum2 = studentList1.stream()
                .map(Student::getScore)
                .reduce(0, (a, b) -> a + b);

        System.out.println(sum);
        System.out.println(sum2);
    }

    @Test
    public void collect_test() {

        List<Student> totalList = Arrays.asList(
                new Student("Jack", 10, Student.Sex.MALE),
                new Student("Jolie", 20, Student.Sex.FEMALE),
                new Student("Smith", 30, Student.Sex.MALE)
        );

        Stream<Student> totalStream = totalList.stream();
        Stream<Student> maleStream = totalStream.filter(s -> s.getSex() == Student.Sex.MALE);
        Collector<Student, ?, List<Student>> collector = Collectors.toList();

        List<Student> maleList = maleStream.collect(collector);

        System.out.println(maleList);

        maleList.stream().forEach(System.out::println);
    }

    @Test
    public void collect_test2() {
        List<Student> totalList = Arrays.asList(
                new Student("Jack", 10, Student.Sex.MALE),
                new Student("Jolie", 20, Student.Sex.FEMALE),
                new Student("Smith", 30, Student.Sex.MALE)
        );

        List<Student> maleList = totalList.stream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(Collectors.toList());

        System.out.println(maleList);

        maleList.stream().forEach(s -> System.out.println(s.getName()));
    }

    @Test
    public void collect_test3() {
        List<Student> studentList = Arrays.asList(
                new Student("Jolie", 19, Student.Sex.FEMALE),
                new Student("Anne", 21, Student.Sex.FEMALE),
                new Student("Martin", 17, Student.Sex.MALE),
                new Student("Pierre", 15, Student.Sex.MALE),
                new Student("Garcons", 19, Student.Sex.MALE)
        );

        // 남학생들만 묶어서 List 생성
        List<Student> maleList = studentList.stream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(Collectors.toList());

        maleList.stream()
                .forEach(s -> System.out.println(s.getName()));

        System.out.println();

        // 여학생들만 묶어서 HashSet 생성
        Set<Student> femaleSet = studentList.stream()
                .filter(s -> s.getSex() == Student.Sex.FEMALE)
                .collect(Collectors.toCollection(HashSet::new));

        femaleSet.stream()
                .forEach(s -> System.out.println(s.getName()));
    }

    @Test
    public void user_defined_collector() {
        List<Student> studentList = Arrays.asList(
                new Student("Jolie", 19, Student.Sex.FEMALE),
                new Student("Anne", 21, Student.Sex.FEMALE),
                new Student("Martin", 17, Student.Sex.MALE),
                new Student("Pierre", 15, Student.Sex.MALE),
                new Student("Garcons", 19, Student.Sex.MALE)
        );

        Stream<Student> studentStream = studentList.stream();
        Stream<Student> maleStream = studentStream.filter(s -> s.getSex() == Student.Sex.MALE);

        Supplier<MaleStudent> supplier = () -> new MaleStudent();
        BiConsumer<MaleStudent, Student> accumulator = (ms, s) -> ms.accumulate(s);
        BiConsumer<MaleStudent, MaleStudent> combiner = (ms1, ms2) -> ms1.combine(ms2);

        MaleStudent maleStudent = maleStream.collect(supplier, accumulator, combiner);

        maleStudent.getList().stream().forEach(e -> System.out.println(e.getName()));
    }

    @Test
    public void user_defined_collector2() {
        List<Student> studentList = Arrays.asList(
                new Student("Jolie", 19, Student.Sex.FEMALE),
                new Student("Anne", 21, Student.Sex.FEMALE),
                new Student("Martin", 17, Student.Sex.MALE),
                new Student("Pierre", 15, Student.Sex.MALE),
                new Student("Garcons", 19, Student.Sex.MALE)
        );
        MaleStudent maleStudent = studentList.stream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(
                        () -> new MaleStudent(),
                        (r, t) -> r.accumulate(t),
                        (r1, r2) -> r1.combine(r2)
                );

        maleStudent.getList().stream().forEach(e -> System.out.println(e.getName()));
    }

    @Test
    public void user_defined_collector3() {
        List<Student> studentList = Arrays.asList(
                new Student("Man1", 11, Student.Sex.MALE),
                new Student("Tmp1", 11, Student.Sex.FEMALE),
                new Student("Man3", 11, Student.Sex.MALE),
                new Student("Tmp2", 11, Student.Sex.FEMALE),
                new Student("Man5", 11, Student.Sex.MALE)
        );

        MaleStudent maleStudent = studentList.stream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(MaleStudent::new, MaleStudent::accumulate, MaleStudent::combine);

        maleStudent.getList().stream()
                .forEach(s -> System.out.println(s.getName()));
    }

    @Test
    public void grouping_test() {
        List<Student> studentList = Arrays.asList(
                new Student("Man1", 11, Student.Sex.MALE),
                new Student("Tmp1", 11, Student.Sex.FEMALE),
                new Student("Man3", 11, Student.Sex.MALE),
                new Student("Tmp2", 11, Student.Sex.FEMALE),
                new Student("Man5", 11, Student.Sex.MALE)
        );

        Stream<Student> studentStream = studentList.stream();

        Function<Student, Student.Sex> classifier = Student::getSex;
        Collector<Student, ?, Map<Student.Sex, List<Student>>> collector =
                Collectors.groupingBy(classifier);

        Map<Student.Sex, List<Student>> mapBySex = studentStream.collect(collector);

        System.out.println(mapBySex);
    }

    @Test
    public void grouping_test2() {
        List<Student> studentList = Arrays.asList(
                new Student("Man1", 11, Student.Sex.MALE),
                new Student("Tmp1", 11, Student.Sex.FEMALE),
                new Student("Man3", 11, Student.Sex.MALE),
                new Student("Tmp2", 11, Student.Sex.FEMALE),
                new Student("Man5", 11, Student.Sex.MALE)
        );
        Map<Student.Sex, List<Student>> mapBySex = studentList.stream()
                .collect(Collectors.groupingBy(Student::getSex));

        System.out.println(mapBySex);
    }

    @Test
    public void grouping_test3() {
        List<Student> list = Arrays.asList(new Student("Kush", 40, Student.Sex.MALE, Student.City.Seoul),
                new Student("Pierre", 22, Student.Sex.MALE, Student.City.Seoul),
                new Student("Jolie", 18, Student.Sex.FEMALE, Student.City.Seoul),
                new Student("Jane", 18, Student.Sex.FEMALE, Student.City.Pusan),
                new Student("Sozi", 22, Student.Sex.FEMALE, Student.City.Pusan));

        Map<Student.Sex, List<Student>> mapBySex =
                list.stream().collect(Collectors.groupingBy(Student::getSex));

        System.out.println("[남학생] ");
        mapBySex.get(Student.Sex.MALE).stream()
                .forEach(s -> System.out.println("\t" + s.getName()));

        System.out.println("[여학생] ");
        mapBySex.get(Student.Sex.FEMALE).stream()
                .forEach(s -> System.out.println("\t" + s.getName()));

        System.out.println();

        Map<Student.City, List<String>> mapByCity = list.stream()
                .collect(
                        Collectors.groupingBy(
                                Student::getCity,
                                Collectors.mapping(Student::getName, Collectors.toList())
                        )
                );

        System.out.print("[서울] ");
        mapByCity.get(Student.City.Seoul).stream()
                .forEach(s -> System.out.println("\t" + s));

        System.out.print("[부산] ");
        mapByCity.get(Student.City.Pusan).stream()
                .forEach(s -> System.out.println("\t" + s));
    }

    @Test
    public void GroupingAndReducing() {
        List<Student> list = Arrays.asList(new Student("Kush", 40, Student.Sex.MALE, Student.City.Seoul),
                new Student("Pierre", 22, Student.Sex.MALE, Student.City.Seoul),
                new Student("Jolie", 18, Student.Sex.FEMALE, Student.City.Seoul),
                new Student("Jane", 18, Student.Sex.FEMALE, Student.City.Pusan),
                new Student("Sozi", 22, Student.Sex.FEMALE, Student.City.Pusan));

        // 성별로 평균 점수를 저장하는 Map 얻기
        Map<Student.Sex, Double> mapBySex = list.stream()
                .collect(
                        Collectors.groupingBy(
                                Student :: getSex,
                                Collectors.averagingDouble(Student :: getScore)
                        )
                );

        System.out.println("남학생 평균 점수: " + mapBySex.get(Student.Sex.MALE));
        System.out.println("여학생 평균 점수: " + mapBySex.get(Student.Sex.FEMALE));


        // 성별을 쉼표로 구분한 이름을 저장하는 Map 얻기
        Map<Student.Sex, String> mapByName = list.stream()
                .collect(
                        Collectors.groupingBy(
                                Student :: getSex,
                                Collectors.mapping(Student :: getName, Collectors.joining(","))
                        )
                );

        System.out.println("남학생 전체 이름: " + mapByName.get(Student.Sex.MALE));
        System.out.println("여학생 전체 이름: " + mapByName.get(Student.Sex.FEMALE));
    }

    @Test
    public void parallelCollect() {
        List<Student> studentList = Arrays.asList(
                new Student("Man1", 11, Student.Sex.MALE),
                new Student("Tmp1", 11, Student.Sex.FEMALE),
                new Student("Man3", 11, Student.Sex.MALE),
                new Student("Tmp2", 11, Student.Sex.FEMALE),
                new Student("Man5", 11, Student.Sex.MALE)
        );

        MaleStudent maleStudent = studentList.parallelStream()
                .filter(s -> s.getSex() == Student.Sex.MALE)
                .collect(MaleStudent :: new, MaleStudent :: accumulate, MaleStudent :: combine);

        maleStudent.getList().stream()
                .forEach(s -> System.out.println(s.getName()));
    }

    @Test
    public void sequence_parallel() {
        List<Integer> list = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);

        long timeSequential = testSequential(list);

        long timeParallel = testParallel(list);

        if (timeSequential < timeParallel) {
            System.out.println("순차처리가 더 빠름");
        } else {
            System.out.println("병렬처리가 더 빠름");
        }
    }

    public static void work(int value) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static long testSequential(List<Integer> list) {
        long start = System.nanoTime();
        list.stream().forEach((a) -> work(a));
        long end = System.nanoTime();
        long runTime = end - start;

        return runTime;
    }

    public static long testParallel(List<Integer> list) {
        long start = System.nanoTime();
        list.stream().parallel().forEach((a) -> work(a));
        long end = System.nanoTime();
        long runTime = end - start;

        return runTime;
    }

    @Test
    public void sequence_parallel2() {
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        for (int i = 0; i < 100000; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        long arrListListParallel = testParallel2(arrayList);
        long linkListListParallel = testParallel2(linkedList);

        if (arrListListParallel < linkListListParallel) {
            System.out.println("ArrayList 처리가 더 빠름");
        } else {
            System.out.println("LinkedList 처리가 더 빠름");
        }
    }

    public static long testParallel2(List<Integer> list) {
        long start = System.nanoTime();
        list.stream().parallel().forEach((a) -> work2(a));
        long end = System.nanoTime();
        long runTime = end - start;

        return runTime;
    }

    public static void work2(int value) {
    }
}