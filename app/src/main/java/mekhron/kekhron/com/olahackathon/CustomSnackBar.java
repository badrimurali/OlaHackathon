package mekhron.kekhron.com.olahackathon;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

/**
 * Created by badri on 17/12/17.
 */

public class CustomSnackBar {
    static Snackbar snackbar;
    public static void show(Activity activity, String message, String actionStr){
        snackbar = Snackbar.make(activity.findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.DKGRAY);
        TextView sbTextView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        sbTextView.setTextColor(ContextCompat.getColor(activity, R.color.snackBarTextColor));
        snackbar.setAction(actionStr, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        })
                .setActionTextColor(ContextCompat.getColor(activity, R.color.text_color_white)).show();
    }

    public static void dismiss(){
        if(snackbar!=null && snackbar.isShown()){
            snackbar.dismiss();
        }
    }
}
