package com.first.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity  extends AppCompatActivity {

    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordInput;
    private Button loginButton;
    private ProgressBar progressBar;
    private BlogPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences =new BlogPreferences(this);
        if (preferences.isLoggedIn()){
            startMainActivity();
            finish();
            return;
        }
        setContentView(R.layout.activity_login);


        progressBar = findViewById(R.id.progressBar);
        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textPasswordInput = findViewById(R.id.textPasswordInput);
        loginButton = findViewById(R.id.loginButton);



        loginButton.setOnClickListener(e -> onLoginClicked());
        textUsernameLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textUsernameLayout));

        textPasswordInput
                .getEditText()
                .addTextChangedListener(createTextWatcher(textPasswordInput));
    }

    private void onLoginClicked() {
        String username= textUsernameLayout.getEditText().getText().toString();
        String password= textPasswordInput.getEditText().getText().toString();
        if (username.isEmpty()){
            textUsernameLayout.setError("Username must not be empty");
        }else if(password.isEmpty()){
            textPasswordInput.setError("Password must not be empty");
        }else if (!username.equals("admin") && !password.equals("admin")) {
            showErrorDialog();
        }else {
            performLogin();
        }
    }

    private void performLogin() {
        preferences.setLoggedIn(true);
        textUsernameLayout.setEnabled(false);
        textPasswordInput.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startMainActivity();
            finish();
        }, 2000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private TextWatcher createTextWatcher(TextInputLayout textPasswordInput) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s,int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s,int start, int before, int count) {
                textPasswordInput.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Username or password is not correct. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();

    }
}



