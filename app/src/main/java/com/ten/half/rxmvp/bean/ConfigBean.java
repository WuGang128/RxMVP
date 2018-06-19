package com.ten.half.rxmvp.bean;


import java.io.Serializable;
import java.util.List;

/**
 * Created by wugang on 2017/8/31.
 */

public class ConfigBean implements Serializable {
    public String msg = "服务器繁忙,请稍后重试111!";
    public String code = "";
    public Data data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        /**
         * share_times : 10
         * share_day : 3
         * buy_times_coin : 1
         * share_info : {"title":"这是分享的标题","desc":"下载快撸APP，领取红包奖励！打开链接跳转下载链接","url":"https://www.baidu.com/","logo":"http://www.baiwujie.com/Logo.png"}
         * coin_ratio : 10
         * chat_characters_count : 200
         * chat_show_time : 300
         * custom_service_uid : 110
         * message_apply_video : 15
         */


        /**
         * buy_times_coin : 1
         * coin_ratio : 10
         * message_apply_video : 15
         * video_time : 20
         * api_sign_model : 7
         * custom_service_weixin : 17128071781
         * template_video : http://static.yygo.tv/Fakemedia/o_1bqacq6juluq16151n3u18jd1lbua.mp4
         * template_image : http://static.yygo.tv/template_image_1.png
         * activity_invite_img : http://static.yygo.tv/activity/invite_coin.png
         * activity_invite_txt : 注册成功，获得10金币邀请奖励
         */
        private String video_time;
        private String api_sign_model;
        private String custom_service_weixin;
        private String template_video;
        private String template_image;
        private List<String> auth_pretty_text;  // 描述文字
        private List<List<String>> withdraw_text;  // 描述文字
        private String activity_invite_img;//邀请好友活动背景图
        private String activity_invite_txt;//邀请好友活动文字
        private String withdraw_chat_name;//公众号
        private String is_bind_mobile;  // 是否强制绑手机
        private String is_greeting;  // 新用户是否需要一键打招呼
        private String vip_on;  // 是否强制开启vip
        private String vip_chat;  // 是否隐藏收费提示
        private int video_limit_minute;  // 最少视频时间
        private List<String> anchor_price_between;  // 报价范围
        private Paymethod paymethod;
        private String share_times;
        private String share_day;
        private int buy_times_coin;
        private ShareInfoBean share_info;
        private int coin_ratio;
        private String chat_characters_count;
        private String chat_show_time;
        private String custom_service_uid;

        private String coin_minute;  //每分钟需要钻石数
        private String msg_need_coin;  //聊天一条信息需要的金币
        private int message_apply_video;
        private Wxpay wxpay;


        public Paymethod getPaymethod() {
            return paymethod;
        }

        public void setPaymethod(Paymethod paymethod) {
            this.paymethod = paymethod;
        }

        public List<String> getAnchor_price_between() {
            return anchor_price_between;
        }

        public void setAnchor_price_between(List<String> anchor_price_between) {
            this.anchor_price_between = anchor_price_between;
        }

        public String getVip_chat() {
            return vip_chat;
        }

        public void setVip_chat(String vip_chat) {
            this.vip_chat = vip_chat;
        }

        public String getVip_on() {
            return vip_on;
        }

        public void setVip_on(String vip_on) {
            this.vip_on = vip_on;
        }

        public int getVideo_limit_minute() {
            return video_limit_minute;
        }

        public void setVideo_limit_minute(int video_limit_minute) {
            this.video_limit_minute = video_limit_minute;
        }

        public String getIs_greeting() {
            return is_greeting;
        }

        public void setIs_greeting(String is_greeting) {
            this.is_greeting = is_greeting;
        }

        public List<List<String>> getWithdraw_text() {
            return withdraw_text;
        }

        public void setWithdraw_text(List<List<String>> withdraw_text) {
            this.withdraw_text = withdraw_text;
        }

        public List<String> getAuth_pretty_text() {
            return auth_pretty_text;
        }

        public void setAuth_pretty_text(List<String> auth_pretty_text) {
            this.auth_pretty_text = auth_pretty_text;
        }

        public String getIs_bind_mobile() {
            return is_bind_mobile;
        }

        public void setIs_bind_mobile(String is_bind_mobile) {
            this.is_bind_mobile = is_bind_mobile;
        }

        public String getWithdraw_chat_name() {
            return withdraw_chat_name;
        }

