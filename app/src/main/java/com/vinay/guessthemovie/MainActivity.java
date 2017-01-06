package com.vinay.guessthemovie;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String[] db = {"dhruva","roy","ram","ekkadiki pothavu chinnavada","saptagiri express","vangaveeti",
    "dangal","khaidi 150","gautamiputra","satamanam bhavathi","raaes","kaabil"};
    String moviename;
    LinearLayout ll_holder;
    LinearLayout[] holder_row;
    Button[] iv_holder;
    ImageView[] life;
    Button nextLevel;
    int num_col;
    LinearLayout.LayoutParams lp1,lp2;
    int finish;
    TextView hint;

    int life_available;
    EditText editText;
    Button button;
    LinearLayout keyboard,livesHolder;
    int screenWidth,screenHeight;
    DisplayMetrics metrics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Initialize();
        CreateHolder();
        CreateLivesIndic();
        CreateKeyBoard();


    }


    private void Initialize() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        num_col=8;
        life_available=5;
        finish=0;

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        moviename = db[(int)(Math.random()*db.length)];

        hint = (TextView)findViewById(R.id.hint);
        hint.setText("");

        livesHolder = (LinearLayout)findViewById(R.id.lives_holder);
        life = new ImageView[5];

        nextLevel = (Button)findViewById(R.id.button_next);
        nextLevel.setVisibility(View.GONE);
        nextLevel.setOnClickListener(this);
    }

    private void CreateHolder() {
        ll_holder= (LinearLayout) findViewById(R.id.Ll_holder);
        holder_row = new LinearLayout[(moviename.length()/num_col)+1];
        iv_holder = new Button[moviename.length()];
        lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        lp2 = new LinearLayout.LayoutParams(screenWidth/(num_col), ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i=0; i<=(moviename.length()/num_col);i++){
            holder_row[i] = new LinearLayout(this);
            holder_row[i].setLayoutParams(lp1);
            ll_holder.addView(holder_row[i]);
            int r = (i==(moviename.length()/num_col))?(moviename.length()%num_col):num_col;
            for (int j=0; j<r;j++){
                iv_holder[i*num_col+j]=new Button(this);
                iv_holder[i*num_col+j].setLayoutParams(lp2);
                iv_holder[i*num_col+j].setTextSize( TypedValue.COMPLEX_UNIT_DIP,screenWidth/(4*num_col));
                Character c=moviename.charAt(i*num_col+j);
                if(!(c.toString().equals(" "))){
                    iv_holder[i*num_col+j].setText("_");

                }else finish++;
                iv_holder[i*num_col+j].setBackgroundColor(getResources().getColor(android.R.color.transparent));
                holder_row[i].addView(iv_holder[i*num_col+j]);
            }
        }
    }


    private void CreateLivesIndic() {
        if (life_available>0) {
            for (int l = 0; l < life_available; l++) {
                life[l] = new ImageView(this);
                life[l].setImageResource(R.mipmap.ic_launcher);
                livesHolder.addView(life[l]);
            }
        }
    }


    private void UpdateLivesIndic() {
        if (life_available>=0) {
            livesHolder.removeView(life[life_available]);
        }
    }

    private void CreateKeyBoard() {
        String[][] str = new String[][]{
                {"0","1","2","3","4","5","6","7","8","9"},
                {"Q","W","E","R","T","Y","U","I","O","P"},
                {"A","S","D","F","G","H","J","K","L",""},
                {"","Z","X","C","V","B","N","M","",""},
        };
        keyboard = (LinearLayout)findViewById(R.id.keyboard);
        LinearLayout[] r = new LinearLayout[4];
        Button[] k = new Button[40];
       // int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(screenWidth/10, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.setMargins(0,0,0,0);

        for(int ind1=0; ind1<4;ind1++){
            r[ind1] = new LinearLayout(this);
            r[ind1].setLayoutParams(lp1);
            keyboard.addView(r[ind1]);
            for (int ind2=0;ind2<str[ind1].length;ind2++){
                k[ind1*10+ind2] = new Button(this);
                k[ind1*10+ind2].setTextSize(TypedValue.COMPLEX_UNIT_DIP,screenWidth/40);
                k[ind1*10+ind2].setLayoutParams(lp3);
                k[ind1*10+ind2].setText(str[ind1][ind2]);
                k[ind1*10+ind2].setId(ind1*100+ind2);
                k[ind1*10+ind2].setOnClickListener(this);
                r[ind1].addView(k[ind1*10+ind2]);
            }
        }

    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button_next) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {

            Button key = (Button) view;
            Boolean b = false;
            for (int v = 0; v < moviename.length(); v++) {

                if (!key.getText().toString().equals("")) {

                    if (Character.toString(moviename.charAt(v)).matches(key.getText().toString().toLowerCase())) {

                        iv_holder[v].setText(key.getText().toString());
                        b = true;
                        finish++;
                        //Toast.makeText(getApplicationContext(),finish+"",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (!b) {
                life_available--;
                UpdateLivesIndic();
                //b=false;
                hint.setText("wrong attempt \n" +
                        "chances available = " + (5 - life_available));
            }

            if (finish == moviename.length() || (life_available == 0)) {
                if (life_available == 0) {
                    hint.setText("All life_availables over");

                } else {
                    hint.setText("Congratulations");

                }

                nextLevel.setVisibility(View.VISIBLE);
            }
        }
    }


}

