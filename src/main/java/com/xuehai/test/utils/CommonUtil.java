package com.xuehai.test.utils;

import com.xuehai.test.base.Log;
import org.apache.commons.codec.digest.DigestUtils;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

/**
 * @ClassName CommonUtil
 * @Description:    常用工具类
 * @Author Sniper
 * @Date 2019/3/14 15:25
 */
public class CommonUtil {

    private static final String CLASS_NAME = CommonUtil.class.getName();
    private static final String SECRET="eptim]q34imt5b]-q04i5q=fdkfjfsadlkjfasdfrt573df4pltoy]-pn965498d";

    /**
     * @description:        构造请求签名
     * @param method        请求方法
     * @param url           请求url,带http://
     * @param queryString   url查询字符串
     * @param body          请求body
     * @param accessToken   登录session
     * @return java.lang.String
     * @author Sniper
     * @date 2019/4/19 10:13
     */
    public static String createRequestSignature(String method, String url, String queryString, String body,
                                                String accessToken) {
        StringBuilder sb = new StringBuilder();
        long now = System.currentTimeMillis();
        sb.append(method)
            .append(url)
            .append(queryString != null ? queryString : "")
            .append(now)
            .append(SECRET)
            .append(body != null ? body : "")
            .append(accessToken != null ? accessToken : "");
        String sign = CommonUtil.MD5(sb.toString());
        if(queryString != null && !queryString.isEmpty()) {
            return url + "?" + queryString + "&sign=" + sign + "&t=" + now;
        } else {
            return url + "?sign=" + sign + "&t=" + now;
        }
    }

    /**
     * @description:        字符串md5加密
     * @param sourceString  源字符串
     * @return java.lang.String
     * @author Sniper
     * @date 2019/4/19 10:15
     */
    public static String MD5(String sourceString) {
        return DigestUtils.md5Hex(sourceString);
    }

    /**
     * @description:    文件md5加密
     * @param file      目标文件
     * @return java.lang.String
     * @author Sniper
     * @date 2019/4/19 10:16
     */
    public static String MD5(File file) {
        String md5 = null;
        FileInputStream fileInputStream = null;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                md5 = DigestUtils.md5Hex(fileInputStream);
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "MD5加密失败", e);
        }
        return md5;
    }

    /**
     * @description:    url编码
     * @param target    目标url
     * @param charset   字符集:如UTF-8
     * @return java.lang.String
     * @author Sniper
     * @date 2019/4/19 10:16
     */
    public static String urlEncode(String target, String charset) {
        String encodeString = null;
        try {
            encodeString = URLEncoder.encode(target, charset);
        } catch (UnsupportedEncodingException e) {
            Log.error(CLASS_NAME, "encode失败", e);
        }
        return encodeString;
    }

    /**
     * @description:    url解码
     * @param target    目标url
     * @param charset   字符集:如UTF-8
     * @return java.lang.String
     * @author Sniper
     * @date 2019/4/19 10:16
     */
    public static String urlDecode(String target, String charset) {
        String decodeString = null;
        try {
            decodeString = URLDecoder.decode(target, charset);
        } catch (UnsupportedEncodingException e) {
            Log.error(CLASS_NAME, "decode失败", e);
        }
        return decodeString;
    }

    /**
     * @description:   base64编码
     * @param bytes    目标数据byte[]
     * @return java.lang.String
     * @author Sniper
     * @date 2019/4/19 10:16
     */
    public static String base64Encode(byte[] bytes) {
        String encodeString = null;
        if (bytes != null) {
            encodeString = Base64.getEncoder().encodeToString(bytes);
        }
        return encodeString;
    }

    /**
     * @description:    base64解码
     * @param target    目标字符串
     * @param charset   字符集:如UTF-8
     * @return java.lang.String
     * @throws
     * @author Sniper
     * @date 2019/4/19 10:19
     */
    public static String base64Decode(String target, String charset) {
        String decodeString = null;
        try {
            if (target !=  null) {
                byte[] temp = Base64.getDecoder().decode(target.getBytes());
                decodeString = new String(temp, charset);
            }
        } catch (IOException e) {
           Log.error(CLASS_NAME, "BASE64Decode失败", e);
        }
        return decodeString;
    }

    /**
     * @description:    格式化StringBuilder
     * @param target    StringBuilder对象
     * @return          String
     * @author Sniper
     * @date 2019/3/14 15:27 
     */
    public static String format(StringBuilder target) {
        int index = target.lastIndexOf(",}");
        while (index > 0) {
            target.replace(index, index + 1, "");
            index = target.lastIndexOf(",}");
        }
        index = target.lastIndexOf(",]");
        while (index > 0) {
            target.replace(index, index + 1, "");
            index = target.lastIndexOf(",]");
        }
        index = target.lastIndexOf(",)");
        while (index > 0) {
            target.replace(index, index + 1, "");
            index = target.lastIndexOf(",)");
        }
        return target.toString();
    }

    /**
     * @description: 执行cmd命令
     * @param command       命令集
     * @return java.lang.String
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:23
     */
    @SuppressWarnings("Duplicates")
    public static String cmdExecutor(String ... command) {
        StringBuilder output = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            try {
                ProcessBuilder pb = new ProcessBuilder(command);
                Log.info(CLASS_NAME, "执行命令:{}", pb.command().toString());
                pb.redirectErrorStream(true);
                Process p = pb.start();
                inputStream = p.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    output.append(line);
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException e) {
            Log.error(CLASS_NAME, "命令执行失败", e);
        }
        return output.toString();
    }

    /**
     * @description: 执行cmd命令
     * @param command       命令行
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:24
     */
    public static void cmdExecutor(String command) {
        try {
            Log.info(CLASS_NAME, "执行命令: {}", command);
            Runtime rm = Runtime.getRuntime();
            rm.exec(command);
            Log.info(CLASS_NAME, "命令执行成功");
        } catch (IOException e) {
            Log.error(CLASS_NAME,  "命令执行失败", e);
        }
    }

    /**
     * @description: 杀进程
     * @param processName   进程名
     * @return void
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:24
     */
    public static void killProcess(String processName) {
        try {
            String command = "cmd /c start taskkill /f /im " + processName;
            Log.info(CLASS_NAME, "执行结束进程命令: {}" , command);
            Runtime rm = Runtime.getRuntime();
            rm.exec(command);
            Log.info(CLASS_NAME, "结束进程成功");
        } catch (IOException e) {
            Log.error(CLASS_NAME, "结束进程失败", e);
        }
    }

    /**
     * @description: 获取当前方法名
     * @return java.lang.String
     * @throws
     * @author Sniper
     * @date 2019/5/23 9:26
     */
    public static String methodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

}
