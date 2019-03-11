package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private EditText editText;
    private Button secondActivity;
    private Button makeCall;
    private Button goWeb;
    private Button sendMail;
    private Button getData;
    private Button sendTextToReceiver;

    private ButtonListener bl = new ButtonListener();

    public static final String KEY = "text_key";
    public static final String INVALID_BUTTON = "Invalid button pressed";
    public static final String WEB_PAGE = "https://www.youtube.com";
    public static final String PHONE = "tel:";
    public static final String NUMBER = "0751842781";
    public static final String CHOOSER_STRING = "Use one of the following apps to send the e-mail";
    public static final String LOG_TAG = "LOG";
    public static final Integer REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.editText);
        secondActivity = findViewById(R.id.second_activity_button);
        makeCall = findViewById(R.id.make_a_call_button);
        goWeb = findViewById(R.id.go_to_web_page_button);
        sendMail = findViewById(R.id.send_email_button);
        getData = findViewById(R.id.get_data_button);
        sendTextToReceiver = findViewById(R.id.send_text_to_receiver_button);

        secondActivity.setOnClickListener(bl);
        goWeb.setOnClickListener(bl);
        makeCall.setOnClickListener(bl);
        sendMail.setOnClickListener(bl);
        getData.setOnClickListener(bl);
        sendTextToReceiver.setOnClickListener(bl);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK) {
//              TextView textView = findViewById(R.id.textView);
                textView.setText(data.getStringExtra(MainActivity.KEY));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG, "in pauza");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG, "in rezumat");
    }

    class ButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            String text = null;

            switch (v.getId()) {
                case R.id.second_activity_button:
                    text = editText.getText().toString();
                    intent = new Intent(MainActivity.this, SecondActivity.class);
                    intent.putExtra(KEY, text);
                    startActivity(intent);
                    break;
                case R.id.go_to_web_page_button:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(WEB_PAGE));
                    startActivity(intent);
                    break;
                case R.id.make_a_call_button:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(PHONE + NUMBER));
                    startActivity(intent);
                    break;
                case R.id.send_email_button:
                    text = editText.getText().toString().trim();
                    // We only send an email if there is text in the EditText
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(MainActivity.this, "Insert text", Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(Intent.ACTION_SEND);
                        String chooserTitle = CHOOSER_STRING;
                        Intent intentChooser = Intent.createChooser(intent, chooserTitle);

                        if(intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intentChooser);
                        }
                        Intent sendEmailIntent = new Intent(Intent.ACTION_SEND);
                        sendEmailIntent.setType("text/html");
                        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"stancuandrei97@yahoo.com"});
                        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, "First email");
                        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString().trim());
                        startActivity(sendEmailIntent);
                    }
                    break;
                case R.id.get_data_button:
                    intent = new Intent(MainActivity.this, ThirdActivity.class);
                    startActivityForResult(intent, REQ_CODE); // 1 is the request code
                    break;
                case R.id.send_text_to_receiver_button:
                    text = editText.getText().toString().trim();
                    // We only send text back if there is any
                    if (TextUtils.isEmpty(text)) {
                        Toast.makeText(MainActivity.this, "Insert text", Toast.LENGTH_SHORT).show();
                    } else {
                        intent = new Intent(MainActivity.this, MyReceiver.class);
                        intent.putExtra(KEY, text);
                        MainActivity.this.sendBroadcast(intent);
                    }
                    break;
                default:
                    Toast.makeText(getApplicationContext(), INVALID_BUTTON, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
