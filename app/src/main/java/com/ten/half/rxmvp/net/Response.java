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

import com.ten.half.rxmvp.util.SignUtils;

import org.json.JSONObject;

/**
 * <p>Created by gizthon on 16/9/23. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class Response<T> {
    public String msg = "服务器繁忙,请稍后重试111!";
    public String code = "";

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return code.startsWith("0");
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                ", data=" + data +
                '}';
    }

    public Response<T> parserObject(String json) {
        try {

            JSONObject root = new JSONObject(json);
            this.code = root.optString("code");
            this.msg = root.optString("msg");
            this.data = (T) root.opt("data");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Response<T> parserObject2String(String json) {
        try {
            JSONObject root = new JSONObject(json);
            this.code = root.optString("code");
            this.msg = root.optString("msg");
            if ((root.opt("data") instanceof String)) {
                this.data = (T) root.opt("data").toString();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Response<T> encrypt(String json) {
        try {
            JSONObject root = new JSONObject(json);
            this.code = root.optString("code");
            this.msg = root.optString("msg");
            String data = root.optString("data");
            String stroxstr = SignUtils.parDecode(data, "");
            this.data = (T) stroxstr;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public static class OperBean {
    }
}
