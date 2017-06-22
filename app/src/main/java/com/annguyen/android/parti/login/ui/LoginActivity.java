package com.annguyen.android.parti.login.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.login.LoginPresenter;
import com.annguyen.android.parti.login.LoginPresenterImpl;
import com.annguyen.android.parti.main.ui.MainActivity;
import com.annguyen.android.parti.quickstart.ui.QuickStartActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.login_email)
    TextInputEditText loginEmail;
    @BindView(R.id.login_email_container)
    TextInputLayout loginEmailContainer;
    @BindView(R.id.login_password)
    TextInputEditText loginPassword;
    @BindView(R.id.login_password_container)
    TextInputLayout loginPasswordContainer;
    @BindView(R.id.sign_in_btn)
    Button signInBtn;
    @BindView(R.id.sign_up_btn)
    Button signUpBtn;
    @BindView(R.id.login_progress_bar)
    ProgressBar loginProgressBar;
    @BindView(R.id.login_activity_container)
    RelativeLayout loginActivityContainer;

    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        presenter = new LoginPresenterImpl(this);
        presenter.start();  //init presenter
        presenter.checkAuth(); //check if user is already signed in
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    @OnClick({R.id.sign_in_btn, R.id.sign_up_btn})
    public void onViewClicked(View view) {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();
        switch (view.getId()) {
            case R.id.sign_in_btn:
                presenter.signIn(email, password);
                break;
            case R.id.sign_up_btn:
                presenter.createAccount(email, password);
                break;
        }
    }

    @Override
    public void showProgressBar() {
        loginProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        loginProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showInput() {
        signInBtn.setVisibility(View.VISIBLE);
        signUpBtn.setVisibility(View.VISIBLE);
        loginEmailContainer.setVisibility(View.VISIBLE);
        loginPasswordContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        signInBtn.setVisibility(View.GONE);
        signUpBtn.setVisibility(View.GONE);
        loginEmailContainer.setVisibility(View.GONE);
        loginPasswordContainer.setVisibility(View.GONE);
    }

    @Override
    public void onError(String msg) {
        Snackbar.make(loginActivityContainer, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void goToMainScreen() {
        Intent goToMainIntent = new Intent(this, MainActivity.class);
        goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainIntent);
    }

    @Override
    public void goToQuickStart() {
        Intent goToQuickStartIntent = new Intent(this, QuickStartActivity.class);
        goToQuickStartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToQuickStartIntent);
    }
}
