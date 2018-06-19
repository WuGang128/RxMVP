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
 * <p>Created by gizthon on 16/10/5. email:2013mzhou@gmail.com</p>
 * <p>
 * des:
 */
public class ServerException extends RuntimeException {
    public String code;
    public String message;

    public ServerException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ServerException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public ServerException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }


}