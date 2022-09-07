package com.github.admincaofuqiang.ImageUtil;

import cn.hutool.extra.qrcode.QrCodeUtil;
import org.apache.commons.lang3.ObjectUtils;


import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.util.Base64;


/*
 * @name Image
 * @description  Create a composite picture with a centered copy above and below the picture
 * @date 2022/2/18 17:04
 * @author yunhua
 * @email caofuqiangmail@163.com
 */
public class Image {
    public static BufferedImage makeImageStream(BufferedImage bufferedImage, int outputWidth, int outputHeight, int qrcodeWidth, Integer descriptionMarginBottom, Integer explainMarginBottom, String description, String explain, Color backgroundColor, Font font, Color foregroundColor) {
        if (outputWidth <= 0)
            throw new RuntimeException("输出图片尺寸过小");
        if (qrcodeWidth <= 0)
            throw new RuntimeException("内嵌图片尺寸过小");
        if (outputWidth < qrcodeWidth)
            throw new RuntimeException("输出文件尺寸大于内嵌图片尺寸");
        BufferedImage outBufferedImage = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = outBufferedImage.createGraphics();
        if (ObjectUtils.isEmpty(backgroundColor))
            graphics.setBackground(Color.white);
        else
            graphics.setBackground(backgroundColor);
        graphics.clearRect(0, 0, outputWidth, outputHeight);
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setPaint(new Color(0, 0, 0, 64));//阴影颜色
        if (ObjectUtils.isEmpty(font))
            font = new Font("微软雅黑", Font.PLAIN, 20);
        graphics.setFont(font);
        if (ObjectUtils.isEmpty(foregroundColor))
            graphics.setColor(Color.BLACK);
        else
            graphics.setColor(foregroundColor);
        FontMetrics fm = graphics.getFontMetrics(font);
        int textWidth = fm.stringWidth(description);
        int widthX = (outputWidth - textWidth) / 2;
        graphics.drawString(description, widthX, outputHeight - descriptionMarginBottom);

        Font font1 = new Font("微软雅黑", Font.PLAIN, 13);
        graphics.setFont(font1);
        graphics.setColor(Color.gray);
        FontMetrics fm1 = graphics.getFontMetrics(font1);
        int explainWidth = fm1.stringWidth(explain);
        int expWidth = (outputWidth - explainWidth) / 2;
        graphics.drawString(explain, expWidth, outputHeight - explainMarginBottom);
        graphics.drawImage(bufferedImage, (outputWidth - qrcodeWidth) / 2, 10, qrcodeWidth, qrcodeWidth, null);
        graphics.dispose();
        return outBufferedImage;
    }

    public static String makeImageBase64(BufferedImage bufferedImage, int outputWidth, int outputHeight, int qrcodeWidth, Integer descriptionMarginBottom, Integer explainMarginBottom, String description, String explain, Color backgroundColor, Font font, Color foregroundColor) {
        BufferedImage out = makeImageStream(bufferedImage, outputWidth, outputHeight, qrcodeWidth, descriptionMarginBottom, explainMarginBottom, description, explain, backgroundColor, font, foregroundColor);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(out, "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = Base64.getEncoder().encodeToString(stream.toByteArray());
        return "data:image/png;base64," + s;
    }



}
