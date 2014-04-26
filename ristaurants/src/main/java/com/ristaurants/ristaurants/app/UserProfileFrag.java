package com.ristaurants.ristaurants.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.ristaurants.ristaurants.misc.HelperClass;
import com.ristaurants.ristaurants.misc.SingletonVolley;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.Date;

/**
 * Fragment to display user profile
 */
public class UserProfileFrag extends Fragment {
    // instance variables
    private ParseFile mProfileImage;
    private ImageView mIvProfileImagePreview;
    private static final int SELECT_PHOTO = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflate view
        View view = inflater.inflate(R.layout.frag_user_profile, null);

        // set-up action bar
        getActivity().getActionBar().setTitle(HelperClass.setActionbarTitle(getActivity(), getResources().getString(R.string.ab_title_profile)));

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

    /**
     * Try to login user to account
     */
    private void loginUser() {
        // dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // get dialog view
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_login, null);

        // views
        final EditText mEtEmail = (EditText) view.findViewById(R.id.et_login_email);
        final EditText mEtPassword = (EditText) view.findViewById(R.id.et_login_password);
        TextView mTvForgotPassword = (TextView) view.findViewById(R.id.tv_forgot_password);

        // handle forgot password
        mTvForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword(mEtEmail.getText().toString());
            }
        });

        // inflate and set the layout for the dialog
        builder.setView(view);

        // handle ok button
        builder.setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {


                // login user
                ParseUser.logInInBackground(mEtEmail.getText().toString(), mEtPassword.getText().toString(), new LogInCallback() {
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
                            /// log error
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

    /**
     * Try to sign up user
     */
    private void signUp() {
        // dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // inflate and set the layout for the dialog
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_sign_up, null);
        builder.setView(view);

        // views
        final EditText mEtEmail = (EditText) view.findViewById(R.id.et_sign_up_email);
        final EditText mEtPassword = (EditText) view.findViewById(R.id.et_sign_up_password);
        final EditText mEtFirstName = (EditText) view.findViewById(R.id.et_sign_up_first);
        final EditText mEtLastName = (EditText) view.findViewById(R.id.et_sign_up_last);
        final EditText mEtLocation = (EditText) view.findViewById(R.id.et_sign_up_location);
        final EditText mEtBio = (EditText) view.findViewById(R.id.et_sign_up_bio);

        // upload avatar preview
        mIvProfileImagePreview = (ImageView) view.findViewById(R.id.iv_sign_up_image_preview);
        mIvProfileImagePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery, select image and crop it
                selectProfileImage();
            }
        });

        // handle ok button
        builder.setPositiveButton(R.string.sign_up, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //
                if (!mEtEmail.getText().toString().equals("") &&
                        !mEtFirstName.getText().toString().equals("") &&
                        !mEtLastName.getText().toString().equals("") &&
                        !mEtPassword.getText().toString().equals("") &&
                        !mEtLocation.getText().toString().equals("") &&
                        !mEtBio.getText().toString().equals("")) {

                    // sign up user
                    ParseUser user = new ParseUser();
                    user.setUsername(mEtEmail.getText().toString().toLowerCase());
                    user.setPassword(mEtPassword.getText().toString().toLowerCase());
                    user.setEmail(mEtEmail.getText().toString().toLowerCase());
                    user.put("firstName", mEtFirstName.getText().toString().toLowerCase());
                    user.put("lastName", mEtLastName.getText().toString().toLowerCase());
                    user.put("location", mEtLocation.getText().toString().toLowerCase());
                    user.put("bio", mEtBio.getText().toString().toLowerCase());
                    user.put("postTotal", 0);

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
                                        reloadFragment();
                                    }
                                });

                                // show dialog
                                builder.show();

                            } else {
                                // log error
                                Toast.makeText(getActivity(), "Sign up was not successful.\n\n" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    // let user know to complete all fields
                    Toast.makeText(getActivity(), R.string.please_fill_all_fields, Toast.LENGTH_LONG).show();
                }
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
     * handle user forgot password
     */
    private void forgotPassword(String email) {
        if (!email.equals("") || !email.equals(null)) {
            ParseUser.requestPasswordResetInBackground(email,
                    new RequestPasswordResetCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // An email was successfully sent with reset instructions.
                                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("An email was successfully sent with password reset instructions.");
                                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });

                                // show dialog
                                builder.show();

                            } else {
                                // Something went wrong. Look at the ParseException to see what's up.
                                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
            );
        }
    }

    /**
     * Display user information
     */
    private void displayUserInfo(View view) {
        // get user data
        final ParseUser user = ParseUser.getCurrentUser();

        // check if user have an image
        if (user.getParseFile("userImage") != null) {// set user image
            NetworkImageView mIvProfileImage = (NetworkImageView) view.findViewById(R.id.niv_profile_image);
            mIvProfileImage.setImageUrl(user.getParseFile("userImage").getUrl(), SingletonVolley.getImageLoader());
        }

        // set full name
        TextView mTvFullName = (TextView) view.findViewById(R.id.tv_profile_full_name);
        mTvFullName.setText(user.getString("firstName") + " " + user.getString("lastName"));

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

    /**
     * Open gallery to select image and crop.
     */
    private void selectProfileImage() {
        // open gallery, select image and crop it
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 128);
        intent.putExtra("outputY", 128);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, SELECT_PHOTO);
    }

    /**
     * Reload fragment
     */
    private void reloadFragment() {
        // reload fragment
        Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("UserProfileFrag");
        FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
        fragTrans.detach(frag);
        fragTrans.attach(frag);
        fragTrans.commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
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
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        // input stream
                        Bundle extras = data.getExtras();

                        // get image from device
                        Bitmap image = extras.getParcelable("data");
                        image = HelperClass.getCircleImage(image);

                        // convert image to byte[]
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        // set preview avatar image
                        mIvProfileImagePreview.setImageBitmap(image);

                        // convert image to parse file
                        mProfileImage = new ParseFile("profile_image.jpg", stream.toByteArray());
                        mProfileImage.saveInBackground();

                        if (ParseUser.getCurrentUser() != null) {
                            ParseUser user = ParseUser.getCurrentUser();
                            user.put("userImage", mProfileImage);

                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    // reload fragment
                                    Fragment frag = getActivity().getSupportFragmentManager().findFragmentByTag("UserProfileFrag");
                                    FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragTrans.detach(frag);
                                    fragTrans.attach(frag);
                                    fragTrans.commit();
                                }
                            });
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
