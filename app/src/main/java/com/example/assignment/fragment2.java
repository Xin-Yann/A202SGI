package com.example.assignment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;
import androidx.fragment.app.Fragment;
import com.example.assignment.AppData;


public class fragment2 extends Fragment {

    TextInputLayout inputLayout;
    TextInputEditText inputOrigin, inputDes, inputDate, inputArrDate, inputPax;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);


     // Find the TextInputEditText within the fragment's view
        TextInputLayout inputLayoutOrigin = view.findViewById(R.id.inputOrigin);
        inputOrigin = inputLayoutOrigin.findViewById(R.id.inputOriginText);

        TextInputLayout inputLayoutDes = view.findViewById(R.id.inputDes);
        inputDes = inputLayoutDes.findViewById(R.id.inputDesText);

        TextInputLayout inputLayoutDate = view.findViewById(R.id.inputDepart);
        inputDate = inputLayoutDate.findViewById(R.id.inputDepart1);

        TextInputLayout inputLayoutArrDate = view.findViewById(R.id.inputArrival);
        inputArrDate = inputLayoutArrDate.findViewById(R.id.inputArrival1);

        TextInputLayout inputLayoutPax = view.findViewById(R.id.inputPax);
        inputPax = inputLayoutPax.findViewById(R.id.inputPax1);


        // Find the button and set an OnClickListener
        Button toTicket = view.findViewById(R.id.selectTicket);
        toTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set flags to indicate that return ticket and seat selection are allowed
                AppData.isReturnTicketAllowed = true;

                String trainOrigin = inputOrigin.getText().toString();
                String trainDes = inputDes.getText().toString();
                String trainDate = inputDate.getText().toString();
                String trainArr = inputArrDate.getText().toString();
                String trainPax = inputPax.getText().toString();

                // Perform a search based on the train name
                /*filterList(trainOrigin, trainDes);*/

                // Start the Select_depart_ticket activity
                Intent intent = new Intent(getContext(), Select_depart_ticket.class);
                intent.putExtra("search_query", trainOrigin);
                intent.putExtra("search_destination", trainDes);
                intent.putExtra("search_date", trainDate);
                intent.putExtra("search_arr", trainArr);
                intent.putExtra("search_pax", trainPax);
                startActivity(intent);

                // Create an Intent to navigate to the Select_depart_ticket activity
                /*Intent intent = new Intent(getActivity(), Select_depart_ticket.class);
                startActivity(intent);*/
            }
        });

        return view;
    }

}

