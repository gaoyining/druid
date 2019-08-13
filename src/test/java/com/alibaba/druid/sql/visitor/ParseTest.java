package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;

import java.util.List;

/**
 * @Author : gaoyining@ecej.com
 * @Description :
 * @Date : Created in 18:48 2019/7/22
 * @Modify by :
 */
public class ParseTest {

    public ParseTest() {
        // TODO Auto-generated constructor stub
    }

    public static void  main(String[] args){

        String sql ="select * from book as a inner join stu as b on a.sutid = b.stuid";

        StringBuffer select = new StringBuffer();
        StringBuffer from = new StringBuffer();
        StringBuffer where = new StringBuffer();

        // parser得到AST
        SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcUtils.MYSQL);
        List<SQLStatement> stmtList = parser.parseStatementList(); //

        // 将AST通过visitor输出
        SQLASTOutputVisitor visitor = SQLUtils.createFormatOutputVisitor(from, stmtList, JdbcUtils.MYSQL);
        SQLASTOutputVisitor whereVisitor = SQLUtils.createFormatOutputVisitor(where, stmtList, JdbcUtils.MYSQL);

        for (SQLStatement stmt : stmtList) {
//       stmt.accept(visitor);
            if(stmt instanceof SQLSelectStatement){
                SQLSelectStatement sstmt = (SQLSelectStatement)stmt;
                SQLSelect sqlselect = sstmt.getSelect();
                SQLSelectQueryBlock query = (SQLSelectQueryBlock)sqlselect.getQuery();

                query.getFrom().accept(visitor);
//                query.getWhere().accept(whereVisitor);
            }
        }

        System.out.println(from.toString());
//        System.out.println(select);
//        System.out.println(where);
    }
}
