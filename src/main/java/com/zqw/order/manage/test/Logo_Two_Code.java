package com.zqw.order.manage.test;

import com.swetake.util.Qrcode;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @作者  Relieved
 * @创建日期   2014年11月8日
 * @描述  （带logo二维码）
 * @版本 V 1.0
 */
public class Logo_Two_Code {

    public  int createImg(String outputPath,String qrcodePath,String topPicPath,String word1,
                                String word2,String word3,String word4,String word5) throws IOException {
        int width = 1080;
        int height = 1920;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gs = image.createGraphics();
        gs.setBackground(Color.BLACK);
        gs.clearRect(0, 0, width, height);
        // 设定图像颜色 > BLACK
        gs.setColor(Color.WHITE);
        //绘制二维码
        if(qrcodePath != null){
            Image qrcode = ImageIO.read(new File(qrcodePath));
            int qrcodeWidth = qrcode.getWidth(null)*3;
            int qrcodeHeight = qrcode.getHeight(null)*3;
            int x = (image.getWidth() - qrcodeWidth) / 2;
            int y = (image.getHeight() - qrcodeWidth) / 2;
            gs.drawImage(qrcode, x, y, qrcodeWidth, qrcodeHeight, null);
        }
        if(topPicPath != null){
            Image topImg = ImageIO.read(new File(topPicPath));
            int topImgWidth = topImg.getWidth(null);
            int topImgHeight = topImg.getHeight(null);
            int x1 = (image.getWidth() - topImgWidth) / 2;
            int y1 = 5;
            gs.drawImage(topImg, x1, y1, topImgWidth, topImgHeight, null);
        }
        int wordHeight = 1300;
        wordHeight = drawWords(word1,gs,width,wordHeight);
        wordHeight = drawWords(word2,gs,width,wordHeight);
        wordHeight = drawWords(word3,gs,width,wordHeight);
        wordHeight = drawWords(word4,gs,width,wordHeight);
        wordHeight = drawWords(word5,gs,width,wordHeight);

        gs.dispose();
        image.flush();
        // 生成二维码QRCode图片
        File imgFile = new File(outputPath);
        ImageIO.write(image, "png", imgFile);
        return -1;
    }
    private  int drawWords(String word, Graphics2D gs,Integer width,Integer height){
        if(StringUtils.isEmpty(word)){
            return height;
        }
        Font font=new Font("宋体",Font.PLAIN,48);
        gs.setFont(font);
        // 抗锯齿
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 计算文字长度，计算居中的x点坐标
        FontMetrics fm = gs.getFontMetrics(font);
        int textWidth = fm.stringWidth(word);
        int widthX = (width - textWidth) / 2;
        // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
        gs.drawString(word,widthX,height);
        return height + 100;
    }

    public  int createQRCode(String content, String imgPath,String ccbPath,int version) {
        try {
            Qrcode qrcodeHandler = new Qrcode();
            //设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
            qrcodeHandler.setQrcodeErrorCorrect('M');
            //N代表数字,A代表字符a-Z,B代表其他字符
            qrcodeHandler.setQrcodeEncodeMode('B');
            // 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大
            qrcodeHandler.setQrcodeVersion(version);
            // 图片尺寸
            int imgSize =67 + 12 * (version - 1) ;
//            int width = 1080;
//            int height = 1920;

            byte[] contentBytes = content.getBytes("gb2312");
            BufferedImage image = new BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D gs = image.createGraphics();

            gs.setBackground(Color.WHITE);
            gs.clearRect(0, 0, imgSize, imgSize);

            // 设定图像颜色 > BLACK
            gs.setColor(Color.BLACK);

            // 设置偏移量 不设置可能导致解析出错
            int pixoff = 3;
            // 输出内容 > 二维码
            int size = 0;
            if (contentBytes.length > 0 && contentBytes.length < 130) {
                boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
                size = codeOut.length;
                for (int i = 0; i < codeOut.length; i++) {

                    for (int j = 0; j < codeOut.length; j++) {
                        if (codeOut[j][i]) {
                            gs.fillRect(j * 3 + pixoff, i * 3  + pixoff, 3, 3);

                        }
                    }
                }
            } else {
                System.err.println("QRCode content bytes length = "
                        + contentBytes.length + " not in [ 0,125]. ");
                return -1;
            }

            if(ccbPath != null){
                Image logo = ImageIO.read(new File(ccbPath));//实例化一个Image对象。
                int widthLogo = logo.getWidth(null)>image.getWidth()*10/10?(image.getWidth()*10/10):logo.getWidth(null),
                        heightLogo = logo.getHeight(null)>image.getHeight()*10/10?(image.getHeight()*10/10):logo.getWidth(null);

                /**
                 * logo放在中心
                 */
                int x = (image.getWidth() - widthLogo) / 2;
                int y = (image.getHeight() - heightLogo) / 2;
                gs.setColor(Color.WHITE);
                int feng =3;
                gs.fillRect(x-feng , y-feng , widthLogo + feng*2, feng);
                gs.fillRect(x-feng  , y+heightLogo , widthLogo + feng*2, feng);
                gs.fillRect(x-feng , y , feng, heightLogo);
                gs.fillRect(x+widthLogo, y  , feng, heightLogo);
                gs.drawImage(logo, x, y, widthLogo, heightLogo, null);
            }

            gs.dispose();
            image.flush();

            // 生成二维码QRCode图片
            File imgFile = new File(imgPath);
            ImageIO.write(image, "png", imgFile);


        } catch (Exception e)
        {
            e.printStackTrace();
            return -100;
        }

        return 0;
    }

    public static void main(String[] args) {
        String imgPath = "D:/logo_QRCode.png";
        String logoPath = "D:/a.jpeg";
        String encoderContent = "http://blog.csdn.net/gao36951";
        Logo_Two_Code logo_Two_Code = new Logo_Two_Code();
        logo_Two_Code.createQRCode(encoderContent, imgPath, logoPath,8);

        //绘制二维码
        try {
            logo_Two_Code.createImg("D:/output.png","D:/logo_QRCode.png","D:/c.gif",
                    "NIKE中国官方活动·NIKE套装","无需宣传·自助下单·七天内包退换",
                    "长按图片识别二维码即可领取","官方网站:http://www.nike.com",
                    "领取套装人数过多·网页缓慢请耐心等待");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}