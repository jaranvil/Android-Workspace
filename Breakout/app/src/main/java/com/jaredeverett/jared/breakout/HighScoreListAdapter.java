package com.jaredeverett.jared.breakout;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class HighScoreListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private ArrayList<Integer> scores = new ArrayList<>();
    private int lastScore;
    private boolean lastScoreMarked;

    public HighScoreListAdapter(Activity context, ArrayList<Integer> scores, String[] test, int lastScore) {
        super(context, R.layout.list_item, test);
        this.context = context;
        this.scores = scores;
        this.lastScore = lastScore;
        lastScoreMarked = false;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);

        TextView tvName = (TextView) rowView.findViewById(R.id.tvName);
        TextView tvScore = (TextView) rowView.findViewById(R.id.tvScore);
        RelativeLayout rlLayout = (RelativeLayout) rowView.findViewById(R.id.rlLayout);

        if (position % 2 == 0) {
            rlLayout.setBackgroundColor(Color.parseColor("#444444"));
        }

        if (position == 10) {
            if (lastScore < scores.get(position-1)) {

                int rank = 0;
                for (int i = 0;i < scores.size();i++)
                {
                    if (scores.get(i) == lastScore)
                        rank = i;
                }

                tvScore.setText(lastScore+"");
                tvName.setText(""+rank);
                rlLayout.setBackgroundColor(Color.parseColor("#ff9900"));
            } else {
                tvScore.setText("");
                tvName.setText("");
                rlLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
        } else {
            tvScore.setText(Integer.toString(scores.get(position)));
            int temp = position + 1;
            tvName.setText(Integer.toString(temp));
        }

        if (scores.get(position) == lastScore) {
            rlLayout.setBackgroundColor(Color.parseColor("#ff9900"));
            lastScoreMarked = true;
        }

        return rowView;
    }
}
