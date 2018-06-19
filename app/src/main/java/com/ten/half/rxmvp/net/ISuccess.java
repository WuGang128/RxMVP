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
 * <p>Created by gizthon on 16/9/23. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public abstract class ISuccess<T> {
    public abstract void onSuccess(T entity);

    public void onFailed(String message) {
    }
}
