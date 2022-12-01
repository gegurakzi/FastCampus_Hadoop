package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;

public class SortWordCount extends Configured implements Tool {

    // 정렬에 사용될 mapper를 정의한다.
    public static class SortMapper extends Mapper<Text, Text, LongWritable, Text> {
        @Override
        protected void map(Text key, Text value, Mapper<Text, Text, LongWritable, Text>.Context context) throws IOException, InterruptedException {
            // MapReduce는 key를 기준으로 정렬하기 때문에, 단어 개수를 key로 반환하도록 한다.
            context.write(new LongWritable(Long.parseLong(value.toString())), key);

        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        // WordCount의 출력은 tab separated key-value 형태이다.
        // separator는 configuration을 통해 설정해준다.
        Configuration conf = getConf();
        conf.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator", "\t");

        Job job = Job.getInstance(conf, "sort-wordcount");

        job.setJarByClass(SortWordCount.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setMapperClass(SortMapper.class);
        // Reducer 클래스는 따로 정의하지 않고 default Reducer 를 사용한다.
        // 대신, 하나의 Reducer 에 데이터를 전달함으로써 하나의 파일에 전체정렬이 일어나도록 한다.
        // 만약 여러개의 Reducer 를 사용하게 된다면, 여러개의 파일에 나뉘어 각각 정렬되는 부분정령이 일어나게 된다.
        job.setNumReduceTasks(1);

        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new SortWordCount(), args);
        System.out.println(exitCode);
    }
}
