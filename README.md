# Getting Started

![diagram](img.png)


### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3/maven-plugin/build-image.html)
* [Spring Batch](https://docs.spring.io/spring-boot/3.4.3/how-to/batch.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql/)

### Main components
+ Job
+ Flow
+ Steps

### Steps
+ Tasklet
+ Chunk

## Tasklet
### Flow
+ sequential 
```java
start(step1)
   .next(step2)
   .next(step3)
   .end()
```
+ conditional 
```java 
    start(step1).on("FAILED").to(step2)
    .from(step1).on(*).to(step3)
    . end()
```

### Termination 
+ .end
+ .stop (rerunnable)
+ .fail (rerunnable)

### Listener
+ JobExecutionListener
+ StepExecutionListener
+ ChunkListener
+ SkipListener
+ RetryListener

### nested Jobs and Steps
```java
	@Bean
	public Step nestedBillingJobStep() {
		return this.stepBuilderFactory.get("nestedBillingJobStep").job(billingJob()).build();
	}

@Bean
public Job deliverPackageJob() {
    return this.jobBuilderFactory.get("deliverPackageJob")
            .start(packageItemStep())
            .on("*").to(deliveryFlow())
            .next(nestedBillingJobStep())
            .end()
            .build();
}
```
### Splits - execute in parallel
```java 
	public Job deliverPackageJob() {
		return this.jobBuilderFactory.get("deliverPackageJob")
				.start(packageItemStep())
				.split(new SimpleAsyncTaskExecutor())
				.add(deliveryFlow(), loyaltyFlow())
				.end()
				.build();
	}
```



## Chunks
+ Step
    + ItemReader - one item at a time, chunk by chunk (OOTB: Kafka; FlatFile; HibernateCursor (one thread);  HibernatePaging(multi thread);  JdbcCursor; JdbcPaging; JpaPaging; Mongo; StaxEvent; Json
    + ItemProcessor - (optional) one item by one in chunk, chunk by chunk
    + ItemWriter - chunk by chunk (each chunk is transactional)


04-03
04_05   chunks from file
04_05 - chunks from DB (single thread) jdbcCursorItemReader with .sql
04_06 - chunks from DB (multi thread) jdbcPageItemReader with .queryProvider and .pageSize
05_02 - FlatFileItemWriter
05_03 - JdbcBatchItemWriter (prepared statment BAD)
05_04 - JdbcBatchItemWriter named parameters GOOD (beanMapped)
05_05 - JasonFileWriter


06_02 processor - bean validator
java -jar .\target\linkedin-batch-02-02-end-0.0.1-SNAPSHOT.jar "item=rrr" "run.date(date)=2025/03/12"
