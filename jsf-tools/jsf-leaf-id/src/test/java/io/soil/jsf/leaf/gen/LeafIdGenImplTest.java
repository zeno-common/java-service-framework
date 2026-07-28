package io.soil.jsf.leaf.gen;

import io.soil.jsf.leaf.IDGen;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class LeafIdGenImplTest {

    @Test
    void testNextId() {
        Set<Long> idSet = new HashSet<>();
        IDGen idGen = new LeafIdGenImpl(0, 0);
        for (int i = 1; i < 1000; ++i) {
            long r = idGen.nextId();
            assertTrue(idSet.add(r), "出现重复ID");
        }
    }
}
