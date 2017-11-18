package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Order;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class ExcelExportService {
    public void exportOrder(HttpServletResponse response, List<Order> orderList) throws IOException {
        response.setContentType("octets/stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + new Date().getTime() + ".xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        // 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        XSSFSheet sheet = workBook.createSheet("订单导出");
        sheet.setDefaultColumnWidth(300*256);
//        sheet.DefaultColumnWidth=100*256;
//        sheet.setColumnWidth(0,80);
//        sheet.setColumnWidth(2,80);
//        sheet.setColumnWidth(3,80);
//        sheet.setColumnWidth(4,80);
//        sheet.setColumnWidth(5,80);
//        sheet.setColumnWidth(6,80);
//        sheet.setColumnWidth(7,80);
//        sheet.setColumnWidth(8,80);
//        sheet.setColumnWidth(9,80);
//        sheet.setColumnWidth(10,80);
//        sheet.setColumnWidth(11,80);

        ExportUtil exportUtil = new ExportUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;

        for (int i = 0; i <= 11; i++) {
            String value = "";
            if(i == 0){
                value = "单号";
            }else if(i == 1){
                value = "姓名";
            }else if(i == 2){
                value = "手机";
            }else if(i == 3){
                value = "下单时间";
            }else if(i == 4){
                value = "商品名称";
            }else if(i == 5){
                value = "款式";
            }else if(i == 6){
                value = "尺码";
            }else if(i == 7){
                value = "地址";
            }else if(i == 8){
                value = "充值卡类型";
            }else if(i == 9){
                value = "推广人";
            }else if(i == 10){
                value = "发货时间";
            }else if(i == 11 ){
                value = "id(此列不可编辑)";
            }
            cell = headRow.createCell(i);
            cell.setCellType(XSSFCell.CELL_TYPE_STRING);
            cell.setCellStyle(headStyle);
            cell.setCellValue(value);

        }
        if(orderList != null && orderList.size() > 0){
            for (int j = 0; j < orderList.size(); j++) {
                Order order = orderList.get(j);
                XSSFRow bodyRow = sheet.createRow(j+1);
                for (int i = 0; i <= 11; i++) {
                    String value = "";
                    if(i == 0){
                        value = order.getCourierNum();
                    }else if(i == 1){
                        value = order.getName();
                    }else if(i == 2){
                        value = order.getPhoneNum();
                    }else if(i == 3){
                        value = order.getOrderDate();
                    }else if(i == 4){
                        value = order.getGoodsName();
                    }else if(i == 5){
                        value = order.getStyle();
                    }else if(i == 6){
                        value = order.getClothSize();
                    }else if(i == 7){
                        value = order.getAddress();
                    }else if(i == 8){
                        String type = order.getCardType();
                        if("1".equals(type)){
                            value = "移动";
                        }else if ("2".equals(type)){
                            value = "联通";
                        }else if ("3".equals(type)){
                            value = "电信";
                        }
//                        value = "充值卡类型";
                    }else if(i == 9){
                        value = order.getSpreadUserName();
                    }else if(i == 10){
                        value = order.getRecordDate();
                    }else if(i == 11 ){
                        value = order.getId().toString();
                    }
                    cell = bodyRow.createCell(i);
                    cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                    cell.setCellStyle(bodyStyle);
                    cell.setCellValue(value);
                }
            }
        }
        workBook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

}
