package com.ten.half.rxmvp.util;

import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    public static void main(String[] args) {
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String md5sum(String filename) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            String md5Str = toHexString(md5.digest());
            return TextUtils.isEmpty(md5Str) ? "" : md5Str;
        } catch (Exception e) {
            return "";
        }
    }

    public static String get(String id) {
        String str = id;
        byte[] digest = null;
        try {
            digest = MessageDigest.getInstance("MD5").digest(
                    str.getBytes("UTF-8"));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder md5 = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            if ((b & 0xFF) < 0x10)
                md5.append("0");
            md5.append(Integer.toHexString(b & 0xFF));
        }
        return md5.toString();
    }
}