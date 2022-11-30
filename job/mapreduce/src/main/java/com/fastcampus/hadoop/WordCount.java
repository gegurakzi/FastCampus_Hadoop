package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.StringTokenizer;

//job을 더욱 간편하게 작성할 수 있게 상속받는 Configure, Tool
public class WordCount extends Configured implements Tool {
    // 맵퍼 클래스는 static으로 선언
    // 제네릭은 순서대로 입력 key, 입력 value, 출력 key, 출력 value
    public static class TokenizeMapper extends Mapper<Object, Text, Text, IntWritable> {
        private Text word;
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
    // 리듀서 클래스는 static으로 선언
    // 제네릭은 순서대로 입력 key, 입력 value, 출력 key, 출력 value
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
        job.setReducerClass(IntSumReducer.class); // 리듀서 클래스
        job.setOutputKeyClass(Text.class); // 출력 키
        job.setOutputValueClass(IntWritable.class); // 출력 값

        // job 실행
        FileInputFormat.addInputPath(job, new Path(strings[0])); // 입력 파일 경로
        FileOutputFormat.setOutputPath(job, new Path(strings[1])); // 출력 파일 경로

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        // main 함수를 통해 실행할 수 있도록 생성
        int exitCode = ToolRunner.run(new WordCount(), args);
        System.out.println(exitCode);
    }

}
