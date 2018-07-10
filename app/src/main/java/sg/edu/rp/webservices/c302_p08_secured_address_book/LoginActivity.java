package sg.edu.rp.webservices.c302_p08_secured_address_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.editTextUsername);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO (1) When Login button is clicked, call doLogin.php web service to check if the user is able to log in
                // What is the web service URL?
                // What is the HTTP method?
                // What parameters need to be provided?

                // Code for step 1 start
                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/doLogin.php";
                HttpRequest request = new HttpRequest(url);
                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("username", etUsername.getText().toString().trim());
                request.addData("password", etPassword.getText().toString().trim());

                request.execute();
                // Code for step 1 end






            }
        });
    }

    // TODO (2) In the HttpResponseListener, check if the user has been authenticated successfully
    // If the user can log in, extract the id and API Key from the JSON object, set them into Intent and start MainActivity Intent.
    // If the user cannot log in, display a toast to inform user that login has failed.



    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);
                        String au = jsonObj.getBoolean("authenticated")+"";

                        if(au.equalsIgnoreCase("true")){
                            String apikey = jsonObj.getString("apikey");
                            String id = jsonObj.getString("id");
                            Intent intent =new Intent(getBaseContext(),MainActivity.class);
                            intent.putExtra("apikey",apikey);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "Please check your username and password", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };


}