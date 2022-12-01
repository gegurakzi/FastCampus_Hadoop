package com.fastcampus.hadoop.key;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class EntryWritable<K extends Writable, V extends Writable> implements WritableComparable<EntryWritable<K, V>>, Map.Entry<K, V> {
    private K key;
    private V value;

    public EntryWritable(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void set(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        V oldVal = this.value;
        this.value = value;
        return oldVal;
    }

    @Override
    public int compareTo(EntryWritable<K, V> o) {
        int cmp = ((Comparable<K>)key).compareTo(o.key);
        if (cmp!=0) return cmp;
        return ((Comparable<V>)value).compareTo(o.value);
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
        EntryWritable<?, ?> that = (EntryWritable<?, ?>) o;
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
