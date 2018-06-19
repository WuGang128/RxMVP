/*******************************************************************************
 * Copyright (c) 2016 Mickael Gizthon . All rights reserved. Email:2013mzhou@gmail.com
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.ten.half.rxmvp.net;

import com.ten.half.rxmvp.bean.HelperCenterList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


public interface ApiInterface {

    //获取配置信息
    @GET("/app/User/getConfig/")
   // Observable<retrofit2.Response<String>> configApi();
    Observable<retrofit2.Response<String>> configApi(@Query("uid") String uid, @Query("token") String token);
    //帮助中心
    @GET("/?m=api")
    Observable<Response<HelperCenterList>> mHelperListApi(@Query("v") String v);

    @GET("api/public/?service=User.searchArea")
    Observable<retrofit2.Response<String>> getHotList(@Query("page") int page,@Query("pageSize") int pageSize);
}
