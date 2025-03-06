CURRENT_DATE=`date '+%Y/%m/%d'`
LESSON=$(basename $PWD)
mvn clean package -Dmaven.test.skip=true;
java -jar -Dspring.batch.job.names=deliverPackageJob ./target/tasklet-sample-1.0.jar "item=shoes" "run.date(date)=$CURRENT_DATE" "lesson=$LESSON" type=$1;
read;
