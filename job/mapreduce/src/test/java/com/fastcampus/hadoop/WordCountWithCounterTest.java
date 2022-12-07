package com.fastcampus.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class WordCountWithCounterTest {


    MapDriver<Object, Text, Text, IntWritable> mapDriver;
    ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
    MapReduceDriver<Object, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

    @Before
    public void setUp() {
        mapDriver = new MapDriver<>(new WordCountWithCounter.TokenizeMapper());
        reduceDriver = new ReduceDriver<>(new WordCountWithCounter.IntSumReducer());
        mapReduceDriver = new MapReduceDriver<>(new WordCountWithCounter.TokenizeMapper(), new WordCount.IntSumReducer());
    }

    @Test
    // Mapper 단위 테스트 1
    public void wordCountWithCounterMapTest() throws IOException {
        new MapDriver<Object, Text, Text, IntWritable>()
                // given
                .withMapper(new WordCountWithCounter.TokenizeMapper())
                // when
                .withInput(new LongWritable(0L), new Text("dog dog cat owl dog cow cat owl dog"))
                // then
                .withOutput(new Text("dog"), new IntWritable(1))
                .withOutput(new Text("dog"), new IntWritable(1))
                .withOutput(new Text("cat"), new IntWritable(1))
                .withOutput(new Text("owl"), new IntWritable(1))
                .withOutput(new Text("dog"), new IntWritable(1))
                .withOutput(new Text("cow"), new IntWritable(1))
                .withOutput(new Text("cat"), new IntWritable(1))
                .withOutput(new Text("owl"), new IntWritable(1))
                .withOutput(new Text("dog"), new IntWritable(1))
                .runTest();
    }

    @Test
    // Mapper 단위 테스트 2
    public void wordCountWithCounterMapTest2() throws IOException {
        /* GIVEN */
        List<Pair<Text, IntWritable>> result;
        result = mapDriver.withInput(new LongWritable(0L), new Text("dog dog cat owl dog cow cat owl dog")) // 테스츠 할 입력
                /* WHEN */
                .run();
        /* THEN */
        // result.toString() == [(dog, 1), (dog, 1), (cat, 1), (owl, 1), (dog, 1), (cow, 1), (cat, 1), (owl, 1), (dog, 1)]

    }

    @Test
    // Reducer 단위 테스트 1
    public void wordCountWithCounterReduceTest() throws IOException {
        new ReduceDriver<Text, IntWritable, Text, IntWritable>()
                // given
                .withReducer(new WordCountWithCounter.IntSumReducer())
                // when
                .withInput(new Text("dog"), Arrays.asList(new IntWritable(1), new IntWritable(1), new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("cat"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("owl"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("cow"), Arrays.asList(new IntWritable(1)))
                // then
                .withOutput(new Text("dog"), new IntWritable(4))
                .withOutput(new Text("cat"), new IntWritable(2))
                .withOutput(new Text("owl"), new IntWritable(2))
                .withOutput(new Text("cow"), new IntWritable(1))
                .runTest();

    }

    @Test
    // Reducer 단위 테스트 2
    public void wordCountReduceTest2() throws IOException {
        /* GIVEN */
        List<Pair<Text, IntWritable>> result;
        result = reduceDriver
                .withInput(new Text("dog"), Arrays.asList(new IntWritable(1), new IntWritable(1), new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("cat"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("owl"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("cow"), Arrays.asList(new IntWritable(1)))
                /* WHEN */
                .run();
        /* THEN */
        // result.toString() == [(dog, 4), (cat, 2), (owl, 2), (cow, 1)]

    }

    @Test
    // Mapper Counter 테스트
    public void wordCountWithCounterCounterTest() throws IOException {
        /* GIVEN */
        List<Pair<Text, IntWritable>> result;
        result = mapDriver.withInput(new LongWritable(0L), new Text("dog dog cat owl! dog cow? cat owl! dog")) // 테스츠 할 입력
                /* WHEN */
                .run();
        /* THEN */
        // getCounter 의 findCounter 메소드를 이용하여 특정 Counter 를 가져올 수 있다.
        // Word.WITH_SPECIAL_CHARACTER Counter 와 Word.WITHOUT_SPECIAL_CHARACTER Counter 를 가져온다.
        long withSpecial = mapDriver.getCounters().findCounter(WordCountWithCounter.Word.WITH_SPECIAL_CHARACTER).getValue();
        long withoutSpecial = mapDriver.getCounters().findCounter(WordCountWithCounter.Word.WITHOUT_SPECIAL_CHARACTER).getValue();
        // result.toString() == [(dog, 1), (dog, 1), (cat, 1), (owl!, 1), (dog, 1), (cow?, 1), (cat, 1), (owl!, 1), (dog, 1)]
        // withSpecial == 3
        // withoutSpecial == 6

        assertEquals(withSpecial, 3);
        assertEquals(withoutSpecial, 6);
    }


}
