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

package io.shulie.tro.web.app.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pamirs.tro.entity.domain.annotation.ExcelTag;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: vernon
 * @Date: 2019/10/31 20:37
 * @Description:
 */
public class ExcelUtil<T> {
    private static void setResponse(HttpServletResponse response, String fileName) {

        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
                response.setContentType("application/octet-stream;charset=ISO8859-1");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xls");
                response.addHeader("Pargam", "no-cache");
                response.addHeader("Cache-Control", "no-cache");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Á
     * 读取
     *
     * @param path
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, List<T>> readExcel(String path, Class<T> clazz) {
        Map<String, List<List<String>>> map = readExcel(path);
        // 转换
        return map.keySet().stream().collect(Collectors.toMap((key) -> key, key -> {
            List<T> listBean = Lists.newArrayList();
            Field[] fields = clazz.getDeclaredFields();
            T uBean = null;
            for (int i = 1; i < map.get(key).size(); i++) {// i=1是因为第一行不要
                try {
                    uBean = (T)clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                List<String> listStr = map.get(key).get(i);
                for (int j = 0; j < listStr.size(); j++) {
                    if (j >= fields.length) {
                        break;
                    }
                    Field field = fields[j];
                    String datastring = listStr.get(j);
                    field.setAccessible(true);
                    setData(uBean, field, datastring);
                }
                listBean.add(uBean);
            }
            return listBean;
        }, (k1, k2) -> k1));

    }

    private static <T> void setData(T uBean, Field field, String dataString) {
        if (dataString.length() > 0) {
            try {
                Class<?> type = field.getType();
                if (type == String.class) {
                    field.set(uBean, dataString);
                } else if (type == Integer.class || type == int.class) {
                    field.set(uBean, Integer.parseInt(dataString));
                } else if (type == Double.class || type == double.class) {
                    field.set(uBean, Double.parseDouble(dataString));
                } else if (type == Float.class || type == float.class) {
                    field.set(uBean, Float.parseFloat(dataString));
                } else if (type == Long.class || type == long.class) {
                    field.set(uBean, Long.parseLong(dataString));
                } else if (type == Boolean.class || type == boolean.class) {
                    field.set(uBean, Boolean.parseBoolean(dataString));
                } else if (type == Short.class || type == short.class) {
                    field.set(uBean, Short.parseShort(dataString));
                } else if (type == Byte.class || type == byte.class) {
                    field.set(uBean, Byte.parseByte(dataString));
                } else if (type == Character.class || type == char.class) {
                    field.set(uBean, dataString.charAt(0));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, List<List<String>>> readExcel(String path) {
        Map<String, List<List<String>>> map = Maps.newHashMap();
        try {
            Workbook workBook = null;
            try {
                workBook = new XSSFWorkbook(path);
            } catch (Exception ex) {
                workBook = new HSSFWorkbook(new FileInputStream(path));
            }
            for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workBook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                /** 得到Excel的行数 */
                int totalRows = sheet.getPhysicalNumberOfRows();
                /** 得到Excel的列数 */
                int totalCells = 0;
                if (totalRows >= 1 && sheet.getRow(0) != null) {
                    totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
                }
                List<List<String>> dataLst = Lists.newArrayList();
                /** 循环Excel的行 */
                for (int r = 0; r < totalRows; r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    List<String> rowLst = Lists.newArrayList();
                    /** 循环Excel的列 */
                    for (int c = 0; c < totalCells; c++) {
                        Cell cell = row.getCell(c);
                        String cellValue = "";
                        if (null != cell) {
                            HSSFDataFormatter hSSFDataFormatter = new HSSFDataFormatter();
                            cellValue = hSSFDataFormatter.formatCellValue(cell);
                        }
                        rowLst.add(cellValue);
                    }
                    /** 保存第r行的第c列 */
                    dataLst.add(rowLst);
                }
                map.put(sheet.getSheetName(), dataLst);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Map<String, List<List<String>>> readExcel(InputStream in) {
        Map<String, List<List<String>>> map = Maps.newHashMap();
        try {
            Workbook workBook = new HSSFWorkbook(in);
            for (int numSheet = 0; numSheet < workBook.getNumberOfSheets(); numSheet++) {
                Sheet sheet = workBook.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                /** 得到Excel的行数 */
                int totalRows = sheet.getPhysicalNumberOfRows();
                /** 得到Excel的列数 */
                int totalCells = 0;
                if (totalRows >= 1 && sheet.getRow(0) != null) {
                    totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
                }
                List<List<String>> dataLst = Lists.newArrayList();
                /** 循环Excel的行 */
                for (int r = 0; r < totalRows; r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) {
                        continue;
                    }
                    List<String> rowLst = Lists.newArrayList();
                    /** 循环Excel的列 */
                    for (int c = 0; c < totalCells; c++) {
                        Cell cell = row.getCell(c);
                        String cellValue = "";
                        if (null != cell) {
                            HSSFDataFormatter hSSFDataFormatter = new HSSFDataFormatter();
                            cellValue = hSSFDataFormatter.formatCellValue(cell);
                        }
                        rowLst.add(cellValue);
                    }
                    /** 保存第r行的第c列 */
                    dataLst.add(rowLst);
                }
                map.put(sheet.getSheetName(), dataLst);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * @param response
     * @param data     数据
     * @param titles   excel标题
     * @param fileName excel文件名字
     */
    public void export(HttpServletResponse response, List<T> data, List<String> titles,
        String fileName) throws Exception {

        if (null == data || data.size() < 1) {
            throw new Exception("导出数据不能为空");
        }

        HSSFWorkbook wb = new HSSFWorkbook();
        //创建工作表
        HSSFSheet sheet = wb.createSheet();
        titles = titles(data);
        HSSFCellStyle style = init(wb);
        //插入表头数据
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = null;
        for (int i = 0; i < titles.size(); i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(titles.get(i));

        }
        //创建行
        for (int i = 0; i < data.size(); i++) {
            HSSFRow irow = sheet.createRow(i + 1);
            HSSFCell icell = null;
            List<String> values = values(data.get(i));
            for (int j = 0; j < values.size(); j++) {
                icell = irow.createCell(j);
                icell.setCellStyle(style);
                icell.setCellValue(values.get(j));
            }
        }
        //输出客户端
        try {
            setResponse(response, fileName);
            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> titles(List<T> data) {

        List<String> result = new ArrayList<>();
        try {
            if (CollectionUtils.isEmpty(data)) {
                return null;
            }
            T t = data.get(0);
            Field[] fields = t.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ExcelTag.class)) {
                    ExcelTag tag = field.getAnnotation(ExcelTag.class);
                    result.add(tag.name());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<String> values(T t) {
        List<String> values = new ArrayList<>();
        Field[] fields = t.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ExcelTag.class)) {
                    ExcelTag tag = field.getAnnotation(ExcelTag.class);
                    Object tagValue = tag.type();
                    if (tagValue == String.class) {
                        String originValue = String.valueOf(field.get(t));
                        if ("null".equalsIgnoreCase(originValue)) {
                            originValue = "";
                        }
                        boolean convert = tag.convert();
                        if (convert) {
                            String rule = tag.rule();
                            String[] ruleSplit = rule.split(",");
                            for (String split : Arrays.asList(ruleSplit)) {
                                String[] splits = split.split("=");
                                if (StringUtils.equalsIgnoreCase(splits[0], originValue)) {
                                    originValue = splits[1];
                                }
                                break;
                            }
                        }
                        values.add(originValue);
                        continue;
                    } else if (((Class)tagValue).getName().equals("java.util.List")) {
                        List tagValueList = (List)field.get(t);
                        JSONArray jsonArray = new JSONArray();
                        for (int i = 0; i < tagValueList.size(); i++) {
                            Object obj = tagValueList.get(i);
                            Field[] declaredFields = obj.getClass().getDeclaredFields();
                            JSONObject jsonObject = new JSONObject();
                            for (Field field1 : declaredFields) {
                                field1.setAccessible(true);
                                ExcelTag annotation = field1.getAnnotation(ExcelTag.class);
                                if (null == annotation) {
                                    continue;
                                }
                                String s = String.valueOf(field1.get(obj));
                                jsonObject.put(annotation.name(), s);
                            }
                            jsonArray.add(i, jsonObject);
                        }
                        values.add(jsonArray.toJSONString());
                    }
                    if (tagValue == java.util.Date.class) {
                        try {
                            values.add(formate((java.util.Date)field.get(t)));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return values;
    }

    private String formate(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        return dateFormat.format(date);
    }

    //初始化样式
    private HSSFCellStyle init(HSSFWorkbook wb) {
        //  创建字体
        HSSFFont font1 = wb.createFont();
        HSSFFont font2 = wb.createFont();
        font1.setFontHeightInPoints((short)14);
        font1.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        font2.setFontHeightInPoints((short)12);
        font2.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //  创建单元格样式
        HSSFCellStyle css1 = wb.createCellStyle();
        HSSFCellStyle css2 = wb.createCellStyle();
        HSSFDataFormat df = wb.createDataFormat();
        //  设置单元格字体及格式
        css1.setFont(font1);
        css1.setDataFormat(df.getFormat("#,##0.0"));
        css2.setFont(font2);

        css1.setBorderBottom(BorderStyle.THIN);
        css1.setBorderTop(BorderStyle.THIN);
        css1.setBorderRight(BorderStyle.THIN);
        css1.setBorderLeft(BorderStyle.THIN);
        return css1;
    }

    public void verify(MultipartFile[] files) throws Exception {
        if (files == null) {
            throw new Exception("上传文件为空");
        }

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            if (!(fileName.endsWith(".xls") || fileName.endsWith(".xlsx"))) {
                throw new Exception("不支持的上传文件类型");
            }
        }
    }

}
