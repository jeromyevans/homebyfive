parser grammar TagExpressionParser;

options {
  output=AST;                        // output is a tree
  ASTLabelType=CommonTree;           // create tree with nodes of type CommonTree
  
  // use the tokens already defined in TagExpressionLexer
  tokenVocab = TagExpressionLexer;
}

@header { package com.blueskyminds.homebyfive.business.tag.expression; }

/* 
AST Tree postfix annotations:
   no annotation	: leaf node in the tree
   ^ 				: token becomes a subexpression root (required for each operand)   
   ! 				: token is not included in the tree (assign to parenthesis)
  */

/* parser rules 

 An atom is a word or an expression grouped by parenthesis
 the three expr ruels combine recursively to match the binary and unary expressions

*/
script	: orExpr * EOF!;

/* instead of using ( OR | AND ), we use two separate rules to simplify the rewrite */
/* Match an OR operator */
orExpr : andExpr ( OR^ andExpr )*   
	;	

/* Match an AND operator */
andExpr : notExpr ( AND^ notExpr )*  
	;	

/* Match a NOT operator and/or atom.  */	
notExpr
	: (NOT^)? atom 
	;

atom
	: WORD | LEFT_PAREN! orExpr RIGHT_PAREN!  
	;	
