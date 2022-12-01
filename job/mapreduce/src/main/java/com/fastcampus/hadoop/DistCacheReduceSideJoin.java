package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DistCacheReduceSideJoin extends Configured implements Tool {

    public static class EmployeeMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text outKey = new Text();
        Text outValue = new Text();
        Map<String, String[]> employeesMap = new HashMap<>();

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            URI[] uris = context.getCacheFiles();
            for (URI uri : uris) {
                Path path = new Path(uri.getPath());
                // 파일명을 사용하여 정의한 메모리 로드함수를 호출한다.
                System.out.println(path.getName());
                if(path.getName().equals("employees")) loadEmployeesMap(path.getName());
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            // mapper 의 출력 key 를 dept_no 로 설정한다.
            String[] cols = employeesMap.get(split[0]);
            outKey.set(cols[5]);
            // 어느 mapper 에서 출력한 레코드인지 알 수 있도록 prefix 를 추가한다.
            outValue.set("e"+"\t"+split[0]+"\t"+cols[1]+"\t"+cols[3]+"\t");
            context.write(outKey, outValue);
        }

        private void loadEmployeesMap(String fileName) throws IOException {
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(",");
                    employeesMap.put(split[0], Arrays.copyOfRange(split, 1, split.length));
                }
            }
        }
    }

    public static class DepartmentMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text outKey = new Text();
        Text outValue = new Text();
        Map<String, String[]> departmentsMap = new HashMap<>();

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            URI[] uris = context.getCacheFiles();
            for (URI uri : uris) {
                Path path = new Path(uri.getPath());
                // 파일명을 사용하여 정의한 메모리 로드함수를 호출한다.
                System.out.println(path.getName());
                if(path.getName().equals("department")) loadDepartmentsMap(path.getName());
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            // mapper 의 출력 key 를 dept_no 로 설정한다.
            String[] cols = departmentsMap.get(split[0]);
            outKey.set(split[0]);
            // 어느 mapper 에서 출력한 레코드인지 알 수 있도록 prefix 를 추가한다.
            outValue.set("d"+"\t"+cols[0]);
            context.write(outKey, outValue);
        }
        private void loadDepartmentsMap(String fileName) throws IOException {
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(",");
                    departmentsMap.put(split[0], Arrays.copyOfRange(split, 1, split.length));
                }
            }
        }
    }

    public static class ReduceSideJoinReducer extends Reducer<Text, Text, Text, Text> {
        Text outKey = new Text();
        Text outValue = new Text();

        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            Map<String, String> employeeMap = new HashMap<>();
            String deptName = "Not Found";
            for (Text t : values) {
                String[] split = t.toString().split("\t");
                // EmployeeMapper 에서 입력받은 데이터
                if(split[0].equals("e")) {
                    employeeMap.put(split[1], split[2]+"\t"+split[3]);
                }
                // DepartmentMapper 에서 입력받은 데이터
                else if (split[0].equals("d")) {
                    deptName = split[1];
                }
            }
            // employeeMap 을 순회하며 deptName 을 join 한다.
            for (Map.Entry<String, String> e : employeeMap.entrySet()) {
                outKey.set(e.getKey());
                outValue.set(e.getValue()+"\t"+deptName);
                context.write(outKey, outValue);
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(getConf(), "DistributedCacheReduceSideJoin");
        job.setJarByClass(DistCacheReduceSideJoin.class);

        job.setReducerClass(ReduceSideJoinReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Mapper나 InputFormat이 서로 다른 하나 이상의 파일 경로로 입력을 받기 위해 MultipleInputs api를 사용한다.
        // MultipleInputs.addInputPath(job, new Path(strings[0]), TextInputFormat.class, EmployeeMapper.class);
        // MultipleInputs.addInputPath(job, new Path(strings[1]), TextInputFormat.class, DepartmentMapper.class);

        FileOutputFormat.setOutputPath(job, new Path(strings[0]));
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new DistCacheReduceSideJoin(), args);
        System.out.println(exitCode);
    }
}
