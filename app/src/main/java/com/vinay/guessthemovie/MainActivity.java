package com.vinay.guessthemovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    String[] db = {"dhruva","roy","ram","ekkadiki pothavu chinnavada","saptagiri express","vangaveeti",
    "dangal","khaidi 150","gautamiputra","satamanam bhavathi","raaes","kaabil"};
    String moviename;
    LinearLayout ll_holder;
    LinearLayout[] holder_row;
    Button[] iv_holder;
    ImageView[] life;
    ImageButton nextLevel,button_Finish;
    TextView tv_Score;
    int num_col;
    LinearLayout.LayoutParams lp1,lp2,lp3;
    int finish;
    TextView hint;

    int life_available;
    EditText editText;
    Button button;
    LinearLayout keyboard,livesHolder;
    public static int screenWidth,screenHeight;
    DisplayMetrics metrics;
    SharedPreferences score;

    Bitmap bitmap=null;

    DBHelper dBhelper;
    ArrayList<HashMap<String,String>> m_details;
    HashMap<String,String> movie;

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


        RelativeLayout ln = (RelativeLayout)findViewById(R.id.ln_holder);
//        ln.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,1000));

        score = getSharedPreferences("score",MODE_WORLD_WRITEABLE);

        dBhelper = new DBHelper(this);
        m_details = dBhelper.getAllMovieDetails();

        movie = m_details.get((int)(Math.random()*m_details.size()));
        moviename = movie.get("title");

        if(Utils.isOnline(getApplicationContext())) {
            Some some=new Some();
            some.execute();
        }else bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.life);
        // moviename= (String) ((Map.Entry)entries.next()).getValue();
        //moviename = db[(int)(Math.random()*db.length)];

        hint = (TextView)findViewById(R.id.hint);
        hint.setText("");

        livesHolder = (LinearLayout)findViewById(R.id.lives_holder);
        life = new ImageView[5];

        nextLevel = (ImageButton) findViewById(R.id.button_next);
        nextLevel.setVisibility(View.INVISIBLE);
        nextLevel.setOnClickListener(this);

        button_Finish = (ImageButton) findViewById(R.id.button_finish);
        button_Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finish_intent = new Intent(getApplicationContext(),Finish.class);
                startActivity(finish_intent);
                overridePendingTransition(R.anim.slide_l,R.anim.slide_r);
                finish();
            }
        });
        tv_Score = (TextView)findViewById(R.id.tv_score);
        tv_Score.setText(score.getInt("score",0)+" / "+score.getInt("nQ",0));
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
            holder_row[i].setGravity(Gravity.CENTER_HORIZONTAL);
            ll_holder.addView(holder_row[i]);
            int r = (i==(moviename.length()/num_col))?(moviename.length()%num_col):num_col;
            for (int j=0; j<r;j++){
                iv_holder[i*num_col+j]=new Button(this);
                iv_holder[i*num_col+j].setLayoutParams(lp2);
                iv_holder[i*num_col+j].setTextColor(Color.WHITE);
                iv_holder[i*num_col+j].setTextSize( TypedValue.COMPLEX_UNIT_DIP,screenWidth/(4*num_col));
                Character c=moviename.charAt(i*num_col+j);
                if(!(c.toString().equals(" "))){
                    iv_holder[i*num_col+j].setText("_");

                }else finish++;
                iv_holder[i*num_col+j].setBackgroundColor(Color.TRANSPARENT);
                holder_row[i].addView(iv_holder[i*num_col+j]);
            }
        }
    }


    private void CreateLivesIndic() {
        LinearLayout.LayoutParams lp_life = new LinearLayout.LayoutParams(screenWidth/15,screenWidth/15 );
        if (life_available>0) {
            for (int l = 0; l < life_available; l++) {
                life[l] = new ImageView(this);
                life[l].setLayoutParams(lp_life);
                life[l].setPadding(5,5,5,5);
                life[l].setImageResource(R.drawable.life);
                livesHolder.addView(life[l]);
            }
        }
    }


    private void UpdateLivesIndic() {
        if (life_available>=0) {
            life[life_available].setImageResource(R.drawable.life_hollow);

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
        lp3 = new LinearLayout.LayoutParams(screenWidth/10,3*screenWidth/20 );
        lp3.setMargins(0,0,0,0);

        for(int ind1=0; ind1<4;ind1++){
            r[ind1] = new LinearLayout(this);
            r[ind1].setLayoutParams(lp1);
            keyboard.addView(r[ind1]);
            for (int ind2=0;ind2<str[ind1].length;ind2++){
                k[ind1*10+ind2] = new Button(this);
                k[ind1*10+ind2].setTextSize(TypedValue.COMPLEX_UNIT_DIP,screenWidth/40);
                k[ind1*10+ind2].setLayoutParams(lp3);
                k[ind1*10+ind2].setBackgroundResource(R.drawable.button_click_bg);
                k[ind1*10+ind2].setTextColor(Color.WHITE);
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
            overridePendingTransition(R.anim.slide_l,R.anim.slide_r);
            finish();
        } else if((life_available==0) || (finish == moviename.length())) {
                //no action on keyboard
        }else{

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
                    UpdateHints();
                    //b=false;
//                hint.setText("wrong attempt \n chances available = " + (5 - life_available));
                }

                if (finish == moviename.length() || (life_available == 0)) {
                    Boolean win = false;
                    if (life_available == 0) {
                        hint.setText("All life_availables over");
                        win = false;
                    } else {
                        win = true;
                        hint.setText("Congratulations");
                    }

                    UpdateScore(win);
                    Finalize(win);
                }
            }
        }

    private void UpdateHints() {

        switch(life_available+1){

            case 0:hint.setText("");break;
            case 1:{
                hint.setText("Actors:-"+movie.get("Actors"));
                ll_holder.setBackground( new BitmapDrawable(getResources(), bitmap));
                break;}
            case 2:hint.setText("Director:-"+movie.get("Director"));break;
            case 3:hint.setText("Plot:-"+movie.get("Plot"));break;
            case 4:hint.setText("Language:-"+movie.get("Language"));break;
            case 5:hint.setText("Genre:-"+movie.get("Genre"));break;
        }


    }

    private void Finalize(Boolean win) {
        if (win){
            //nextLevel.setTextColor(Color.BLACK);
            nextLevel.setColorFilter(Color.GREEN);

        }else {
           // nextLevel.setTextColor(Color.WHITE);
            nextLevel.setColorFilter(Color.RED);
            Answer_Preview();

        }
        nextLevel.setVisibility(View.VISIBLE);
    }

    private void Answer_Preview() {
        for (int v = 0; v < moviename.length(); v++) {
            iv_holder[v].setTextColor(Color.RED);
          //  iv_holder[v].setBackgroundResource(R.drawable.button_bg);
            iv_holder[v].setText(Character.toString(moviename.charAt(v)));
        }
    }

    private void UpdateScore(Boolean win) {
        SharedPreferences.Editor edit = score.edit();
        int i = win ? 1 : 0;
        edit.putInt("score",score.getInt("score",0)+i);
        edit.putInt("nQ",score.getInt("nQ",0)+1);
        edit.apply();

        tv_Score.setText(score.getInt("score",0)+" / "+score.getInt("nQ",0));


    }

    class Some extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

}

