package com.ceu.lavanderia.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.ceu.lavanderia.ListagemActivity;

/**
 * ViewModel for {@link ListagemActivity}.
 */

public class AgendamentosActivityViewModel extends ViewModel {

    private boolean mIsSigningIn;

    public AgendamentosActivityViewModel() {
        mIsSigningIn = false;
    }

    public boolean getIsSigningIn() {
        return mIsSigningIn;
    }

    public void setIsSigningIn(boolean mIsSigningIn) {
        this.mIsSigningIn = mIsSigningIn;
    }
}
