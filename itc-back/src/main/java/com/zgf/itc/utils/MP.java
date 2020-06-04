package com.zgf.itc.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * @author ZGF
 */
public class MP {
    private static String author = "ZGF";
    private static String outputDir = "G:\\javaProject\\idea\\IdeaProjects\\it-community\\src\\main\\java";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String url = "jdbc:mysql://192.168.56.110:3306/it-community?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF-8";
    private static String username = "root";
    private static String password = "123456";
    private static String tablePrefix = "ic_";
    private static String[] tables = {"ic_report"};
    private static String parentPackage = "com.zgf.itc";
    private static String dao = "mapper";
    private static String entity = "entity";
    private static String service = "service";
    private static String controller = "controller";
    private static String mapperxml = "mapper";

    @Test
    public void testGenerator(){
        //全局配置
        GlobalConfig config = new GlobalConfig();
        config.setAuthor(author)
                .setOutputDir(outputDir)
                .setFileOverride(true)
                .setIdType(IdType.AUTO)
                .setServiceName("%sService")
                .setBaseResultMap(true)
                .setBaseColumnList(true);
        //数据源配置
        DataSourceConfig dsConfig = new DataSourceConfig();
        dsConfig.setDbType(DbType.MYSQL)
                .setDriverName(driver)
                .setUrl(url)
                .setUsername(username)
                .setPassword(password);

        //策略配置
        StrategyConfig stConfig = new StrategyConfig();
        stConfig.setCapitalMode(true)   //全局大写命名
                .setNaming(NamingStrategy.underline_to_camel)
                .setTablePrefix(tablePrefix)
                .setInclude(tables);

        //包名策略配置
        PackageConfig pkConfig = new PackageConfig();
        pkConfig.setParent(parentPackage)
                .setMapper(dao)
                .setService(service)
                .setController(controller)
                .setEntity(entity)
                .setXml(mapperxml);

        //整合
        AutoGenerator ag = new AutoGenerator();
        ag.setGlobalConfig(config)
                .setDataSource(dsConfig)
                .setStrategy(stConfig)
                .setPackageInfo(pkConfig);
        ag.execute();

    }

}
