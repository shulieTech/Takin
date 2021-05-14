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

package io.shulie.tro.web.app.utils.xlsx;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import io.shulie.tro.web.common.vo.excel.ExcelSheetVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: mubai
 * @Date: 2021-02-24 09:43
 * @Description: 主要针对xlsx格式文件进行读写
 */

@Slf4j
public class ExcelUtils {

    /**
     * 读取Excel xlsx后缀名文件数据， 支持多sheet导入
     *
     * @param file      要读取的文件
     * @param ignoreRow 需要忽略的行数
     * @return 数据
     */
    public static Map<String, ArrayList<ArrayList<String>>> readExcelForXlsx(MultipartFile file, Integer ignoreRow) {
        Map<String, ArrayList<ArrayList<String>>> result = new HashMap<>();
        int rowSize = 0;
        BufferedInputStream in = null;
        XSSFWorkbook workbook = null;

        try {
            try {
                in = new BufferedInputStream(file.getInputStream());
                workbook = new XSSFWorkbook(in);
            } catch (IOException e) {
                log.error("应用管理配置导入--错误: {}", e.getMessage(), e);
                throw new RuntimeException("文件读取错误!");
            }

            XSSFCell cell;
            for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
                ArrayList<ArrayList<String>> lists = new ArrayList<>();
                for (int rowIndex = ignoreRow; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                    XSSFRow row = sheet.getRow(rowIndex);
                    if (null == row) {
                        continue;
                    }

                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }

                    ArrayList<String> list = new ArrayList<>();
                    int col = 0;

                    for (int colIndex = 0; colIndex <= row.getLastCellNum(); colIndex++) {
                        cell = row.getCell(colIndex);
                        String value;
                        if (cell != null) {
                            CellType cellType = cell.getCellTypeEnum();

                            switch (cellType) {
                                case NUMERIC:
                                    if (DateUtil.isCellDateFormatted(cell)) {
                                        value = String.valueOf(cell.getDateCellValue());
                                    } else {
                                        value = String.valueOf(
                                            new DecimalFormat("0").format(cell.getNumericCellValue()));
                                    }
                                    break;
                                case STRING:
                                    value = String.valueOf(cell.getStringCellValue());
                                    break;
                                case FORMULA:
                                    value = String.valueOf(cell.getCellFormula());
                                    break;
                                case BOOLEAN:
                                    value = String.valueOf(cell.getBooleanCellValue());
                                    break;
                                case ERROR:
                                    value = String.valueOf(cell.getErrorCellValue());
                                    break;
                                default:
                                    value = "";
                            }
                            list.add(value);
                        }
                    }
                    if (col == row.getRowNum()) {
                        continue;
                    }
                    if (list.size() > 0) {
                        lists.add(list);
                    }
                }

                result.put(sheet.getSheetName(), lists);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return result;
    }

    /**
     * excel 包含多sheet 进行浏览器导出
     *
     * @param response 响应
     * @param fileName 文件名称
     * @param sheetDTOList 导出数据
     * @throws Exception 异常
     */
    public static void exportExcelManySheet(HttpServletResponse response, String fileName, List<ExcelSheetVO<?>> sheetDTOList)
        throws Exception {
        ServletOutputStream out = null;
        if (CollectionUtils.isEmpty(sheetDTOList)) {
            return;
        }
        try {
            out = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
            String excelFileName = new String((fileName + "-" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .getBytes(), "UTF-8");

            for (int i = 0; i < sheetDTOList.size(); i++) {
                ExcelSheetVO excelSheetDTO = sheetDTOList.get(i);
                Sheet sheet = new Sheet(i + 1, 0, excelSheetDTO.getExcelModelClass());
                sheet.setSheetName(excelSheetDTO.getSheetName());
                writer.write(excelSheetDTO.getData(), sheet);
            }
            response.setContentType("application/octec-stream");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(excelFileName + ".xlsx", "UTF-8"));
            writer.finish();
            out.flush();

        } catch (Exception e) {
            log.error("导出excel 失败 ， {}", e.getMessage());
            throw new Exception("导出" + fileName + "列表失败！");
        } finally {
            if (out != null) {
                out.close();
            }

        }

    }

}
