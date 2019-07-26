package com.example.dkcd1.pokego_raidchat;

import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageActivity extends AppCompatActivity {

    ImageButton btn_send;
    EditText text_send;
    LinearLayout place_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        place_holder = findViewById(R.id.place_holder);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = text_send.getText().toString();
                System.out.println(msg);


                text_send.setText("");

                getLayoutInflater().inflate(R.layout.chat_item_right,place_holder);
                TextView v = findViewById(R.id.user_message);
                v.setText(msg);

                client(msg);
            }
        });
    }

    public void client(final String text) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //String hostName = "130.217.252.16";
                String hostName = "130.217.252.18";
                int portNumber = 8080;
                System.out.println("2");

                // establish socket connection to server
                Socket socket;
                try {
                    socket = new Socket(hostName, portNumber);
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

                // write to socket using OutputStream
                try {
                    System.out.println("1");

                    // Tell client how to end
                    System.out.print("Connection made; type the word END to end the connection\n");

                    OutputStream out = socket.getOutputStream();
                    PrintWriter output = new PrintWriter(out);

                    output.println(text);
                    output.flush();
                    out.close();
                } catch (IOException e) {
                    System.out.println("IO exception trying to write to socket");
                    return;
                }
            }
        });
        t.start();
    }

}
