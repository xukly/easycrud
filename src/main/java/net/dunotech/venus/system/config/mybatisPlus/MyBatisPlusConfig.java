package net.dunotech.venus.system.config.mybatisPlus;

import com.baomidou.mybatisplus.incrementer.H2KeyGenerator;
import com.baomidou.mybatisplus.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.plugins.SqlExplainInterceptor;
import com.baomidou.mybatisplus.plugins.parser.ISqlParser;
import com.baomidou.mybatisplus.plugins.parser.tenant.TenantHandler;
import com.baomidou.mybatisplus.plugins.parser.tenant.TenantSqlParser;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis-plus配置
 * @author mybatis-plus
 * @date 2018/07/22
 */
@Configuration
@MapperScan(value = "net.dunotech.venus.*.mapper*")
public class MyBatisPlusConfig {

    /**
     * mybatis-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * mybatis-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
        paginationInterceptor.setLocalPage(true);
        /*
         * 【测试多租户】 SQL 解析处理拦截器<br>
         * 这里固定写成住户 1 实际情况你可以从cookie读取，因此数据看不到 【 麻花藤 】 这条记录（ 注意观察 SQL ）<br>
         */
        List<ISqlParser> sqlParserList = new ArrayList<>();
        TenantSqlParser tenantSqlParser = new TenantSqlParser();
        tenantSqlParser.setTenantHandler(new TenantHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(1L);
            }

            @Override
            public String getTenantIdColumn() {
                return "";
            }

            @Override
            public boolean doTableFilter(String tableName) {
                return false;
            }
        });

        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    @Bean
    public MybatisSqlInterceptor mybatisSqlInterceptor(){
        return  new MybatisSqlInterceptor();
    }

    /**
     * 注入主键生成器
     */
    @Bean
    public IKeyGenerator keyGenerator(){
        return new H2KeyGenerator();
    }

    /**
     * 注入sql注入器
     */
    @Bean
    public ISqlInjector sqlInjector(){
        return new LogicSqlInjector();
    }
}
