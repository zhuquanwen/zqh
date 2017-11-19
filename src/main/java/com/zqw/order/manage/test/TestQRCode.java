package com.zqw.order.manage.test;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.*;

public class TestQRCode {
    public static void main(String[] args) {
        ByteArrayOutputStream out = QRCode.from("http://mvnrepository.com/artifact/com.google.zxing/core/3.3.1").to(ImageType.PNG).stream();

        try {
            FileOutputStream fout = new FileOutputStream(new File(
                    "D:/QR_Code.JPG"));

            fout.write(out.toByteArray());

            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            // Do Logging
        } catch (IOException e) {
            // Do Logging
        }
    }
}
