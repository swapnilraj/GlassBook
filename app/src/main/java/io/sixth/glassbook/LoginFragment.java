package io.sixth.glassbook;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import io.realm.Realm;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.User;
import io.sixth.glassbook.utils.FragmentUtils;

/**
 * Created by thawne on 26/12/16.
 */

public class LoginFragment extends Fragment implements View.OnClickListener,
    GlassBook.AuthListener {

  private LoginManager mLoginManager;
  private EditText mUsername;
  private EditText mPassword;

  public LoginFragment() {}

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    try {
      mLoginManager = (LoginManager) context;
    } catch (ClassCastException e) {
      throw new ClassCastException(context.toString() + " must implement LoginManager");
    }
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.fragment_login, null);
    mUsername = (EditText) view.findViewById(R.id.inputUsername);
    mPassword = (EditText) view.findViewById(R.id.inputPassword);
    Button loginButton = (Button) view.findViewById(R.id.loginButton);
    loginButton.setOnClickListener(this);

    return view;
  }

  @Override public void onClick(View v) {
    final String username = mUsername.getText().toString();
    final String password = mPassword.getText().toString();

    if (username.equals("") || password.equals("")) {
      mLoginManager.onFail();
    } else {
      GlassBook.authenticate(username, password, this);
      //mLoginManager.onLogin(mUsername, mPassword);
    }
  }

  @Override public void onResult(final User user) {
    mLoginManager.onLogin(user);
  }
}
