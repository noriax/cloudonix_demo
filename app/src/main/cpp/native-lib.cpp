#include <jni.h>
#include <string>
#include <iostream>

extern "C" JNIEXPORT jint

JNICALL
Java_com_io_cloudonix_test_MainActivity_getInt(JNIEnv *env, jobject thiz) {
    return 10;
}
