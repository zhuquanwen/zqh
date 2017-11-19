package com.zqw.order.manage.controller;

import com.zqw.order.manage.domain.p.Goods;
import com.zqw.order.manage.domain.p.Qrcode;
import com.zqw.order.manage.entity.AjaxException;
import com.zqw.order.manage.entity.PageException;
import com.zqw.order.manage.service.api.GoodsService;
import com.zqw.order.manage.service.api.QrcodeService;
import com.zqw.order.manage.test.Logo_Two_Code;
import com.zqw.order.manage.util.FileDownload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

@Controller
public class QrcodeController {
    private static String GOODS_ID = "GOODS_ID";
    private static String TOP_PIC = "TOP_PIC";
    private static String MIDDLE_LOGO = "MIDDLE_LOGO";
    private static String WORD1 = "WORD1";
    private static String WORD2 = "WORD2";
    private static String WORD3= "WORD3";
    private static String WORD4 = "WORD4";
    private static String WORD5 = "WORD5";
    @Value("${pic_address}")
    private String picAddress;
    @Autowired
    private QrcodeService qrcodeService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/downloadQrcode")
    public void downloadQrcode(HttpServletRequest request, HttpServletResponse response, Long id, String goodsName, String address) throws AjaxException {
        if(address == null){
            address = "测试二维码效果";
        }
        try{
            String path = request.getRealPath("/qrcode/result");
            File file = new File(path);
            if(!file.exists()){
                file.mkdirs();
            }
            path += "/";
            Goods goods = goodsService.findByName(goodsName);
            if(goods != null){
                id = goods.getId();
            }else if(id == null){
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                pw.print("没有查到该商品信息，请联系管理员");
                pw.close();
                return;
            }
            Qrcode qr = qrcodeService.findByGoodsId(id);
            if(qr == null){
                response.setCharacterEncoding("utf-8");
                response.setHeader("Content-type", "text/html;charset=UTF-8");
                PrintWriter pw = response.getWriter();
                pw.print("该商品没有配置二维码信息，请联系管理员配置后再尝试");
                pw.close();
                return;
            }
            String imgPath = path + new Date().getTime() + ".png";
            String logoPath = qr.getLogPath();
            if(!StringUtils.isEmpty(logoPath)){
                logoPath = picAddress + "/qrcode/" + id + "/" + logoPath;
            }
            String encoderContent = address;
            Logo_Two_Code logo_Two_Code = new Logo_Two_Code();
            logo_Two_Code.createQRCode(encoderContent, imgPath, logoPath,8);
            String outputPath = path + new Date().getTime() + ".png";
            //绘制二维码
            String topPath = qr.getTopPath();
            if(!StringUtils.isEmpty(topPath)){
                topPath = picAddress + "/qrcode/" + id + "/" + topPath;
            }
            logo_Two_Code.createImg(outputPath,imgPath,topPath,
                        qr.getWord1(),qr.getWord2(),
                        qr.getWord3(),qr.getWord4(),
                        qr.getWord5());
            FileDownload.download(response, new File(outputPath));
        }catch (Exception e){
            e.printStackTrace();
            throw new AjaxException();
        }

    }

