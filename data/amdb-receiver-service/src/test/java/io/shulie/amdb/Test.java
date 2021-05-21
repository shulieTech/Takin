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

package io.shulie.amdb;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.ExecutorType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Test {


    private static final String TMPSTR = "|||";
    private static final String LEFT_SLASH = "/";
    private static final String SELECT = "select";
    private static final String UPDATE = "update";
    private static final String DELETE = "delete";
    private static final String WHERE = "where";
    private static final String COUNT_STAR = "count(*)";
    private static final String STAR = "*";
//    protected final Properties config;
//    protected final FileSystem fileSystem;


    private String project;
    private String version;
    private String module;
    private String reportPath;
    private String scanDate;


//    public Test(FileSystem fileSystem, Properties config) {
//        this.config = config;
//        try {
//            Method getProperties = config.getClass().getMethod("getProperties");
//            Map<String, String> data = (Map<String, String>) getProperties.invoke(config, null);
//            for (Map.Entry<String, String> entry : data.entrySet()) {
//                log.info(" key {} : {}", entry.getKey(), entry.getValue());
//            }
//        } catch (Exception e) {
//            log.error("get config properties error", e);
//        }
//        if (config != null) {
//            reportPath = (String) config.get("sonar.sql.scan.report.path");
//            scanDate = (String) config.get("sonar.sql.scan.report.date");
//            project = (String) config.get("sonar.projectName");
//            module = (String) config.get("sonar.moduleKey");
//            version = (String) config.get("sonar.projectVersion");
//        }
//
//        if (project != null) {
//            project = project.replace("org.sonarqube:", "");
//            module = module.replace("org.sonarqube:", "");
//        }
//        if (scanDate != null) {
//            scanDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        }
//        log.info("start scan::::::::::::::::::::::::::::: {}, {}, {}, {}", project, module, version, reportPath);
//        this.fileSystem = fileSystem;
//    }

    private static String getRepositoryKeyForLanguage() {
        return "";
    }


    public static void main(String args[]) {
        Test test = new Test();
        test.processXml();

//        saveReport();
    }

    private void processXml() {
        Map<String, String> mybatisMapperMap = new HashMap<>(16);
        List<File> reducedFileList = new ArrayList<>();
        List<org.apache.ibatis.session.Configuration> mybatisConfigurations = new ArrayList<>();


//        Iterable<InputFile> xmlInputFiles = fs.inputFiles(fs.predicates().hasLanguage(SqlLanguage.KEY));
//        for (InputFile xmlInputFile : xmlInputFiles) {

        org.apache.ibatis.session.Configuration mybatisConfiguration = new org.apache.ibatis.session.Configuration();
        mybatisConfiguration.setDefaultExecutorType(ExecutorType.SIMPLE);
//            String xmlFilePath = xmlInputFile.uri().getPath();
//            File xmlFile = new File(xmlFilePath);
        try {
//                XmlParser xmlParser = new XmlParser();
//                Document document = xmlParser.parse(xmlFile);
//                Element rootElement = document.getRootElement();
//                String publicIdOfDocType = "";
//                DocumentType documentType = document.getDocType();
//                if (null != documentType) {
//                    publicIdOfDocType = documentType.getPublicID();
//                    if (null == publicIdOfDocType) {
//                        publicIdOfDocType = "";
//                    }
//                }
            // handle mybatis mapper file
            File reducedXmlFile = new File("/Users/cyf/projects/surge/amdb-db-api/src/main/resources/generator/AppMapper.xml");
            String reducedXmlFilePath = reducedXmlFile.getPath();
            log.info("temp file : {}", reducedXmlFilePath);
            reducedFileList.add(reducedXmlFile);
            //MyBatisMapperXmlHandler myBatisMapperXmlHandler = new MyBatisMapperXmlHandler();
            //myBatisMapperXmlHandler.handleMapperFile(xmlFile, reducedXmlFile);
            //mybatisMapperMap.put(reducedXmlFilePath, xmlFilePath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(new FileInputStream(reducedXmlFile),
                    mybatisConfiguration, reducedXmlFilePath, mybatisConfiguration.getSqlFragments());

            //如果不同文件有相同的名称 mybatis解析时会解析出来出错
            //@see org.apache.ibatis.session.Configuration.StrictMap.put
            //mybatisConfiguration 需要分开
            xmlMapperBuilder.parse();
            mybatisConfigurations.add(mybatisConfiguration);
        } catch (IOException e) {
            log.info(e.toString());
        }
//        }


        // parse MappedStatements
        Set<MappedStatement> stmts = mybatisConfigurations.stream().flatMap(s -> s.getMappedStatements().stream()).collect(Collectors.toSet());
        //parseStatement(stmts, mybatisMapperMap);
        // clean reduced.xml
        //cleanFiles(reducedFileList);
        System.out.println("");
    }

