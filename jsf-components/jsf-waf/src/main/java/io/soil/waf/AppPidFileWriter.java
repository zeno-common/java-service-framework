package io.soil.waf;

import org.springframework.boot.context.ApplicationPidFileWriter;

/**
 * 应用 PID 文件写入器，将应用进程 ID 写入 ./app.pid 文件。
 * <p>
 * 继承自 Spring Boot 的 {@link ApplicationPidFileWriter}，默认 PID 文件路径为当前目录下的 app.pid。
 * </p>
 *
 * @author zeno.w
 * @date 2022/5/25
 */
public class AppPidFileWriter extends ApplicationPidFileWriter {

    /**
     * 构造 AppPidFileWriter，PID 文件路径为 ./app.pid
     */
    public AppPidFileWriter(){
        super("./app.pid");
    }
}
