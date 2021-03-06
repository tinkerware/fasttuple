package com.boundary.tuple;

import com.boundary.tuple.codegen.HeapTupleCodeGenerator;
import com.boundary.tuple.codegen.TupleAllocatorGenerator;
import com.boundary.tuple.unsafe.Coterie;
import sun.misc.Unsafe;

/**
 * Created by cliff on 5/9/14.
 */
public class HeapTupleSchema extends TupleSchema {
    private static final Unsafe unsafe = Coterie.unsafe();
    private TupleAllocatorGenerator.TupleAllocator allocator;

    public static class Builder extends TupleSchema.Builder {

        public Builder(TupleSchema.Builder builder) {
            super(builder);
        }

        public HeapTupleSchema build() throws Exception {
            return new HeapTupleSchema(this);
        }
    }

    public HeapTupleSchema(Builder builder) throws Exception {
        super(builder);
        generateClass();
    }

    @Override
    protected void generateClass() throws Exception {
        this.clazz = new HeapTupleCodeGenerator(iface, fieldNames, fieldTypes).cookToClass();
        this.allocator = new TupleAllocatorGenerator(clazz).createAllocator();
    }

    @Override
    public FastTuple createTuple() throws Exception {
        return allocator.allocate();
    }

    @Override
    public FastTuple[] createTupleArray(int size) throws Exception {
        FastTuple[] tuples = new FastTuple[size];
        for (int i=0; i<size; i++) {
            tuples[i] = createTuple();
        }
        return tuples;
    }

    @Override
    public void destroyTuple(FastTuple tuple) {
        //noop
    }

    @Override
    public void destroyTupleArray(FastTuple[] ary) {
        //noop
    }
}
