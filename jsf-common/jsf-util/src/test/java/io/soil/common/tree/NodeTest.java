package io.soil.common.tree;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * {@link Node} 单元测试
 */
public class NodeTest {

    @Test
    public void defaultConstructor_shouldInitializeEmptyChildren() {
        Node<String, String> node = new Node<>();

        assertNotNull(node.getChildren());
        assertTrue(node.getChildren().isEmpty());
    }

    @Test
    public void addChild_firstChild_shouldInitializeChildrenList() {
        Node<String, String> parent = new Node<>();
        Node<String, String> child = new Node<>();

        parent.addChild(child);

        assertEquals(1, parent.getChildren().size());
        assertSame(child, parent.getChildren().iterator().next());
    }

    @Test
    public void addChild_multipleChildren_shouldAddAll() {
        Node<String, String> parent = new Node<>();
        Node<String, String> child1 = new Node<>();
        Node<String, String> child2 = new Node<>();

        parent.addChild(child1);
        parent.addChild(child2);

        assertEquals(2, parent.getChildren().size());
    }

    @Test
    public void setters_shouldSetFieldsCorrectly() {
        Node<Long, String> node = new Node<>();

        node.setId(1L);
        node.setName("root");
        node.setType("folder");
        node.setParentId(null);
        node.setOrdinal(0);
        node.setAddition("extra");

        assertEquals(Long.valueOf(1L), node.getId());
        assertEquals("root", node.getName());
        assertEquals("folder", node.getType());
        assertNull(node.getParentId());
        assertEquals(0, node.getOrdinal());
        assertEquals("extra", node.getAddition());
    }
}
