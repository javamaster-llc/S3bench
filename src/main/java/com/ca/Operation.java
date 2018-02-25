package com.ca;

/**
 * @author jenshadlich@googlemail.com
 * @author jingyoko@gmail.com
 *
 */
public enum Operation {

    CLEAR_BUCKET(false),
    CREATE_BUCKET(false),
    DELETE_BUCKET(false),
    CREATE_KEY_FILE(false),
    RANDOM_READ(true),
    RANDOM_WRITE(true),   
    RANDOM_READ_METADATA(true),
    UPLOAD_AND_READ(true),
    UPLOAD(true);

    private final boolean multiThreaded;

    Operation(boolean multiThreaded) {
        this.multiThreaded = multiThreaded;
    }

    public boolean isMultiThreaded() {
        return multiThreaded;
    }

}
