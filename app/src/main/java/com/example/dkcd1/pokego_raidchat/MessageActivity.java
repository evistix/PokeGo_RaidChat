package com.example.dkcd1.pokego_raidchat;

import android.content.Context;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    final static String END_KEY = "end_con";
    Thread t;
    boolean connected = false;
    String userId= "Paul";
    Socket[] socket = new Socket[1];
    OutputStream out;
    PrintWriter output;
    String msg;

    ImageButton btn_send;
    //Button btn_connect;
    EditText text_send;
    ScrollView scroll_view;
    LinearLayout place_holder;
    List messages = new ArrayList();

    // float values to allow for a swipe action
    float xSwipe, ySwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        place_holder = findViewById(R.id.place_holder);
        scroll_view = findViewById(R.id.scroll_view);
        scroll_view.setOnTouchListener(new OnSwipeTouchListener(MessageActivity.this));
        server_connect();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                msg = text_send.getText().toString();
                System.out.println(msg);

                text_send.setText("");

                server_connect();
                //Display all messages

                place_holder.removeAllViews();
                for(int i = messages.size() - 1; i >= 0; i--) {
                    View s = getLayoutInflater().inflate(R.layout.chat_item_right, null);
                    TextView v = s.findViewById(R.id.user_message);
                    v.setText(messages.get(i).toString());
                    System.out.println(messages.get(i).toString());
                    if(messages.get(i).toString() != "") {
                        place_holder.addView(s);
                    }
                }
                System.out.println(socket[0]);
            }
        });
    }

    public void server_connect () {

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                //String hostName = "130.217.252.16";
                String hostName = "130.217.243.90";
                int portNumber = 6050;

                // establish socket connection to server
                try {
                    if(!connected) {
                        socket[0] = new Socket(hostName, portNumber);
                        out = socket[0].getOutputStream();
                        output = new PrintWriter(out);
                        output.println(userId);
                        messages.add("");
                        connected = true;
                    }
                } catch (UnknownHostException e) {
                    System.out.println("Couldn't establish socket connection");
                    return;
                } catch (IOException e) {
                    System.out.println("IO exception on attempting to connect socket");
                    return;
                } catch (Exception e) {
                    System.out.println(e);
                    return;
                }

                while(t.isAlive() && socket != null) {
                    // write to socket using OutputStream
                    try {
                        System.out.println("1");

                        // Tell client how to end
                        if(!connected)
                        System.out.print("Connection made; type the word 'end_con' to end the connection\n");

                        while(true) {
                            if(msg == END_KEY) {
                                break;
                            }
                            if(msg != "" && msg != null) {
                                output.println(msg);
                                output.flush();
                                messages.add(msg);
                                msg = "";
                            }
                        }
                        out.close();
                    } catch (IOException e) {
                        System.out.println("IO exception trying to write to socket");
                        return;
                    }
                }
            }
        });
        t.start();
        if(!t.isAlive()) {
            connected = false;
        }
    }

    // Detects left to right swipes across the scroll view and switches to MapActivity
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new GestureListener());
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                xSwipe = e2.getX() - e1.getX();
                ySwipe = e2.getY() - e1.getY();
                if (Math.abs(xSwipe) > Math.abs(ySwipe) && Math.abs(xSwipe) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (xSwipe > 0)
                    {
                        Intent i = new Intent(MessageActivity.this, MapActivity.class);
                        startActivity(i);
                    }
                    return true;
                }
                return false;
            }
        }
    }
}
