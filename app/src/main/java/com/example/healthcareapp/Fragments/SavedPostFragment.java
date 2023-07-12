package com.example.healthcareapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.healthcareapp.Adapter.savedpAdapter;
import com.example.healthcareapp.R;


public class SavedPostFragment extends Fragment {

//private RecyclerView lv;
   public static ListView lv;
   ImageButton back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_post, container, false);
        lv = view.findViewById(R.id.lvsaved);
        back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ProfileFragment fragment= new ProfileFragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_layout, fragment);
//                transaction.addToBackStack(null);
//                transaction.commit();
                getParentFragmentManager().popBackStack();
            }
        });
        if(ProfileFragment.savedpostlist.size()>0) {
            savedpAdapter adapter = new savedpAdapter(getActivity(), ProfileFragment.savedpostlist);
            lv.setAdapter(adapter);
        }
        return view;
    }

}
