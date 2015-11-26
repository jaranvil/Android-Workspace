package com.example.jared.picselect;

//import android.app.DialogFragment;
import android.support.v4.app.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class MyDialogFragment extends DialogFragment {
    private ImageView ivImage;
    private Animation animation;
    private Integer[] images = {
            R.drawable.buster,
            R.drawable.gob,
            R.drawable.annyong,
            R.drawable.george,
            R.drawable.george_michael,
            R.drawable.oscar,
            R.drawable.tobias,
            R.drawable.who
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int mNum = 3;

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch (mNum) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch (mNum) {
            case 1: theme = android.R.style.Theme_Holo; break;
            case 2: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 3: theme = android.R.style.Theme_Holo_Light; break;
            case 4: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 5: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        return view;
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
////        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.names, android.R.layout.simple_list_item_1);
////        setListAdapter(adapter);
////        getListView().setOnItemClickListener(this);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
//    }

    public void showImage(int pos)
    {
        ivImage.setBackgroundResource(images[pos]);
        animate();
    }

    public void animate()
    {
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        animation.setRepeatCount(1);
        ivImage.startAnimation(animation);
    }

}
