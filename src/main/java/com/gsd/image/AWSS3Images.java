package com.gsd.image;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

public class AWSS3Images {

	private String clientRegion = "us-west-2";
    private String bucketName = "images4app";
    private AmazonS3 s3Client;
    
	public void writeImage(File file) {

		connectWithAWS();
        System.out.println("Received at AWS Class & sending to AWS S3");
        String datee = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        
        TransferManager tm = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        tm.upload(new PutObjectRequest(bucketName, datee+"_"+file.getName(), file).withCannedAcl(CannedAccessControlList.PublicRead));
        System.out.println("File Uploaded");

//        s3Client.putObject(new PutObjectRequest(bucketName, datee+"_"+file.getName(), file));
//        s3Client.putObject(new PutObjectRequest(bucketName, datee, createSampleFile()));

	}
	
	public ArrayList<ImageData> readImage(){
		ArrayList<ImageData> imgListPath = new ArrayList<ImageData>();
		connectWithAWS();
		System.out.println("Accessing PATH from AWS S3");
		ObjectListing oListPath = s3Client.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for(S3ObjectSummary oListSumm : oListPath.getObjectSummaries()) {
			System.out.println("File Summary : "+oListSumm);
			imgListPath.add(new ImageData("https://images4app.s3-us-west-2.amazonaws.com/"+oListSumm.getKey()));
		}
		System.out.println("All images path: "+imgListPath.toString());
		return imgListPath;
	}
	
	private void connectWithAWS() {
        try {
            s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(clientRegion)
                    .build();
            
            System.out.println();
            System.out.println("===========================================");
            System.out.println("Getting Started with Amazon S3");
            System.out.println("===========================================\n");
            
            if (!s3Client.doesBucketExistV2(bucketName)) {
                // Because the CreateBucketRequest object doesn't specify a region, the
                // bucket is created in the region specified in the client.
            	System.out.println("Creating bucket " + bucketName + "\n");
                s3Client.createBucket(new CreateBucketRequest(bucketName));
                
                // Verify that the bucket was created by retrieving it and checking its location.
                String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
                System.out.println("Bucket location: " + bucketLocation);
                
            }
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process 
            // it and returned an error response.
            e.printStackTrace();
        }
        catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }
    }
}