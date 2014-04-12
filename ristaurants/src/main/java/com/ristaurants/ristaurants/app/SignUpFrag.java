package com.ristaurants.ristaurants.app;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Sign up user fragment
 */
public class SignUpFrag extends Fragment {
    // instance variables
    private static final int SELECT_PHOTO = 50;
    private EditText mEtUsername;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private ImageView mIvProfileImage;
    private Button mBtProfileImage;
    private Button mBtSignUp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_signup, null);

        // views
        mIvProfileImage = (ImageView) view.findViewById(R.id.iv_profile_image);
        mEtUsername =(EditText) view.findViewById(R.id.et_username);
        mEtEmail = (EditText) view.findViewById(R.id.et_email);
        mEtPassword = (EditText) view.findViewById(R.id.et_password);

        mBtProfileImage = (Button) view.findViewById(R.id.bt_Profile_image);
        mBtProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

        mBtSignUp = (Button) view.findViewById(R.id.bt_signup);
        mBtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser user = new ParseUser();
                user.setUsername(mEtUsername.getText().toString().toLowerCase());
                user.setPassword(mEtPassword.getText().toString().toLowerCase());
                user.setEmail(mEtEmail.getText().toString().toLowerCase());
                user.put("firstName", "Christian");
                user.put("lastName", "Soler");
                user.put("dob", "03-21-1980");
                user.put("bio", "Bio Here!");
                user.put("place", "Providence, RI");

                // listener
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Sign up was successful
                            // let user know to verify the email
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.please_verify_email);
                            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // do nothing
                                }
                            });

                            // show dialog
                            builder.show();

                            // finish fragment
                            getActivity().onBackPressed();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(getActivity(), "Sign up was not successful.\n\n" + e.getMessage() , Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // return view
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // refresh menu
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                    mIvProfileImage.setImageBitmap(yourSelectedImage);
                }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_login).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
}
