package com.hyphenate.easeui.adapter;


import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobObject{

    private final static int OFF_LINE = 0;
    private final static int ON_LINE = 1;
    private final static int BUSY = 2;

    private String nickName;
    private String telephone;
    private BmobFile avatar;
    private int state;
    private List<Integer> noShowGroup;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public List<Integer> getNoShowGroup() {
        return noShowGroup;
    }

    public void setNoShowGroup(List<Integer> noShowGroup) {
        this.noShowGroup = noShowGroup;
    }
}
