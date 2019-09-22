package com.thisisabir.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thisisabir.chatapp.model.ChatMessage;
// i manually write below line of code for fixed date format error
import android.text.format.DateFormat;

public class MainActivity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout relativeLayout;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = findViewById(R.id.activity_main);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText input = findViewById(R.id.messagebox);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                // after send messgae set input text box empty
                input.setText("");

            }
        });


        // check if user sign in then navigate to sign in page
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(relativeLayout,"Welcome"+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            // load chat messaege & display
            displayChatMessage();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)  {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Snackbar.make(relativeLayout,"You Have been Sign out",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK)
            {
                Snackbar.make(relativeLayout,"Successfully Sign in Welcome ",Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                Snackbar.make(relativeLayout,"We could not sign in, Try Again later ",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void displayChatMessage() {

        ListView listView = findViewById(R.id.message_list);
        adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class, R.layout.chat_list_item, FirebaseDatabase.getInstance().getReference())
        {
            // manually overide below method
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // get refrence to view list item
                TextView messageText,messageUser,messageTime;
                messageText = v.findViewById(R.id.text);
                messageUser = v.findViewById(R.id.user);
                messageTime = v.findViewById(R.id.time);

                messageText.setText(model.getMessgaeText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getMessageTime()));
            }
        };
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }
}
