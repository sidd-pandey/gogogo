package com.herokuapp.gogogo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends Activity {

    Button login, signup;
    TextView username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (TextView) findViewById(R.id.username_editText);
        password = (TextView) findViewById(R.id.password_editText);
        login = (Button) findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoginTask().execute(StaticAddress.BASE+"LoginServlet");
            }
        });
        signup = (Button) findViewById(R.id.signup_btn);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Signup.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class LoginTask extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(LoginActivity.this,"Signing In...","Please wait...",true);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> map = new HashMap<>();
            map.put("username",username.getText().toString());
            map.put("password",password.getText().toString());
           BufferedReader br = HttpUtility.openPostConnection(params[0], map);
            try {
                return br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "false";
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("Tag:", s);
            progressDialog.dismiss();
            if(s.equals("true")){
                Toast.makeText(getBaseContext(),"Login Successful",Toast.LENGTH_LONG).show();
                CurrentData.USER = username.getText().toString();
                Intent intent = new Intent(getBaseContext(), JourneyActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getBaseContext(),"Login Failed",Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
            }
        }
    }
}
