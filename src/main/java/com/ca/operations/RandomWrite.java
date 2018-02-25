package com.ca.operations;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import com.ca.OperationResult;
import com.ca.operations.data.ObjectKeys;
import com.ca.operations.data.S3ObjectKeysDataProvider;
import com.ca.operations.data.SingletonFileObjectKeysDataProvider;

/**
 * @author eugenetan
 *
 */
public class RandomWrite extends AbstractOperation {

	private static final Logger LOG = LoggerFactory.getLogger(RandomWrite.class);

	private final AmazonS3 s3Client;
	private final String bucket;
	private final int n;
	private final String keyFileName;

	public RandomWrite(AmazonS3 s3Client, String bucket, int n, String keyFileName) {
		this.s3Client = s3Client;
		this.bucket = bucket;
		this.n = n;
		this.keyFileName = keyFileName;
	}

	@Override
	public OperationResult call() {
		LOG.info("Random write: n={}", n);

		final ObjectKeys objectKeys;
		if (keyFileName == null) {
			objectKeys = new S3ObjectKeysDataProvider(s3Client, bucket).get();
		} else {
			objectKeys = new SingletonFileObjectKeysDataProvider(keyFileName).get();
		}
		StopWatch stopWatch = new StopWatch();

		for (int i = 0; i < n; i++) {
			final String randomKey = objectKeys.getRandom();
			LOG.debug("write object: {}", randomKey);

			stopWatch.reset();
			stopWatch.start();

//			S3Object object = s3Client.getObject(bucket, randomKey);

			InputStream in = s3Client.getObject(bucket, "test_file").getObjectContent();

			try {
				Files.copy(in, new File("https://s3.amazonaws.com/patrick-realestate-llc/test_file").toPath());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			byte[] buf = new byte[1024];
			int count = 0;
			try {
				String file = "test_file";
				OutputStream out = new FileOutputStream(file);
				while ((count = in.read(buf)) != -1) {
					if (Thread.interrupted()) {
						throw new InterruptedException();
					}
					out.write(buf, 0, count);
				}
				out.close();
				in.close();
//				object.close();
			} catch (IOException | InterruptedException e) {
				LOG.warn("An exception occurred while trying to close object with key: {}", randomKey);
			}

			stopWatch.stop();

			LOG.debug("Time = {} ms", stopWatch.getTime());
			getStats().addValue(stopWatch.getTime());

			if (i > 0 && i % 1000 == 0) {
				LOG.info("Progress: {} of {}", i, n);
			}
		}

		return new OperationResult(getStats());
	}

}
