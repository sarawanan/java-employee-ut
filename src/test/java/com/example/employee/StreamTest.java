package com.example.employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
public class StreamTest {
    private List<Employee> list;
    @BeforeEach
    void setUp() {
        list = List.of(
                Employee.builder()
                        .id(1L)
                        .firstName("Sarawanan")
                        .lastName("Mahalingam")
                        .email("sarawananm@icloud.com")
                        .salary(15000F)
                        .build(),
                Employee.builder()
                        .id(2L)
                        .firstName("Krithika")
                        .lastName("Sarawanan")
                        .email("krithikasarawanan@yahoo.com")
                        .salary(10000F)
                        .build(),
                Employee.builder()
                        .id(3L)
                        .firstName("Shreeya")
                        .lastName("Sarawanan")
                        .email("shreeyasarawanan@yahoo.com")
                        .salary(8000F)
                        .build(),
                Employee.builder()
                        .id(4L)
                        .firstName("Sahana")
                        .lastName("Sarawanan")
                        .email("sahanasarawanan@yahoo.com")
                        .salary(5000F)
                        .build());
    }

    @Test
    void testMap() {
        var empIdList = list.stream().map(Employee::getId).toList();
        assertEquals(4, empIdList.size());
        empIdList.forEach(System.out::println);
    }

    @Test
    void testFilter() {
        var empList = list.stream().filter(employee -> employee.getSalary() > 10000F).toList();
        assertEquals(1, empList.size());
        empList.forEach(System.out::println);
    }

    @Test
    void findFirst() {
        var emp = list.stream().filter(employee -> employee.getSalary() > 10000F).findFirst();
        assertNotNull(emp);
    }

    @Test
    void findFirstOrNull() {
        var emp = list.stream().filter(employee -> employee.getSalary() > 10000F).findFirst().orElse(null);
        assertNotNull(emp);
    }

    @Test
    void listToArray() {
        var empArray = list.toArray(Employee[]::new);
        assertEquals("Sarawanan", empArray[0].getFirstName());
    }

    @Test
    void testPeek() {
        var empList = list.stream()
                .peek(employee -> employee.setSalary(employee.getSalary() + employee.getSalary() * 2))
                .peek(System.out::println)
                .toList();
        assertEquals(45000L, empList.get(0).getSalary());
    }

    @Test
    void flatMap() {
        var nested = Arrays.asList(
                Arrays.asList("Sarawanan", "Krithika"),
                Arrays.asList("Shreeya", "Sahana"));
        var flatList = nested.stream().flatMap(Collection::parallelStream).toList();
        flatList.forEach(System.out::println);
        assertTrue(flatList.contains("Sahana"));
    }

    @Test
    void countAndPipe() {
        var count = list.stream().filter(employee -> employee.getSalary() > 8000F).count();
        assertEquals(2, count);
        var empList = list.stream().skip(1).limit(2).toList();
        empList.forEach(System.out::println);
        assertEquals(2, empList.size());
    }

    @Test
    void minMaxAndAverage() {
        var minSalary = list.stream().min(Comparator.comparing(Employee::getSalary)).orElseThrow();
        System.out.println(minSalary);
        assertEquals(5000F, minSalary.getSalary());
        var minSal = list.stream().mapToDouble(Employee::getSalary).min().orElseThrow();
        assertEquals(5000F, minSal);
        var average = list.stream().mapToDouble(Employee::getSalary).average().orElseThrow();
        System.out.println(average);
        assertEquals(9500F, average);
        var maxSalary = list.stream().max(Comparator.comparing(Employee::getSalary)).orElseThrow();
        assertEquals(15000F, maxSalary.getSalary());
        var maxSal = list.stream().mapToDouble(Employee::getSalary).max().orElseThrow();
        assertEquals(15000F, maxSal);
    }

    @Test
    void findAllNoneAny() {
        assertTrue(list.stream().anyMatch(employee -> employee.getSalary() > 8000F));
        assertFalse(list.stream().allMatch(employee -> employee.getSalary() > 20000F));
        assertTrue(list.stream().noneMatch(employee -> employee.getSalary() > 20000F));
    }

    @Test
    void reduce() {
        assertEquals(38000F, list.stream().map(Employee::getSalary).reduce(0.0F, Float::sum));
    }

    @Test
    void statistics() {
        var statistics = list.stream().mapToDouble(Employee::getSalary).summaryStatistics();
        assertEquals(38000F, statistics.getSum());
        assertEquals(9500F, statistics.getAverage());
        assertEquals(5000F, statistics.getMin());
        assertEquals(15000F, statistics.getMax());
    }

    @Test
    void partitioning() {
        var map = list.stream()
                .collect(Collectors.partitioningBy(employee -> employee.getSalary() < 8000F));
        map.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }

    @Test
    void grouping() {
        var map = list.stream()
                .collect(Collectors.groupingBy(employee -> employee.getSalary() > 8000F));
        map.forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });
    }

    @Test
    void distinctList() {
        var integerList = List.of(2, 5, 4, 2, 7, 5, 6, 9, 9);
        var distinctList = integerList.stream().distinct().toList();
        System.out.println("Distinct List 1");
        distinctList.forEach(System.out::println);
        assertEquals(6, distinctList.size());
        System.out.println("Distinct List 2");
        IntStream.of(2, 5, 4, 2, 7, 5, 6, 9, 9).distinct().forEach(System.out::println);
        System.out.println("Distinct List 3");
        var set = new HashSet<>(integerList);
        set.forEach(System.out::println);
        var formatted = "Size: %s".formatted(set.size());
        System.out.println(formatted);
    }

    @Test
    void coreConcepts() {
        var ints = IntStream.range(20, 50);
        ints.forEach(System.out::println);
        System.out.println(list.stream().limit(3)
                .map(Employee::getFirstName).collect(Collectors.joining(",")));
        Stream.generate(Math::random).limit(5).forEach(System.out::println);
//        RandomGeneratorFactory.of("test").create().ints(10).forEach(System.out::println);
    }

}
