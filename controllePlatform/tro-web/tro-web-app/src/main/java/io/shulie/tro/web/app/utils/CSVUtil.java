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

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.shulie.tro.web.app.response.user.UserCsvBean;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: fanxx
 * @Date: 2021/3/9 4:48 下午
 * @Description:
 */
public class CSVUtil {
    private static Logger log = LoggerFactory.getLogger(CSVUtil.class);

    public static void writeCsvFile(List<UserCsvBean> dataList, String finalPath) {
        File file = new File(finalPath);
        if (!file.exists()) {
            FileUtils.makeDir(file.getParentFile());
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        try {
            //Writer writer = new FileWriter(finalPath);
            Writer writer = new FileWriterWithEncoding(finalPath, "GBK");
            // 设置显示的顺序
            ColumnPositionMappingStrategy<UserCsvBean> mapper =
                new ColumnPositionMappingStrategy<>();
            mapper.setType(UserCsvBean.class);

            // 写表头
            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, '\\',
                "\n");
            String[] header = {"部门（多个部门使用/分隔）", "账号名称", "账号密码", "错误信息"};
            csvWriter.writeNext(header);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer)
                .withMappingStrategy(mapper)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withEscapechar('\\')
                .build();
            beanToCsv.write(dataList);
            csvWriter.close();
            writer.close();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("writeCsvFile filed:", e);
        }
    }
}
