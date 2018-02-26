#include <jni.h>
#include <string>
#include <pthread.h>
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/ptrace.h>
#include <android/log.h>

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "ceshi", __VA_ARGS__)

const int handledSignals[] = {SIGSEGV, SIGABRT, SIGFPE, SIGILL, SIGBUS};
const int handledSignalsNum = sizeof(handledSignals) / sizeof(handledSignals[0]);
struct sigaction old_handlers[sizeof(handledSignals) / sizeof(handledSignals[0])];

void my_sigaction(int signal, siginfo_t *info, void *reserved) {
    LOGD("signal:%d", signal);
}

char key_src[] = {'z', 'y', 't', 'y', 'r', 'T', 'R', 'A', '*', 'B', 'n', 'i', 'q', 'C', 'P', 'p',
                  'V', 's'};

int is_number(const char *src) {
    if (src == NULL) {
        return 0;
    }
    while (*src != '\0') {
        if (*src < 48 || *src > 57) {
            return 0;
        }
        src++;
    }
    return 1;
}

char *get_encrypt_str(const char *src) {
    if (src == NULL) {
        return NULL;
    }
    int len = strlen(src);
    len++;
    char *dest = (char *) malloc(len * sizeof(char));
    char *head = dest;
    char *tmp = (char *) src;
    int i = 0;
    int key_len = 18;
    for (; i < len; i++) {
        int index = (*tmp) - 48;
        if (index == 0) {
            index = 1;
        }
        *dest = key_src[key_len - index];
        tmp++;
        dest++;
    }
    dest++;
    *dest = '\0';
    return head;
}

extern "C"
JNIEXPORT jboolean JNICALL
isEquals(JNIEnv *env, jobject obj, jstring str) {
    LOGD("JNIEnv1:%p", env);
    const char *strAry = env->GetStringUTFChars(str, 0);
    int len = strlen(strAry);
    char *dest = (char *) malloc(len);
    strcpy(dest, strAry);

    int number = is_number(strAry);
    if (number == 0) {
        return JNI_FALSE;
    }

    char *encry_str = get_encrypt_str(strAry);
    const char *pas = "ssBCqpBssP";
    int result = strcmp(pas, encry_str);

    env->ReleaseStringUTFChars(str, strAry);

    if (result == 0) {
        return JNI_TRUE;
    } else {
        return JNI_FALSE;
    }
}

//获取TracePid
int get_number_for_str(char *str) {
    if (str == NULL) {
        return -1;
    }
    char result[20];
    int count = 0;
    while (*str != '\0') {
        if (*str >= 48 && *str <= 57) {
            result[count] = *str;
            count++;
        }
        str++;
    }
    int val = atoi(result);
    return val;
}

//开启循环轮训检查TracePid字段
void *thread_function(void *argv) {
    int pid = getpid();
    char file_name[20] = {'\0'};
    sprintf(file_name, "/proc/%d/status", pid);
    char linestr[256];
    int i = 0, traceid;
    FILE *fp;
    while (1) {
        i = 0;
        fp = fopen(file_name, "r");
        if (fp == NULL) {
            break;
        }
        while (!feof(fp)) {
            fgets(linestr, 256, fp);
            if (i == 5) {
                traceid = get_number_for_str(linestr);
                LOGD("traceId:%d", traceid);
                if (traceid > 0) {
                    LOGD("I was be traced...trace pid:%d", traceid);
                    exit(0);
                }
                break;
            }
            i++;
        }
        fclose(fp);
        sleep(5);
    }
    return ((void *) 0);
}

void create_thread_check_traceid() {
    pthread_t t_id;
    int err = pthread_create(&t_id, NULL, thread_function, NULL);
    if (err != 0) {
        LOGD("create thread fail: %s\n", strerror(err));
    }
}

/**
 * 签名校验函数
 */
