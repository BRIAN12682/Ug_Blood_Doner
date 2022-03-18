package gr.gdschua.bloodapp.Activities.HospitalActivities;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;

import java.util.Objects;

import gr.gdschua.bloodapp.Activities.MainActivity;
import gr.gdschua.bloodapp.DatabaseAccess.DAOHospitals;
import gr.gdschua.bloodapp.Entities.Hospital;
import gr.gdschua.bloodapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HospitalHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HospitalHomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    final DAOHospitals daoHospitals = new DAOHospitals();
    Hospital currUser;
    private boolean isFABOpen;

    public HospitalHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HospitalHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HospitalHomeFragment newInstance(String param1, String param2) {
        HospitalHomeFragment fragment = new HospitalHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) requireContext()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        isFABOpen = false;
        View view = inflater.inflate(R.layout.fragment_hospital_home, container, false);
        TextView email = view.findViewById(R.id.hosp_emailTextView);
        TextView name = view.findViewById(R.id.hosp_fullNameTextView);
        TextView address = view.findViewById(R.id.hosp_adrressTextView);
        daoHospitals.getUser().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    currUser = task.getResult().getValue(Hospital.class);
                    email.setText(Objects.requireNonNull(currUser).getEmail());
                    name.setText(currUser.getName());
                    address.setText(currUser.getAddress());
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        LinearLayout fabLayout1 = (LinearLayout) view.findViewById(R.id.alertFabLayout);
        LinearLayout fabLayout2 = (LinearLayout) view.findViewById(R.id.eventFabLayout);

        FloatingActionButton fab1 = (FloatingActionButton) view.findViewById(R.id.fab1);
        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HospitalAddAlertFragment hospitalAddAlertFragment = HospitalAddAlertFragment.newInstance(null, null);
                FragmentTransaction ft = ((MainActivity) requireActivity()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
                ft.replace(R.id.nav_host_fragment_content_hosp, hospitalAddAlertFragment).addToBackStack(null).commit();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HospitalAddEventFragment hospitalAddEventFragment = HospitalAddEventFragment.newInstance(null, null);
                FragmentTransaction ft = ((MainActivity) requireActivity()).getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_up, 0, 0, R.anim.slide_out_down);
                ft.replace(R.id.nav_host_fragment_content_hosp, hospitalAddEventFragment).addToBackStack(null).commit();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFABOpen) {
                    showFABMenu(fabLayout1, fabLayout2);
                } else {
                    closeFABMenu(fabLayout1, fabLayout2);
                }
            }
        });
        return view;
    }

    private void showFABMenu(LinearLayout fab1, LinearLayout fab2) {
        isFABOpen = true;
        requireActivity().findViewById(R.id.alertFabTxt).setVisibility(View.VISIBLE);
        requireActivity().findViewById(R.id.eventFabTxt).setVisibility(View.VISIBLE);
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu(LinearLayout fab1, LinearLayout fab2) {
        isFABOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isFABOpen) {
                    requireActivity().findViewById(R.id.alertFabTxt).setVisibility(View.INVISIBLE);
                    requireActivity().findViewById(R.id.eventFabTxt).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isFABOpen) {
                    fab1.setVisibility(View.INVISIBLE);
                    fab2.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}