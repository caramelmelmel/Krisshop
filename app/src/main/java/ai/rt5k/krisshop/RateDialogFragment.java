package ai.rt5k.krisshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RateDialogFragment extends DialogFragment {

    private Button btnConfirmRating;
    private ImageView[] ratingStars;
    private int rating;
    private String qrcodeText;

    public RateDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Pass null as the parent view because its going in the dialog layout
        final View rateView = inflater.inflate(R.layout.fragment_rate_dialog, null);
        builder.setView(rateView);

        btnConfirmRating = rateView.findViewById(R.id.btnConfirmRating);
        ratingStars = new ImageView[5];
        ratingStars[0] = rateView.findViewById(R.id.ratingStar1);
        ratingStars[1] = rateView.findViewById(R.id.ratingStar2);
        ratingStars[2] = rateView.findViewById(R.id.ratingStar3);
        ratingStars[3] = rateView.findViewById(R.id.ratingStar4);
        ratingStars[4] = rateView.findViewById(R.id.ratingStar5);

        // Activate stars base on click
        for (int i = 0; i <= 4; i++) {
            final int curRating = i;
            ratingStars[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int j = 0; j <= 4; j++) {
                        if (j <= curRating) {
                            ratingStars[j].setActivated(true);
                        } else {
                            ratingStars[j].setActivated(false);
                        }
                    }
                    rating = curRating;
                    btnConfirmRating.setClickable(true);
                    btnConfirmRating.setTextColor(getResources().getColor(R.color.colorOK));
                }
            });
        }

        btnConfirmRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: save rating
                Activity parent = getActivity();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("qrcode", qrcodeText);
                parent.setResult(Activity.RESULT_OK, returnIntent);
                parent.finish();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void setQrcodeText(String text) {
        qrcodeText = text;
    }

}