        public void setWithdraw_chat_name(String withdraw_chat_name) {
            this.withdraw_chat_name = withdraw_chat_name;
        }

        public String getMsg_need_coin() {
            return msg_need_coin;
        }

        public void setMsg_need_coin(String msg_need_coin) {
            this.msg_need_coin = msg_need_coin;
        }

        public String getCoin_minute() {
            return coin_minute;
        }

        public void setCoin_minute(String coin_minute) {
            this.coin_minute = coin_minute;
        }

        public Wxpay getWxpay() {
            return wxpay;
        }

        public void setWxpay(Wxpay wxpay) {
            this.wxpay = wxpay;
        }

        public String getShare_times() {
            return share_times;
        }

        public void setShare_times(String share_times) {
            this.share_times = share_times;
        }

        public String getShare_day() {
            return share_day;
        }

        public void setShare_day(String share_day) {
            this.share_day = share_day;
        }

        public int getBuy_times_coin() {
            return buy_times_coin;
        }

        public void setBuy_times_coin(int buy_times_coin) {
            this.buy_times_coin = buy_times_coin;
        }

        public ShareInfoBean getShare_info() {
            return share_info;
        }

        public void setShare_info(ShareInfoBean share_info) {
            this.share_info = share_info;
        }

        public int getCoin_ratio() {
            return coin_ratio;
        }

        public void setCoin_ratio(int coin_ratio) {
            this.coin_ratio = coin_ratio;
        }

        public String getChat_characters_count() {
            return chat_characters_count;
        }

        public void setChat_characters_count(String chat_characters_count) {
            this.chat_characters_count = chat_characters_count;
        }

        public String getChat_show_time() {
            return chat_show_time;
        }

        public void setChat_show_time(String chat_show_time) {
            this.chat_show_time = chat_show_time;
        }

        public String getCustom_service_uid() {
            return custom_service_uid;
        }

        public void setCustom_service_uid(String custom_service_uid) {
            this.custom_service_uid = custom_service_uid;
        }

        public int getMessage_apply_video() {
            return message_apply_video;
        }

        public void setMessage_apply_video(int message_apply_video) {
            this.message_apply_video = message_apply_video;
        }


        public String getVideo_time() {
            return video_time;
        }

        public void setVideo_time(String video_time) {
            this.video_time = video_time;
        }

        public String getApi_sign_model() {
            return api_sign_model;
        }

        public void setApi_sign_model(String api_sign_model) {
            this.api_sign_model = api_sign_model;
        }

        public String getCustom_service_weixin() {
            return custom_service_weixin;
        }

        public void setCustom_service_weixin(String custom_service_weixin) {
            this.custom_service_weixin = custom_service_weixin;
        }

        public String getTemplate_video() {
            return template_video;
        }

        public void setTemplate_video(String template_video) {
            this.template_video = template_video;
        }

        public String getTemplate_image() {
            return template_image;
        }

        public void setTemplate_image(String template_image) {
            this.template_image = template_image;
        }

        public String getActivity_invite_img() {
            return activity_invite_img;
        }

        public void setActivity_invite_img(String activity_invite_img) {
            this.activity_invite_img = activity_invite_img;
        }

        public String getActivity_invite_txt() {
            return activity_invite_txt;
        }

        public void setActivity_invite_txt(String activity_invite_txt) {
            this.activity_invite_txt = activity_invite_txt;
        }

        public class ShareInfoBean implements Serializable {
            /**
             * title : 这是分享的标题
             * desc : 下载快撸APP，领取红包奖励！打开链接跳转下载链接
             * url : https://www.baidu.com/
             * logo : http://www.baiwujie.com/Logo.png
             */

            private String title;
            private String desc;
            private String url;
            private String logo;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getLogo() {
                return logo;
            }

            public void setLogo(String logo) {
                this.logo = logo;
            }
        }

        public class Wxpay implements Serializable {

            /**
             * appid : wx92660a7d2dfa0c45
             */

            private String appid;

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }
        }

        public class Paymethod implements Serializable {
            private String alipay;
            private String wx;

            //  支付方式  origin 原生  qifenqian 七分钱
            public String getAlipay() {
                return alipay;
            }

            public void setAlipay(String alipay) {
                this.alipay = alipay;
            }

            public String getWx() {
                return wx;
            }

            public void setWx(String wx) {
                this.wx = wx;
            }
        }
    }

}
