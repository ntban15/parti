package com.annguyen.android.parti.login;

import android.support.annotation.NonNull;

import com.annguyen.android.parti.login.events.LoginEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by annguyen on 6/20/2017.
 */

public class LoginModelImpl implements LoginModel {

    FirebaseAuth auth;

    public LoginModelImpl() {
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.SIGN_IN_SUCCESS, null));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.SIGN_IN_FAIL,
                                e.getLocalizedMessage()));
                    }
                });
    }

    @Override
    public void createAccount(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CREATE_ACC_SUCCESS, null));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CREATE_ACC_FAIL,
                                e.getLocalizedMessage()));
                    }
                });
    }

    @Override
    public void checkAuth() {
        if (auth.getCurrentUser() != null) {
            EventBus.getDefault().post(new LoginEvent(LoginEvent.AUTH_SUCCESS, null));
        }
        else {
            EventBus.getDefault().post(new LoginEvent(LoginEvent.AUTH_FAIL, null));
        }
    }
}
