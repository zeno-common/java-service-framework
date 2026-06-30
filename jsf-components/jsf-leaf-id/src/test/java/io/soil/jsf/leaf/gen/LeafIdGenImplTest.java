package io.soil.jsf.leaf.gen;

import io.soil.jsf.leaf.IDGen;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class LeafIdGenImplTest {
    @Test
    public void testNextIdId() {

        Set<Long> idSet = new HashSet<>();
        IDGen idGen = new LeafIdGenImpl(0, 0);
        for (int i = 1; i < 1000; ++i) {
            long r = idGen.nextId();
            Assert.assertTrue("出现重复ID",idSet.add(r));
        }
    }
}
