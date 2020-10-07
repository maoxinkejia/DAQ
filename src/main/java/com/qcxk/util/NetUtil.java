package com.qcxk.util;

import org.apache.commons.lang3.StringUtils;

import java.net.*;
import java.util.*;

public class NetUtil {

    public static String NETWORK_INTERFACE_NAME;

    static {
        NETWORK_INTERFACE_NAME = System.getProperty("network.interface.name");
    }

    public static InetAddress getLocalHost() throws UnknownHostException {
        return InetAddress.getLocalHost();
    }

    public static String getLocalIP() throws Exception {
        return getLocalHost().getHostAddress();
    }

    public static String getHostname() {
        try {
            return getLocalHost().getHostName();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getSiteIP() throws Exception {
        return getSiteLocalAddress().getHostAddress();
    }

    public static InetAddress getSiteLocalAddress() throws Exception {
        InetAddress localHost = InetAddress.getLocalHost();
        if (localHost.isSiteLocalAddress()) {
            return localHost;
        }
        Map<String, List<InetAddress>> siteLocalAddresses = getSiteLocalAddresses();
        if (siteLocalAddresses.size() <= 0) {
            return localHost;
        }
        if (siteLocalAddresses.size() == 1 && siteLocalAddresses.get(0).size() == 1) {
            return siteLocalAddresses.get(0).get(0);
        }
        List<InetAddress> inetAddresses = new ArrayList<InetAddress>();
        if (StringUtils.isNotBlank(NETWORK_INTERFACE_NAME)) {
            inetAddresses = siteLocalAddresses.get(NETWORK_INTERFACE_NAME);
        } else {
            for (Map.Entry<String, List<InetAddress>> entry : siteLocalAddresses.entrySet()) {
                inetAddresses.addAll(entry.getValue());
            }
        }
        InetAddress result = inetAddresses.get(0);
        String hostname = getHostname();
        if (StringUtils.isNotBlank(hostname)) {
            for (InetAddress inetAddress : inetAddresses) {
                if (inetAddress.getHostName().startsWith(hostname)) {
                    result = inetAddress;
                    break;
                }
            }
        }
        return result;
    }

    public static Map<String, List<InetAddress>> getSiteLocalAddresses() throws Exception {
        Map<String, List<InetAddress>> result = new HashMap<>();
        Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
        while (netInterfaces.hasMoreElements()) {
            NetworkInterface ni = netInterfaces.nextElement();
            // site-local类型的地址
            List<InetAddress> inetAddresses = new ArrayList<>();
            // site-local类型的地址未被发现，先记录候选地址
            List<InetAddress> candidateAddress = new ArrayList<>();
            result.put(ni.getName(), inetAddresses);
            Enumeration<InetAddress> ias = ni.getInetAddresses();
            while (ias.hasMoreElements()) {
                InetAddress inetAddress = ias.nextElement();
                if (inetAddress.isSiteLocalAddress()) {
                    inetAddresses.add(inetAddress);
                } else {
                    //site-local类型的地址未被发现，加入到候选地址
                    candidateAddress.add(inetAddress);
                }
            }
            //site-local类型的地址未被发现，则使用候选地址
            if (inetAddresses.size() == 0 && candidateAddress.size() > 0) {
                result.put(ni.getName(), candidateAddress);
            }
        }
        return result;
    }

    /**
     * 获取地址字符串
     *
     * @param address 地址
     * @return 地址字符串
     */
    public static String getAddress(SocketAddress address) {
        if (address == null) {
            return null;
        }
        if (address instanceof InetSocketAddress) {
            InetSocketAddress isa = (InetSocketAddress) address;
            return isa.getAddress().getHostAddress() + ":" + isa.getPort();
        } else {
            return address.toString();
        }
    }


    /**
     * 地址转化成字节数组
     *
     * @param socketAddress 地址对象
     * @return 字节数组
     */
    public static byte[] toByte(InetSocketAddress socketAddress) {
        if (socketAddress == null) {
            throw new IllegalArgumentException("socketAddress is null");
        }
        InetAddress inetAddress = socketAddress.getAddress();
        if (inetAddress == null) {
            throw new IllegalArgumentException("socketAddress is invalid");
        }
        byte[] address = inetAddress.getAddress();
        byte[] result = new byte[address.length + 2];
        System.arraycopy(address, 0, result, 2, address.length);
        int port = socketAddress.getPort();
        result[1] = (byte) (port >> 8 & 0xFF);
        result[0] = (byte) (port & 0xFF);
        return result;
    }


    /**
     * 把10进制地址字符串转化成字节数组
     *
     * @param address 地址
     * @return 字节数组
     */
    public static byte[] toByteByAddress(String address) {
        if (address == null) {
            throw new IllegalArgumentException("address is invalid");
        }
        String[] ips = address.split("[.:_]");
        if (ips.length < 4) {
            throw new IllegalArgumentException("broker is invalid");
        }
        int pos = 0;
        byte[] buf;
        if (ips.length > 4) {
            buf = new byte[6];
            int port = Integer.parseInt(ips[4]);
            buf[pos++] = (byte) (port & 0xFF);
            buf[pos++] = (byte) (port >> 8 & 0xFF);
        } else {
            buf = new byte[4];
        }
        buf[pos++] = (byte) Integer.parseInt(ips[0]);
        buf[pos++] = (byte) Integer.parseInt(ips[1]);
        buf[pos++] = (byte) Integer.parseInt(ips[2]);
        buf[pos++] = (byte) Integer.parseInt(ips[3]);
        return buf;
    }

}
