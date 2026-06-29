package io.soil.pojo.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * {@link PagedResult} 单元测试
 */
public class PagedResultTest {

    @Test
    public void empty_shouldReturnZeroTotalAndEmptyItems() {
        PagedResult<String> result = PagedResult.empty();

        assertEquals(0, result.getTotal());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void emptyWithTotal_shouldReturnSpecifiedTotalAndEmptyItems() {
        PagedResult<String> result = PagedResult.empty(100L);

        assertEquals(100, result.getTotal());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void ofLongTotal_shouldReturnCorrectTotalAndItems() {
        Collection<String> items = Arrays.asList("a", "b", "c");
        PagedResult<String> result = PagedResult.of(50L, items);

        assertEquals(50, result.getTotal());
        assertEquals(3, result.getItems().size());
        assertTrue(result.getItems().contains("a"));
        assertTrue(result.getItems().contains("b"));
        assertTrue(result.getItems().contains("c"));
    }

    @Test
    public void ofIntTotal_shouldReturnCorrectTotalAndItems() {
        Collection<Integer> items = Arrays.asList(1, 2, 3, 4);
        PagedResult<Integer> result = PagedResult.of(4, items);

        assertEquals(4, result.getTotal());
        assertEquals(4, result.getItems().size());
    }

    @Test
    public void of_withNullItems_shouldReturnEmptyList() {
        PagedResult<String> result = PagedResult.of(10, null);

        assertEquals(10, result.getTotal());
        assertNotNull(result.getItems());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void of_withEmptyItems_shouldReturnEmptyList() {
        PagedResult<String> result = PagedResult.of(0, Collections.emptyList());

        assertEquals(0, result.getTotal());
        assertTrue(result.getItems().isEmpty());
    }

    @Test
    public void of_withLargeTotal_shouldTruncateToInt() {
        Collection<String> items = Arrays.asList("x");
        PagedResult<String> result = PagedResult.of((long) Integer.MAX_VALUE + 1, items);

        // long 截断为 int
        assertEquals(Integer.MIN_VALUE, result.getTotal());
    }

    @Test
    public void setters_shouldWorkCorrectly() {
        PagedResult<String> result = PagedResult.empty();
        Collection<String> newItems = new ArrayList<>(Arrays.asList("a", "b"));

        result.setTotal(99);
        result.setItems(newItems);

        assertEquals(99, result.getTotal());
        assertEquals(newItems, result.getItems());
    }
}
