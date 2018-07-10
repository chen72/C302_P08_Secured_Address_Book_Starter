package sg.edu.rp.webservices.c302_p08_secured_address_book;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvContact;
    private ArrayList<Contact> alContact;
    private ArrayAdapter<Contact> aaContact;
    private String loginID,apikey;

    // TODO (3) Declare loginId and apikey

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvContact = (ListView) findViewById(R.id.listViewContact);
        alContact = new ArrayList<Contact>();

        aaContact = new ContactAdapter(this, R.layout.contact_row, alContact);
        lvContact.setAdapter(aaContact);



        Intent i = getIntent();//get
        loginID = i.getStringExtra("id");
        apikey = i.getStringExtra("apikey");



        // TODO (4) Get loginId and apikey from the previous Intent


        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contact selectedContact = alContact.get(position);

                // TODO (7) When a contact is selected, create an Intent to View Contact Details
                // Put the following into intent:- contact_id, loginId, apikey

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        alContact.clear();
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact c = alContact.get(position);
                int cID = c.getContactId();
                String cFname = c.getFirstName();
                String cLname = c.getLastName();
                String cMobile = c.getMobile();

                Intent i = new Intent(MainActivity.this,
                        ViewContactDetailsActivity.class);
                i.putExtra("id", cID);
                i.putExtra("firstname", cFname);
                i.putExtra("lastname", cLname);
                i.putExtra("mobile", cMobile);
                i.putExtra("apikey",apikey);
                i.putExtra("loginId",loginID);
                Log.i("info", cID+"");
                startActivity(i);
            }
        });






        // TODO (5) Refresh the main activity with the latest list of contacts by calling getListOfContacts.php
        // What is the web service URL?
        // What is the HTTP method?
        // What parameters need to be provided?

        // Code for step 1 start
        HttpRequest request = new HttpRequest
                ("http://10.0.2.2/C302_P08_SecuredCloudAddressBook/getListOfContacts.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginId",loginID);
        request.addData("apikey",apikey);
        request.execute();
        // Code for step 1 end

    }
    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){

                    // process response here
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            int id = jsonObj.getInt("id");
                            String firstname = jsonObj.getString("firstname");
                            String lastname = jsonObj.getString("lastname");
                            String mobile = jsonObj.getString("mobile");
                            alContact.add(new Contact(id,firstname,lastname,mobile));
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    aaContact.notifyDataSetChanged();
                }
            };

    // TODO (6) In the HttpResponseListener for getListOfContacts.php, get all contacts from the results and show in the list


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {

            // TODO (8) Create an Intent to Create Contact
            // Put the following into intent:- loginId, apikey

            Intent intent =new Intent(getBaseContext(),CreateContactActivity.class);
            intent.putExtra("apikey",apikey);
            intent.putExtra("id",loginID);
            startActivity(intent);
            return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
