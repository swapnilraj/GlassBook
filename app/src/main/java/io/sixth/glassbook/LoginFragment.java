package io.sixth.glassbook;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.sixth.glassbook.data.api.GlassBook;
import io.sixth.glassbook.data.local.User;
import okhttp3.Response;

/**
 * Created by thawne on 26/12/16.
 */

public class LoginFragment extends Fragment
    implements View.OnClickListener, GlassBook.AuthListener {

  private LoginManager mLoginManager;
  private EditText mUsername;
  private EditText mPassword;
  private ProgressBar mLoadingBar;
  private TextView mLoadingText;
  private Button mLoginButton;

  public LoginFragment() {
  }

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

    View view =  inflater.inflate(R.layout.fragment_login, null);
    mUsername = (EditText) view.findViewById(R.id.inputUsername);
    mPassword = (EditText) view.findViewById(R.id.inputPassword);
    mLoginButton = (Button) view.findViewById(R.id.loginButton);

    mLoadingBar = (ProgressBar) view.findViewById(R.id.progressBarLoading);
    mLoadingText = (TextView) view.findViewById(R.id.textLoading);

    dismissLoading();

    mLoginButton.setOnClickListener(this);

    return view;
  }

  private void showLoading() {
    mUsername.setVisibility(View.INVISIBLE);
    mPassword.setVisibility(View.INVISIBLE);
    mLoginButton.setVisibility(View.INVISIBLE);


    mLoadingBar.setVisibility(View.VISIBLE);
    mLoadingText.setVisibility(View.VISIBLE);
  }

  public void dismissLoading() {
    mUsername.setVisibility(View.VISIBLE);
    mPassword.setVisibility(View.VISIBLE);
    mLoginButton.setVisibility(View.VISIBLE);

    mLoadingBar.setVisibility(View.INVISIBLE);
    mLoadingText.setVisibility(View.INVISIBLE);
  }

  @Override public void onClick(View v) {
    showLoading();
    final String username = mUsername.getText().toString();
    final String password = mPassword.getText().toString();

    if (username.equals("") || password.equals("")) {
      mLoginManager.onFail();
    } else {
      GlassBook.authenticate(username, password, this);
    }
  }

  @Override public void onLoginFail(Response response) {
    dismissLoading();
    if (response.code() == 401 && getView() != null) {
      Snackbar.make(getView(), R.string.error_unauth, Snackbar.LENGTH_LONG).show();
    }
  }

  @Override public void onLoginSuccess(final User user) {
    dismissLoading();
    mLoginManager.onLogin(user);
  }
}
