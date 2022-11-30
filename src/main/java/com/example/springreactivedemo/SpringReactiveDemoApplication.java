package com.example.springreactivedemo;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.example.springreactivedemo.domain.AwsConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PreDestroy;
import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class SpringReactiveDemoApplication {

	@Value("${SERVICE_PATH:}")
	private String servicePath;
	@Value("${PVC_PATH:}")
	private String pvcPath;

	AwsConfig awsConfig = new AwsConfig();
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");

	public static void main(String[] args) {
		SpringApplication.run(SpringReactiveDemoApplication.class, args);
//		if(args == null || args.length<=0){
//			SpringApplication.run(SpringReactiveDemoApplication.class, args);
//			System.out.println("System is running");
//			return;
//		}
//
//		String option = args[0];
//		if(option.equals("halt")){
//			System.out.println("*** System halt");
//			Runtime.getRuntime().halt(0);
//		}else if(option.equals("exit")){
//			System.out.println("*** System exit");
//			Runtime.getRuntime().exit(0);
//		}
	}

	@PreDestroy
	public void onExit() {
		System.out.println("### STOPPING ### ");
		try {
			uploadFile();
			//printFileSizeAndUpload("/Users/ab732698/Downloads/systemtests/demodest_local/jacoco.exec");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			System.out.println(""+ e);
		}
		System.out.println("### STOPPED FROM THE LIFECYCLE ###");
	}

	void uploadFile(){
		String filePath = pvcPath+"/logs/spring-reactive-demo/jacoco.exec";
		System.out.println("File path "+filePath);
//			System.out.println("Service path from System env "+System.getenv("SERVICE_PATH") );
		printFileSizeAndUpload(filePath);
	}

	void printFileSizeAndUpload(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			long bytes = file.length();
			long kilobytes = (bytes / 1024);

//			System.out.println(String.format("%,d bytes", bytes));
			System.out.println(String.format("%,d kilobytes", kilobytes));

			//upload(file);

		} else {
			System.out.println("File does not exist!");
		}

	}

	String getUploadPath(){
		Date date = new Date();
		//String tsText = InetAddress.getLocalHost().getHostName();
		return dateFormat.format(date)+"/demoservice/jacoco-" + dateTimeFormat.format(date) + ".exec";
	}

	private void upload(File file){
		try {
			if(StringUtils.isEmpty(awsConfig.getBucketName())){
				System.out.println("Invalid details to upload");
				return;
			}

			AWSCredentials credentials = new BasicAWSCredentials(awsConfig.getAccessKey(), awsConfig.getSecretKey());

			AmazonS3 s3client = AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.EU_WEST_1)
				.build();
			String destFileName = getUploadPath();
			System.out.println("bucket name " + awsConfig.getBucketName() + " dest file name "+ destFileName);

			//PutObjectRequest putObjectRequest = new PutObjectRequest(awsConfig.getBucketName(), destFileName, file);
			s3client.putObject(awsConfig.getBucketName(), destFileName, file);

			System.out.println("File uploaded " );
		}catch (Exception e){
			System.out.println("Failed to upload "+e.getMessage());
		}
	}
}
