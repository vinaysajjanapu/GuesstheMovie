package com.vinay.guessthemovie.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vinay.guessthemovie.R;
import com.vinay.guessthemovie.activities.HintActivity;
import com.vinay.guessthemovie.activities.MainActivity;
import com.vinay.guessthemovie.utils.MovieDb;

import jp.wasabeef.glide.transformations.BlurTransformation;

@SuppressLint("ValidFragment")
public class GameFragment extends Fragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    String moviename;
    LinearLayout ll_holder;
    LinearLayout[] holder_row;
    Button[] iv_holder;
    ImageView[] life;
    ImageButton nextLevel, button_hint;
    TextView tv_Score;
    int num_col;
    LinearLayout.LayoutParams lp1, lp2, lp3;
    int finish;
    int life_available;
    LinearLayout keyboard, livesHolder;
    int screenWidth, screenHeight;
    ImageView backgroundImage;
    MovieDb.ResultsBean movie;

    public GameFragment() {
    }

    public GameFragment(MovieDb.ResultsBean resultsBean) {
        movie = resultsBean;
        screenWidth = MainActivity.screenWidth;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game, container, false);
        Initialize(view);
        CreateHolder(view);
        CreateLivesIndic();
        CreateKeyBoard(view);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @SuppressLint("SetTextI18n")
    private void Initialize(View view) {
        num_col = 8;
        life_available = 5;
        finish = 0;

        moviename = movie.getTitle();
        moviename = moviename.replaceAll("[^A-Za-z0-9 ]", "");
        if (moviename.length() > 24) num_col = 12;

        backgroundImage = view.findViewById(R.id.iv_poster);
        if (movie.getPoster_path() != null)
            Glide.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                    .into(backgroundImage);
        else
            Glide.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w185/" + movie.getBackdrop_path())
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25)))
                    .into(backgroundImage);

        livesHolder = view.findViewById(R.id.lives_holder);
        life = new ImageView[5];

        nextLevel = view.findViewById(R.id.button_next);
        nextLevel.setVisibility(View.INVISIBLE);
        nextLevel.setOnClickListener(this);

        button_hint = view.findViewById(R.id.button_hint);

        tv_Score = view.findViewById(R.id.tv_score);
        tv_Score.setText(MainActivity.score + " / " + MainActivity.nQ);

        button_hint.setOnClickListener(v -> {
            getActivity().startActivity(new Intent(getActivity(), HintActivity.class).putExtra("hint", movie.getOverview()));
        });
    }

    private void CreateHolder(View view) {
        ll_holder = view.findViewById(R.id.Ll_holder);
        holder_row = new LinearLayout[(moviename.length() / num_col) + 1];
        iv_holder = new Button[moviename.length()];
        lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        lp2 = new LinearLayout.LayoutParams(screenWidth / (num_col), ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i <= (moviename.length() / num_col); i++) {
            holder_row[i] = new LinearLayout(getActivity());
            holder_row[i].setLayoutParams(lp1);
            holder_row[i].setGravity(Gravity.CENTER_HORIZONTAL);
            ll_holder.addView(holder_row[i]);
            int r = (i == (moviename.length() / num_col)) ? (moviename.length() % num_col) : num_col;
            for (int j = 0; j < r; j++) {
                iv_holder[i * num_col + j] = new Button(getActivity());
                iv_holder[i * num_col + j].setLayoutParams(lp2);
                iv_holder[i * num_col + j].setTextColor(Color.WHITE);
                iv_holder[i * num_col + j].setTextSize(TypedValue.COMPLEX_UNIT_DIP, screenWidth / (4 * num_col));
                iv_holder[i * num_col + j].setTypeface(null, Typeface.BOLD);
                Character c = moviename.charAt(i * num_col + j);
                if (!(c.toString().equals(" "))) {
                    iv_holder[i * num_col + j].setText("_");

                } else finish++;
                iv_holder[i * num_col + j].setBackgroundColor(Color.TRANSPARENT);
                holder_row[i].addView(iv_holder[i * num_col + j]);
            }
        }
    }


    private void CreateLivesIndic() {
        LinearLayout.LayoutParams lp_life = new LinearLayout.LayoutParams(screenWidth / 15, screenWidth / 15);
        if (life_available > 0) {
            for (int l = 0; l < life_available; l++) {
                life[l] = new ImageView(getActivity());
                life[l].setLayoutParams(lp_life);
                life[l].setPadding(5, 5, 5, 5);
                life[l].setImageResource(R.drawable.life);
                livesHolder.addView(life[l]);
            }
        }
    }


    private void UpdateLivesIndic() {
        if (life_available >= 0) {
            life[life_available].setImageResource(R.drawable.life_hollow);
        }
    }

    private void CreateKeyBoard(View view) {
        String[][] str = new String[][]{
                {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"},
                {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
                {"A", "S", "D", "F", "G", "H", "J", "K", "L", ""},
                {"", "Z", "X", "C", "V", "B", "N", "M", "", ""},
        };
        keyboard = (LinearLayout) view.findViewById(R.id.keyboard);
        LinearLayout[] r = new LinearLayout[4];
        Button[] k = new Button[40];
        // int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        lp3 = new LinearLayout.LayoutParams(screenWidth / 10, 3 * screenWidth / 20);
        lp3.setMargins(0, 0, 0, 0);

        for (int ind1 = 0; ind1 < 4; ind1++) {
            r[ind1] = new LinearLayout(getActivity());
            r[ind1].setLayoutParams(lp1);
            keyboard.addView(r[ind1]);
            for (int ind2 = 0; ind2 < str[ind1].length; ind2++) {
                k[ind1 * 10 + ind2] = new Button(getActivity());
                k[ind1 * 10 + ind2].setTextSize(TypedValue.COMPLEX_UNIT_DIP, screenWidth / 40);
                k[ind1 * 10 + ind2].setLayoutParams(lp3);
                k[ind1 * 10 + ind2].setBackgroundResource(R.drawable.button_click_bg);
                k[ind1 * 10 + ind2].setTextColor(Color.WHITE);
                k[ind1 * 10 + ind2].setText(str[ind1][ind2]);
                k[ind1 * 10 + ind2].setId(ind1 * 100 + ind2);
                k[ind1 * 10 + ind2].setOnClickListener(this);
                r[ind1].addView(k[ind1 * 10 + ind2]);
            }
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button_next) {
            if (mListener != null) {
                mListener.onFragmentInteraction();
            }
        } else if ((life_available == 0) || (finish == moviename.length())) {
            //no action on keyboard
        } else {
            Button key = (Button) view;
            Boolean b = false;
            for (int v = 0; v < moviename.length(); v++) {

                if (!key.getText().toString().equals("")) {

                    if (Character.toString(moviename.charAt(v)).toLowerCase().matches(key.getText().toString().toLowerCase())) {

                        iv_holder[v].setText(key.getText().toString());
                        key.setBackgroundColor(Color.GREEN);
                        key.setClickable(false);
                        b = true;
                        finish++;
                        //Toast.makeText(getApplicationContext(),finish+"",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (!b) {
                key.setBackgroundColor(Color.RED);
                //Toast.makeText(getApplicationContext(), key.getText(), Toast.LENGTH_SHORT).show();
                life_available--;
                UpdateLivesIndic();
            }

            if (finish == moviename.length() || (life_available == 0)) {
                Boolean win = life_available != 0;
                UpdateScore(win);
                Finalize(win);
            }
        }
    }

    private void Finalize(Boolean win) {
        if (win) {
            //nextLevel.setTextColor(Color.BLACK);
            nextLevel.setColorFilter(Color.rgb(50, 0, 255));

        } else {
            // nextLevel.setTextColor(Color.WHITE);
            nextLevel.setColorFilter(Color.RED);
            Answer_Preview();

        }
        Glide.with(getActivity())
                .load("http://image.tmdb.org/t/p/w185/" + movie.getPoster_path())
                .into(backgroundImage);
        nextLevel.setVisibility(View.VISIBLE);
    }

    private void Answer_Preview() {
        for (int v = 0; v < moviename.length(); v++) {
            iv_holder[v].setTextColor(Color.RED);
            //iv_holder[v].setBackgroundResource(R.drawable.button_bg);
            iv_holder[v].setText(Character.toString(moviename.charAt(v)));
        }
    }

    private void UpdateScore(Boolean win) {
        int i = win ? 1 : 0;
        MainActivity.score += i;
        MainActivity.nQ += 1;
        tv_Score.setText(MainActivity.score + " / " + MainActivity.nQ);
    }

    /*public void showLeaderboard() {
        startActivityForResult(
                Games.Leaderboards.getLeaderboardIntent(apiClient,
                        getString(R.string.leaderboard_telugu_score)), 0);
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
