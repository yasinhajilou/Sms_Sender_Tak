package com.example.yasin.taksmssender.Fragment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.yasin.taksmssender.AddNewItemActivity;
import com.example.yasin.taksmssender.R;
import com.example.yasin.taksmssender.db.Contracts.PeopleInformationContract;
import com.example.yasin.taksmssender.db.SQLiteOpenHelperTak;

import static com.pchmn.materialchips.R2.id.container;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends Fragment {


    public AddContactFragment() {
        // Required empty public constructor
    }


    Button save,back;
    TextInputEditText edtName , edtFamily , edtPhoneNumber;
    String name , family , phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);
        edtName = view.findViewById(R.id.edtContactName);
        edtFamily = view.findViewById(R.id.edtContactFamily);
        edtPhoneNumber = view.findViewById(R.id.edtContactPhone);

        save = view.findViewById(R.id.btnSave);
        back = view.findViewById(R.id.btnBack);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edtName.getText().toString().trim();
                family = edtFamily.getText().toString().trim();
                phoneNumber = edtPhoneNumber.getText().toString().trim();

                if (name.isEmpty() || family.isEmpty() || phoneNumber.isEmpty()){
                    Toast.makeText(getContext(), "هیچ فیلدی نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
                }else {
                    try {
                        SQLiteOpenHelperTak openHelperTak = new SQLiteOpenHelperTak(getContext());
                        SQLiteDatabase database = openHelperTak.getWritableDatabase();

                        ContentValues cv = new ContentValues();
                        cv.put(PeopleInformationContract.PeopleEntry.COLUMN_FULL_NAME_PEOPLE , name + " " + family);
                        cv.put(PeopleInformationContract.PeopleEntry.COLUMN_PHONE_NUMBER , phoneNumber);
                        Long id = database.insert(PeopleInformationContract.PeopleEntry.TABLE_NAME_PEOPLE ,null , cv);
                        Toast.makeText(getContext(), "مخاطب ذخیره شد.", Toast.LENGTH_SHORT).show();
                        edtName.setText("");
                        edtPhoneNumber.setText("");
                        edtFamily.setText("");
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }

    public void getFragmentData(Context context){


    }

}