//    public void parseStatement(Set<MappedStatement> stmts, Map<String, String> mybatisMapperMap) {
//
//        for (Object obj : stmts) {
//            if (!(obj instanceof MappedStatement)) {
//                continue;
//            }
//            MappedStatement stmt = null;
//            stmt = (MappedStatement) obj;
//            if (stmt.getSqlCommandType() == SqlCommandType.SELECT
//                    || stmt.getSqlCommandType() == SqlCommandType.UPDATE
//                    || stmt.getSqlCommandType() == SqlCommandType.DELETE) {
////                if (stmt.getSqlCommandType() == SqlCommandType.SELECT) {
//                SqlSource sqlSource = stmt.getSqlSource();
//                BoundSql boundSql = null;
//                try {
//                    boundSql = sqlSource.getBoundSql(null);
//                } catch (Exception e) {
//                    log.warn(e.getMessage());
//                }
//                if (null != boundSql) {
//                    String sql = boundSql.getSql();
//                    String stmtId = stmt.getId();
//                    if (!StringUtils.endsWith(stmtId, "!selectKey")) {
//                        sql = sql.replaceAll("\\n", "");
//                        sql = sql.replaceAll("\\s{2,}", " ");
//                        final String mapperResource = stmt.getResource();
//                        String reducedXmlFilePath = mapperResource;
//                        if (reducedXmlFilePath.contains("[")) {
//                            reducedXmlFilePath = mapperResource.substring(mapperResource.indexOf('[') + 1,
//                                    mapperResource.indexOf(']'));
//                        }
//
//                        // windows environment
//                        if (!reducedXmlFilePath.startsWith(LEFT_SLASH)) {
//                            reducedXmlFilePath = LEFT_SLASH + reducedXmlFilePath.replace("\\", LEFT_SLASH);
//                        }
//                        log.debug("reducedMapperFilePath: " + reducedXmlFilePath);
//
//                        final String sourceMapperFilePath = mybatisMapperMap.get(reducedXmlFilePath);
//
//                        // get lineNumber by mapper file and keyWord
//                        final String[] stmtIdSplit = stmtId.split("\\.");
//                        final String stmtIdTail = stmtIdSplit[stmtIdSplit.length - 1];
//                        final String sqlCmdType = stmt.getSqlCommandType().toString().toLowerCase();
//                        log.debug("sourceMapperFilePath: " + sourceMapperFilePath + " stmtIdTail:  "
//                                + stmtIdTail + " sqlCmdType: " + sqlCmdType);
//                        final int lineNumber = getLineNumber(sourceMapperFilePath, stmtIdTail, sqlCmdType);
//                        // match Rule And Save Issue
//                        matchRuleAndSaveIssue(stmt.getSqlCommandType(), sql, sourceMapperFilePath, stmtId, lineNumber);
//                    }
//                }
//            }
//        }
//    }
//
//    private void matchRuleAndSaveIssue(SqlCommandType sqlCommandType, String sql, String sourceMapperFilePath, String stmtId, int line) {
//        sql = sql.toLowerCase();
//        String errorMessage = "";
//        String ruleId = "";
//
////        try {
////            if (isJoinSql(sqlCommandType, sql)) {
////                errorMessage = "sql 包含 join";
////                ruleId = "SingleSQLNOTJOIN";
////            }else if (isExistsSql(sqlCommandType,sql)){
////                errorMessage = "sql 包含 exists";
////                ruleId = "SingleSQLNOTEXISTS";
////            }
////            else if (isInSql(sqlCommandType,sql)){
////                errorMessage = "sql 包含 in";
////                ruleId = "SingleSQLNOTIN";
////            }
////            else if (isSelectInUpdate(sqlCommandType,sql)){
////                errorMessage = "sql select/update/insert 中包含 select";
////                ruleId = "SingleSQLNOTSELECTINUPDATE";
////            }
//
////        } catch (SqlScanException e) {
////            errorMessage = "sql 匹配异常:" + e.getMessage();
////            ruleId = "SingleSQLError";
////        }
//        ruleId = "SQL";
//
//        if (!"".equals(ruleId)) {
//            log.info("ruleId=" + ruleId + " errorMessage=" + errorMessage + " filePath=" + sourceMapperFilePath + " stmtId="
//                    + stmtId);
//            ErrorDataFromLinter mybatisError = new ErrorDataFromLinter(ruleId, errorMessage, sourceMapperFilePath,
//                    stmtId, line, sql);
//            errorDataFromLinterList.add(mybatisError);
//            getResourceAndSaveIssue(mybatisError);
//        }
//    }
//
//    private boolean isSelectInUpdate(SqlCommandType sqlCommandType, String sql) throws SqlScanException {
//        try {
//
//            if (sqlCommandType != SqlCommandType.SELECT) {
//                return sql.contains(" select ") || sql.contains("(select ");
//            }
//
//            return false;
//        } catch (Exception e) {
//            throw new SqlScanException(e);
//        }
//    }
//
//    private boolean isInSql(SqlCommandType sqlCommandType, String sql) throws SqlScanException {
//        try {
//            return sql.contains(" in ");
//        } catch (Exception e) {
//            throw new SqlScanException(e);
//        }
//    }
//
//    private boolean isExistsSql(SqlCommandType sqlCommandType, String sql) throws SqlScanException {
//        try {
//            return sql.contains(" exists ");
//        } catch (Exception e) {
//            throw new SqlScanException(e);
//        }
//    }
//
//    private boolean isJoinSql(SqlCommandType sqlCommandType, String sql) throws SqlScanException {
//        try {
//            return sql.contains(" join ");
////            if (sql.contains(" join ")) {
////                return true;
////            }
////                if (sql.contains(" from ")) {
////                    String fromSql = sql.substring(sql.indexOf(" from ") + 6);
////                    if (fromSql.contains(" where ")) {
////                        return fromSql.substring(0, fromSql.indexOf(" where ")).contains(",");
////                    }
////                }
////                sql = sql.replaceFirst("select ", "");
////                return sql.contains(" select ") || sql.contains("(select ");
//
//        } catch (Exception e) {
//            throw new SqlScanException(e);
//        }
//    }
//
//
//    private void getResourceAndSaveIssue(final ErrorDataFromLinter error) {
//        log.debug(error.toString());
//
//
//        final FileSystem fs = context.fileSystem();
//        final InputFile inputFile = fs.inputFile(fs.predicates().hasAbsolutePath(error.getFilePath()));
//        log.debug("inputFile null ? " + (inputFile == null));
//
//        if (inputFile != null) {
//            saveIssue(inputFile, error.getLine(), error.getType(), error.getDescription() + "/" + error.getStmtId() + "/" + error.getSql());
//        } else {
//            log.error("Not able to find a InputFile with " + error.getFilePath());
//        }
//    }
//
//    private void saveIssue(final InputFile inputFile, int line, final String externalRuleKey, final String message) {
//        RuleKey ruleKey = RuleKey.of(getRepositoryKeyForLanguage(), externalRuleKey);
//
//        NewIssue newIssue = context.newIssue().forRule(ruleKey).gap(2.0);
//
//        NewIssueLocation primaryLocation = newIssue.newLocation().on(inputFile).message(message);
//        if (line > 0) {
//            primaryLocation.at(inputFile.selectLine(line));
//        }
//        newIssue.at(primaryLocation);
//        newIssue.save();
//        log.info(" issue save : {}", newIssue);
//    }
//
//    public void cleanFiles(List<File> files) {
//        for (File file : files) {
//            if (file.exists() && file.isFile()) {
//                try {
//                    Files.delete(Paths.get(new URI("file:///" + file.getAbsolutePath().replace("\\", LEFT_SLASH))));
//                } catch (IOException | URISyntaxException e) {
//                    log.warn(e.toString());
//                }
//            }
//        }
//    }
//
//    private int getLineNumber(final String filePath, final String stmtIdTail, final String sqlCmdType) {
//        return IOUtils.getLineNumber(filePath, stmtIdTail, sqlCmdType);
//    }
    @org.junit.Test
    public void test(){
//        List<String> ipList = Arrays.asList("1","2");
//        System.out.println("'" + StringUtils.join( ipList,"','") + "'");
        System.out.println(new Date("Tue Nov 10 00:00:00 CST 2020"));
        System.out.println(new Date("2020-11-10 00:00:00"));
    }
}
