package com.krypt.Hubble.Users.Staff;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krypt.Hubble.R;
import com.krypt.Hubble.utils.SessionHandler;
import com.krypt.Hubble.utils.UserModel;

public class FragmentProfile extends Fragment {

    private SessionHandler session;
    private UserModel user;
    private TextView txv_name, txv_phoneNo, txv_email,
            txv_username, txv_userType;

    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        txv_name = root.findViewById(R.id.txv_name);
        txv_username = root.findViewById(R.id.txv_username);
        txv_phoneNo = root.findViewById(R.id.txv_phoneNo);
        txv_email = root.findViewById(R.id.txv_email);
        txv_userType = root.findViewById(R.id.txv_usertype);

        try {
            session = new SessionHandler(getContext());
            user = session.getUserDetails();

            txv_userType.setText(user.getUser_type());
            txv_email.setText(user.getEmail());
            txv_name.setText(user.getFirstname() + " " + user.getLastname());
            txv_phoneNo.setText("Phone No " + user.getPhoneNo());
            txv_username.setText(user.getUsername());

        } catch (Exception e) {
            Log.e("ERROR", e.toString());
        }


        return root;
    }


}