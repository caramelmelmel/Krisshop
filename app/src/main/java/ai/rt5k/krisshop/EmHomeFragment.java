package ai.rt5k.krisshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EmHomeFragment extends Fragment {
    EditText edtFlightNumber;
    Button btnStartPacking;

    public EmHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_em_home, container, false);

        edtFlightNumber = rootView.findViewById(R.id.edtFlightNumber);
        btnStartPacking = rootView.findViewById(R.id.btnStartPacking);

        btnStartPacking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String flightNo = edtFlightNumber.getText().toString();

                // TODO: Uncomment empty string check
                /*if(flightNo.equals("")) {
                    Snackbar.make(rootView, "Please enter a flight number", Snackbar.LENGTH_SHORT).setAction("CLOSE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                }*/

                Intent orderIntent = new Intent(getContext(), EmOrdersActivity.class);
                startActivity(orderIntent);
            }
        });
        return rootView;
    }

}
