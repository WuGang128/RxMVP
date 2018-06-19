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

/**
 * <p>Created by gizthon on 16/10/6. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表解析类适配器的工厂类
 * 必须通过注解@JsonAdapter方式才能优先于默认的CollectionTypeAdapterFactory
 */
public final class ListTypeAdapterFactory implements TypeAdapterFactory {

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        Class<? super T> rawType = typeToken.getRawType();
        if (!List.class.isAssignableFrom(rawType)) {
            return null;
        }

        Type elementType = $Gson$Types.getCollectionElementType(type, rawType);
        TypeAdapter<?> elementTypeAdapter = gson.getAdapter(TypeToken.get(elementType));

        @SuppressWarnings({"unchecked", "rawtypes"}) // create() doesn't define a type parameter
                TypeAdapter<T> result = new Adapter(gson, elementType, elementTypeAdapter);
        return result;
    }

    private static final class Adapter<E> extends TypeAdapter<List<E>> {
        private final TypeAdapter<E> elementTypeAdapter;

        public Adapter(Gson context, Type elementType,
                       TypeAdapter<E> elementTypeAdapter) {
            this.elementTypeAdapter = new TypeAdapterRuntimeTypeWrapper<E>(
                    context, elementTypeAdapter, elementType);
        }


        //关键部分是这里，重写解析方法
        public List<E> read(JsonReader in) throws IOException {
            //null值返回null
            //新建一个空的列表
            List<E> list = new ArrayList<>();
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return list;
            }
            try {
                in.beginArray();
                while (in.hasNext()) {
                    E instance = elementTypeAdapter.read(in);
                    list.add(instance);
                }
                in.endArray();
                //正常解析成为列表
            } catch (IllegalStateException e) { //如果是空字符串，会有BEGIN_ARRAY报错
                //此时尝试解析成字符串，如果不是空字符串，则依旧抛出异常
                //如果是空字符串，则不抛出异常，使最终返回一个空的列表
                if (!"".equals(in.nextString())) {
                    throw e;
                }
            }

            return list;
        }

        public void write(JsonWriter out, List<E> list) throws IOException {
            if (list == null) {
                out.nullValue();
                return;
            }

            out.beginArray();
            for (E element : list) {
                elementTypeAdapter.write(out, element);
            }
            out.endArray();
        }

        final class TypeAdapterRuntimeTypeWrapper<T> extends TypeAdapter<T> {
            private final Gson context;
            private final TypeAdapter<T> delegate;
            private final Type type;

            TypeAdapterRuntimeTypeWrapper(Gson context, TypeAdapter<T> delegate, Type type) {
                this.context = context;
                this.delegate = delegate;
                this.type = type;
            }

            @Override
            public T read(JsonReader in) throws IOException {
                return delegate.read(in);
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                // Order of preference for choosing type adapters
                // First preference: a type adapter registered for the runtime type
                // Second preference: a type adapter registered for the declared type
                // Third preference: reflective type adapter for the runtime type (if it is a sub class of the declared type)
                // Fourth preference: reflective type adapter for the declared type

                TypeAdapter chosen = delegate;
                Type runtimeType = getRuntimeTypeIfMoreSpecific(type, value);
                if (runtimeType != type) {
                    TypeAdapter runtimeTypeAdapter = context.getAdapter(TypeToken.get(runtimeType));
                    if (!(runtimeTypeAdapter instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                        // The user registered a type adapter for the runtime type, so we will use that
                        chosen = runtimeTypeAdapter;
                    } else if (!(delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
                        // The user registered a type adapter for Base class, so we prefer it over the
                        // reflective type adapter for the runtime type
                        chosen = delegate;
                    } else {
                        // Use the type adapter for runtime type
                        chosen = runtimeTypeAdapter;
                    }
                }
                chosen.write(out, value);
            }

            /**
             * Finds a compatible runtime type if it is more specific
             */
            private Type getRuntimeTypeIfMoreSpecific(Type type, Object value) {
                if (value != null
                        && (type == Object.class || type instanceof TypeVariable<?> || type instanceof Class<?>)) {
                    type = value.getClass();
                }
                return type;
            }
        }

    }
}