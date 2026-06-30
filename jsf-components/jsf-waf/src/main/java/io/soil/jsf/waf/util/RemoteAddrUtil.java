package io.soil.jsf.waf.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 远程地址工具类，用于获取客户端真实 IP 地址。
 * <p>
 * 处理代理、负载均衡等场景下的 IP 获取，支持 X-Real-IP 和 X-Forwarded-For 头。
 * </p>
 *
 * @author zeno
 */
public class RemoteAddrUtil {

    /**
     * 获取客户端真实IP地址
     * 处理代理、负载均衡等场景下的IP获取
     */
    public static String getRealClientIp() {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        // 常见的代理服务器传递真实IP的HTTP头
        String[] headers = {
                "X-Real-IP",          // Nginx等代理常用
                "X-Forwarded-For",    // 最常用，多个代理时会包含多个IP，逗号分隔
        };

        // 依次检查各代理头
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (isValidIp(ip)) {
                // X-Forwarded-For可能包含多个 IP（客户端IP, 代理1IP, 代理2IP...）
                if (ip.contains(",")) {
                    // 取第一个IP作为真实客户端IP
                    ip = ip.split(",")[0].trim();
                }
                return ip;
            }
        }

        // 所有代理头都无法获取时，使用getRemoteAddr()兜底
        return request.getRemoteAddr();
    }

    /**
     * 验证IP是否有效（非null、非空、非unknown）
     */
    private static boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }
}
