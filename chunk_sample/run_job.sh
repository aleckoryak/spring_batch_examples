CURRENT_DATE=`date '+%Y/%m/%d'`
LESSON=$(basename $PWD)
mvn clean package -Dmaven.test.skip=true;
java -jar ./target/chunk-sample-1.0.jar "run.date(date)=$CURRENT_DATE" "lesson=$LESSON";
read;
