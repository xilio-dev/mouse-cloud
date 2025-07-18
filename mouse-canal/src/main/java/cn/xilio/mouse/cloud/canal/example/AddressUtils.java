package cn.xilio.mouse.cloud.canal.example;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class AddressUtils {

    private static final Logger  logger       = LoggerFactory.getLogger(AddressUtils.class);
    private static final String  LOCALHOST_IP = "127.0.0.1";
    private static final String  EMPTY_IP     = "0.0.0.0";
    private static final Pattern IP_PATTERN   = Pattern.compile("[0-9]{1,3}(\\.[0-9]{1,3}){3,}");

    public static boolean isAvailablePort(int port) {
        try (ServerSocket ss = new ServerSocket(port)) {
            ss.bind(null);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isValidHostAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) return false;
        String name = address.getHostAddress();
        return (name != null && !EMPTY_IP.equals(name) && !LOCALHOST_IP.equals(name) && IP_PATTERN.matcher(name)
            .matches());
    }

    public static String getHostIp() {
        InetAddress address = getHostAddress();
        return address == null ? null : address.getHostAddress();
    }

    public static String getHostName() {
        InetAddress address = getHostAddress();
        return address == null ? null : address.getHostName();
    }

    public static InetAddress getHostAddress() {
        InetAddress localAddress = null;
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidHostAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    logger.warn("Failed to retriving network card ip address. cause:" + e.getMessage());
                                }
                            }
                        }
                    } catch (Throwable e) {
                        logger.warn("Failed to retriving network card ip address. cause:" + e.getMessage());
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn("Failed to retriving network card ip address. cause:" + e.getMessage());
        }
        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    /**
     * 拆分IP地址和端口号
     *
     * @param text ip地址和端口号，ip和端口号以英文冒号(:)分隔;
     *
     *     <pre>
     *             ipv4 127.0.0.1:3306
     *             ipv6 [::1]:3306
     *     </pre>
     *
     * @return
     */
    public static String[] splitIPAndPort(String text) {
        text = text.replace("[", "").replace("]", "");
        int idx = text.lastIndexOf(':');
        if (idx > 0) {
            String ip = text.substring(0, idx);
            String port = text.substring(idx + 1);
            return new String[] { ip, port };
        }
        return ArrayUtils.EMPTY_STRING_ARRAY;
    }
}
