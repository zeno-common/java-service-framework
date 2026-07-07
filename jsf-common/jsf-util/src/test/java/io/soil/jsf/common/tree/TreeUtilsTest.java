package io.soil.jsf.common.tree;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link TreeUtils} 单元测试
 */
public class TreeUtilsTest {

    private Node<Long, String> buildNode(Long id, String name, Long parentId) {
        Node<Long, String> node = new Node<>();
        node.setId(id);
        node.setName(name);
        node.setParentId(parentId);
        return node;
    }

    // ==================== toTree ====================

    @Test
    public void toTree_shouldConvertFlatListToTree() {
        Node<Long, String> root = buildNode(1L, "root", null);
        Node<Long, String> child1 = buildNode(2L, "child1", 1L);
        Node<Long, String> child2 = buildNode(3L, "child2", 1L);
        Node<Long, String> grandChild = buildNode(4L, "grandchild", 2L);

        List<Node<Long, String>> nodes = Arrays.asList(root, child1, child2, grandChild);
        List<Node<Long, String>> tree = TreeUtils.toTree(nodes);

        assertEquals(1, tree.size());
        Node<Long, String> rootNode = tree.get(0);
        assertEquals("root", rootNode.getName());
        assertEquals(2, rootNode.getChildren().size());

        // child1 有子节点 grandchild
        Iterator<Node<Long, String>> it = rootNode.getChildren().iterator();
        Node<Long, String> firstChild = it.next();
        if ("child1".equals(firstChild.getName())) {
            assertEquals(1, firstChild.getChildren().size());
            assertEquals("grandchild", firstChild.getChildren().iterator().next().getName());
        }
    }

    @Test
    public void toTree_withMultipleRoots_shouldReturnAllRoots() {
        Node<Long, String> root1 = buildNode(1L, "root1", null);
        Node<Long, String> root2 = buildNode(2L, "root2", null);

        List<Node<Long, String>> nodes = Arrays.asList(root1, root2);
        List<Node<Long, String>> tree = TreeUtils.toTree(nodes);

        assertEquals(2, tree.size());
    }

    @Test
    public void toTree_withEmptyList_shouldReturnEmptyTree() {
        List<Node<Long, String>> nodes = Collections.emptyList();
        List<Node<Long, String>> tree = TreeUtils.toTree(nodes);

        assertTrue(tree.isEmpty());
    }

    @Test
    public void toTree_withSingleRootNoChildren_shouldReturnSingleNode() {
        Node<Long, String> root = buildNode(1L, "root", null);

        List<Node<Long, String>> nodes = Collections.singletonList(root);
        List<Node<Long, String>> tree = TreeUtils.toTree(nodes);

        assertEquals(1, tree.size());
        assertEquals("root", tree.get(0).getName());
    }

    // ==================== toList ====================

    @Test
    public void toList_shouldConvertTreeToList() {
        Node<Long, String> root = buildNode(1L, "root", null);
        Node<Long, String> child = buildNode(2L, "child", 1L);
        root.addChild(child);

        List<Node<Long, String>> treeNodes = new ArrayList<>();
        treeNodes.add(root);

        List<Node<Long, String>> list = TreeUtils.toList(treeNodes);

        assertEquals(2, list.size());
    }

    @Test
    public void toList_withEmptyCollection_shouldReturnEmptyList() {
        List<Node<Long, String>> list = TreeUtils.toList(Collections.emptyList());
        assertTrue(list.isEmpty());
    }

    // ==================== toMap ====================

    @Test
    public void toMap_shouldConvertListToMap() {
        Node<Long, String> node1 = buildNode(1L, "node1", null);
        Node<Long, String> node2 = buildNode(2L, "node2", null);

        List<Node<Long, String>> nodes = Arrays.asList(node1, node2);
        Map<Long, Node<Long, String>> map = TreeUtils.toMap(nodes);

        assertEquals(2, map.size());
        assertEquals("node1", map.get(1L).getName());
        assertEquals("node2", map.get(2L).getName());
    }

    @Test
    public void toMap_withTreeNodes_shouldIncludeChildren() {
        Node<Long, String> root = buildNode(1L, "root", null);
        Node<Long, String> child = buildNode(2L, "child", 1L);
        root.addChild(child);

        List<Node<Long, String>> treeNodes = new ArrayList<>();
        treeNodes.add(root);

        Map<Long, Node<Long, String>> map = TreeUtils.toMap(treeNodes);

        assertEquals(2, map.size());
        assertNotNull(map.get(1L));
        assertNotNull(map.get(2L));
    }

    @Test
    public void toMap_withEmptyCollection_shouldReturnEmptyMap() {
        Map<Long, Node<Long, String>> map = TreeUtils.toMap(Collections.emptyList());
        assertTrue(map.isEmpty());
    }
}
