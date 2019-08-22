package com.example.singhrahuldeep.igethappy.api;


import com.example.singhrahuldeep.igethappy.utils.PreferenceKeys;
import com.example.singhrahuldeep.igethappy.utils.SharedPrefsHelper;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import static com.example.singhrahuldeep.igethappy.App.instance;

/**
 * Created by Gharjyot Singh
 */


public class GetRestAdapter {

    public static final String lIVE = "https://np.seasiafinishingschool.com/igethappy/";
    public static final String tESTING = "http://10.8.18.54:8063";

    static final String HOST_URL = lIVE;
    static GitHubService retrofitInterface;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static GitHubService getRestAdapter(boolean isAuthRequired) {
        retrofitInterface = null;
        SharedPrefsHelper sharedFrefsHelper = new SharedPrefsHelper(instance);
        final String auth_token = sharedFrefsHelper.get(PreferenceKeys.PREF_AUTHENTICATION_TOKEN, "");

        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder();
        builder.readTimeout(30000, TimeUnit.SECONDS);
        builder.connectTimeout(30000, TimeUnit.SECONDS);
        if (isAuthRequired) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request request = chain.request().newBuilder().
                            addHeader("authorization", auth_token).
                            addHeader("Content-Type", "application/json").
                            addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });

        } else {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });
        }

        if (retrofitInterface == null || !isAuthRequired) {
            retrofitInterface = new Retrofit.Builder()
                    .baseUrl(HOST_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
                    .create(GitHubService.class);
        }
        return retrofitInterface;
    }
//TODO add session token implimentation in chat and face detection api as well.

    public static GitHubService getChatRestAdapter(boolean isAuthRequired) {

        String CHAT_URL = "http://np.seasiafinishingschool.com:8062/";

        retrofitInterface = null;
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder();
        builder.readTimeout(3000, TimeUnit.SECONDS);
        builder.connectTimeout(15000, TimeUnit.SECONDS);
        if (isAuthRequired) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request request = chain.request().newBuilder().
                            // addHeader("Content-Type", "application/x-www-form-urlencoded").
                                    addHeader("Content-Type", "application/json").
                                    addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });

        } else {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });
        }

        if (retrofitInterface == null || !isAuthRequired) {
            retrofitInterface = new Retrofit.Builder()
                    .baseUrl(CHAT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
                    .create(GitHubService.class);
        }
        return retrofitInterface;
    }


    public static GitHubService getFacialRestAdapter(boolean isAuthRequired) {
        String CHAT_URL = "http://np.seasiafinishingschool.com:8090/";

        retrofitInterface = null;
        OkHttpClient.Builder builder = UnsafeOkHttpClient.getUnsafeOkHttpClient().newBuilder();
        builder.readTimeout(3000, TimeUnit.SECONDS);
        builder.connectTimeout(15000, TimeUnit.SECONDS);
        if (isAuthRequired) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request request = chain.request().newBuilder().
                            // addHeader("Content-Type", "application/x-www-form-urlencoded").
                                    addHeader("Content-Type", "application/json").
                                    addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });

        } else {
            builder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });
        }

        if (retrofitInterface == null || !isAuthRequired) {
            retrofitInterface = new Retrofit.Builder()
                    .baseUrl(CHAT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
                    .create(GitHubService.class);
        }
        return retrofitInterface;
    }


}
