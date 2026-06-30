package io.soil.jsf.common.collection;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * {@link CollectionUtil} 单元测试
 */
public class CollectionUtilTest {

    // ==================== toMap ====================

    @Test
    public void toMap_shouldConvertCollectionToMap() {
        List<String> values = Arrays.asList("apple", "banana", "cherry");
        Map<Character, String> result = CollectionUtil.toMap(values, s -> s.charAt(0));

        assertEquals(3, result.size());
        assertEquals("apple", result.get('a'));
        assertEquals("banana", result.get('b'));
        assertEquals("cherry", result.get('c'));
    }

    @Test(expected = IllegalStateException.class)
    public void toMap_withDuplicateKeys_shouldThrowException() {
        List<String> values = Arrays.asList("banana", "cherry");
        // "banana".length() == 6, "cherry".length() == 6, duplicate key
        CollectionUtil.toMap(values, String::length);
    }

    @Test(expected = NullPointerException.class)
    public void toMap_withNullCollection_shouldThrowNPE() {
        CollectionUtil.toMap(null, Function.identity());
    }

    // ==================== mapList (Function) ====================

    @Test
    public void mapList_shouldMapElements() {
        List<Integer> source = Arrays.asList(1, 2, 3);
        List<String> result = CollectionUtil.mapList(source, Object::toString);

        assertEquals(3, result.size());
        assertEquals("1", result.get(0));
        assertEquals("2", result.get(1));
        assertEquals("3", result.get(2));
    }

    @Test
    public void mapList_withNullSource_shouldReturnEmptyList() {
        List<String> result = CollectionUtil.mapList(null, Object::toString);
        assertTrue(result.isEmpty());
    }

    @Test
    public void mapList_withEmptySource_shouldReturnEmptyList() {
        List<String> result = CollectionUtil.mapList(Collections.emptyList(), Object::toString);
        assertTrue(result.isEmpty());
    }

    // ==================== mapList (FunctionWithParam) ====================

    @Test
    public void mapList_withOneParam_shouldMapElements() {
        List<String> source = Arrays.asList("a", "b", "c");
        List<String> result = CollectionUtil.mapList(source, (s, p) -> s + p, "-suffix");

        assertEquals(3, result.size());
        assertEquals("a-suffix", result.get(0));
        assertEquals("b-suffix", result.get(1));
        assertEquals("c-suffix", result.get(2));
    }

    @Test
    public void mapList_withOneParam_nullSource_shouldReturnEmptyList() {
        List<String> result = CollectionUtil.mapList(null, (s, p) -> "", 0);
        assertTrue(result.isEmpty());
    }

    @Test
    public void mapList_withOneParam_emptySource_shouldReturnEmptyList() {
        List<String> result = CollectionUtil.mapList(Collections.emptyList(), (s, p) -> "", 0);
        assertTrue(result.isEmpty());
    }

    // ==================== mapList (FunctionWith2Param) ====================

    @Test
    public void mapList_withTwoParams_shouldMapElements() {
        List<String> source = Arrays.asList("a", "b");
        List<String> result = CollectionUtil.mapList(source, (s, p1, p2) -> s + p1 + p2, "-", "z");

        assertEquals(2, result.size());
        assertEquals("a-z", result.get(0));
        assertEquals("b-z", result.get(1));
    }

    @Test
    public void mapList_withTwoParams_nullSource_shouldReturnEmptyList() {
        List<String> result = CollectionUtil.mapList(null, (s, p1, p2) -> "", "a", "b");
        assertTrue(result.isEmpty());
    }

    // ==================== mapList (FunctionWith3Param) ====================

    @Test
    public void mapList_withThreeParams_shouldMapElements() {
        List<Integer> source = Arrays.asList(1, 2);
        List<String> result = CollectionUtil.mapList(source,
                (s, p1, p2, p3) -> String.valueOf(s + p1 + p2 + p3), 10, 20, 30);

        assertEquals(2, result.size());
        assertEquals("61", result.get(0));
        assertEquals("62", result.get(1));
    }

    @Test
    public void mapList_withThreeParams_nullSource_shouldReturnEmptyList() {
        List<String> result = CollectionUtil.mapList(null, (s, p1, p2, p3) -> "", 1, 2, 3);
        assertTrue(result.isEmpty());
    }
}
