package com.zqw.order.manage.service;

import com.zqw.order.manage.domain.p.Order;
import com.zqw.order.manage.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 读取Excel 
 * @author lp
 *
 */
@Service
public class ExcelReadService {
//    public int totalRows; //sheet中总行数
//    public  int totalCells; //每一行总单元格数
    /**
     * read the Excel .xlsx,.xls
     * @param file jsp中的上传文件 
     * @return
     * @throws IOException
     */
    public List<Order> readExcel(MultipartFile file ) throws Exception {
        if(file==null|| ExcelUtils.EMPTY.equals(file.getOriginalFilename().trim())){
            return null;
        }else{
            String postfix = ExcelUtils.getPostfix(file.getOriginalFilename());
            if(!ExcelUtils.EMPTY.equals(postfix)){
                if(ExcelUtils.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)){
                    return readXls(file);
                }else if(ExcelUtils.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)){
                    return readXlsx(file);
//                    return readXlsxByFile(realFile);
                }else{
                    return null;
                }
            }
        }
        return null;
    }

    public static void main(String[] args){
        File file = new File("C:/Users/gmh/Desktop/aaa.xlsx");
        new ExcelReadService().readXlsxByFile(file);
    }

    public List<Order> readXlsxByFile(File file){
        List<Order> list = new ArrayList<Order>();
        // IO流读取文件
        InputStream input = null;
        XSSFWorkbook wb = null;
        Order order = null;
        try {
            input = new FileInputStream(file);
            // 创建文档
//            wb = new XSSFWorkbook(input);
            wb = (XSSFWorkbook) WorkbookFactory.create(input);
            //读取sheet(页)
            for(int numSheet=0;numSheet<wb.getNumberOfSheets();numSheet++){

                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if(xssfSheet == null){
                    continue;
                }
                int totalRows = xssfSheet.getLastRowNum();
                //读取Row,从第二行开始
                for(int rowNum = 1;rowNum <= totalRows;rowNum++){
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if(xssfRow!=null){
                        order = new Order();
                        int totalCells = xssfRow.getLastCellNum();
                        //读取列，从第一列开始
                        for(int c=0;c<=totalCells+1;c++){
                            XSSFCell cell = xssfRow.getCell(c);
                            if(c == 0){
                                order.setCourierNum(cell != null ? cell.toString() : null);
                            }else if(c == 1){
                                order.setName(cell != null ? cell.toString() : null);
                            }else if (c == 2){
                                order.setPhoneNum(cell != null ? cell.toString() : null);
                            }else if (c == 3){
                                order.setOrderDate(cell != null ? cell.toString() : null);
                            }
                        }
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        order.setRecordDate(df.format(new Date()));
                        list.add(order);
                    }
                }
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        } finally{
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }


    }

    @SuppressWarnings("deprecation")
    public List<Order> readXlsx(MultipartFile file){
        List<Order> list = new ArrayList<Order>();
        // IO流读取文件  
        InputStream input = null;
        XSSFWorkbook wb = null;
        Order order = null;
        try {
            input = file.getInputStream();
            // 创建文档  
            wb = (XSSFWorkbook) WorkbookFactory.create(input);
            //读取sheet(页)  
            for(int numSheet=0;numSheet<wb.getNumberOfSheets();numSheet++){
                XSSFSheet xssfSheet = wb.getSheetAt(numSheet);
                if(xssfSheet == null){
                    continue;
                }
                int totalRows = xssfSheet.getLastRowNum();
                //读取Row,从第二行开始  
                for(int rowNum = 1;rowNum <= totalRows;rowNum++){
                    XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                    if(xssfRow!=null){
                        order = new Order();
                        int totalCells = xssfRow.getLastCellNum();
                        //读取列，从第一列开始  
                        for(int c=0;c<=totalCells+1;c++){
                            XSSFCell cell = xssfRow.getCell(c);
                            if(c == 0){
                                order.setCourierNum(cell != null ? cell.toString() : null);
                            }else if(c == 1){
                                order.setName(cell != null ? cell.toString() : null);
                            }else if (c == 2){
                                order.setPhoneNum(cell != null ? cell.toString() : null);
                            }else if (c == 3){
                                order.setOrderDate(cell != null ? cell.toString() : null);
                            }
                        }
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        order.setRecordDate(df.format(new Date()));
                        list.add(order);
                    }
                }
            }
            return list;
        }catch (Exception e){
            throw e;
        } finally{
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;
        }


    }


    public List<Order> readXls(MultipartFile file) throws Exception{
        List<Order> list = new ArrayList<Order>();
        // IO流读取文件  
        InputStream input = null;
        HSSFWorkbook wb = null;
        Order order = null;
       try{
            input = file.getInputStream();
            // 创建文档  
            wb = new HSSFWorkbook(input);
            //读取sheet(页)  
            for(int numSheet=0;numSheet<wb.getNumberOfSheets();numSheet++){
                HSSFSheet hssfSheet = wb.getSheetAt(numSheet);
                if(hssfSheet == null){
                    continue;
                }
                int totalRows = hssfSheet.getLastRowNum();
                //读取Row,从第二行开始  
                for(int rowNum = 1;rowNum <= totalRows;rowNum++){
                    HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                    if(hssfRow!=null){
                        order = new Order();
                        int totalCells = hssfRow.getLastCellNum();
                        //读取列，从第一列开始  
                        for(short c=0;c<=totalCells+1;c++){
                            HSSFCell cell = hssfRow.getCell(c);
                            if(c == 0){
                                order.setCourierNum(cell != null ? cell.toString() : null);
                            }else if(c == 1){
                                order.setName(cell != null ? cell.toString() : null);
                            }else if (c == 2){
                                order.setPhoneNum(cell != null ? cell.toString() : null);
                            }else if (c == 3){
                                order.setOrderDate(cell != null ? cell.toString() : null);
                            }

//                            rowList.add(ExcelUtils.getHValue(cell).trim());
                        }
                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        order.setRecordDate(df.format(new Date()));
                        list.add(order);
                    }
                }
            }
            return list;

        }catch (Exception e){
           throw e;
       }finally{
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
           return list;
        }

    }
}  
