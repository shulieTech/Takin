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

package io.shulie.tro.cloud.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * excel文件读取操作工具，基于poi之上
 */
public class ExcelReader implements AutoCloseable {

    private static final String EMPTY_VALUE = "";

    private int startIndex;
    private int maxCellIndex;
    private Workbook workbook;
    private Sheet sheet;
    private InputStream inputStream;

    public ExcelReader(InputStream is) throws Exception {
        inputStream = is;
        workbook = WorkbookFactory.create(inputStream);
        sheet = workbook.getSheetAt(0);
    }

    public ExcelReader(File file) throws Exception {
        inputStream = new FileInputStream(file);
        workbook = WorkbookFactory.create(inputStream);
        sheet = workbook.getSheetAt(0);
    }

    public void close() throws Exception {
        if (workbook != null) {
            workbook.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    /**
     * Excel读取 操作
     */
    public List<Object[]> readExcel() throws Exception {
        return readExcel(new ArrayList<Object[]>(sheet.getPhysicalNumberOfRows() - startIndex));
    }

    /**
     * Excel读取 操作
     */
    public List<Object[]> readExcel(List<Object[]> collection) throws Exception {
        /** 得到Excel的行数 */
        int totalRows = sheet.getLastRowNum() + 1;
        /** 循环Excel的行 */
        for (int rIndex = startIndex; rIndex < totalRows; rIndex++) {
            Row row = sheet.getRow(rIndex);
            if (row == null) {
                continue;
            }
            /** 得到该行的列数 */
            Object[] objects = new Object[maxCellIndex];
            /** 循环Excel的列 */
            for (int cIndex = 0; cIndex < objects.length; cIndex++) {
                Cell cell = row.getCell(cIndex);
                if (null != cell) {
                    switch (cell.getCellTypeEnum()) {
                        case NUMERIC: // 数字
                            if (DateUtil.isCellDateFormatted(cell)) {
                                objects[cIndex] = cell.getDateCellValue();
                            } else {
                                //cell.setCellType(CellType.STRING);
                                objects[cIndex] = cell.getNumericCellValue();
                            }
                            break;
                        case STRING: // 字符串
                            objects[cIndex] = cell.getStringCellValue();
                            break;
                        case BOOLEAN: // Boolean
                            objects[cIndex] = cell.getBooleanCellValue();
                            break;
                        case FORMULA: // 公式
                            objects[cIndex] = cell.getCellFormula();
                            break;
                        default:
                            objects[cIndex] = EMPTY_VALUE;
                            break;
                    }
                }
            }
            if (validate(objects)) {
                collection.add(objects);
            }
        }
        return collection;
    }

    private boolean validate(Object[] objects) {
        if (objects == null || objects.length <= 0) {
            return false;
        }

        for (Object object : objects) {
            if (object != null && StringUtils.isNotBlank(object.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * getter method for startIndex
     *
     * @return the startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * setter method for startIndex
     *
     * @param startIndex the startIndex to set
     */
    public void setIndex(int startIndex, int maxCellIndex) {
        this.startIndex = startIndex;
        this.maxCellIndex = maxCellIndex;
    }
}
