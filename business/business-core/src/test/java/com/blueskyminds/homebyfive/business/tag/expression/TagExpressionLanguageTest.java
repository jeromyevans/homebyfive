package com.blueskyminds.homebyfive.business.tag.expression;

import junit.framework.TestCase;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.List;

import com.blueskyminds.homebyfive.business.tag.TagSet;

/**
 * Date Started: 18/03/2009
 */
public class TagExpressionLanguageTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(TagExpressionLanguageTest.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    private TagSet tagSet = new TagSet();

    private TagExpression assembleTree(CommonTree node) {
        String token = node.getToken().getText();

        if (node.getChildCount() == 0) {
            return new EqualsExpression(tagSet.getTag(token));
        } else {
            if (node.getChildCount() == 1) {
                if ("not".equals(token)) {
                    return new NotExpression(assembleTree((CommonTree) node.getChild(0)));
                }
            } else {
                if (node.getChildCount() == 2) {
                    if ("and".equals(token)) {
                        return new AndExpression(assembleTree((CommonTree) node.getChild(0)), assembleTree((CommonTree) node.getChild(1)));
                    } else {
                        if ("or".equals(token)) {
                            return new OrExpression(assembleTree((CommonTree) node.getChild(0)), assembleTree((CommonTree) node.getChild(1)));
                        }
                    }
                }
            }
        }
        return null;
    }

    public void testLexer() throws Exception {

        final String s = "a or (b and c)";
        InputStream is = new ByteArrayInputStream(s.getBytes("UTF-8"));
        ANTLRInputStream input = new ANTLRInputStream(is);
        TagExpressionLexer lexer = new TagExpressionLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        TagExpressionParser parser = new TagExpressionParser(tokens);
        CommonTree ast = (CommonTree) parser.script().getTree();
        assertNotNull(ast);

        TagExpression expr = assembleTree(ast);

        LOG.info(expr.asString());

//        LOG.info("Running script");
//        TagExpressionTree tree = new TagExpressionTree(new CommonTreeNodeStream(ast));
//        tree.script();
    }
}
