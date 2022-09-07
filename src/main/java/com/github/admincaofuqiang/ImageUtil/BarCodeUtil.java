package com.github.admincaofuqiang.ImageUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.krysalis.barcode4j.HumanReadablePlacement;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
/*This is bar code util*/
public class BarCodeUtil {
    public static ByteArrayOutputStream makeBarCode(String code, Boolean quietZone, HumanReadablePlacement position) {
        Code128Bean bean = new Code128Bean();
        // 分辨率
        int dpi = 512;
        // 设置两侧是否留白
        if (ObjectUtils.isEmpty(quietZone)) {
            quietZone = Boolean.FALSE;
        }
        bean.doQuietZone(quietZone);

        bean.setBarHeight((double) ObjectUtils.defaultIfNull(15.00, 9.0D));
        bean.setModuleWidth(0.4);
        if (ObjectUtils.isEmpty(position)) {
            position = HumanReadablePlacement.HRP_NONE;
        }
        bean.setMsgPosition(position);
        // 设置图片类型
        String format = "image/png";
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        BitmapCanvasProvider canvas = new BitmapCanvasProvider(ous, format, dpi,
                BufferedImage.TYPE_BYTE_BINARY, false, 0);

        // 生产条形码
        bean.generateBarcode(canvas, code);
        try {
            canvas.finish();
        } catch (IOException e) {
            throw new RuntimeException("条形码生成异常");
        }
        return ous;
    }


    public static ByteArrayOutputStream makeBarCode(String code) {
        return makeBarCode(code, null, null);
    }

    public static ByteArrayOutputStream makeBarCode(String code, Boolean quietZone) {
        return makeBarCode(code, quietZone, null);
    }

    public static ByteArrayOutputStream makeBarCode(String code, HumanReadablePlacement position) {
        return makeBarCode(code, null, position);
    }

    public static String makeBarCodeBase64(String code, Boolean quietZone, HumanReadablePlacement position) {
        ByteArrayOutputStream byteArrayOutputStream = makeBarCode(code, quietZone, position);
        return "data:image/png;base64," + Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
    }

    public static String makeBarCodeBase64(String code, Boolean quietZone) {
        ByteArrayOutputStream byteArrayOutputStream = makeBarCode(code, quietZone, null);
        return "data:image/png;base64," + Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
    }

    public static String makeBarCodeBase64(String code, HumanReadablePlacement position) {
        ByteArrayOutputStream byteArrayOutputStream = makeBarCode(code, null, position);
        return "data:image/png;base64," + Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
    }


}
