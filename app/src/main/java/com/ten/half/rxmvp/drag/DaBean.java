package com.ten.half.rxmvp.drag;

/**
 * Created by wugang on 2017/12/24.
 */

public class DaBean {
    private boolean is_show_del = false;
    private int location;

    public boolean is_show_del() {
        return is_show_del;
    }

    public void setIs_show_del(boolean is_show_del) {
        this.is_show_del = is_show_del;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }
}
