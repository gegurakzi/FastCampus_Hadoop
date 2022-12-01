package com.fastcampus.hadoop.key;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class TextTupleWritable implements WritableComparable<TextTupleWritable> {
    private Text key;
    private Text value;

    public TextTupleWritable() {
        this.key = new Text();
        this.value = new Text();
    }

    public void set(Text key, Text value) {
        this.key = key;
        this.value = value;
    }

    public Text getKey() {
        return key;
    }
    public Text getValue() {
        return value;
    }

    @Override
    public int compareTo(TextTupleWritable o) {
        int cmp = key.compareTo(o.key);
        if (cmp!=0) return cmp;
        return value.compareTo(o.value);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        key.write(dataOutput);
        value.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        key.readFields(dataInput);
        value.readFields(dataInput);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextTupleWritable that = (TextTupleWritable) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }
    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "EntryWritable{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
