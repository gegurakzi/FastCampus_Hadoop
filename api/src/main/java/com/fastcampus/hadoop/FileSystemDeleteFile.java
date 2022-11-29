package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;

public class FileSystemDeleteFile {

    public static void main(String[] args) throws IOException {
        String url = args[0];
        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(URI.create(url), conf);
        Path path = new Path(url);
        if(fs.exists(path)) {
            fs.delete(path, false);
        }

    }

}