    @PostMapping("/qrcodeCommit")
    public ModelAndView qrcodeCommit(HttpServletRequest request, Long id,String word1,String word2,String word3,
                                     String word4,String word5) throws PageException{
        try{
            Qrcode qrcode = qrcodeService.findByGoodsId(id);
            if(qrcode == null){
                qrcode = new Qrcode();
                qrcode.setGoodsId(id);
            }
            qrcode.setWord1(word1);
            qrcode.setWord2(word2);
            qrcode.setWord3(word3);
            qrcode.setWord4(word4);
            qrcode.setWord5(word5);
            String path = request.getRealPath("/qrcode");
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

            /** 页面控件的文件流* */
            MultipartFile multipartFile = null;
            Map map = multipartRequest.getFileMap();
            int j = 1;
            for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
                Object obj = i.next();
                multipartFile = (MultipartFile) map.get(obj);
                InputStream inputStream;
                String filename = multipartFile.getOriginalFilename();
                if(!StringUtils.isEmpty(filename)){
                    inputStream = multipartFile.getInputStream();
                    String lastName = filename.substring(filename.lastIndexOf("."));
                    String picPath = path + "/" + j + lastName;
                    File tmpFile = new File(picPath);
                    FileUtils.copyInputStreamToFile(inputStream, tmpFile);
                    if(inputStream != null ){
                        inputStream.close();
                    }
                    String picPath1 = picAddress + "/qrcode/" + id + "/" + j + lastName;
                    FileInputStream fis = new FileInputStream(tmpFile);
                    FileUtils.copyInputStreamToFile(fis, new File(picPath1));
                    if(fis != null){
                        fis.close();
                    }
                    if(j ==1){
                        qrcode.setTopPath(j + lastName);
                    }else if(j == 2){
                        qrcode.setLogPath(j + lastName);
                    }
                }
                j++;
            }

            qrcodeService.save(qrcode);
        }catch (Exception e){
            e.printStackTrace();
            throw new PageException();
        }
        ModelAndView mav = new ModelAndView("redirect:qrcodeEdit?id=" + id);
        return mav;
    }

    @GetMapping("/qrcodeEdit")
    public ModelAndView qrcodeEdit(HttpServletRequest request, Long id) throws PageException {
        ModelAndView mav = new ModelAndView("qrcodeEdit");
        String path = request.getRealPath("/qrcode");
        try {
            mav.addObject(GOODS_ID, id);
            Qrcode qrcode = qrcodeService.findByGoodsId(id);
            if (qrcode != null) {
                String topPic = qrcode.getTopPath();
                if (!StringUtils.isEmpty(topPic)) {
                    topPic = picAddress + "/qrcode/" + id + "/" + topPic;
                    File file = new File(topPic);
                    if (file.exists()) {
                        String picPath1 = path +"/"+ id + "/" + file.getName();
                        String picPath2 = "/qrcode/" + id + "/" + file.getName();
                        FileInputStream fis = new FileInputStream(topPic);
                        FileUtils.copyInputStreamToFile(fis, new File(picPath1));
                        if (fis != null) {
                            fis.close();
                        }
                        mav.addObject(TOP_PIC, picPath2);
                    }
                }
                String middlePic = qrcode.getLogPath();
                if (!StringUtils.isEmpty(middlePic)) {
                    topPic = picAddress + "/qrcode/" + id + "/" + middlePic;
                    File file = new File(topPic);
                    if (file.exists()) {
                        String picPath1 = path + "/" + id + "/" + file.getName();
                        String picPath2 = "/qrcode/" + id + "/" + file.getName();
                        FileInputStream fis = new FileInputStream(topPic);
                        FileUtils.copyInputStreamToFile(fis, new File(picPath1));
                        if (fis != null) {
                            fis.close();
                        }
                        mav.addObject(MIDDLE_LOGO, picPath2);
                    }
                }
                String word1 = qrcode.getWord1();
                if (!StringUtils.isEmpty(word1)) {
                    mav.addObject(WORD1, word1);
                }
                String word2 = qrcode.getWord2();
                if (!StringUtils.isEmpty(word2)) {
                    mav.addObject(WORD2, word2);
                }
                String word3 = qrcode.getWord3();
                if (!StringUtils.isEmpty(word3)) {
                    mav.addObject(WORD3, word3);
                }
                String word4 = qrcode.getWord4();
                if (!StringUtils.isEmpty(word4)) {
                    mav.addObject(WORD4, word4);
                }
                String word5 = qrcode.getWord5();
                if (!StringUtils.isEmpty(word5)) {
                    mav.addObject(WORD5, word5);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new PageException();
        }
        return mav;
    }

}
