package com.annguyen.android.parti.party.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.annguyen.android.parti.R;
import com.annguyen.android.parti.entities.Message;
import com.annguyen.android.parti.entities.User;
import com.annguyen.android.parti.main.ui.MainActivity;
import com.annguyen.android.parti.party.PartyPresenter;
import com.annguyen.android.parti.party.PartyPresenterImpl;
import com.annguyen.android.parti.party.adapters.MemberListAdapter;
import com.annguyen.android.parti.party.adapters.MessagesAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PartyActivity extends AppCompatActivity implements PartyView {

    @BindView(R.id.member_list)
    RecyclerView memberList;
    @BindView(R.id.send_btn)
    ImageButton sendBtn;
    @BindView(R.id.send_message)
    EditText sendMessage;
    @BindView(R.id.send_message_container)
    RelativeLayout sendMessageContainer;
    @BindView(R.id.member_messages)
    RecyclerView memberMessages;
    @BindView(R.id.party_progress_bar)
    ProgressBar partyProgressBar;
    @BindView(R.id.party_toolbar)
    Toolbar partyToolbar;

    private boolean asHost;
    private String partyKey;
    private PartyPresenter presenter;
    private MemberListAdapter memberListAdapter;
    private MessagesAdapter messagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party);
        ButterKnife.bind(this);
        setSupportActionBar(partyToolbar);
        getIntentExtra();
        initInjection();
        initRecyclerView();

        //participate in party
        presenter.start();
        presenter.participate(partyKey);
    }

    private void initRecyclerView() {

        memberList.setAdapter(memberListAdapter);
        memberList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        memberMessages.setAdapter(messagesAdapter);
        memberMessages.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void getIntentExtra() {
        Bundle extras = getIntent().getExtras();
        asHost = (boolean) extras.get("asHost");
        partyKey = (String) extras.get("partyKey");
    }

    private void initInjection() {
        presenter = new PartyPresenterImpl(this);
        memberListAdapter = new MemberListAdapter();
        messagesAdapter = new MessagesAdapter(null, null);
    }

    @Override
    protected void onDestroy() {
        presenter.stop();
        super.onDestroy();
    }

    @Override
    public void injectMessages(List<Message> messageList) {
        messagesAdapter.setMessageList(messageList);
    }

    @Override
    public void injectMembers(List<User> users) {
        memberListAdapter.setUserList(users);
    }

    @Override
    public void addNewMessage(Message message) {
        messagesAdapter.addMessage(message);
    }

    @Override
    public void addNewMember(User user) {
        memberListAdapter.addUser(user);
    }

    @Override
    public void removeMember(String userKey) {
        memberListAdapter.removeUser(userKey);
    }

    @Override
    public void goToMain() {
        Intent goToMainIntent = new Intent(this, MainActivity.class);
        goToMainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(goToMainIntent);
    }

    @Override
    public void hideInput() {
        sendMessageContainer.setVisibility(View.GONE);
    }

    @Override
    public void showInput() {
        sendMessageContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        partyProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showProgressBar() {
        partyProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setCurrentUserId(String userKey) {
        messagesAdapter.setUserKey(userKey);
    }

    @OnClick(R.id.send_btn)
    public void onViewClicked() {
        String msg = sendMessage.getText().toString();
        presenter.sendMsg(msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.party_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.leave_party) {
            presenter.leaveGroup(asHost);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}