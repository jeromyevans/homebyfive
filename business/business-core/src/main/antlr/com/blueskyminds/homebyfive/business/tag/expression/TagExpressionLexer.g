lexer grammar TagExpressionLexer;

/* 
Splits a TagExpression into tokens of operators, words and parenthesis.
Whitespace separates the tokens but is discarded  
*/

@header { package com.blueskyminds.homebyfive.business.tag.expression; }

AND	:	('and');

OR	:	('or');

NOT	:	('not');

WORD 
	:	('a'..'z'|'A'..'Z'|'0'..'9'|'.')+;

LEFT_PAREN : '(';
RIGHT_PAREN	: ')';

// whitespace is discarded
WS : (' '|'\t'|'\n'|'\r'|'\u000c')+ { skip(); };
