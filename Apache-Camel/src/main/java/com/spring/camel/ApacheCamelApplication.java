package com.spring.camel;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ApacheCamelApplication extends RouteBuilder {

	public static void main(String[] args) {
		SpringApplication.run(ApacheCamelApplication.class, args);
	}

	@Override
	public void configure() throws Exception {
		System.out.println("File moving process started!");
		//moveAllFiles();
		//moveSpecificFile("a.1");
		//moveSpecificContentFile("deepak");
		//fileProcessTextToCsv();
	//	processIntoMultipleFile();
		System.out.println("File Moving process is Completed");
	}

	private void moveAllFiles() {
		from("file:F:\\SpringBoot_Learning\\Apache Camel\\a?noop=true").to("file:F:\\SpringBoot_Learning\\Apache Camel\\b");
	}

	private void moveSpecificFile(String type){
		from("file:F:\\SpringBoot_Learning\\Apache Camel\\a?noop=true").filter(
				header(Exchange.FILE_NAME).startsWith(type)).to("file:F:\\SpringBoot_Learning\\Apache Camel\\b");
	}

	private void moveSpecificContentFile(String content){
		from("file:F:\\SpringBoot_Learning\\Apache Camel\\a?noop=true").filter(
				body().contains(content)).to("file:F:\\SpringBoot_Learning\\Apache Camel\\b");
	}

	private void fileProcessTextToCsv(){
		from("file:F:\\SpringBoot_Learning\\Apache Camel\\a?noop=true").process(p->{
			String body = p.getIn().getBody(String.class);
			StringBuilder stringBuilder = new StringBuilder();
					Arrays.stream(body.split(" ")).forEach(s->{
						stringBuilder.append(s + ",");
					});
					p.getIn().setBody(stringBuilder);
				})
				.to("file:F:\\SpringBoot_Learning\\Apache Camel\\b?fileName=record.csv");
	}

	private void processIntoMultipleFile(){
		from("file:F:\\SpringBoot_Learning\\Apache Camel\\a?noop=true")
				.unmarshal().csv().split(body().tokenize(",")).choice().when(body().contains("Closed"))
				.to("file:F:\\SpringBoot_Learning\\Apache Camel\\b?fileName=Closed.csv")
				.when(body().contains("Pending"))
				.to("file:F:\\SpringBoot_Learning\\Apache Camel\\b?fileName=Pending.csv")
				.when(body().contains("Interest"))
				.to("file:F:\\SpringBoot_Learning\\Apache Camel\\b?fileName=Interest.csv")
		;
	}
}
