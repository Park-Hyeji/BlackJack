LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := blackjack
LOCAL_SRC_FILES := blackjack.c

include $(BUILD_SHARED_LIBRARY)