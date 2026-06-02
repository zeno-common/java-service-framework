package io.soil.waf;

import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * @author wangzezhou
 * @date 2022/5/25
 */
public class AppPidFileWriter extends ApplicationPidFileWriter {

    public AppPidFileWriter(){
        super("./app.pid");
    }
}
