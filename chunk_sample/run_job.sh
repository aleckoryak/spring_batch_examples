CURRENT_DATE=`date '+%Y/%m/%d'`
LESSON=$(basename $PWD)
mvn clean package -Dmaven.test.skip=true;
java -Dspring.batch.job.names=jobDBOneThreadTransformRetryToFile -jar ./target/chunk-sample-1.0.jar "test=1.12" "run.date(date)=$CURRENT_DATE" "lesson=$LESSON";
read;
