package com.ca.operations;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.ca.OperationResult;
import com.ca.operations.data.ObjectKeys;
import com.ca.operations.data.S3ObjectKeysDataProvider;
import com.ca.operations.data.SingletonFileObjectKeysDataProvider;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author jenshadlich@googlemail.com
 * @author jingyoko@gmail.com
 */
public class RandomRead extends AbstractOperation {

    private static final Logger LOG = LoggerFactory.getLogger(RandomRead.class);

    private final AmazonS3 s3Client;
    private final String bucket;
    private final int n;
    private final String keyFileName;

    public RandomRead(AmazonS3 s3Client, String bucket, int n, String keyFileName) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.n = n;
        this.keyFileName = keyFileName;
    }

    @Override
    public OperationResult call() {
        LOG.info("Random read: n={}", n);

        final ObjectKeys objectKeys;
        if (keyFileName == null) {
            objectKeys = new S3ObjectKeysDataProvider(s3Client, bucket).get();
        } else {
            objectKeys = new SingletonFileObjectKeysDataProvider(keyFileName).get();
        }
        StopWatch stopWatch = new StopWatch();

        for (int i = 0; i < n; i++) {
            final String randomKey = objectKeys.getRandom();
            LOG.debug("Read object: {}", randomKey);

            stopWatch.reset();
            stopWatch.start();

            S3Object object = s3Client.getObject(bucket, randomKey);
            try {
                object.close();
            } catch (IOException e) {
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
