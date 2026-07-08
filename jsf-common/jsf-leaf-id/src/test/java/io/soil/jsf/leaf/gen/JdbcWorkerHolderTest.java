package io.soil.jsf.leaf.gen;

import io.soil.jsf.leaf.LeafAutoConfig;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {LeafAutoConfig.class})
@EnableAutoConfiguration
@MapperScan("io.soil.jsf.leaf.mapper")
class JdbcWorkerHolderTest {

    @Autowired
    private ILeafGenerator workerHolder;

    @Autowired
    private LeafId leafId;

    @Test
    void testLeafWorkerHolder() {
        workerHolder.workerId();
    }

    @Test
    void testLeafId() {
        Long id = leafId.nextId();
        assertNotNull(id);
    }
}
