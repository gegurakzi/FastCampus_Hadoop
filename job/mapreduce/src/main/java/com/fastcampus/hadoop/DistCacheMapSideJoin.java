package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DistCacheMapSideJoin extends Configured implements Tool {

    public static class MapSideJoinMapper extends Mapper<LongWritable, Text, Text, Text> {
        protected Text outKey = new Text();
        protected Text outValue = new Text();

        // Mapper 내에선 캐싱되어있는 파일을 불러오는 코드가 필요하다.
        // 캐싱된 파일을 메모리에 담아두기 위해 객체를 생성한다.
        protected Map<String, String> departmentsMap = new HashMap<>();

        // setup 함수의 오버라이딩을 통해 캐싱된 파일을 불러올 수 있다.
        @Override
        protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            URI[] uris = context.getCacheFiles();
            for (URI uri : uris) {
                Path path = new Path(uri.getPath());
                // 파일명을 사용하여 정의한 메모리 로드함수를 호출한다.
                loadDepartmentMap(path.getName());
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            // employees 데이터는 TextInputFormat 의 형태로 입력받는    다.
            /* employees 데이터의 형태는 다음과 같다.
             (emp_no, birth_date, first_name, last_name, sex, hire_date, dept_no)
             10012,1960-10-04,Patricio,Bridgland,M,1992-12-18,d005
             10013,1963-06-07,Eberhardt,Terkki,M,1985-10-20,d003
             10014,1956-02-12,Berni,Genin,M,1987-03-11,d005
             10015,1959-08-19,Guoxiang,Nooteboom,M,1987-07-02,d008
            */
            String[] split = value.toString().split(",");
            outKey.set(split[0]);
            String deptName = departmentsMap.get(split[6]);
            deptName = deptName==null ? "Not Found" : deptName;
            outValue.set(split[2]+"\t"+split[4]+"\t"+deptName);
            context.write(outKey, outValue);
        }

        // departmentMap 객체에 데이터를 담아줄 함수를 정의한다.
        /* department 데이터의 형태는 다음과 같다.
         (dept_no, dept_name)
         d009,Customer Service
         d005,Development
         d002,Finance
         d003,Human Resources
        */
        private void loadDepartmentMap(String fileName) throws IOException {
            String line = "";
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                while ((line = br.readLine()) != null) {
                    String[] split = line.split(",");
                    departmentsMap.put(split[0], split[1]);
                }
            }
        }
    }

    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(getConf(), "DistributedCacheMapSideJoin");
        // 분산캐시를 사용하기 위해 HDFS에 업로드 되어있는 데이터셋을 로드한다.
        job.addCacheFile(new URI("/user/malachai/dataset/departments"));

        job.setJarByClass(DistCacheMapSideJoin.class);

        job.setMapperClass(MapSideJoinMapper.class);

        // Reducer 를 사용하지 않음으로써 Mapper 작업만 수행하도록 한다.
        job.setNumReduceTasks(0);

        FileInputFormat.addInputPath(job, new Path(strings[0]));
        FileOutputFormat.setOutputPath(job, new Path(strings[1]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new DistCacheMapSideJoin(), args);
        System.out.println(exitCode);
    }
}
