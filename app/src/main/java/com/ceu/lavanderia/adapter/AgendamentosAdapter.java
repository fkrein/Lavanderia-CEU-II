package com.ceu.lavanderia.adapter;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ceu.lavanderia.R;
import com.ceu.lavanderia.model.Agendamento;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecyclerView adapter for a list of Agendamentos.
 */
public class AgendamentosAdapter extends FirestoreAdapter<AgendamentosAdapter.ViewHolder> {

    public interface OnAgendamentoSelectedListener {

        void onAgendamentoSelected(DocumentSnapshot agendamento);

    }

    private OnAgendamentoSelectedListener mListener;

    public AgendamentosAdapter(Query query, OnAgendamentoSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_agendamento, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.agendamento_item_image)
        ImageView imageView;

        @BindView(R.id.agendamento_item_name)
        TextView nameView;

        @BindView(R.id.agendamento_item_data)
        TextView categoryView;

        @BindView(R.id.agendamento_item_hora)
        TextView cityView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnAgendamentoSelectedListener listener) {

            Agendamento agendamento = snapshot.toObject(Agendamento.class);
            Resources resources = itemView.getResources();

            // Load image
            Glide.with(imageView.getContext())
                    .load(agendamento.getPhoto())
                    .into(imageView);

            nameView.setText(agendamento.getName());
            cityView.setText(agendamento.getHora());
            categoryView.setText(agendamento.getData());

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onAgendamentoSelected(snapshot);
                    }
                }
            });
        }

    }
}
