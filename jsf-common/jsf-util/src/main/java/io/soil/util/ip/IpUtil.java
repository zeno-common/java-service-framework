package io.soil.util.ip;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 本地连接IP工具类，用于获取与指定远程IP端口连接的本地IP地址
 */
public class IpUtil {
    /**
     * 获取本地与指定远程IP端口连接时使用的IP地址
     * @param remoteIp 远程IP地址（如："192.168.1.100"）
     * @param remotePort 远程端口（如：8080）
     * @return 本地连接IP地址（字符串形式），连接失败时返回null
     * @throws IllegalArgumentException 当远程IP格式无效或端口超出范围时抛出
     */
    public static String getLocalConnectedIp(String remoteIp, Integer remotePort) {
        validateInput(remoteIp, remotePort);
        try (Socket socket = new Socket()) {
            // 连接到远程地址（超时设置为3秒）
            socket.connect(new java.net.InetSocketAddress(InetAddress.getByName(remoteIp), remotePort), 3000);
            // 获取本地绑定的IP地址
            return socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            System.err.println("连接远程地址失败: " + e.getMessage());
            return "localhost";
        }
    }

    /**
     * 验证输入参数有效性
     */
    private static void validateInput(String remoteIp, int remotePort) {
        // 验证IP格式
        try {
            InetAddress.getByName(remoteIp);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("无效的远程IP地址: " + remoteIp);
        }
        // 验证端口范围
        if (remotePort < 0 || remotePort > 65535) {
            throw new IllegalArgumentException("端口号超出范围（0-65535）: " + remotePort);
        }
    }
}
