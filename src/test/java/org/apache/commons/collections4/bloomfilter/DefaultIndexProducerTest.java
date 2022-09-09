/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.bloomfilter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;

public class DefaultIndexProducerTest extends AbstractIndexProducerTest {

    private int[] values = generateIntArray( 10, 512 );

    @Override
    protected IndexProducer createProducer() {
        return IndexProducer.fromIndexArray( values );
    }

    @Override
    protected IndexProducer createEmptyProducer() {
        return new IndexProducer() {

            @Override
            public boolean forEachIndex(IntPredicate predicate) {
                return true;
            }
        };
    }

    /**
     * Generates an array of integers.
     * @param size the size of the array
     * @param bound the upper bound (exclusive) of the values in the array.
     * @return an array of int.
     */
    public static int[] generateIntArray( int size, int bound ) {
        Random rnd = new Random();
        int[] expected = new int[size];
        for (int i=0; i<size; i++) {
            expected[i] = rnd.nextInt(bound);
        }
        return expected;
    }

    /**
     * Creates a sorted set of Integers.
     * @param ary the array to sort and make unique
     * @return the sorted Set.
     */
    public static SortedSet<Integer> uniqueSet(int[] ary) {
        SortedSet<Integer> uniq = new TreeSet<Integer>();
        for (int idx : ary) {
            uniq.add(idx);
        }
        return uniq;
    }

    /**
     * Creates a sorted unique array of ints.
     * @param ary the array to sort and make unique
     * @return the sorted unique array.
     */
    public static int[] unique(int[] ary) {
        Set<Integer> uniq = uniqueSet(ary);
        int[] result = new int[uniq.size()];
        int i=0;
        for (int idx : uniq) {
            result[i++] = idx;
        }
        return result;
    }

    @Test
    public void testFromBitMapProducer() {
        for (int i=0; i<5000; i++) {
            int[] expected = generateIntArray( 7, 256 );
            long[] bits = new long[BitMap.numberOfBitMaps(256)];
            for (int bitIndex : expected) {
                BitMap.set(bits, bitIndex);
            }
            IndexProducer ip = IndexProducer.fromBitMapProducer(BitMapProducer.fromBitMapArray(bits));
            assertArrayEquals(unique(expected), ip.asIndexArray());
        }
    }

    @Test
    public void testFromIndexArray() {
        for (int i=0; i<5000; i++) {
            int[] expected = generateIntArray(10, 256);
            IndexProducer ip = IndexProducer.fromIndexArray(expected);
            assertArrayEquals(unique(expected), ip.asIndexArray());
        }
    }
}