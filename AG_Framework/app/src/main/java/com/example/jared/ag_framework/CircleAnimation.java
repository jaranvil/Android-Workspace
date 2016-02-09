package com.example.jared.ag_framework;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;

public class CircleAnimation {

    int radius = 0;
    int d = 500;
    float alpha = 0f;
    Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
    Canvas c = new Canvas(bm);
    Paint p = new Paint();


    public GroundOverlayOptions draw(double lat, double lng)
    {
        radius++;
        if (radius % 5 == 0)
            alpha = alpha + 0.1f;
        if (alpha >= 1f)
            alpha = 1f;

        if (radius > 50)
        {
            radius = 0;
            alpha = 0f;
        }


        LatLng position = new LatLng(lat, lng);
        p.setColor(Color.parseColor("#0000ff"));
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10);
        c.drawCircle(d / 2, d / 2, d / 2, p);

        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);

        GroundOverlayOptions drawOptions = new GroundOverlayOptions().
                image(bmD).
                position(position, radius * 2, radius * 2).
                transparency(alpha);

        return drawOptions;
    }

}
