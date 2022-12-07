package com.fastcampus.hadoop;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DistCacheMapSideJoinTest {

    /**
     * Before mapper tests
     **/
    DistCacheMapSideJoin.MapSideJoinMapper mapper;
    @Before
    public void setup() {
        mapper = new DistCacheMapSideJoin.MapSideJoinMapper();
    }

    /**
     * Mapper setUp() Test
     */
    @Test
    public void mapperMapTest() throws IOException, InterruptedException {
        /* GIVEN */
        mapper.departmentsMap.put("d009", "Customer Service");
        mapper.departmentsMap.put("d005", "Development");
        mapper.departmentsMap.put("d002", "Finance");
        mapper.departmentsMap.put("d003", "Human Resources");
        LongWritable key = new LongWritable(0L);
        Text value = new Text("10012,1963-06-07,Eberhardt,Terkki,M,1985-10-20,d003");
        Mapper.Context context = mock(Mapper.Context.class);
        /* WHEN */
        mapper.map(key, value, context);
        /* THEN */
        assertEquals(new Text("10012"), mapper.outKey);
        assertEquals(new Text("Eberhardt\tM\tHuman Resources"), mapper.outValue);
    }
}
