package com.server.hll;


import net.agkn.hll.HLL;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HLLTest {

	public static void main(String[] args) {
		// test1();
		final int seed = 123456;
		HashFunction hash = Hashing.murmur3_128(seed);
		// data on which to calculate distinct count
		final Integer[] data = new Integer[] { 1, 1, 2, 3, 4, 5, 6, 6, 6, 7, 7,
				7, 7, 8, 10 };
		final HLL hll = new HLL(13, 5); // number of bucket and bits per bucket
		for (int item : data) {
			final long value = hash.newHasher().putInt(item).hash().asLong();
			hll.addRaw(value);
		}
		System.out.println(hll.cardinality());
		System.out.println(HLL.fromBytes(hll.toBytes()).cardinality());
		System.out.println("Distinct count=" + hll.cardinality());
		hll.clear();
		System.out.println("Distinct count=" + hll.cardinality());
	}

	public static void test1() {
		int seed = 123456;
		HLL hll = new HLL(13, 5);
		HashFunction hash = Hashing.murmur3_128(seed);
		// Hasher hasher = hash.newHasher();
		// hasher.putLong(1L);
		// hasher.putLong(10L);
		// hasher.putLong(11L);
		// hasher.putLong(12L);
		long hashedValue = hash.newHasher().putLong(1L).hash().asLong();
		hll.addRaw(hashedValue);
		hashedValue = hash.newHasher().putLong(2L).hash().asLong();
		hll.addRaw(hashedValue);
		hashedValue = hash.newHasher().putLong(3L).hash().asLong();
		hll.addRaw(hashedValue);
		hashedValue = hash.newHasher().putLong(21L).hash().asLong();
		hll.addRaw(hashedValue);
		long cardinality = hll.cardinality();
		System.out.println(cardinality);
		// final Murmur3_128HashFunction hash = new
		// Murmur3_128HashFunction(seed);
		// final Hasher hasher = hash.newHasher();
		// hasher.putLong(1L/*value to hash*/);
		//
		// final long hashedValue = hasher.hash().asLong();
		//
		// final HLL hll = new HLL(13/*log2m*/, 5/*registerWidth*/);
		// hll.addRaw(hashedValue);
	}

}
