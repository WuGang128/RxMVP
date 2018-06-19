/*******************************************************************************
 *
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.ten.half.rxmvp.net;


import android.util.Log;

import com.commonslibrary.commons.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.ten.half.rxmvp.base.BaseApplication;
import com.ten.half.rxmvp.util.SignUtils;
import com.ten.half.rxmvp.util.TimeZoneUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


/**
 */
public class RetrofitSingleton {

    static Gson gson;
    private static ApiInterface apiService = null;
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static boolean is_debug = false;
    public static boolean is_gzip = false;

    private RetrofitSingleton() {
        init();
    }

    public static RetrofitSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static void initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (is_debug) {
            // https://drakeet.me/retrofit-2-0-okhttp-3-0-config
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new HeaderInrceptor());
        // 缓存 http://www.jianshu.com/p/93153b34310e
        File cacheFile = new File(BaseApplication.getCacheFileDir(), "/NetCache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);

        Interceptor cacheInterceptor = chain -> {
            Request request = chain.request();
            if (!NetWorkUtils.isAvailable(BaseApplication.getMContext())) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Response response = chain.proceed(request);
            Headers headers = response.headers();

            Log.d("返回信息", headers.toString());

            is_gzip = response.header("Content-Encoding", "").equals("gzip") ? true : false;
            if (NetWorkUtils.isAvailable(BaseApplication.INSTANCE.getMContext())) {
                int maxAge = 0;
                // 有网络时 设置缓存超时时间0个小时
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            } else {
                // 无网络时，设置超时为 60 分钟
                int maxStale = 60 * 60 /** 24 * 7 * 4*/;
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
            return response;
        };
        builder.cache(cache).addInterceptor(cacheInterceptor);
        //设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        okHttpClient = builder.build();
    }


    static class HeaderInrceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            String stringRandom = SignUtils.getStringRandom(7);
            long timestamp = TimeZoneUtil.getWebsiteDatetime() / 1000;
            HttpUrl url = chain.request().url();
            HashMap<String, String> stringStringHashMap = new HashMap<>();
            Set<String> strings = url.queryParameterNames();
            for (String string : strings) {
                stringStringHashMap.put(string, url.queryParameter(string));
                Log.d("请求参数", url.queryParameter(string));
            }
            Log.d("请求链接", chain.request().url().toString());

            String signKey = SignUtils.signKey(stringStringHashMap, stringRandom, timestamp + "");
//                   .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

            Request request = chain.request().newBuilder()
                    .addHeader("oncekey", stringRandom)
                    .addHeader("timestr", timestamp + "")
                    .addHeader("signkey", signKey)
                    .addHeader("apiagent", BaseApplication.getInstance().getApiagent())
                    .addHeader("Accept-Encoding", "gzip")
                    .build();
//            Log.d("signkey", signKey);
            return chain.proceed(request);

        }
    }


    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
//                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())

                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())

                    .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory())
                    .registerTypeAdapterFactory(new ListTypeAdapterFactory())
                    .create();
        }
        return gson;
    }

    private static void initRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseApplication.HOST)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFixStringFactory.create(buildGson()))
                .build();

    }

    private void init() {
        initOkHttp();
        initRetrofit();
        apiService = retrofit.create(ApiInterface.class);

    }

    public ApiInterface getApiService() {
        return apiService;
    }

    public static class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("")) {
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    public static class DoubleDefaultAdapter implements JsonSerializer<Double>, JsonDeserializer<Double> {
        @Override
        public Double deserialize(JsonElement json, Type typeOfT,
                                  JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("")) {
                    return 0.00;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsDouble();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    public static class NullStringToEmptyAdapterFactory<T> implements TypeAdapterFactory {
        @SuppressWarnings("unchecked")
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            Class<T> rawType = (Class<T>) type.getRawType();


            if (rawType != String.class) {
                return null;
            }
            return (TypeAdapter<T>) new StringNullAdapter();
        }
    }

    public static class StringNullAdapter extends TypeAdapter<String> {
        @Override
        public String read(JsonReader reader) throws IOException {
            // TODO Auto-generated method stub
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return "";
            }
            return reader.nextString();
        }

        @Override
        public void write(JsonWriter writer, String value) throws IOException {
            // TODO Auto-generated method stub
            if (value == null) {
                writer.nullValue();
                return;
            }
            writer.value(value);
        }
    }

    private static class SingletonHolder {
        private static final RetrofitSingleton INSTANCE = new RetrofitSingleton();
    }


}

