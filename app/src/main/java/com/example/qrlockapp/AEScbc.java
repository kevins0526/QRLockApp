package com.example.qrlockapp;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;

public class AEScbc {
    private static final String secretKey = "Chin_Yi_"+BuildConfig.AES_KEY_PART2; //利用JAVA硬編碼 + BuildConfig中的方式增加安全性
    public static String encrypt(String data, String ivString) {

        // 偏移量
        //String ivString = "1234567890123456";

        byte[] iv = ivString.getBytes();
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int length = dataBytes.length;
            // 计算需填充长度
            if (length % blockSize != 0) {
                length = length + (blockSize - (length % blockSize));
            }
            byte[] plaintext = new byte[length];
            // 填充
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            // 设置偏移量参数
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryped = cipher.doFinal(plaintext);
            return base64Encode(addBytes(encryped,iv));
            //return base64Encode(encryped);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static String desEncrypt(String data) {


        try {
            byte[] encryp = base64Decode(data);
            byte[] encryp1 = splitBytesToData(encryp);
            byte[] iv = splitBytesToIV(encryp);
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(encryp1);
            return new String(original);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public static String base64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }
    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }
    public static byte[] splitBytesToData(byte[] data) {
        byte[] newData = new byte[16];
        System.arraycopy(data, 0, newData, 0, data.length - 16);
        return newData;
    }

    public static byte[] splitBytesToIV(byte[] data) {
        byte[] dataIv = new byte[16];
        System.arraycopy(data, data.length - 16, dataIv, 0, 16);
        return dataIv;
    }
}
