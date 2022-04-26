package utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IP {
    public static void getLocalhostStr() {
        final Enumeration<NetworkInterface> nis;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                final NetworkInterface ni = nis.nextElement();
                final Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    InetAddress ia = ias.nextElement();
                    if (ia instanceof Inet4Address) {
                        System.out.println(ia);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

}
