package top.yeliusu.websocketserver.websocket.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Description
 * @Author SongJunWei
 * @Version V1.0.0
 * @Date 2020/1/3
 */
public class IpUtils {

    private static Logger log = LoggerFactory.getLogger(IpUtils.class);

    public static String getIpAddr() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();

            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {

                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address) {
                        log.info("获取到的ip地址：{}", ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            log.error("获取ip地址失败", e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(getIpAddr());
    }

    /**
     * 获取html中的纯文本
     *
     * @param strHtml html内容
     * @return 指定文字
     */
    public static String getHtmlText(String strHtml, Integer size) {
        //剔出<html>的标签
        String txtcontent = strHtml.replaceAll("</?[^>]+>", "");
        //去除字符串中的空格,回车,换行符,制表符
        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n|&nbsp;|</a>", "");
        return txtcontent.length() > size ? txtcontent.substring(0, size) : txtcontent;
    }
}

