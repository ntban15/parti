package com.annguyen.android.parti.quickstart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.main.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuickStartActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @BindView(R.id.qstr_name)
    TextInputEditText qstrName;
    @BindView(R.id.qstr_name_container)
    TextInputLayout qstrNameContainer;
    @BindView(R.id.qstr_finish_btn)
    Button qstrFinishBtn;
    @BindView(R.id.qstr_container)
    RelativeLayout qstrContainer;
    @BindView(R.id.qstr_progress_bar)
    ProgressBar qstrProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_start);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.qstr_finish_btn)
    public void onViewClicked() {
        hideInput();
        showProgressBar();
        String userName = qstrName.getText().toString();
        Map<String, Object> nameUpdate = new HashMap<>();
        nameUpdate.put("/users/" + firebaseAuth.getCurrentUser().getUid() + "/name", userName);
        reference.updateChildren(nameUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgressBar();
                showInput();
                if (task.isSuccessful()) {
                    goToMainActivity();
                } else {
                    onError(task.getException().getLocalizedMessage());
                }
            }
        });
    }

    private void onError(String localizedMessage) {
        Snackbar.make(qstrContainer, localizedMessage, Snackbar.LENGTH_SHORT).show();
    }

    private void goToMainActivity() {
        Intent goToMainIntent = new Intent(this, MainActivity.class);
        goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainIntent);
    }

    private void showProgressBar() {
        qstrProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        qstrProgressBar.setVisibility(View.GONE);
    }

    private void showInput() {
        qstrContainer.setVisibility(View.VISIBLE);
        qstrFinishBtn.setVisibility(View.VISIBLE);
    }

    private void hideInput() {
        qstrContainer.setVisibility(View.GONE);
        qstrFinishBtn.setVisibility(View.GONE);
    }
}
