package com.qcxk.component.netty;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: think
 * Date: 2020/3/3
 */
public class DataModel {

    //ID号
    private byte[] idNumber;
    private String idNumberSTR;
    //后续报文长度
    private byte length;
    private int lengthINT;
    //功能码
    private byte fun;
    private String funSTR;
    //报文体
    private byte[] body;
    private String bodySTR;
    //校验位
    private byte crch;
    private String crchSTR;
    //校验位
    private byte crcl;
    private String crclSTR;
    //全部报文
    private byte[] data;
    private String dataSTR;

    public byte[] exCrcH(){
        String data = "";
        int v = length & 0xFF;
        String hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            data+=(0);
        }
        data+=(hv);

        v = fun & 0xFF;
        hv = Integer.toHexString(v);
        if (hv.length() < 2) {
            data+=(0);
        }
        data+=(hv);

        data+=bytesToHexString(byteMerger(idNumber,body));
        if (data == null || data.equals("")) {
            return null;
        }
        int total = 0;
        int len = data.length();
        int num = 0;
        while (num < len) {
            String s = data.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total/256 % 256;
        String hex = Integer.toHexString(mod);
        len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }

        return hexStringToBytes(hex.toUpperCase());
    }
    //System.arraycopy()方法
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
    //苏标校验码算法
    public static String makeChecksum(byte[] data) {
        if (data == null || data.length==0) {
            return "";
        }
        int total = 0;
        for(byte b:data){
            total+=b;
        }

        /**
         * 用256求余最大是255，即16进制的FF
         */
        int mod = total % 256;
        mod = 0x100-mod;
        String hex = Integer.toHexString(mod);
        int len = hex.length();
        // 如果不够校验位的长度，补0,这里用的是两位校验
        if (len < 2) {
            hex = "0" + hex;
        }

        return hex.toUpperCase();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }
//    public static void main(String[] args) {
//        DataModel dataModel = new DataModel();
//        dataModel.setLength((byte)0x06);
//
//        byte[] idn = new byte[]{(byte)0x02,(byte)0x04,(byte)0x08,(byte)0x0b,};
//        dataModel.setIdNumber(idn);
//
//        dataModel.setFun((byte)0xa2);
//
//        byte[] bodyb = hexStringToBytes("899817F17823");
//        dataModel.setBody(bodyb);
//
//        System.out.println(bytesToHexString(dataModel.exCrcH()));
//        System.out.println(bytesToHexString(dataModel.exCrcl()));
//
//    }
    public static int byteToInt(byte b) {
        int x = b & 0xff;
        return x;
    }
    public byte[] getIdNumber() {
        return idNumber;
    }

    public static Integer byteToInteger(Byte b) {
        return 0xff & b;
    }

    public void setIdNumber(byte[] idNumber) {
        this.idNumber = idNumber;
    }

    public String getIdNumberSTR() {
        return idNumberSTR;
    }

    public void setIdNumberSTR(String idNumberSTR) {
        this.idNumberSTR = idNumberSTR;
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    public int getLengthINT() {
        return lengthINT;
    }

    public void setLengthINT(int lengthINT) {
        this.lengthINT = lengthINT;
    }

    public byte getFun() {
        return fun;
    }

    public void setFun(byte fun) {
        this.fun = fun;
    }

    public String getFunSTR() {
        return funSTR;
    }

    public void setFunSTR(String funSTR) {
        this.funSTR = funSTR;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getBodySTR() {
        return bodySTR;
    }

    public void setBodySTR(String bodySTR) {
        this.bodySTR = bodySTR;
    }

    public byte getCrch() {
        return crch;
    }

    public void setCrch(byte crch) {
        this.crch = crch;
    }

    public String getCrchSTR() {
        return crchSTR;
    }

    public void setCrchSTR(String crchSTR) {
        this.crchSTR = crchSTR;
    }

    public byte getCrcl() {
        return crcl;
    }

    public void setCrcl(byte crcl) {
        this.crcl = crcl;
    }

    public String getCrclSTR() {
        return crclSTR;
    }

    public void setCrclSTR(String crclSTR) {
        this.crclSTR = crclSTR;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataSTR() {
        return dataSTR;
    }

    public void setDataSTR(String dataSTR) {
        this.dataSTR = dataSTR;
    }
}
