package com.ristaurants.ristaurants.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.ristaurants.ristaurants.misc.SingletonVolley;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Fragment to display user profile
 */
public class UserProfileFrag extends Fragment {
    // instance variables
    private ParseFile mProfileImage;
    private NetworkImageView mIvProfileImage;
    private static final int SELECT_PHOTO = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_user_profile, null);

        // get user data
        if (ParseUser.getCurrentUser() != null) {
            // hide sign up container
            LinearLayout mLlLoginContainer = (LinearLayout) view.findViewById(R.id.ll_login_sign_up);
            mLlLoginContainer.setVisibility(View.GONE);

            // display user information
            displayUserInfo(view);

        } else {
            // handle login
            TextView mTvLogin = (TextView) view.findViewById(R.id.tv_profile_login);
            mTvLogin.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // login user
                    loginUser();
                }
            });

            // handle sign up
            TextView mTvSignUp = (TextView) view.findViewById(R.id.tv_profile_sign_up);
            mTvSignUp.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // display login fragment
                    signUp();
                }
            });
        }

        // return view
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
    }

    /**
     * try to login user to account
     */
    private void loginUser() {
        // dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // inflate and set the layout for the dialog
        builder.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null));

        // handle ok button
        builder.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // views
                EditText mEtUsername = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_login_username);
                EditText mEtPassword = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_login_password);

                // login user
                ParseUser.logInInBackground(mEtUsername.getText().toString(), mEtPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // let user know everything went well
                            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_LONG).show();

                            // reload fragment
                            Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("UserProfileFrag");
                            FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                            fragTrans.detach(frag);
                            fragTrans.attach(frag);
                            fragTrans.commit();
                        } else {
                            // Sign up failed. Look at the ParseException to see what happened.
                            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // handle cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // close dialog
                dialog.cancel();
            }
        });

        // show dialog
        builder.create().show();
    }

    private void signUp(){
        // dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // inflate and set the layout for the dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_sign_up, null);
        builder.setView(view);

        Button mBtProfileImage = (Button) view.findViewById(R.id.bt_upload_image);
        mBtProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PHOTO);
            }
        });

        // handle ok button
        builder.setPositiveButton(R.string.sign_up, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                // views
                EditText mEtUsername = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_username);
                EditText mEtEmail = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_email);
                EditText mEtPassword = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_password);
                EditText mEtFirstName = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_first);
                EditText mEtLastName = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_last);
                EditText mEtLocation = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_location);
                EditText mEtBio = (EditText) ((AlertDialog) dialog).findViewById(R.id.et_sign_up_bio);

                // sign up user
                ParseUser user = new ParseUser();
                user.setUsername(mEtUsername.getText().toString().toLowerCase());
                user.setPassword(mEtPassword.getText().toString().toLowerCase());
                user.setEmail(mEtEmail.getText().toString().toLowerCase());
                user.put("firstName", mEtFirstName.getText().toString().toLowerCase());
                user.put("lastName", mEtLastName.getText().toString().toLowerCase());
                user.put("location", mEtLocation.getText().toString().toLowerCase());
                user.put("bio", mEtBio.getText().toString().toLowerCase());

                // set image
                if (mProfileImage != null) {
                    user.put("userImage", mProfileImage);
                }

                // listener
                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            // Sign up was successful
                            // let user know to verify the email
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage(R.string.please_verify_email);
                            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // let user know everything went well
                                    Toast.makeText(getActivity(), "Sign up successful", Toast.LENGTH_LONG).show();

                                    // reload fragment
                                    Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("UserProfileFrag");
                                    FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragTrans.detach(frag);
                                    fragTrans.attach(frag);
                                    fragTrans.commit();
                                }
                            });

                            // show dialog
                            builder.show();

                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            // to figure out what went wrong
                            Toast.makeText(getActivity(), "Sign up was not successful.\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // handle cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // close dialog
                dialog.cancel();
            }
        });

        // show dialog
        builder.create().show();
    }

    /**
     * display user information
     */
    private void displayUserInfo(View view) {
        // get user data
        ParseUser user = ParseUser.getCurrentUser();

        // set user image
        mIvProfileImage = (NetworkImageView) view.findViewById(R.id.niv_profile_image);

        // check if user have an image
        if (user.getParseFile("userImage") != null) {
            mIvProfileImage.setImageUrl(user.getParseFile("userImage").getUrl(), SingletonVolley.getImageLoader());
        }

        // set user name
        TextView mTvUsername = (TextView) view.findViewById(R.id.tv_profile_username);
        mTvUsername.setText(user.getUsername());

        // set full name
        TextView mTvFullName = (TextView) view.findViewById(R.id.tv_profile_full_name);
        mTvFullName.setText(mTvFullName.getText().toString() + " " + user.getString("firstName") + " " + user.getString("lastName"));

        // set location
        TextView mTvLocation = (TextView) view.findViewById(R.id.tv_profile_location);
        mTvLocation.setText(mTvLocation.getText().toString() + " " + user.getString("location"));

        // set member since
        Date date = user.getCreatedAt();
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        TextView mTvMemberSince = (TextView) view.findViewById(R.id.tv_profile_member_since);
        mTvMemberSince.setText(mTvMemberSince.getText().toString() + " " + dateFormat.format(date));

        // set bio
        TextView mTvBio = (TextView) view.findViewById(R.id.tv_profile_bio);
        mTvBio.setText(mTvBio.getText().toString() + " " + user.getString("bio"));

        // set bio
        TextView mTvPost = (TextView) view.findViewById(R.id.tv_profile_post_count);
        mTvPost.setText(mTvPost.getText().toString() + " " + user.getInt("postTotal"));
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_user_profile).setVisible(false);
        menu.findItem(R.id.menu_profile_edit).setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // inflate menu
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // edit profile
        if (item.getItemId() == R.id.menu_profile_edit) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // get image from device
        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    try {
                        // input stream
                        InputStream imageStream = getActivity().getContentResolver().openInputStream(data.getData());

                        // get image from device
                        Bitmap image = BitmapFactory.decodeStream(imageStream);

                        // convert image to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        // convert image to parse file
                        mProfileImage = new ParseFile("profile_image.jpg", stream.toByteArray());
                        mProfileImage.saveInBackground();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
