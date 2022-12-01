package com.fastcampus.hadoop;

import com.fastcampus.hadoop.key.EntryWritable;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.Iterator;

public class DistCacheReduceSideJoinCustomKey extends Configured implements Tool {

    enum DataType {
        DEPARTMENT("ENUM_DATATYPE_DEPARTMENT"),
        EMPLOYEE("ENUM_DATATYPE_EMPLOYEE");

        private final String value;
        DataType(String value){
            this.value = value;
        }
        public String value() {
            return value;
        }
    }

    // Paritioner 클래스
    public static class KeyPartitioner extends Partitioner<EntryWritable<Text, Text>, Text> {

        // 파라미터는 순서대로 키, 값, 파티션 개수이다.
        // 반환값은 Partition의 번호이다.
        // EntryWritable의 key값이 같은 경우엔 같은 partition 번호를 반환한다.
        // Bitwise 연산을 한 이유는, partition 번호는 항상 양수여야 하기 떄문이다.
        @Override
        public int getPartition(EntryWritable<Text, Text> key, Text value, int i) {
            return (key.getKey().hashCode() & Integer.MAX_VALUE) % i;
        }
    }

    // Grouping 클래스
    // Reducer 에 전달 된 값들을 집계한다.
    // Modular 연산으로 인해 같은 Partitioner 에 다른 해시 값을 가진 key 가 들어올 수 있으므로, 실제 값과 비교하여 집계하는 클래스가 필요하다.
    // Key 가 같은 레코드들은 같은 reduce 함수에 입력된다.
    public static class GroupComparator extends WritableComparator {
        protected GroupComparator() {
            super(EntryWritable.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            EntryWritable<Text, Text> t1 = (EntryWritable) a;
            EntryWritable<Text, Text> t2 = (EntryWritable) b;
            return t1.getKey().compareTo(t2.getKey());
        }
    }

    // Key 정렬 클래스
    // 앞서 Grouping 된 레코드들을 정렬 해준다.
    public static class KeyComparator extends WritableComparator {
        protected KeyComparator() {
            super(EntryWritable.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            EntryWritable<Text, Text> t1 = (EntryWritable) a;
            EntryWritable<Text, Text> t2 = (EntryWritable) b;
            int cmp = t1.getKey().compareTo(t2.getKey());
            if (cmp!=0) return cmp;
            return t1.getValue().compareTo(t2.getValue());
        }
    }

    // Employee Mapper 클래스
    public static class EmployeeMapper extends Mapper<LongWritable, Text, EntryWritable<Text, Text>, Text> {
        EntryWritable<Text, Text> outKey = new EntryWritable<>(new Text(), new Text());
        Text outValue = new Text();
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, EntryWritable<Text, Text>, Text>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            outKey.set(new Text(split[6]), new Text(DataType.EMPLOYEE.value()));
            outValue.set(split[0]+"\t"+split[2]+"\t"+split[4]+"\t");
            context.write(outKey, outValue);
        }
    }
    // Department Mapper 클래스
    public static class DepartmentMapper extends Mapper<LongWritable, Text, EntryWritable<Text, Text>, Text> {
        EntryWritable<Text, Text> outKey = new EntryWritable<>(new Text(), new Text());
        Text outValue = new Text();
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, EntryWritable<Text, Text>, Text>.Context context) throws IOException, InterruptedException {
            String[] split = value.toString().split(",");
            outKey.set(new Text(split[0]), new Text(DataType.DEPARTMENT.value()));
            outValue.set(split[1]);
            context.write(outKey, outValue);
        }
    }

    public static class ReduceSideJoinReducer extends Reducer<EntryWritable<Text, Text>, Text, Text, Text> {
        Text outKey = new Text();
        Text outValue = new Text();
        @Override
        protected void reduce(EntryWritable<Text, Text> key, Iterable<Text> values, Reducer<EntryWritable<Text, Text>, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            Iterator<Text> iter = values.iterator();
            // values는 Enum의 value 값을 기준으로 정렬되어 입력되기 때문에 첫 레코드의 값은 무조건 ENUM_DATATYPE_DEPARTMENT의 값이다.
            String departmentText = iter.next().toString();
            // 이후의 값은 모두 ENUM_DATATYPE_EMPLOYEE의 값이다.
            while(iter.hasNext()) {
                Text employeeText = iter.next();
                String[] employeeSplit = employeeText.toString().split("\t");
                outKey.set(employeeSplit[0]);
                outValue.set(employeeSplit[1]+"\t"+employeeSplit[1]+"\t"+departmentText);
                context.write(outKey, outValue);
            }
        }
    }


    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(getConf(), "ReduceSideJoinCustomKey");

        job.setJarByClass(DistCacheReduceSideJoinCustomKey.class);

        job.setReducerClass(ReduceSideJoinReducer.class);

        job.setMapOutputKeyClass(EntryWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setPartitionerClass(KeyPartitioner.class); // Partitioner 클래스
        job.setSortComparatorClass(KeyComparator.class); // 2차 정렬 클래스
        job.setGroupingComparatorClass(GroupComparator.class); // Grouping 클래스

        MultipleInputs.addInputPath(job, new Path(strings[0]), TextInputFormat.class, EmployeeMapper.class);
        MultipleInputs.addInputPath(job, new Path(strings[1]), TextInputFormat.class, DepartmentMapper.class);

        FileOutputFormat.setOutputPath(job, new Path(strings[2]));

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new DistCacheReduceSideJoinCustomKey(), args);
        System.out.println(exitCode);
    }
}
