package com.blueskyminds.homebyfive.business.tag.expression;

import com.blueskyminds.homebyfive.business.tag.factory.TagFactory;
import com.google.inject.Inject;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

/**
 * Evaluates a plain text expression into a TagExpression instance
 *
 *
 * Date Started: 20/03/2009
 */
public class TagExpressionFactory {

    private TagFactory tagFactory;

    @Inject
    public TagExpressionFactory(TagFactory tagFactory) {
        this.tagFactory = tagFactory;
    }

    private TagExpression assembleTree(CommonTree node) {
        String token = node.getToken().getText();

        if (node.getChildCount() == 0) {
            return new EqualsExpression(tagFactory.create(token));
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

    /**
     * Evaluate a plain text expression into a TagExpression
     *
     * Uses the TagExpressionLexer and TagExpressionParser to tokenise the expression and parse into an
     * Abstract Syntax Tree (AST).  This factory walks the tree to create the expression.
     *
     * The TagFactory provides the Tag instances
     *
     * @param expression
     * @return
     */
    public TagExpression evaluate(String expression) throws TagExpressionException {
        InputStream is = null;
        if (StringUtils.isNotBlank(expression)) {
            try {
                is = new ByteArrayInputStream(expression.getBytes("UTF-8"));

                ANTLRInputStream input = new ANTLRInputStream(is);
                TagExpressionLexer lexer = new TagExpressionLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);

                TagExpressionParser parser = new TagExpressionParser(tokens);
                CommonTree ast = (CommonTree) parser.script().getTree();

                return assembleTree(ast);
            } catch (UnsupportedEncodingException e) {
                // this won't happen
            } catch (RecognitionException e) {
                throw new TagExpressionException(e);
            } catch (IOException e) {
                // this won't happen
            }
        }
        return null;
    }
}
