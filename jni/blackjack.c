#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <termios.h>
#include <sys/mman.h>
#include <android/log.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <android/log.h>
#include <errno.h>

#define TEXTLCD_BASE            0xbc
#define TEXTLCD_COMMAND_SET     _IOW(TEXTLCD_BASE,0,int)
#define TEXTLCD_FUNCTION_SET    _IOW(TEXTLCD_BASE,1,int)
#define TEXTLCD_DISPLAY_CONTROL _IOW(TEXTLCD_BASE,2,int)
#define TEXTLCD_CURSOR_SHIFT    _IOW(TEXTLCD_BASE,3,int)
#define TEXTLCD_ENTRY_MODE_SET  _IOW(TEXTLCD_BASE,4,int)
#define TEXTLCD_RETURN_HOME     _IOW(TEXTLCD_BASE,5,int)
#define TEXTLCD_CLEAR           _IOW(TEXTLCD_BASE,6,int)
#define TEXTLCD_DD_ADDRESS      _IOW(TEXTLCD_BASE,7,int)
#define TEXTLCD_WRITE_BYTE      _IOW(TEXTLCD_BASE,8,int)

struct strcommand_varible {
        char rows;
        char nfonts;
        char display_enable;
        char cursor_enable;

        char nblink;
        char set_screen;
        char set_rightshit;
        char increase;
        char nshift;
        char pos;
        char command;
        char strlength;
        char buf[16];
};
static struct strcommand_varible strcommand;
static int initialized = 0;
void initialize()
{
   if(!initialized)
   {
      strcommand.rows = 0;
      strcommand.nfonts = 0;
      strcommand.display_enable = 1;
      strcommand.cursor_enable = 0;
      strcommand.nblink = 0;
      strcommand.set_screen = 0;
      strcommand.set_rightshit = 1;
      strcommand.increase = 1;
      strcommand.nshift = 0;
      strcommand.pos = 10;
      strcommand.command = 1;
      strcommand.strlength = 16;
      initialized = 1;
   }
}


jint Java_com_example_embeddedblackjack_InGameActivity_SegmentControl (JNIEnv* env, jobject thiz, jint data )
{    
    int dev, ret ;
    dev = open("/dev/segment",O_RDWR | O_SYNC);

    if(dev != -1) {
        ret = write(dev,&data,4);
        close(dev);
    } else {
        exit(1);
    }
    return 0;
}
jint Java_com_example_embeddedblackjack_InGameActivity_SegmentIOControl (JNIEnv* env,  jobject thiz, jint data )
{
    int dev, ret ;
    dev = open("/dev/segment",O_RDWR | O_SYNC);

    if(dev != -1) {
        ret = ioctl(dev, data, NULL, NULL);
        close(dev);
    } else {
        exit(1);
    }
    return 0;
}

int TextLCDIoctl(int cmd, char *buf)
{
   int fd,ret,i;


   fd = open("/dev/textlcd",O_WRONLY | O_NDELAY);
   if(fd < 0) return -errno;

   if(cmd == TEXTLCD_WRITE_BYTE) {
      ioctl(fd,TEXTLCD_DD_ADDRESS,&strcommand,32);
      for(i=0;i<strlen(buf);i++)
      {
         strcommand.buf[0] = buf[i];
         ret = ioctl(fd, cmd, &strcommand, 32);      
      }
   } else {
      ret = ioctl(fd, cmd, &strcommand, 32);
   }

   close(fd);

   return ret;
}


jint Java_com_example_embeddedblackjack_InGameActivity_TextLCDOut( JNIEnv* env, jobject thiz, jstring data0, jstring data1 )
{
   jboolean iscopy;
   char *buf0, *buf1;
   int fd,ret;

   fd = open("/dev/textlcd",O_WRONLY | O_NDELAY);
   if(fd < 0) return -errno;

   initialize();

   buf0 = (char *)(*env)->GetStringUTFChars(env,data0,&iscopy);
   buf1 = (char *)(*env)->GetStringUTFChars(env,data1,&iscopy);

   strcommand.pos = 0;
   ioctl(fd,TEXTLCD_DD_ADDRESS,&strcommand,32);
   ret = write(fd,buf0,strlen(buf0));

   strcommand.pos = 40;
   ioctl(fd,TEXTLCD_DD_ADDRESS,&strcommand,32);
   ret = write(fd,buf1,strlen(buf1));

   close(fd);

   return ret;
}

jint Java_com_example_embeddedblackjack_InGameActivity_IOCtlClear( JNIEnv* env, jobject thiz )
{
   initialize();
   return TextLCDIoctl(TEXTLCD_CLEAR,NULL);
}

jint
Java_com_example_embeddedblackjack_InGameActivity_PiezoControl( JNIEnv* env,
                                                  jobject thiz, jint value )
{
	int fd,ret;
	int data = value;

	fd = open("/dev/piezo",O_WRONLY);
  
	if(fd < 0) return -errno;
  
	ret = write(fd, &data, 1);
	close(fd);
  
	if(ret == 1) return 0;
  	
	return -1;
}
jint
Java_com_example_embeddedblackjack_InGameActivity_DotMatrixControl(JNIEnv* env, jobject thiz, jstring data)
{
	const char *buf; 
	int dev,ret, len;
	char str[100];

	buf = (*env)->GetStringUTFChars(env, data, 0);
	len = (*env)->GetStringLength(env, data);
	
	dev = open("/dev/dotmatrix", O_RDWR | O_SYNC);

	if(dev != -1) {
		ret = write(dev, buf, len);
		close(dev);
	}
	return 0;
}
jint
Java_com_example_embeddedblackjack_InGameActivity_FLEDControl(JNIEnv* env, jobject thiz, jint val)
{	
	int fd,ret;
	int led[8] = {1, 2, 3, 4, 5, 6, 7, 8}; //hit,stay,split,double,evenmoney,insurance,win,loose
	char buf[1];
	int index = 0;
	int i = 0;
	fd = open("/dev/fullcolorled",O_WRONLY);
	if (fd < 0) 
	{
		return -errno;
	}
	for(i=0; i<8; i++){
		if(val == led[i])
			index = led[i];
	}

	buf[0] = index;
	
	write(fd,buf,1);

	close(fd);
	return ret;

}
