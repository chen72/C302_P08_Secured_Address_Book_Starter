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

public class ViewContactDetailsActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobile;
    private Button btnUpdate, btnDelete;
    private int contactId;
    private String loginID,apikey,firstName,lastName,mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact_details);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobile = findViewById(R.id.etMobile);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);



        Intent i = getIntent();//get
        loginID = i.getStringExtra("loginId");
        apikey = i.getStringExtra("apikey");
        contactId = i.getIntExtra("id",0);
        firstName = i.getStringExtra("firstname");
        lastName = i.getStringExtra("lastname");
        mobile = i.getStringExtra("mobile");

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etMobile.setText(mobile);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Code for step 1 start
                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/updateContact.php";

                HttpRequest request = new HttpRequest(url);

                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("id", contactId+"");
                request.addData("loginId",loginID);
                request.addData("apikey",apikey);

                request.addData("FirstName", etFirstName.getText().toString().trim());
                request.addData("LastName", etLastName.getText().toString().trim());
                request.addData("Mobile", etMobile.getText().toString().trim());

                request.execute();
                finish();
                // Code for step 1 end


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Code for step 1 start
                String url = "http://10.0.2.2/C302_P08_SecuredCloudAddressBook/deleteContact.php";

                HttpRequest request = new HttpRequest(url);

                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("id", contactId+"");
                request.addData("loginId",loginID);
                request.addData("apikey",apikey);
                request.execute();
                finish();
                // Code for step 1 end


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Code for step 1 start
        Intent intent = getIntent();
        contactId = intent.getIntExtra("id", -1);

    }


    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObj.getBoolean("authorized")+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
}