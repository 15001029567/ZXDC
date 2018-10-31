package net.edaibu.easywalking.utils.bletooth;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * 针对蓝牙命令加密或者解密
 */
public class AesUtils {
    public static String str = "qwertyuiopasdfgh";
    public static byte[] cKey;

    // aes128加密（）
    public static byte[] encrypt(byte[] sSrc) {
        if (cKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (cKey.length != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw;
        try {
            raw = cKey;

            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] data;
            byte[] sSrcByte = sSrc;
            if (sSrcByte.length % 16 != 0) {
                data = new byte[(sSrcByte.length / 16 + 1) * 16];
                for (int i = 0; i < sSrcByte.length; i++) {
                    data[i] = sSrcByte[i];
                }
            } else {
                data = sSrcByte;
            }
            byte[] encrypted = cipher.doFinal(data);
            return encrypted;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 解密
    public static byte[] decrypt(byte[] sSrc) {
        try {
            // 判断Key是否正确
            if (cKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (cKey.length != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = cKey;
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = sSrc;
            try {
                byte[] original = cipher.doFinal(encrypted1);
                return original;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    /**
     * 解析密钥文件
     * @param sourceFilePath
     * @return
     */
    public static byte[] decryptFile(InputStream sourceFilePath) {
        InputStream in = sourceFilePath;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[100];
            int rc = 0;
            while ((rc = in.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            return swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 根据密钥种子计算
     * @param k1
     * @param k2
     * @param key_table
     * @return
     */
    public static byte[] getKey(int k1, int k2, byte[] key_table) {
        if (k1 < 0) {
            k1 = k1 + 256;
        }
        if (k2 < 0) {
            k2 = k2 + 256;
        }

        byte[] key = new byte[16];
        k1 = k1 % 64;
        k2 = k2 % 64;

        for (int i = 0; i < 16; i++) {
            key[i] = key_table[k1 * 16 + i];
            key[i] ^= key_table[k2 * 16 + i];
        }
        return key;
    }
}
