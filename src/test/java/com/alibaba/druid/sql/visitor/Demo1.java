package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;
import junit.framework.TestCase;

import java.util.List;

/**
 * @Author : gaoyining@ecej.com
 * @Description :
 * @Date : Created in 17:27 2019/7/10
 * @Modify by :
 */
public class Demo1 extends TestCase {

    public void test_demo_0() throws Exception {
        String sql = "select * from book as a inner join stu as b on a.sutid = b.stuid";
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
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
            visitor.addColumn("book" , "bookName");

            //获取表名称
//            System.out.println("Tables : " + visitor.getTableStat());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
        }

        // 将AST通过visitor输出
        StringBuilder out = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);

        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
            out.append(";");
        }

        System.out.println(out.toString());
    }
}
