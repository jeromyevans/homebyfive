mysqldump --compatible=ansi --skip-extended-insert --skip-add-drop-table --no-create-info --skip-add-locks --skip-quote-names --complete-insert landmine region -u root -p > REGION.sql
mysqldump --compatible=ansi --skip-extended-insert --skip-add-drop-table --no-create-info --skip-add-locks --skip-quote-names --complete-insert landmine regionalias -u root -p > REGIONALIAS.sql
mysqldump --compatible=ansi --skip-extended-insert --skip-add-drop-table --no-create-info --skip-add-locks --skip-quote-names --complete-insert landmine regionhierarchy -u root -p > REGIONHIERARCHY.sql
mysqldump --compatible=ansi --skip-extended-insert --skip-add-drop-table --no-create-info --skip-add-locks --skip-quote-names --complete-insert landmine regionindex -u root -p > REGIONINDEX.sql
