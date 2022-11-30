package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.StringTokenizer;

//job을 더욱 간편하게 작성할 수 있게 상속받는 Configure, Tool
public class WordCount extends Configured implements Tool {
    // 맵퍼 클래스는 static 으로 선언한다.
    // 제네릭은 순서대로 입력 key, 입력 value, 출력 key, 출력 value 이다.
    // 출력 key, 출력 value 는 run 함수에서 설정한 타입과 일치해야 한다.
    public static class TokenizeMapper extends Mapper<Object, Text, Text, IntWritable> {
        private Text word = new Text();
        private IntWritable one = new IntWritable(1);

        @Override
        protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString());
            while(st.hasMoreTokens()) {
                word.set(st.nextToken().toLowerCase());
                context.write(word, one);
            }
        }
    }
    // 리듀서 클래스는 static 으로 선언한다.
    // 제네릭은 순서대로 입력 key, 입력 value, 출력 key, 출력 value 이다.
    // 출력 key, 출력 value 는 run 함수에서 설정한 타입과 일치해야 한다.
    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int count = 0;
            for(IntWritable val : values) {
                count += val.get();
            }
            result.set(count);
            context.write(key, result);
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        // job 인스턴스 생성
        Job job = Job.getInstance(getConf(), "wordcount");
        // job에 필요한 설정
        job.setJarByClass(WordCount.class); // entry 클래스

        job.setMapperClass(TokenizeMapper.class); // 맵퍼 클래스
        job.setCombinerClass(IntSumReducer.class); // 컴바이너 클래스
        job.setReducerClass(IntSumReducer.class); // 리듀서 클래스

        //job.setMapOutputKeyClass(Text.class); // 맵퍼 출력 키 타입 - 맵퍼와 리듀서의 출력 키 타입이 같다면 설정 하지 않아도 된다.
        //job.setMapOutputValueClass(IntWritable.class); // 맵퍼 출력 값 타입 - 맵퍼와 리듀서의 출력 값 타입이 같다면 설정 하지 않아도 된다.
        job.setOutputKeyClass(Text.class); // 출력 키 타입
        job.setOutputValueClass(IntWritable.class); // 출력 값 타입

        //job.setInputFormatClass(TextInputFormat.class); // InputFormat 타입 - 설정하지 않으면 기본값(TextInputFormat)으로 지정된다.
        //job.setOutputFormatClass(TextOutputFormat.class); // OutputFormat 타입 - 설정하지 않으면 기본값(TextOutputFormat)으로 지정된다.

        // job 실행
        FileInputFormat.addInputPath(job, new Path(strings[0])); // 입력 파일 경로
        FileOutputFormat.setOutputPath(job, new Path(strings[1])); // 출력 파일 경로

        //verbose 인자는 진행상황 표준출력 여부이다.
        return job.waitForCompletion(true) ? 0 : 1;
    }

    /**
    Hadoop에선 GenericOptionsParser란 도구를 제공한다.
    GenericOptionsParser는 Hadoop CLI를 통해 Job을 실행할 때 실행인자를 통해 Hadoop의 Configuration을 설정할 수 있도록 지원하는 도구이다.
    Hadoop은 이러한 GenericOptionsParser를 편리하게 사용할 수 있도록 Tool이란 인터페이스를 제공한다.
    이 Tool의 run 함수를 구현함으로써 MapReduce job을 생성할 수 있다.
     */
    public static void main(String[] args) throws Exception {
        // main 함수를 통해 실행할 수 있도록 생성
        int exitCode = ToolRunner.run(new WordCount(), args);
        System.out.println(exitCode);
    }

}
