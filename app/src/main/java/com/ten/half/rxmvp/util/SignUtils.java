package com.ten.half.rxmvp.util;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by wugang on 2017/9/22.
 * 签名工具
 */

public class SignUtils {
    private static final String key = "e524d1266c67035ff63a67ac0f4c8621";

    public static String signKey(Map<String, String> map, String oncekey, String timestamp) {
        String riseKey1 = getRiseKey(map);
        String hash = sha1(oncekey + timestamp);
        return MD5.get(hash + riseKey1);
    }

    /**
     * 获取对Map集合key升序后的字符串
     */
    public static String getRiseKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            //对Map key做升序
            Map<String, String> map1 = new TreeMap<String, String>(new Comparator<String>() {
                @Override
                public int compare(String s, String t1) {

                    return s.compareTo(t1);
                }
            });
            map1.putAll(map);
            Set<String> keySet = map1.keySet();
            Iterator<String> iter = keySet.iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                try {
                    String str = encode(map1.get(key));
                    stringBuffer.append(key + "=" + str + "&");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            // AppContext.LogInfo("参数排序", stringBuffer.toString());
            return stringBuffer.toString();
        }
    }

    private static String encode(String str) {
        try {
            String str1 = URLEncoder.encode(str, "UTF-8");
            return str1.replace("*", "%2A");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 获取随机数
     */
    public static String getRandom() {
        Random random = new Random();
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            result.append(random.nextInt(10));

        }
        return result.toString();
    }

    /**
     * 获取签名数据
     */
    public static String getSignString(Map<String, String> map, String nonce, Long timestamp) {
        String riseKey = getRiseKey(map);
        String tmpHashKey = MD5.get(nonce);
        String hashKey = MD5.get(tmpHashKey.substring(0, 10) + timestamp);
        String hash1 = MD5.get(riseKey + hashKey);
        String hash2 = MD5.get(hashKey + riseKey);
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 16; i++) {
            stringBuffer.append(hash1.charAt(i) + "" + hash2.charAt(i));
        }
        return MD5.get(stringBuffer.toString());
    }

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    public static String sha1(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成随机数字和字母
     */

    public static String getStringRandom(int length) {

        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 编码
     *
     * @param bstr
     * @return String
     */
    public static String base64encode(byte[] bstr) {
        return Base64.encodeToString(bstr, Base64.DEFAULT);//  编码后);
    }

    /**
     * 解码
     *
     * @param str
     * @return string
     */
    public static byte[] base64decode(String str) {
        byte[] bt = null;
        try {
            bt = Base64.decode(str, Base64.DEFAULT);// 解码后
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bt;
    }

    public static String stroxstr(String key, String data) {
        int len = Math.min(key.length(), data.length());
        String s = "";
        for (int i = 0; i < data.length(); i++) {
            int c = data.charAt(i);
            int c1 = key.charAt(i % len);
            int cha = c ^ c1;
            s = s + (char) cha;
        }
        return s;
    }

    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    public static String parDecode(String str, String signkey) {
        str = str.trim();
        byte[] bytes = SignUtils.base64decode(str);
        String s = new String(bytes);
        String par_str = s.substring(0, s.length() - 5);
        String once_str = s.substring(s.length() - 5);
        String once_md5 = MD5.get(once_str);
        String hashkey = SignUtils.stroxstr(key, once_md5);
        String sikey = SignUtils.sha1(hashkey + signkey);
        String stroxstr = SignUtils.stroxstr(sikey, par_str);
        Log.d("SIGN_KEY == >", ":" + signkey);
        return stroxstr;
    }
}
