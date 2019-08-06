package com.stream.demo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ParallelExam {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("John", "Simon", "Andy", "Andrew", "Bill");

        // 순차 처리
        Stream<String> stream = list.stream();
        stream.forEach(ParallelExam::print);

        System.out.println();

        // 병렬 처리
        Stream<String> parallelStream = list.parallelStream();
        parallelStream.forEach(ParallelExam::print);
    }

    public static void print(String str) {
        System.out.println(str + " : " + Thread.currentThread().getName());
    }

}