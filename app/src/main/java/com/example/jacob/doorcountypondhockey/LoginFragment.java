package com.example.jacob.doorcountypondhockey;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button loginButton;
    private EditText emailText;
    private EditText passwordText;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginButton = (Button) view.findViewById(R.id.loginButton);
        emailText = (EditText) view.findViewById(R.id.emailTextView);
        passwordText = (EditText) view.findViewById(R.id.passwordTextView);
        mAuth = FirebaseAuth.getInstance();

        //Listens for the user to login in which case it switches to the score entry fragment
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.content, ScoreEntryFragment.newInstance());
                    transaction.commit();
                } else {
                    // User is signed out
                }
            }
        };

        //Signs user in if credentials are approved
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int emailTextLength = emailText.getText().toString().length();
                int passwordTextLength = passwordText.getText().toString().length();
                if (emailTextLength == 0 || passwordTextLength == 0) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(emailText.getText().toString(),
                        passwordText.getText().toString()).
                        addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in
                                // succeeds the auth state listener will be notified and logic to
                                // handle the signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getActivity(),
                                            getResources().getString(R.string.login_failed),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
