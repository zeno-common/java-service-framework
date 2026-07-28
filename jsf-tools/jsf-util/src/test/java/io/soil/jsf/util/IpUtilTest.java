package io.soil.jsf.util;

import io.soil.jsf.util.ip.IpUtil;
import org.junit.jupiter.api.Test;

public class IpUtilTest {

    @Test
    public void getLocalConnectedIp() {
        String localConnectedIp = IpUtil.getLocalConnectedIp("192.168.1.20", 8800);
        localConnectedIp = localConnectedIp;
    }
}
