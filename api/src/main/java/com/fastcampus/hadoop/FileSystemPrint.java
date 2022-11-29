package com.fastcampus.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class FileSystemPrint {

    public static void main(String[] args) throws IOException {
        String url = args[0];
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(url), conf);
        try(InputStream in = fs.open(new Path(url))) {
            IOUtils.copyBytes(in, System.out, 4096, false);
        }
    }

}
