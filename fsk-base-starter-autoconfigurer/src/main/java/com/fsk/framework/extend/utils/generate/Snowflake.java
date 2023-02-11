package com.fsk.framework.extend.utils.generate;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// ref: https://github.com/sony/sonyflake/blob/master/sonyflake.go
// impl: snowflake id

/**
 * The type Snowflake.
 *
 * @author elvizlai
 */
@Deprecated
public class Snowflake {
    private static final long BIT_LEN_TIME = 39L;
    private static final long BIT_LEN_SEQUENCE = 8L;
    private static final long MASK_SEQUENCE = (1 << BIT_LEN_SEQUENCE) - 1L;
    private static final long BIT_LEN_MACHINE_ID = 63 - BIT_LEN_TIME - BIT_LEN_SEQUENCE;
    private static final long FLAKE_TIME_UNIT = 10L;

    private static final long MACHINE_ID = getLower16BitPrivateIP();
    private static final long START_TIME = genStartTime();

    private static long elapsedTime = 0L;
    private static long sequence = (1 << BIT_LEN_SEQUENCE) - 1L;

    private Snowflake() {
    }

    /**
     * Next id generate long format id using snowflake.
     *
     * Return the long
     */
    public static synchronized String nextId() {
        long current = currentElapsedTime();

        if (elapsedTime < current) {
            elapsedTime = current;
            sequence = 0;
        } else {
            sequence = (sequence + 1) & MASK_SEQUENCE;
            // overhead, sleep to slow down.
            if (sequence == 0) {
                elapsedTime++;
                long overtime = elapsedTime - current;
                try {
                    TimeUnit.MILLISECONDS.sleep(sleepTime(overtime));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        long longId = elapsedTime << (BIT_LEN_SEQUENCE + BIT_LEN_MACHINE_ID) | sequence << BIT_LEN_MACHINE_ID | MACHINE_ID;
        return String.valueOf(longId);
    }

    private static long genStartTime() {
        Calendar startTime = Calendar.getInstance();
        startTime.set(2017, Calendar.JULY, 10, 0, 0, 0);
        return toFlakeTime(startTime.getTime());
    }

    private static long toFlakeTime(Date t) {
        return t.getTime() / FLAKE_TIME_UNIT;
    }

    private static long currentElapsedTime() {
        return System.currentTimeMillis() / FLAKE_TIME_UNIT - START_TIME;
    }

    private static long sleepTime(long overtime) {
        return overtime * FLAKE_TIME_UNIT - System.currentTimeMillis() / FLAKE_TIME_UNIT * FLAKE_TIME_UNIT;
    }

    private static long getLower16BitPrivateIP() {
        byte[] ip = getPrivateIPv4();
        return (Byte.toUnsignedLong(ip[2]) << 8) + Byte.toUnsignedLong(ip[3]);
    }

    private static byte[] getPrivateIPv4() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address && !ip.isLoopbackAddress() && !ip.getHostAddress().contains(":") && isPrivateIPv4(ip.getAddress())) {
                        return ip.getAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // using rand to fulfill
        byte[] ip = new byte[4];
        Random r = new Random();
        ip[2] = (byte) r.nextInt(256);
        ip[3] = (byte) (1 + r.nextInt(255));
        return ip;
    }

    private static boolean isPrivateIPv4(byte[] ip) {
        if (ip.length != 4) {
            return false;
        }

        int ip0 = Byte.toUnsignedInt(ip[0]), ip1 = Byte.toUnsignedInt(ip[1]);

        return ip0 == 10 || (ip0 == 172 && (ip1 >= 16 && ip1 < 32)) || (ip0 == 192 && ip1 == 168);
    }
}
