package com.ceu.lavanderia;

import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ceu.lavanderia.adapter.AgendamentosAdapter;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.ceu.lavanderia.viewmodel.AgendamentosActivityViewModel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListagemActivity extends AppCompatActivity implements
        AgendamentosAdapter.OnAgendamentoSelectedListener {

    private static final String TAG = "ListagemActivity";

    private static final int RC_SIGN_IN = 9001;

    private static final int LIMIT = 50;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recycler_agendamentos)
    RecyclerView mAgendamentosRecycler;

    @BindView(R.id.view_empty)
    ViewGroup mEmptyView;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    FirebaseUser user;

    private AgendamentosAdapter mAdapter;

    private AgendamentosActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listagem);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), AgendamentoActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

        // Verifica se o usuário está aguardando resposta do provedor para logar na aplicação
        mViewModel = ViewModelProviders.of(this).get(AgendamentosActivityViewModel.class);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseFirestore.getInstance();

        if (!shouldStartSignIn()){
            buildRecycler();
        }

    }

    public void buildRecycler(){
        user = FirebaseAuth.getInstance().getCurrentUser();

        mFirestore.collection("admins").whereEqualTo("uid", user.getUid()).limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Get ${LIMIT} agendamentos for admin
                                mQuery = mFirestore.collection("agendamentos")
                                        .orderBy("hora", Query.Direction.ASCENDING)
                                        .limit(LIMIT);
                            } else {
                                // Get ${LIMIT} agendamentos for regular user
                                mQuery = mFirestore.collection("agendamentos")
                                        .whereEqualTo("userID", user.getUid())
                                        .orderBy("hora", Query.Direction.ASCENDING)
                                        .limit(LIMIT);
                            }

                            // RecyclerView
                            mAdapter = new AgendamentosAdapter(mQuery, ListagemActivity.this) {
                                @Override
                                protected void onDataChanged() {
                                    // Show/hide content if the query returns empty.
                                    if (getItemCount() == 0) {
                                        mAgendamentosRecycler.setVisibility(View.GONE);
                                        mEmptyView.setVisibility(View.VISIBLE);
                                    } else {
                                        mAgendamentosRecycler.setVisibility(View.VISIBLE);
                                        mEmptyView.setVisibility(View.GONE);
                                    }

                                }

                                @Override
                                protected void onError(FirebaseFirestoreException e) {
                                    // Show a snackbar on errors
                                    Snackbar.make(findViewById(android.R.id.content),
                                            "Error: check logs for info.", Snackbar.LENGTH_LONG);
                                }
                            };

                            mAgendamentosRecycler.setLayoutManager(new LinearLayoutManager(ListagemActivity.this));
                            mAgendamentosRecycler.setAdapter(mAdapter);

                            // Start listening for Firestore updates
                            if (mAdapter != null) {
                                mAdapter.startListening();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();

        // Start sign in if necessary
        if (shouldStartSignIn()) {
            startSignIn();
            return;
        }

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                AuthUI.getInstance().signOut(this);
                startSignIn();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // Manages the login intent response
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            mViewModel.setIsSigningIn(false);

            if (resultCode != RESULT_OK) {
                if (response == null) {
                    // User pressed the back button.
                    finish();
                } else if (response.getError() != null
                        && response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSignInErrorDialog(R.string.message_no_network);
                } else {
                    showSignInErrorDialog(R.string.message_unknown);
                }
            } else {
                buildRecycler();
            }
        }
    }

    @Override
    public void onAgendamentoSelected(DocumentSnapshot agendamento) {
//        Intent intent = new Intent(this, AgendamentoDetailActivity.class);
//        intent.putExtra(AgendamentoDetailActivity.KEY_AGENDAMENTO_ID, agendamento.getId());
//
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

    private boolean shouldStartSignIn() {
        return (!mViewModel.getIsSigningIn() && FirebaseAuth.getInstance().getCurrentUser() == null);
    }

    private void startSignIn() {
        // Sign in with FirebaseUI
        Intent intent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.EmailBuilder().build(),
                        new AuthUI.IdpConfig.PhoneBuilder().build(),
                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                        new AuthUI.IdpConfig.FacebookBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build();

        startActivityForResult(intent, RC_SIGN_IN);
        mViewModel.setIsSigningIn(true);
    }

    private void showSignInErrorDialog(@StringRes int message) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.title_sign_in_error)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(R.string.option_retry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      startSignIn();
                    }
                })
                .setNegativeButton(R.string.option_exit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create();

        dialog.show();
    }
}
