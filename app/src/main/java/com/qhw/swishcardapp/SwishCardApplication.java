package com.qhw.swishcardapp;

import android.app.Activity;
import android.app.Application;

import com.lzy.okgo.OkGo;
import com.pos.api.Icc;
import com.pos.api.Mcr;
import com.pos.api.Pci;
import com.pos.api.Printer;
import com.pos.api.Scan;
import com.pos.emvcore.EmvCore;
import com.qhw.swishcardapp.utils.EmvCoreCallBackImpl;
import com.qhw.swishcardapp.utils.PreferenceUtils;
import com.qhw.swishcardapp.utils.RetrofitUtils;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Retrofit;

/**
 * Created by Hello_world on 2017/9/3.
 */
public class SwishCardApplication extends Application {

    public static Retrofit retrofit;
    public static SwishCardApplication instance;
    public static ArrayList<Activity> list = new ArrayList<Activity>();
    public static PreferenceUtils preferenceUtils;
    private String TAG = "SwishCard";
    public Mcr mcr = null;
    public Icc icc = null;
    public Pci pci = null;
    public EmvCore emv = null;
    public Scan d2000Scan = null;
    public Printer d2000Print = null;
    public int miTransCount = 0;
    private EmvCoreCallBackImpl mEmvCoreCallBackImpl = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        retrofit = RetrofitUtils.getRetrofit(this);
        preferenceUtils = new PreferenceUtils(this);
        initOkGoHttp();
/*

        UnCeHandler catchExcep = new UnCeHandler(this);
        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
        if (BuildConfig.DEBUG) {
            Logger.init(TAG).setLogLevel(LogLevel.FULL);
        } else {
            Logger.init(TAG).setLogLevel(LogLevel.NONE);
        }
*/

    }


    public static SwishCardApplication getInstance() {
        return instance;
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        //android.os.Process.killProcess(android.os.Process.myPid());
    }


    public Mcr getD2000Mcr() {
        if (mcr == null) {
            mcr = new Mcr(this, new Mcr.McrConnectStatusListener() {
                @Override
                public void onConnectResult(boolean bRet) {

                }
            });
        }
        return mcr;
    }

    /**
     * author xiaoyl
     * IC卡刷卡器
     * @return
     */
    public Icc getD2000Icc() {
        if (icc == null) {
            icc = new Icc(this, b -> {

            });
        }
        return icc;
    }

    /**
     * author xiaoyl
     * IC卡刷卡器
     * @return
     */
    public Pci getD2000Pci() {
        if (pci == null) {
            pci = new Pci(this, b -> {

            });
        }
        return pci;
    }

    /**
     * author xiaoyl
     * IC卡刷卡器
     * @return
     */
    public EmvCore getEmvCore() {
        if (emv == null) {
            mEmvCoreCallBackImpl = new EmvCoreCallBackImpl(getD2000Icc(), getD2000Pci());

            emv = new EmvCore();
            File fileDir = getFilesDir();

            String strPath = fileDir.getPath();
            strPath += "/EmvCore";
            emv.EmvLibInit(mEmvCoreCallBackImpl, strPath.getBytes(), strPath.getBytes().length);
        }
        return emv;
    }

    public Scan getD2000Scan() {
        if (d2000Scan == null) {
            d2000Scan = new Scan(this, bRet -> {

            });
        }
        return d2000Scan;
    }

    public void initOkGoHttp(){
        OkGo.getInstance().init(this);
       /* OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
//log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
//log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
//非必要情况，不建议使用，第三方的开源库，使用通知显示当前请求的log，不过在做文件下载的时候，这个库好像有问题，对文件判断不准确
   //     builder.addInterceptor(new ChuckInterceptor(this));
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
//全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);


        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
  //      headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
   //     headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
   //     params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
   //     params.put("commonParamsKey2", "这里支持中文参数");
        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);     */                  //全局公共参数
    }

    public Printer getD2000Print() {
        if (d2000Print == null) {
            d2000Print = new Printer(this, new Printer.PrinterConnectStatusListener() {
                @Override
                public void onConnectResult(boolean bRet) {
                }
            });
        }
        return d2000Print;
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        if (null != mcr) mcr.DLL_McrRelease();
        if (null != d2000Scan) d2000Scan.DLL_ScanRelease();
        if (null != d2000Print) d2000Print.DLL_PrnRelease();
    }

}
