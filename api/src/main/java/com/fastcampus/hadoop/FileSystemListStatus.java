package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class FileSystemListStatus {

    public static void main(String[] args) throws IOException {
        String url = args[0];
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(url), conf);

        Path path = new Path(url);
        FileStatus[] status = fs.listStatus(path);
        Path[] listPath = FileUtil.stat2Paths(status);
       for(Path p : listPath) {
           System.out.println(p);
       }
    }

}
