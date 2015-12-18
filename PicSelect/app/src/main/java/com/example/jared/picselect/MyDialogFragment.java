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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        return view;
    }

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
