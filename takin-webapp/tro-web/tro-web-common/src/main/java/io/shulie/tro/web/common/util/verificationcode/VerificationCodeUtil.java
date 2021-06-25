/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.common.util.verificationcode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.util.verificationcode
 * @date 2021/3/31 7:29 下午
 */
public class VerificationCodeUtil {
    // 时效1分钟
    public static Map<String,Map<Long, HttpSession>> sessionMap = Maps.newConcurrentMap();
    // 
    public static final String VERIFICATION = "verification_code";
    private static Random random = new Random();
    // 宽度 高度
    static int width = 79;
    static int height = 30;
    //验证码
    public static String VERIFICATION_CODE = "";


    /**
     * 得到图片的方法
     *
     * @return
     */
    public static BufferedImage getImage() {
        //内存中创建一张图片
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //获取当前图片的画笔
        Graphics gps = img.getGraphics();
        //设置画笔
        gps.setColor(new Color(240, 240, 240));
        //填充一个与图片一样大小的矩形（其实就是为了设置背景颜色）
        gps.fillRect(0, 0, width, height);
        StringBuilder sb = new StringBuilder();
        //开始画东西
        for (int i = 0; i < 4; i++) {
            String ch = getContent();
            sb.append(ch);
            gps.setColor(getColor());
            gps.setFont(getFont());
            gps.drawString(ch, width / 4 * i, height - 5);//宽度让其不满图片
        }
        drawLine(img);
        VERIFICATION_CODE = sb.toString();
        return img;
    }

    private static String getContent() {
        //在图片中插入字母和十个数字
        String str = "abcdefghijklmnupqrstuvwxyzABCDEFGHIJKLMNUPQRSTUVWZYZ1234567890";
        int index = random.nextInt(str.length());
        return str.charAt(index) + "";
    }

    public static void saveImage(BufferedImage img, OutputStream out) throws IOException {
        //这里为了测试将生成的图片放入f盘，在实际的项目开发中是需要将生成的图片写入客户端的:
        //response.getOutputStream()
        ImageIO.write(img, "JPEG", out);
        //ImageIO.write(img, "JPEG", new FileOutputStream("F:\\a.jpg"));//保存到硬盘
    }



    private static Font getFont() {
        //字号大小
        int[] fontSize = {24, 25, 26, 27, 28};
        //字体样式
        int[] fontStyle = {0, 1, 2, 3};
        //字体
        String[] font = {"宋体", "华文楷体", "华文隶书", "黑体", "华文新魏"};
        int index1 = random.nextInt(font.length);
        String name = font[index1];
        int style = random.nextInt(4);
        int index2 = random.nextInt(fontSize.length);
        int size = fontSize[index2];
        return new Font(name, style, size);
    }

    //得到不同的颜色
    private static Color getColor() {
        //取值范围是0-255
        int R = random.nextInt(256);
        int G = random.nextInt(256);
        int B = random.nextInt(256);
        return new Color(R, G, B);
    }

    //画干扰线
    private static void drawLine(BufferedImage img) {
        Graphics2D gs = (Graphics2D)img.getGraphics();
        gs.setColor(Color.BLACK);
        //设置线的宽度
        gs.setStroke(new BasicStroke(1.0F));
        //横坐标不能超过宽度，纵坐标不能超过高度
        for (int i = 0; i < 5; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            gs.drawLine(x1, y1, x2, y2);

        }
    }

    //添加
    public synchronized static void addSession(HttpSession session) {
        if (session != null) {
            Map<Long, HttpSession> map = Maps.newHashMap();
            map.put(System.currentTimeMillis(),session);
            sessionMap.put(session.getId(), map);
        }
    }

    //获取
    public synchronized static HttpSession getSession(String sessionId) {
        // 超过清理一分钟的HttpSession
        if(sessionMap.isEmpty()) {
            return null;
        }
        for(Entry<String,Map<Long, HttpSession>> entry :sessionMap.entrySet()) {
            // 遍历keySet，并输出key的值
            for(Long key : entry.getValue().keySet()){
                if(System.currentTimeMillis() - key > 60*1000L) {
                    sessionMap.remove(entry.getKey());
                }
            }
        }
        if(sessionId != null){
            Map<Long,HttpSession> map = sessionMap.get(sessionId);
            if(map == null) {
                return null;
            }
            return Lists.newArrayList(map.values()).get(0);
        }
        return null;
    }

    //删除
    public  synchronized static void delSession(String sessionId) {
        if(sessionId != null){
            sessionMap.remove(sessionId);
        }
    }

    public static String VERIFICATION_CODE() {
        return VERIFICATION_CODE;
    }
}

