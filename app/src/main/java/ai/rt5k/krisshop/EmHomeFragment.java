package ai.rt5k.krisshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

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
                }

                StringRequest flightRequest = new StringRequest(Request.Method.POST, MainApplication.SERVER_URL + "/login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("SUCCESS")) {
                            Intent Intent = new Intent(getContext(), EmOrdersActivity.class);
                            startActivity(customerHomeIntent);
                        }
                        Log.e("LoginActivity", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LoginActivity", error.toString());
                    }
                }){
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("mem_id", edtUsername.getText().toString());
                        params.put("password", edtPassword.getText().toString());
                        return params;
                    }
                };*/

                Intent orderIntent = new Intent(getContext(), EmOrdersActivity.class);
                startActivity(orderIntent);
            }
        });
        return rootView;
    }

}
