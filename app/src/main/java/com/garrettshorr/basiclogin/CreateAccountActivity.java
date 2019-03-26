
package com.garrettshorr.basiclogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;
    private Button buttonCreateAccount;

    public static final String EXTRA_NAME_INFO = "nameinfo";
    public static final String EXTRA_USER_INFO = "logininfo";
    public static final String EXTRA_PW_INFO = "pwinfo";
    public static final String EXTRA_CPW_INFO = "cpwinfo";
    public static final String EXTRA_EMAIL_INFO = "emailinfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        wireWidgets();


        buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAccountOnBackendless();
                Toast.makeText(CreateAccountActivity.this, "hi", Toast.LENGTH_SHORT).show();
                String user = editTextUsername.getText().toString();
                String filledname = editTextName.getText().toString();
                String pw = editTextPassword.getText().toString();
                String cpw = editTextConfirmPassword.getText().toString();
                String emailinfo = editTextEmail.getText().toString();

                if (pw == cpw) {
                    Intent loginPage = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    loginPage.putExtra(EXTRA_USER_INFO, user);
                    loginPage.putExtra(EXTRA_NAME_INFO, filledname);
                    loginPage.putExtra(EXTRA_PW_INFO, pw);
                    loginPage.putExtra(EXTRA_CPW_INFO, cpw);
                    loginPage.putExtra(EXTRA_EMAIL_INFO, emailinfo);

                    setResult(1234, loginPage);
                }
            }
        });

    }

    private void registerAccountOnBackendless() {
        //verify all the fields are filled out and passwords are same
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String name = editTextName.getText().toString();

        if(allFieldsValid(password, confirmPassword, username, email, name)) {
            //make the registration call
            // do not forget to call Backendless.initApp when your app initializes

            BackendlessUser user = new BackendlessUser();
            user.setProperty( "email", email );
            user.setProperty("name", name);
            user.setProperty("username", username);
            user.setPassword(password);

            Backendless.UserService.register( user, new AsyncCallback<BackendlessUser>()
            {
                public void handleResponse( BackendlessUser registeredUser )
                {
                    // user has been registered and now can login
                    Toast.makeText(CreateAccountActivity.this, registeredUser.getUserId() + " has registered.",
                            Toast.LENGTH_SHORT).show();
                    finish(); //ends activity
                    //TODO would be nice to return the username to the loginactivity
                    //we would need to call setResult see startActivityForResult
                    //documentation
                }

                public void handleFault( BackendlessFault fault )
                {
                    // an error has occurred, the error code can be retrieved with fault.getCode()
                }
            } );
        }
    }

    private boolean allFieldsValid(String password, String confirmPassword, String username, String email, String name) {
        //validate all the fields
        return password.equals(confirmPassword) && username.length()>0;
    }


    //return to the LoginActivity in the handleResponse



    private void wireWidgets() {
        editTextName = findViewById(R.id.edittext_create_name);
        editTextUsername = findViewById(R.id.edittext_create_username);
        editTextUsername.setText(getIntent().getStringExtra(LoginActivity.EXTRA_SENT_MESSAGE));
        editTextPassword = findViewById(R.id.edittext_create_password);
        editTextConfirmPassword = findViewById(R.id.edittext_create_confirmpassword);
        editTextEmail = findViewById(R.id.edittext_create_email);
        buttonCreateAccount = findViewById(R.id.button_createacc);

    }
}



