package com.example.assignment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UpcomingTripsFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<History> historyArrayList;
    HistoryAdapter historyAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.booking_history_d, container, false);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));

        db = FirebaseFirestore.getInstance();
        historyArrayList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(requireContext(), historyArrayList);

        recyclerView.setAdapter(historyAdapter);

        // Listen to "departseat" and "returnseat" collections
        EventChangeListener("departseat");
        EventChangeListener("returnseat");

        return view;
    }

    private void EventChangeListener(String collectionName) {

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        db.collection(collectionName)
                .whereEqualTo("user_email", userEmail)
                .orderBy("train_date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                            Log.e("Firebase error", error.getMessage());
                            return;

                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                historyArrayList.add(dc.getDocument().toObject(History.class));
                            }

                            historyAdapter.notifyDataSetChanged();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }
                });
    }
}