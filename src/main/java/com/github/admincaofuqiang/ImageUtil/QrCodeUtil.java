package com.github.admincaofuqiang.ImageUtil;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;

public class QrCodeUtil {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    public static BitMatrix makeQrCode(String code, Integer width, Integer height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = null;
        try {
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L); // 指定纠错等级
            bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, width, height,hints);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        return bitMatrix;
    }

    private static BufferedImage toBufferedImage(BitMatrix matrix, int reduceWhiteArea) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width - 2 * reduceWhiteArea, height - 2 * reduceWhiteArea, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = reduceWhiteArea; x < width - reduceWhiteArea; x++) {
            for (int y = reduceWhiteArea; y < height - reduceWhiteArea; y++) {
                image.setRGB(x - reduceWhiteArea, y - reduceWhiteArea, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }
    private static BufferedImage addLogo(BufferedImage image, String logoPath){
        try {
            Graphics2D g = image.createGraphics();
            BufferedImage logoImage = ImageIO.read(new File(logoPath));
            int width =image.getWidth()/6;
            int height = width;
            int x = (image.getWidth() - width) / 2;
            int y = (image.getHeight() - height) / 2;
            g.drawImage(logoImage, x, y, width, height, null);
            g.setStroke(new BasicStroke(2)); //
            g.setColor(Color.WHITE);
            g.drawRect(x, y, width, height);
            logoImage.flush();
            g.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;
    }
    public static  String makeQrCodeBase64(String text,Integer width,Integer height,Integer reduceMargin){
        BitMatrix bitMatrix = makeQrCode(text, width, height);
        BufferedImage bufferedImage = toBufferedImage(bitMatrix, reduceMargin);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[0];
        try {
            //将二维码写入字节输出流
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            //将字节输出流初始化到输入流
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "data:image/png;base64," + Base64.encodeBase64String(data);
    }
    public static String makeQrCodeBase64WithLogo(String text,Integer width,Integer height,Integer reduceMargin,String logoPath){
        BitMatrix bitMatrix = makeQrCode(text, width, height);
        BufferedImage bufferedImageQr = toBufferedImage(bitMatrix, reduceMargin);
        BufferedImage bufferedImageQrWithLogo = addLogo(bufferedImageQr, logoPath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[0];
        try {
            //将二维码写入字节输出流
            ImageIO.write(bufferedImageQrWithLogo, "png", byteArrayOutputStream);
            //将字节输出流初始化到输入流
            InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            byteArrayOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "data:image/png;base64," + Base64.encodeBase64String(data);
    }


    public static void main(String[] args) {
        String s = makeQrCodeBase64WithLogo("1234", 300, 300, 30, "/Users/caofuqiang/Desktop/111.png");
        System.out.println(s);
    }
}
