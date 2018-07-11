package com.ceu.lavanderia.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ceu.lavanderia.model.Agendamento;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/**
 * RecyclerView adapter for displaying the results of a Firestore Query.
 */
public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>
        implements EventListener<QuerySnapshot> {

    private static final String TAG = "FirestoreAdapter";

    private Query mQuery;
    private ListenerRegistration mRegistration;

    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    @Override
    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "onEvent:error", e);
            onError(e);
            return;
        }

        // Dispatch the event
        Log.d(TAG, "onEvent:numChanges:" + documentSnapshots.getDocumentChanges().size());
        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }

        // Excluir mSnapshots onde a data e o horário estão no passado
        Date currentDateTime = Calendar.getInstance().getTime();

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(currentDateTime);
        DateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        String currentTime = new SimpleDateFormat("hh:mm", Locale.ENGLISH).format(currentDateTime);
        DateFormat formatTime = new SimpleDateFormat("hh:mm", Locale.ENGLISH);

        Date date = null;
        Date time = null;
        try {
            date = formatDate.parse(currentDate);
            time = formatTime.parse(currentTime);
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

        Iterator<DocumentSnapshot> i = mSnapshots.iterator();
        while (i.hasNext()) {
            DocumentSnapshot s = i.next();
            if (s.toObject(Agendamento.class).DataFormatada().before(date)){
                i.remove();
            } else if (s.toObject(Agendamento.class).DataFormatada().equals(date)){
                if (s.toObject(Agendamento.class).HoraFormatada().before(time)){
                    i.remove();
                } else {

                }
            } else {

            }
        }

        // Reordenar mSnapshots ArrayList considerando data e hora
        Collections.sort(mSnapshots, new Comparator<DocumentSnapshot>() {
            public int compare(DocumentSnapshot o1, DocumentSnapshot o2) {
                if (o1.toObject(Agendamento.class).DataFormatada() == null || o2.toObject(Agendamento.class).DataFormatada() == null)
                    return 0;
                return o1.toObject(Agendamento.class).DataFormatada().compareTo(o2.toObject(Agendamento.class).DataFormatada());
            }
        });

        onDataChanged();
    }

    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    public void setQuery(Query query) {
        // Stop listening
        stopListening();

        // Clear existing data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    protected DocumentSnapshot getSnapshot(int index) {
        return mSnapshots.get(index);
    }

    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            // Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {
            // Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }

    protected void onError(FirebaseFirestoreException e) {
        Log.w(TAG, "onError", e);
    }

    protected void onDataChanged() {}
}
