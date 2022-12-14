package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Arrays;

public class GenericOptionsParserApp {


    public static void main(String[] args) throws Exception {
        // 1. 모든 argument 확인
        System.out.println("1. "+Arrays.toString(args));

        Configuration conf = new Configuration();
        GenericOptionsParser optionsParser = new GenericOptionsParser(conf, args);
        String val1 = conf.get("mapreduce.map.memory.mb");
        Boolean val2 = conf.getBoolean("job.test", false);
        // 2. 옵션으로 파싱 된 인자 확인
        System.out.println("2. "+"mapreduce.map.memory.mb : "+val1+", "+"job.test : "+val2);

        String[] remainingArgs = optionsParser.getRemainingArgs();
        // 3. 옵션 파싱 후 나머지 argument 확인
        System.out.println("3. "+Arrays.toString(remainingArgs));
    }
}
