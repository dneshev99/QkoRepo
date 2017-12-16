package com.elsys.refpro.refpromobile.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.elsys.refpro.refpromobile.R;
import com.elsys.refpro.refpromobile.database.DataBase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by user on 12.11.2017 г..
 */

public class Menu extends Fragment {

    View menuView;
    int match_id;
    DataBase db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        menuView = inflater.inflate(R.layout.menu_fragment, container, false);

        //region INITIALIZE
        final SharedPreferences mPrefs = this.getActivity().getPreferences(MODE_PRIVATE);
        LinearLayout layout = (LinearLayout) menuView.findViewById(R.id.menu_layout);

        //Get last saved match's ID
        match_id = mPrefs.getInt("ID", 0);

        //endregion
        db = new DataBase(this.getActivity());

        Cursor data = db.getData();

        while (data.moveToNext()) {

            final Button fixture = new Button(this.getActivity());
            fixture.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            fixture.setId(Integer.parseInt(data.getString(0)));
            fixture.setText(data.getString(7) + "  " + data.getString(5) + " - " + data.getString(6));
            //float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics());
            //fixture.setTextSize(dp);

            final int id = Integer.parseInt(data.getString(0));

            fixture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //GET the information about match with it's unique key
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    prefsEditor.putInt("match_id", id);
                    prefsEditor.commit();

                    //CREATE AlertDialog with options to OPEN or DELETE current match
                    AlertDialog.Builder alert = new AlertDialog.Builder(menuView.getContext());
                    alert.setMessage(R.string.alert_text).setCancelable(true)
                            .setPositiveButton(R.string.open, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Info info = new Info();
                                    android.app.FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, info).commit();
                                }
                            })
                            .setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    db.delete(id);

                                    Toast.makeText(getActivity(), R.string.delete_success,
                                            Toast.LENGTH_LONG).show();

                                    Menu menu = new Menu();
                                    android.app.FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.content_frame, menu).commit();
                                }
                    });

                    AlertDialog al = alert.create();
                    al.setTitle(R.string.options);
                    alert.show();
                }

            });

            layout.addView(fixture);
        }

        return menuView;
    }
}
