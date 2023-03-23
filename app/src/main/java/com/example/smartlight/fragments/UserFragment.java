package com.example.smartlight.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.smartlight.Factory;
import com.example.smartlight.R;
import com.example.smartlight.activities.MainActivity;
import com.example.smartlight.interfaces.MyFragment;

public class UserFragment extends Fragment implements MyFragment, View.OnClickListener {

    private View view;
    private TextView fullnameTv, phoneTv, emailTv;
    private Button updateInfoBtn, changePassBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initUI();
        return view;
    }


    private void initUI() {
        // rearrange the bottom menu
        ConstraintSet set = new ConstraintSet();
        set.clone((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));
        set.constrainPercentWidth(R.id.btn_add, 0.0f);
        set.constrainPercentWidth(R.id.btn_menu_home, 0.5f);
        set.constrainPercentWidth(R.id.btn_menu_user, 0.5f);
        set.applyTo((ConstraintLayout) getActivity().findViewById(R.id.menu_bottom));

        fullnameTv = (TextView) view.findViewById(R.id.tv_username);
        emailTv = (TextView) view.findViewById(R.id.tv_email);
        phoneTv = (TextView) view.findViewById(R.id.tv_phone);

        fullnameTv.setText(Factory.user.getFullname());
        emailTv.setText(Factory.user.getEmail());
        phoneTv.setText(Factory.user.getMobile());

        updateInfoBtn = (Button) view.findViewById(R.id.btn_updateinfo);
        updateInfoBtn.setOnClickListener(this);
        changePassBtn = (Button) view.findViewById(R.id.btn_changepass);
        changePassBtn.setOnClickListener(this);
    }

    @Override
    public String getTAG() {
        return "User";
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_updateinfo){
            loadFragment(new UpdateInfoFragment());
        }
        else if(v.getId() == R.id.btn_changepass){
            loadFragment(new ChangePassFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        MainActivity.FRAG_ID = ((MyFragment) fragment).getTAG();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}