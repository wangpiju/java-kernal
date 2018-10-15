package com.hs3.utils.ip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

public class IPSeeker {
    private class IPLocation {
        public String country;
        public String area;

        public IPLocation() {
            this.country = (this.area = "");
        }

        public IPLocation getCopy() {
            //jd-gui
            //IPLocation ret = new IPLocation(IPSeeker.this);
            IPLocation ret = new IPLocation();
            ret.country = this.country;
            ret.area = this.area;
            return ret;
        }
    }

    private static String IP_FILE = null;
    private static Object lockObj = new Object();
    private static final int IP_RECORD_LENGTH = 7;
    private static final byte AREA_FOLLOWED = 1;
    private static final byte NO_AREA = 2;
    private final ConcurrentHashMap<String, IPLocation> ipCache;
    private RandomAccessFile ipFile;
    private static IPSeeker instance = new IPSeeker();
    private long ipBegin;
    private long ipEnd;
    private final IPLocation loc;
    private final byte[] b4;
    private final byte[] b3;

    private IPSeeker() {
        this.ipCache = new ConcurrentHashMap();
        if (IP_FILE == null) {
            IP_FILE = IPSeeker.class.getClassLoader().getResource("").getPath() + "qqwry.dat";
        }
        this.loc = new IPLocation();

        this.b4 = new byte[4];
        this.b3 = new byte[3];
        try {
            this.ipFile = new RandomAccessFile(IP_FILE, "r");
        } catch (FileNotFoundException e) {
            System.out.println(IP_FILE);
            System.out.println("IP地址信息文件没有找到，IP显示功能将无法使用");
            this.ipFile = null;
        }
        if (this.ipFile != null) {
            try {
                this.ipBegin = readLong4(0L);
                this.ipEnd = readLong4(4L);
                if ((this.ipBegin == -1L) || (this.ipEnd == -1L)) {
                    this.ipFile.close();
                    this.ipFile = null;
                }
            } catch (IOException e) {
                System.out.println("IP地址信息文件格式有错误，IP显示功能将无法使用");
                this.ipFile = null;
            }
        }
    }

    public static IPSeeker getInstance() {
        return instance;
    }

    public String getCountry(String ip) {
        if (this.ipFile == null) {
            return "错误的IP数据库文件";
        }
        IPLocation loc = getIPLocation(ip);
        return loc.country;
    }

    public String getArea(String ip) {
        if (this.ipFile == null) {
            return "错误的IP数据库文件";
        }
        IPLocation loc = getIPLocation(ip);
        return loc.area;
    }

    public String getAddress(String ip) {
        String country = getCountry(ip).trim().equals("CZ88.NET") ? "" : getCountry(ip);
        String area = getArea(ip).trim().equals("CZ88.NET") ? "" : getArea(ip);
        String address = country + " " + area;
        return address.trim();
    }

    private IPLocation getIPLocation(String ip) {
        IPLocation info = null;
        if (this.ipCache.containsKey(ip)) {
            IPLocation loc = (IPLocation) this.ipCache.get(ip);
            info = loc.getCopy();
        } else {
            byte[] ipArray = getIpByteArrayFromString(ip);
            synchronized (lockObj) {
                long offset = locateIP(ipArray);
                if (offset != -1L) {
                    info = getIPLocation(offset);
                }
                if (info == null) {
                    info = new IPLocation();
                    info.country = "未知国家";
                    info.area = "未知地区";
                }
            }
        }
        return info;
    }

    private IPLocation getIPLocation(long offset) {
        try {
            this.ipFile.seek(offset + 4L);

            byte b = this.ipFile.readByte();
            if (b == 1) {
                long countryOffset = readLong3();

                this.ipFile.seek(countryOffset);

                b = this.ipFile.readByte();
                if (b == 2) {
                    this.loc.country = readString(readLong3());
                    this.ipFile.seek(countryOffset + 4L);
                } else {
                    this.loc.country = readString(countryOffset);
                }
                this.loc.area = readArea(this.ipFile.getFilePointer());
            } else if (b == 2) {
                this.loc.country = readString(readLong3());
                this.loc.area = readArea(offset + 8L);
            } else {
                this.loc.country = readString(this.ipFile.getFilePointer() - 1L);
                this.loc.area = readArea(this.ipFile.getFilePointer());
            }
            return this.loc;
        } catch (IOException e) {
        }
        return null;
    }

