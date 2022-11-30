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
        System.out.println(Arrays.toString(args));

        Configuration conf = new Configuration();
        GenericOptionsParser optionsParser = new GenericOptionsParser(conf, args);
        String val1 = conf.get("mapreduce.map.memory.mb");
        Boolean val2 = conf.getBoolean("job.test", false);
        System.out.println("mapreduce.map.memory.mb : "+val1+", "+"job.test : "+val2);

        String[] remainingArgs = optionsParser.getRemainingArgs();
        System.out.println(Arrays.toString(remainingArgs));
    }
}
