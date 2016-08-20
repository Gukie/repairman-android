package com.lokia.repairman.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lokia.repairman.model.User;

/**
 * Created by lokia on 16-8-20.
 */
public class ReparimanSharedPreferences {

    private Context myContext;

    private static enum user_fields {
        id, username, password, phone, isLogin, role, sex
    }

    private static String userInfoSharedName = "userInfo";

    private static  String defaultStr = "";

    private static  int defaultInt = 0;

    public ReparimanSharedPreferences() {
    }

    public ReparimanSharedPreferences(Context mContext) {
        this.myContext = mContext;
    }


    public void saveUserInfo(User user) {
        SharedPreferences sp = myContext.getSharedPreferences(userInfoSharedName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(user_fields.id.name(), user.getId());
        editor.putString(user_fields.username.name(), user.getUsername());
        editor.putString(user_fields.password.name(), user.getPassword());
        editor.putString(user_fields.phone.name(), user.getPhone());
        editor.putString(user_fields.sex.name(), user.getSex());
        editor.putBoolean(user_fields.isLogin.name(),user.isLogin());
        editor.putString(user_fields.role.name(),user.getRole());
        editor.commit();
//        Toast.makeText(myContext, "信息已写入SharedPreference中", Toast.LENGTH_SHORT).show();
    }

    public User readUserInfo() {

        User user = new User();

        SharedPreferences sp = myContext.getSharedPreferences(userInfoSharedName, Context.MODE_PRIVATE);
        int id = sp.getInt(user_fields.id.name(), defaultInt);
        String username = sp.getString(user_fields.username.name(), defaultStr);
        String password = sp.getString(user_fields.password.name(), defaultStr);
        String phone = sp.getString(user_fields.phone.name(), defaultStr);
        String sex = sp.getString(user_fields.sex.name(), defaultStr);
        boolean isLogin = sp.getBoolean(user_fields.isLogin.name(),false);
        String role = sp.getString(user_fields.role.name(),defaultStr);

        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        user.setSex(sex);
        user.setLogin(isLogin);
        user.setRole(role);

        return user;
    }

}
