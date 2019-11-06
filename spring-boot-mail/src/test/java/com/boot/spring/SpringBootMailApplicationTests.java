package com.boot.spring;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CannedAccessControlList;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootMailApplicationTests {

    private StorageClient storageClient;

    private TrackerServer trackerServer;
    
    private static final String PROPERTIES_NAME = "fastdfs-client.properties";
	
	@Test
	public void contextLoads() {
		// 1 初始化用户身份信息（secretId, secretKey）。
		String secretId = "AKIDpQkojNujQqzsV17LEqCJAkGXaT5RsO0S";
		String secretKey = "PeK6RaaS7aolpqiQyPccNj18DpESh2B8";
		COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
		// 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
		// clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
		Region region = new Region("ap-chengdu");
		ClientConfig clientConfig = new ClientConfig(region);
		// 3 生成 cos 客户端。
		COSClient cosClient = new COSClient(cred, clientConfig);
		String bucket = "picture-1256881505"; //存储桶名称，格式：BucketName-APPID
		File localFile = new File("C:\\Users\\yaoqiang\\Desktop\\123.jpg");
		try{
		    //Bucket bucketResult = cosClient.createBucket(createBucketRequest);
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, "test", localFile);
			PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
			System.out.println();
		} catch (CosServiceException serverException) {
		    serverException.printStackTrace();
		} catch (CosClientException clientException) {
		    clientException.printStackTrace();
		}
	}
	

	
	@Test
	public void run() throws IOException, MyException {
		ClientGlobal.initByProperties(PROPERTIES_NAME);
		TrackerClient tracker = new TrackerClient();
        trackerServer = tracker.getConnection();
        StorageServer storageServer = null;
        storageClient = new StorageClient(trackerServer, storageServer);
		
		NameValuePair[] metaList = new NameValuePair[1];
        String local_filename = "123.jpg";
        metaList[0] = new NameValuePair("fileName", local_filename);
        File file = new File("C:\\Users\\yaoqiang\\Desktop\\123.jpg");
        InputStream inputStream = new FileInputStream(file);
        int length = inputStream.available();
        byte[] bytes = new byte[length];
        inputStream.read(bytes);
        String[] result = storageClient.upload_file(bytes, "jpg", metaList);
        System.out.println(Arrays.asList(result));
        Assert.assertEquals(2, result.length);
	}

}
