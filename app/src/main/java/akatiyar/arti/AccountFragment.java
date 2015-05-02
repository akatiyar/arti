package akatiyar.arti;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;

import akatiyar.arti.model.ArtiContent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {

    private TextView nameTextView;
    private TextView emailTextView;
    private Button logoutButton;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        nameTextView = (TextView) view.findViewById(R.id.account_name);
        emailTextView = (TextView) view.findViewById(R.id.account_email);
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            emailTextView.setText(currentUser.getEmail());
            String fullName = currentUser.getString("name");
            if (fullName != null) {
                nameTextView.setText(fullName);
            } else {
                nameTextView.setText("");
            }
        }

        logoutButton = (Button) view.findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLogout();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mListener.onFragmentAttached(ArtiContent.ACCOUNT);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
