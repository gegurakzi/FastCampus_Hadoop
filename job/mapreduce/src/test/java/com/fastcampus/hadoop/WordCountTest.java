package com.fastcampus.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;

public class WordCountTest {

    MapDriver<Object, Text, Text, IntWritable> mapDriver;
    ReduceDriver<Text, IntWritable, Text, IntWritable> reduceDriver;
    // MapDriver 와 ReduceDriver 를 합친 Driver
    // Mapper 입력 kwy-value, Mapper 출력 - Reducer 입력 key-value, Reducer 출력 key-value 의 총 6개의 제네릭 타입을 선언해주어야 한다.
    MapReduceDriver<Object, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;

    @Before
    // MapDriver 와 reduceDriver 를 테스트 시작 전 생성
    public void setUp() {
        mapDriver = new MapDriver<>(new WordCount.TokenizeMapper());
        reduceDriver = new ReduceDriver<>(new WordCount.IntSumReducer());
        mapReduceDriver = new MapReduceDriver<>(new WordCount.TokenizeMapper(), new WordCount.IntSumReducer());
    }

    @Test
    // Mapper 단위 테스트 1
    // Setup을 사용하지 않고 다음과 같이 Test 내에서 MapDriver 를 생성할 수 있다.
    // runTest 메소드를 사용한다.
    public void wordCountMapTest1() throws IOException {
        // MapDriver를 통해 테스트 할 Mapper를 생성한다.
        new MapDriver<Object, Text, Text, IntWritable>() // 테스트 할 Mapper의 타입
                /* GIVEN */
                .withMapper(new WordCount.TokenizeMapper()) // 테스트 할 Mapper의 클래스
                /* WHEN */
                .withInput(new LongWritable(0L), new Text("dog dog cat owl dog cow cat owl dog")) // 테스츠 할 입력
                /* THEN */
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
    // Setup을 사용하여 mapDriver 멤버를 사용할 수 있다.
    // run 메소드를 사용한다.
    public void wordCountMapTest2() throws IOException {
        // run() 메소드는 List<Pair<K, V>> 타입의 결과물을 반환한다.
        /* GIVEN */
        List<Pair<Text, IntWritable>> result;
        result = mapDriver.withInput(new LongWritable(0L), new Text("dog dog cat owl dog cow cat owl dog")) // 테스츠 할 입력
                // runTest 대신 run 함수를 통해 결과를 반환받을 수 있다.
                /* WHEN */
                .run();
        /* THEN */
        // result.toString() == [(dog, 1), (dog, 1), (cat, 1), (owl, 1), (dog, 1), (cow, 1), (cat, 1), (owl, 1), (dog, 1)]


    }

    @Test
    // Reducer 단위 테스트
    // Setup을 사용하지 않고 다음과 같이 Test 내에서 ReduceDriver 를 생성할 수 있다.
    // runTest 메소드를 사용한다.
    public void wordCountReduceTest1() throws IOException {
        new ReduceDriver<Text, IntWritable, Text, IntWritable>()
                /* GIVEN */
                .withReducer(new WordCount.IntSumReducer())
                /* WHEN */
                .withInput(new Text("dog"), Arrays.asList(new IntWritable(1), new IntWritable(1), new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("cat"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("owl"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                .withInput(new Text("cow"), Arrays.asList(new IntWritable(1)))
                /* THEN */
                .withOutput(new Text("dog"), new IntWritable(4))
                .withOutput(new Text("cat"), new IntWritable(2))
                .withOutput(new Text("owl"), new IntWritable(2))
                .withOutput(new Text("cow"), new IntWritable(1))
                .runTest();
    }

    @Test
    // Reduce 단위 테스트 2
    // Setup을 사용하여 reduceDriver 멤버를 사용할 수 있다.
    // run 메소드를 사용한다.
    public void wordCountReduceTest2() throws IOException {
        // run() 메소드는 List<Pair<K, V>> 타입의 결과물을 반환한다.
        /* GIVEN */
        List<Pair<Text, IntWritable>> result;
        result = reduceDriver
                    .withInput(new Text("dog"), Arrays.asList(new IntWritable(1), new IntWritable(1), new IntWritable(1), new IntWritable(1)))
                    .withInput(new Text("cat"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                    .withInput(new Text("owl"), Arrays.asList(new IntWritable(1), new IntWritable(1)))
                    .withInput(new Text("cow"), Arrays.asList(new IntWritable(1)))
                // runTest 대신 run 함수를 통해 결과를 반환받을 수 있다.
                /* WHEN */
                .run();
        /* THEN */
        // result.toString() == [(dog, 4), (cat, 2), (owl, 2), (cow, 1)]

    }

    @Test
    // MapReduce 테스트
    // MapReduceDriver 의 runTest 메소드를 사용한다.
    public void wordCountMapReduceTest1() throws IOException {
        /* GIVEN */
        Pair<Object, Text> input = new Pair<>(new LongWritable(0L), new Text("dog dog cat owl dog cow cat owl dog"));
        /* WHEN */
        mapReduceDriver.withInput(input)
                /* THEN */
                .withOutput(new Text("cat"), new IntWritable(2))
                .withOutput(new Text("cow"), new IntWritable(1))
                .withOutput(new Text("dog"), new IntWritable(4))
                .withOutput(new Text("owl"), new IntWritable(2))
                .runTest();

    }

    @Test
    // MapReduce 테스트
    // MapReduceDriver 의 run 메소드를 사용한다.
    public void wordCountMapReduceTest2() throws IOException {
        /* GIVEN */
        List<Pair<Text, IntWritable>> result;
        Pair<Object, Text> input = new Pair<>(new LongWritable(0L), new Text("dog dog cat owl dog cow cat owl dog"));
        /* WHEN */
        result = mapReduceDriver.withInput(input)
                .run();
        /* THEN */
        // result.toString() == [(cat, 2), (cow, 1), (dog, 4), (owl, 2)]
        System.out.println(result);
    }


    @Test
    // Mockito 를 통해 테스트를 진행한다.
    // 객체의 멤버, 메소드의 변경, 호출 등을 관리 및 검증할 수 있는 Mock 객체를 이용한다.
    public void wordCountMapTestWithMockito1() throws IOException, InterruptedException {
        /* GIVEN */
        // 1. 테스트 할 Mapper 의 객체를 생성한다.
        WordCount.TokenizeMapper mapper = new WordCount.TokenizeMapper();

        // 2. 테스트 할 map 메소드의 인자들을 mock 객체로 생성한다.
        // key 와 value 는 검증할 필요가 없기 때문에 실제 객체로 생성한다.
        LongWritable key = new LongWritable(0L);
        Text value = new Text("dog dog cat owl dog cow cat owl dog");
        // map 메소드 내 mapper.word.set, context.write 메소드의 정상 호출 여부를 검증하기 위해 mock 객체로 생성한다.
        mapper.word = mock(Text.class);
        Mapper.Context context = mock(Mapper.Context.class);

        /* WHEN */
        // 3. 인자를 전달하여 테스트를 수행한다.
        mapper.map(key, value, context);

        /* THEN */
        // 4. 테스트 결과를 검증한다.
        // 메소드 호출 순서를 검증하기 위해선 InOrder 객체를 생성해야 한다.
        // 정상적인 호출 순서는 word.set - context.write - word.set - context.write - ... 이다.
        // 이를 검증하기 위해 mapper 의 word 와 context 를 인자로 넘겨 생성한다.
        // mapper.word 의 접근 제한은 protected 로 설정해야 한다.
        InOrder inOrder = inOrder(mapper.word, context);

        // Text 클래스인 mapper.word 의 set 메소드가 먼저 호출됨을 검증한다.
        inOrder.verify(mapper.word)
                // eq 메소드를 통해 set 메소드의 인자로 전달되는 String 이 입력값과 동일한지 검증한다.
                .set(eq("dog"));
        // Context 클래스인 context 의 write 메소드가 다음으로 호출됨은 검증한다.
        inOrder.verify(context)
                // eq 메소드를 통해 set 메소드의 인자로 전달되는 Text(mapper.word)와 IntWritable 이 동일한지 검증한다
                .write(eq(mapper.word), eq(new IntWritable(1)));
        // 이후 반복
        inOrder.verify(mapper.word).set(eq("dog"));
        inOrder.verify(context).write(eq(mapper.word), eq(new IntWritable(1)));
        inOrder.verify(mapper.word).set(eq("cat"));
        inOrder.verify(context).write(eq(mapper.word), eq(new IntWritable(1)));
        inOrder.verify(mapper.word).set(eq("owl"));
        inOrder.verify(context).write(eq(mapper.word), eq(new IntWritable(1)));


    }

    @Test
    public void wordCountReduceTestWithMockito1() throws IOException, InterruptedException {
        /* GIVEN */
        WordCount.IntSumReducer reducer = new WordCount.IntSumReducer();

        Text key = new Text("dog");
        List<IntWritable> values = Arrays.asList(new IntWritable(1), new IntWritable(1), new IntWritable(1));
        Reducer.Context context = mock(Reducer.Context.class);

        /* WHEN */
        reducer.reduce(key, values, context);

        /* THEN */
        verify(context).write(eq(key), eq(new IntWritable(3)));

    }

}