final class GsonConverterFixStringFactory extends Converter.Factory {
    private final Gson gson;

    private GsonConverterFixStringFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    /**
     * Create an instance using a default {@link Gson} instance for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFixStringFactory create() {
        return create(new Gson());
    }

    /**
     * Create an instance using {@code gson} for conversion. Encoding to JSON and
     * decoding from JSON (when no charset is specified by a header) will use UTF-8.
     */
    public static GsonConverterFixStringFactory create(Gson gson) {
        return new GsonConverterFixStringFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {

        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));

        return new GsonResponseBodyConverter<>(gson, adapter, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new GsonRequestBodyConverter<>(gson, adapter);
    }

    final class GsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
        private final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
        private final Charset UTF_8 = Charset.forName("UTF-8");

        private final Gson gson;
        private final TypeAdapter<T> adapter;

        GsonRequestBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public RequestBody convert(T value) throws IOException {
            Buffer buffer = new Buffer();
            Writer writer = new OutputStreamWriter(buffer.outputStream(), UTF_8);
            JsonWriter jsonWriter = gson.newJsonWriter(writer);
            adapter.write(jsonWriter, value);
            jsonWriter.close();

            return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
        }
    }

    final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final Type type;
        private final TypeAdapter<T> adapter;


        GsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter, Type type) {
            this.gson = gson;
            this.adapter = adapter;
            this.type = type;

        }

        @Override
        public T convert(ResponseBody value) throws IOException {

            if (type == String.class) {
                try {
                    if (RetrofitSingleton.is_gzip) {
                        return (T) unzip(value.byteStream());
                    } else {
                        return (T) value.string();
                    }
                    //   return RetrofitSingleton.is_gzip ? (T) unzip(value.byteStream()) : (T) value.string();
                    // return (T)value.string();
                } finally {
                    value.close();
                }
            } else if (type.toString().endsWith(com.ten.half.rxmvp.net.Response.class.getCanonicalName() + "<java.lang.String>")
                    ) {
                try {
                    Response.class.getCanonicalName();
                    //返回这个对象,然后是string
                    com.ten.half.rxmvp.net.Response response = new com.ten.half.rxmvp.net.Response().parserObject2String(value.string());
                    return (T) response;
                } finally {
                    value.close();
                }


            } else if (type.toString().endsWith(com.ten.half.rxmvp.net.Response.class.getCanonicalName() + "<org.json.JSONObject>")
                    || type.toString().endsWith(com.ten.half.rxmvp.net.Response.class.getCanonicalName() + "<org.json.JSONArray>")
                    ) {
                //返回这个对象,然后是string

                try {
                    com.ten.half.rxmvp.net.Response response = new com.ten.half.rxmvp.net.Response().parserObject(value.string());
                    return (T) response;
                } finally {
                    value.close();
                }

            } else {
                JsonReader jsonReader = gson.newJsonReader(value.charStream());
                try {
                    ///   Log.d("请求成功",adapter.read(jsonReader).toString());

                    return adapter.read(jsonReader);
                } finally {
                    value.close();
                }
            }
        }
    }

    public static String unzip(InputStream inputStream) {
        String result = null;

        try {
            GZIPInputStream e = new GZIPInputStream(inputStream);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int i;
            while ((i = e.read()) != -1) {
                baos.write(i);
            }

            result = baos.toString();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return result;
    }

    public static boolean isGzip(InputStream inputStream) {

        InputStream ips = null;
        // 取前两个字节
        byte[] header = new byte[2];
        try {
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            bis.mark(2);
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int ss = (header[0] & 0xff) | ((header[1] & 0xff) << 8);
            if (result != -1 && ss == GZIPInputStream.GZIP_MAGIC) {
                return true;
            } else {
                // 取前两个字节
                return false;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();

        }
        return false;
    }

}
