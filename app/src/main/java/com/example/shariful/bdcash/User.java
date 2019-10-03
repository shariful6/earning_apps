package com.example.shariful.bdcash;

import android.content.Context;
import android.content.SharedPreferences;

public class User
   {
    Context context;

      public  void remove()
      {
          sharedPreferences.edit().clear().commit();
      }

       public String getEmail() {
           return sharedPreferences.getString("userdata","");
       }

       //run koren

       public void setEmail(String email) {
           this.email = email;
           sharedPreferences.edit().putString("userdata",email).commit();
       }

       public String email;


    SharedPreferences sharedPreferences;


    public User(Context context)
    {
      this.context=context;
      sharedPreferences = context.getSharedPreferences("userinfo",Context.MODE_PRIVATE);

    }
}
