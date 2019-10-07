package com.policeequipment.android.cabinetmanagement.util;

import android.util.Log;

import com.deemons.serialportlib.ByteUtils;


public class StringUtil {

    public static String getBoxStatusValue(byte[] integer) {

        StringBuffer stringBuffer = new StringBuffer();
        //1-4  11 1 11 11 1
        byte byte6 = integer[6];
        String Box_status1_4 = Long.toString(byte6 & 0xff, 2);
        char[] chars1_4 = Box_status1_4.toCharArray();
        String reverse1_4 = "";
        for (int i = chars1_4.length - 1; i >= 0; i--) {
            reverse1_4 += chars1_4[i];
        }

        Log.e("tag", "getBoxStatusValue:6位 "+reverse1_4 );
        stringBuffer.append(OddNumberSystem(reverse1_4));

        //5-8
        byte byte7 = integer[7];
        String Box_status5_8 = Long.toString(byte7 & 0xff, 2);
        Log.e("tag", "getBoxStatusValue:58 "+Box_status5_8 );
        char[] chars5_8 = Box_status5_8.toCharArray();
        String reverse5_8 = "";
        for (int i = chars5_8.length - 1; i >= 0; i--) {
            reverse5_8 += chars5_8[i];
        }

        stringBuffer.append(OddNumberSystem(reverse5_8));


        return stringBuffer.toString();
    }

    private static String OddNumberSystem(String ms){
        Log.e("tag", "OddNumberSystem: "+ms );
        String newStr = new String();
        for (int i = 0; i < ms.length(); i += 2) {
            newStr += ms.charAt(i);
        }
        return newStr;
    }
    public static String initListener(byte[] bytes) {

        byte[] bytes1 = subByte(bytes, 4, 3);
        String s = ByteUtils.bytesToHexString(bytes1);

        byte[] bytes2 = subByte(bytes, 7, 4);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes2.length; i++) {
            Integer integer = Integer.valueOf("" + (bytes2[i] & 0xff), 16);
            stringBuffer.append(integer);
        }
        String ic_ = stringBuffer.toString();

        return ic_;
    }
    private static byte[] subByte(byte[] b,int off,int length){
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }
}
