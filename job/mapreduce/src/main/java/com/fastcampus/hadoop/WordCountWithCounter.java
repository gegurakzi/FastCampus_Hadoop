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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCountWithCounter extends Configured implements Tool {
    static enum Word {
        WITHOUT_SPECIAL_CHARACTER,
        WITH_SPECIAL_CHARACTER
    }

    public static class TokenizeMapper extends Mapper<Object, Text, Text, IntWritable> {
        private Text word = new Text();
        private IntWritable one = new IntWritable(1);

        // Pattern matching 을 통해 특수문자를 포함하지 않는 단어의 개수를 counter로 집계
        private Pattern pattern = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);

        @Override
        protected void map(Object key, Text value, Mapper<Object, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            StringTokenizer st = new StringTokenizer(value.toString());
            while(st.hasMoreTokens()) {
                String str = st.nextToken().toLowerCase();

                // Matcher를 통한 pattern matching
                Matcher matcher = pattern.matcher(str);

                // Counter는 Enum 또는 String을 통해 불러온다.
                // 특수문자가 포함된 단어의 Counter
                if (matcher.find()) context.getCounter(Word.WITH_SPECIAL_CHARACTER).increment(1);
                // 특수문자가 포함되지 않은 단어의 Counter
                else context.getCounter(Word.WITHOUT_SPECIAL_CHARACTER).increment(1);
                word.set(str);
                context.write(word, one);
            }
        }
    }
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
        job.setJarByClass(WordCountWithCounter.class); // entry 클래스

        job.setMapperClass(TokenizeMapper.class); // 맵퍼 클래스
        job.setCombinerClass(IntSumReducer.class); // 컴바이너 클래스
        job.setReducerClass(IntSumReducer.class); // 리듀서 클래스

        job.setOutputKeyClass(Text.class); // 출력 키 타입
        job.setOutputValueClass(IntWritable.class); // 출력 값 타입

        FileInputFormat.addInputPath(job, new Path(strings[0])); // 입력 파일 경로
        FileOutputFormat.setOutputPath(job, new Path(strings[1])); // 출력 파일 경로

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new WordCountWithCounter(), args);
        System.out.println(exitCode);
    }

}
