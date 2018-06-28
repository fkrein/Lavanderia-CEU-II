package com.ceu.lavanderia.util;

import android.content.Context;
import android.net.Uri;

import com.ceu.lavanderia.R;
import com.ceu.lavanderia.model.Agendamento;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

/**
 * Utilities for Agendamentos.
 */
public class AgendamentoUtil {

    private static final String TAG = "AgendamentoUtil";

    private static final String AGENDAMENTO_URL_FMT = "https://storage.googleapis.com/firestorequickstarts.appspot.com/food_%d.png";
    private static final int MAX_IMAGE_NUM = 22;

    FirebaseUser user;

    private static final String[] NAME_FIRST_WORDS = {
            "Foo",
            "Bar",
            "Baz",
            "Qux",
            "Fire",
            "Sam's",
            "World Famous",
            "Google",
            "The Best",
    };

    private static final String[] NAME_SECOND_WORDS = {
            "Agendamento",
            "Cafe",
            "Spot",
            "Eatin' Place",
            "Eatery",
            "Drive Thru",
            "Diner",
    };

    /**
     * Create a random Agendamento POJO.
     */
    public static Agendamento getRandom(Context context) {

        String name;
        Uri photoUrl;

        Agendamento agendamento = new Agendamento();
        Random random = new Random();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String[] horarios = context.getResources().getStringArray(R.array.horarios);
        horarios = Arrays.copyOfRange(horarios,0, horarios.length);

        String[] datas = context.getResources().getStringArray(R.array.datas);
        datas = Arrays.copyOfRange(datas,0, datas.length);

        // Cities (first elemnt is 'Any')
        String[] cities = context.getResources().getStringArray(R.array.cities);
        cities = Arrays.copyOfRange(cities, 1, cities.length);

        // Categories (first element is 'Any')
        String[] categories = context.getResources().getStringArray(R.array.categories);
        categories = Arrays.copyOfRange(categories, 1, categories.length);

        int[] prices = new int[]{1, 2, 3};

        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            photoUrl = user.getPhotoUrl();

            agendamento.setName(name);
            agendamento.setHora(getRandomString(horarios, random));
            agendamento.setData(getRandomString(datas, random));
            agendamento.setPhoto(photoUrl.toString());
        }

        return agendamento;
    }


    /**
     * Get a random image.
     */
    private static String getRandomImageUrl(Random random) {
        // Integer between 1 and MAX_IMAGE_NUM (inclusive)
        int id = random.nextInt(MAX_IMAGE_NUM) + 1;

        return String.format(Locale.getDefault(), AGENDAMENTO_URL_FMT, id);
    }

    private static String getRandomString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }

}
