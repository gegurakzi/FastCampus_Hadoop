package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Arrays;


public class ToolRunnerApp extends Configured implements Tool {

    @Override
    public int run(String[] strings) throws Exception {
        Configuration conf = getConf();
        String val1 = conf.get("mapreduce.map.memory.mb");
        Boolean val2 = conf.getBoolean("job.test", false);
        // 2. 옵션으로 파싱 된 인자 확인
        System.out.println("2. "+"mapreduce.map.memory.mb : "+val1+", "+"job.test : "+val2);

        // 3. 옵션 파싱 후 나머지 argument 확인
        System.out.println("3. "+Arrays.toString(strings));
        return 0;
    }

    public static void main(String[] args) throws Exception {
        // 1. 모든 argument 확인
        System.out.println("1. "+Arrays.toString(args));
        int exitCode = ToolRunner.run(new ToolRunnerApp(), args);
        System.out.println(exitCode);
    }
}