const char *app_signature = "308202b9308201a1a003020102020405af33a4300d06092a864886f70d01010b0500300d310b3009060355040613023836301e170d3137313131313032313633335a170d3432313130353032313633335a300d310b300906035504061302383630820122300d06092a864886f70d01010105000382010f003082010a0282010100c98bea8924c6c3e5258cc61068dcf9c239056086aede80830f76bd120feb7004b0d524a288653b5845fcfe8a0dd562cdb388ef0aa3bff98d8124953b76afcb3113e04abbcdbf4c505f42739d2a37118be0be3195ab811be072e18faa6d7350e1be62049f82b02bdfe18ff2ebafeb06b67d7ffebe2a1d1e59faa9544a2c652fdff18bb0933c197350c310c25eb7a66b1501ac4f321c3311bfd130c456177abf3d5df254f12db499e719ead6cecebf85f3a3a1ae31e43f2c46f6ef5a509908041a67863f1e2587264715947ca01bd7aa68568d7ef4709604ad4e999e5383004f5d76426ce4e05216153cb4972fc35d0a7f124f1376038077b0be2359f660f534e50203010001a321301f301d0603551d0e04160414038bd8206cc6e761f8fb60eec7194ce77b91b9af300d06092a864886f70d01010b0500038201010064f665f0c3f72f0139fc963bc47510b9b9ce1d2ff90c0ee5689bbfaa34f063a0996a06096e58a0fd939ef0b61b8924936fee57ba2af8f5f48a5e5e98302a3700d381b33d375878e8d09f4eb3698e30e4086900165b050d21bd5fdb92d9a096cab0b27e139a8dfa1a1cde592f811da26997c90e153ea12ab8b85c721678dc5728b140683dd13191d0db21e43a82531c229e2c183ae900cc33875c63b64a59bc34862f3dabb88701198982325afc92fe0011825bc055627ca5e3cbea98243d24b72634a3153dff52dc63ed5fbdeb473413552cf96bc86fc684333a24ef486d143f54cbfa9386e7c038eb177727dcf9891538871376f2f6f267d371a3c110069664";

int check_signature(JNIEnv *env) {
    //调用Java层的Utils中的获取签名的方法
    jclass javaUtilClass = env->FindClass("com/example/lahm/ctest/Utils");
    if (javaUtilClass == NULL) {
        LOGD("not find class");
        return JNI_FALSE;
    }

    LOGD("class name:%p", javaUtilClass);

    jmethodID method = (env)->GetStaticMethodID(javaUtilClass, "getSignature",
                                                "()Ljava/lang/String;");
    if (method == NULL) {
        LOGD("not find method '%s'", method);
        return JNI_FALSE;
    }
//
    jstring obj = (jstring) (env)->CallStaticObjectMethod(javaUtilClass, method);
    if (obj == NULL) {
//        LOGD("method invoke error:%p", obj);
        return JNI_FALSE;
    }

    const char *strAry = (env)->GetStringUTFChars(obj, 0);
    int cmpVal = strcmp(strAry, app_signature);
    (env)->ReleaseStringUTFChars(obj, strAry);

    if (cmpVal == 0)
        return JNI_TRUE;
    else
        return JNI_FALSE;

}

//定义目标类名称
static const char *className = "com/example/lahm/ctest/MyApplication";

//定义方法隐射关系
//http://blog.csdn.net/JansonZhe/article/details/48651503
static JNINativeMethod methods[] = {
        {"isEquals", "(Ljava/lang/String;)Z", (void *) isEquals},
};

//复写jnin_onload完成动态注册
//https://mp.weixin.qq.com/s/nR9ejCysG38nDr68aAjF_w
extern "C"
JNIEXPORT jint JNICALL
JNI_OnLoad(JavaVM *vm, void *reserved) {
    LOGD("JNI on load...");

    //自己附加
    LOGD("ptrace myself...");
//    ptrace(PTRACE_TRACEME, 0, 0, 0);

    //检测自己有没有被trace
    create_thread_check_traceid();

    //声明变量
    jint result = JNI_ERR;
    JNIEnv *env;
    //获取JNI环境对象
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
//    LOGD("ERROR: GetEnv failed\n");
        return JNI_ERR;
    }
    //签名校验
    LOGD("JNIEnv:%p", env);
    LOGD("start equal signature...");
    int check_sign_result = check_signature(env);
//    LOGD("check_sign:%d", check_sign_result);
    if (check_sign_result == JNI_FALSE) {
        exit(0);
    }

    JNINativeMethod methods[] = {
            {"isEquals", "(Ljava/lang/String;)Z", (void *) isEquals},
    };
    jclass clazz = env->FindClass("com/example/lahm/ctest/MyApplication");
    if (clazz == NULL) {
        LOGD("Native registration unable to find class '%s'", className);
        return JNI_ERR;
    }
    int methodsLength;
//    //建立方法隐射关系
//    //取得方法长度
    methodsLength = sizeof(methods) / sizeof(methods[0]);
    if (env->RegisterNatives(clazz, methods, methodsLength) != 0) {
        LOGD("RegisterNatives failed for '%s'", className);
        return JNI_ERR;
    }

    result = JNI_VERSION_1_6;
    return result;
}


//onUnLoad方法，在JNI组件被释放时调用
void JNI_OnUnload(JavaVM *vm, void *reserved) {
    LOGD("JNI unload...");
}