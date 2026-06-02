package io.soil.util;

import io.soil.util.ip.IpUtil;
import org.junit.Test;

public class IpUtilTest {

    @Test
    public void getLocalConnectedIp() {
        String localConnectedIp = IpUtil.getLocalConnectedIp("192.168.1.20", 8800);
        localConnectedIp = localConnectedIp;
    }
}
