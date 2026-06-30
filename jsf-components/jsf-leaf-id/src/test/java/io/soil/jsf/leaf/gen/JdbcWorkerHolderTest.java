package io.soil.jsf.leaf.gen;

import io.soil.jsf.test.YamlPropertySourceFactory;
import io.soil.jsf.leaf.LeafAutoConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = {LeafAutoConfig.class})
@PropertySource(value = "classpath:application.yml",factory = YamlPropertySourceFactory.class)
public class JdbcWorkerHolderTest {

    @Autowired
    private ILeafGenerator workerHolder;

    @Autowired
    private LeafId leafId;

    @Test
    public void testLeafWorkerHolder() {
        workerHolder.workerId();
    }

    @Test
    public void testGaeaLeafId() {
        Long id = leafId.nextId();
        id = id;
    }
}
