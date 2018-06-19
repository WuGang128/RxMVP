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

package com.ten.half.rxmvp.bean;

import com.google.gson.annotations.JsonAdapter;
import com.ten.half.rxmvp.net.ListTypeAdapterFactory;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Created by gizthon on 16/10/5. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class HelperCenterList implements Serializable {
    @JsonAdapter(ListTypeAdapterFactory.class)
    private List<ListBean> list;


    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }


    public static class ListBean {
        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
