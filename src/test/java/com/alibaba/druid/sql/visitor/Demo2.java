package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.util.JdbcConstants;
import junit.framework.TestCase;

import java.util.List;

/**
 * @Author : gaoyining@ecej.com
 * @Description :
 * @Date : Created in 17:27 2019/7/10
 * @Modify by :
 */
public class Demo2 extends TestCase {

    public void test_demo_0() throws Exception {
        String sql = "select * from book as a inner join stu as b on a.sutid = b.stuid where a.name = 'a' order by a.stuid ";
        // String sql = "select * from tablename limit 10";

//        String sql = "select user from emp_table";
        String dbType = JdbcConstants.MYSQL;

        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);

            String conditionSql = "a.name in ('1','2','3')";

            SQLExpr sqlExpr = SQLUtils.toSQLExpr(conditionSql, dbType);

            SQLUtils.addCondition(stmt , SQLBinaryOperator.BooleanAnd , sqlExpr, false);

            String xiugaiSql = SQLUtils.toSQLString(stmt, dbType);

            System.out.println("修改之后得sql:" + xiugaiSql);

            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);

            //获取表名称
//            System.out.println("Tables : " + visitor.getTableStat());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
        }
    }
}
