package com.fastcampus.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.*;
import java.net.URI;

public class FileSystemCopyFromLocal {

    public static void main(String[] args) throws IOException {
        String localSrc = args[0];
        String dest = args[1];

        Configuration conf = new Configuration();
        InputStream in = new BufferedInputStream(new FileInputStream(localSrc));
        FileSystem fs = FileSystem.get(URI.create(dest), conf);
        OutputStream out = fs.create(new Path(dest));

        IOUtils.copyBytes(in, out, 4096, true);
    }

}
