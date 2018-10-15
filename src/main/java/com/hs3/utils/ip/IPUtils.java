package com.hs3.utils.ip;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPUtils {
    public static String getServerIP() {
        return serverIp;
    }

    //jd-gui
    //private static final String serverIp = ;
    private static final String serverIp = getIP();

    private static String getIP() {
        String localip = null;
        String netip = null;
        try {
            Enumeration<NetworkInterface> netInterfaces =
                    NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;
            do {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = (InetAddress) address.nextElement();
                    if ((!ip.isLoopbackAddress()) && (!ip.getHostAddress().contains(":"))) {
                        if (!ip.isSiteLocalAddress()) {
                            netip = ip.getHostAddress();
                            finded = true;
                            break;
                        }
                        if (localip == null) {
                            localip = ip.getHostAddress();
                        }
                    }
                }
                if (!netInterfaces.hasMoreElements()) {
                    break;
                }
            } while (!finded);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if ((netip != null) && (!"".equals(netip))) {
            return netip;
        }
        return localip;
    }
}