    private byte[] getIpByteArrayFromString(String ip) {
        byte[] ret = new byte[4];
        StringTokenizer st = new StringTokenizer(ip, ".");
        try {
            ret[0] = ((byte) (Integer.parseInt(st.nextToken()) & 0xFF));
            ret[1] = ((byte) (Integer.parseInt(st.nextToken()) & 0xFF));
            ret[2] = ((byte) (Integer.parseInt(st.nextToken()) & 0xFF));
            ret[3] = ((byte) (Integer.parseInt(st.nextToken()) & 0xFF));
        } catch (Exception localException) {
        }
        return ret;
    }

    private String getString(byte[] b, int offset, int len, String encoding) {
        try {
            return new String(b, offset, len, encoding);
        } catch (UnsupportedEncodingException e) {
        }
        return new String(b, offset, len);
    }

    private long readLong4(long offset) {
        long ret = 0L;
        try {
            this.ipFile.seek(offset);
            ret |= this.ipFile.readByte() & 0xFF;
            ret |= this.ipFile.readByte() << 8 & 0xFF00;
            ret |= this.ipFile.readByte() << 16 & 0xFF0000;
            return ret | this.ipFile.readByte() << 24 & 0xFF000000;
        } catch (IOException e) {
        }
        return -1L;
    }

    private long readLong3(long offset) {
        long ret = 0L;
        try {
            this.ipFile.seek(offset);
            this.ipFile.readFully(this.b3);
            ret |= this.b3[0] & 0xFF;
            ret |= this.b3[1] << 8 & 0xFF00;
            return ret | this.b3[2] << 16 & 0xFF0000;
        } catch (IOException e) {
        }
        return -1L;
    }

    private long readLong3() {
        long ret = 0L;
        try {
            this.ipFile.readFully(this.b3);
            ret |= this.b3[0] & 0xFF;
            ret |= this.b3[1] << 8 & 0xFF00;
            return ret | this.b3[2] << 16 & 0xFF0000;
        } catch (IOException e) {
        }
        return -1L;
    }

    private void readIP(long offset, byte[] ip) {
        try {
            this.ipFile.seek(offset);
            this.ipFile.readFully(ip);
            byte temp = ip[0];
            ip[0] = ip[3];
            ip[3] = temp;
            temp = ip[1];
            ip[1] = ip[2];
            ip[2] = temp;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private int compareIP(byte[] ip, byte[] beginIp) {
        for (int i = 0; i < 4; i++) {
            int r = compareByte(ip[i], beginIp[i]);
            if (r != 0) {
                return r;
            }
        }
        return 0;
    }

    private int compareByte(byte b1, byte b2) {
        if ((b1 & 0xFF) > (b2 & 0xFF)) {
            return 1;
        }
        if ((b1 ^ b2) == 0) {
            return 0;
        }
        return -1;
    }

    private long locateIP(byte[] ip) {
        long m = 0L;


        readIP(this.ipBegin, this.b4);
        int r = compareIP(ip, this.b4);
        if (r == 0) {
            return this.ipBegin;
        }
        if (r < 0) {
            return -1L;
        }
        long i = this.ipBegin;
        for (long j = this.ipEnd; i < j; ) {
            m = getMiddleOffset(i, j);
            readIP(m, this.b4);
            r = compareIP(ip, this.b4);
            if (r > 0) {
                i = m;
            } else if (r < 0) {
                if (m == j) {
                    j -= 7L;
                    m = j;
                } else {
                    j = m;
                }
            } else {
                return readLong3(m + 4L);
            }
        }
        m = readLong3(m + 4L);
        readIP(m, this.b4);
        r = compareIP(ip, this.b4);
        if (r <= 0) {
            return m;
        }
        return -1L;
    }

    private long getMiddleOffset(long begin, long end) {
        long records = (end - begin) / 7L;
        records >>= 1;
        if (records == 0L) {
            records = 1L;
        }
        return begin + records * 7L;
    }

    private String readArea(long offset)
            throws IOException {
        this.ipFile.seek(offset);
        byte b = this.ipFile.readByte();
        if ((b == 1) || (b == 2)) {
            long areaOffset = readLong3(offset + 1L);
            if (areaOffset == 0L) {
                return "未知地区";
            }
            return readString(areaOffset);
        }
        return readString(offset);
    }

    private String readString(long offset) {
        try {
            this.ipFile.seek(offset);


            int i = 0;
            byte[] buf = new byte[256];
            while ((buf[i] = this.ipFile.readByte()) != 0) {
                i++;
                if (i >= buf.length) {
                    byte[] tmp = new byte[i + 100];
                    System.arraycopy(buf, 0, tmp, 0, i);
                    buf = tmp;
                }
            }
            if (i != 0) {
                return getString(buf, 0, i, "GBK");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}
