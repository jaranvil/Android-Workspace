package ca.jaredeverett.jared.dropchat;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ViewDropActivity extends Activity {
    private ImageView ivThumbnail;

    private String user_id = "";
    private String url = "";
    private String title = "";
    private String text = "";
    private String link = "";
    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_view);

        ivThumbnail = (ImageView) findViewById(R.id.ivThumbnail);

        Bundle extras=getIntent().getExtras();
        if(extras != null)//if bundle has content
        {
            String[] data = extras.getString("snippetString").split(":");
            user_id = data[0];
            url = data[1];
            title = data[2];
            text = data[3];
            link = data[4];
            type = data[5];
        }
    }

    public void setupScreen()
    {
        if (type.equals("1"))
            ivThumbnail.setBackgroundResource(R.drawable.text_thumb);
    }
}
