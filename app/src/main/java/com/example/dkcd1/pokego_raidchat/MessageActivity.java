package com.example.dkcd1.pokego_raidchat;

import android.support.v7.app.AppCompatActivity;
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

                //client(msg);
            }
        });
    }

    public void client(String msg) {
        String hostName = "cms-r1-18";
        int portNumber = 8080;

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
        }

        // write to socket using OutputStream
        try {
            String keyboardInput;
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Tell client how to end
            System.out.print( "Connection made; type the word END to end the connection\n");

            while(true){
                // Read input form keyboard
                keyboardInput = "test";

                // Allow the client to end the connection
                if (keyboardInput.equals("END")){
                    System.out.print("Ending connection\n");
                    break;
                }

                // Send the message
                os.writeBytes(keyboardInput + "\n");
            }
            os.close();
        } catch (IOException e) {
            System.out.println("IO exception trying to write to socket");
            return;
        }
    }
}
