package hagai.edu.firebasedatabase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.Scopes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hagai.edu.firebasedatabase.models.User;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    //properties:
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseUser user;

    private static final int RC_SIGN_IN = 1;

    FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

            user = firebaseAuth.getCurrentUser();
            if (user == null) {
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.
                                Builder(AuthUI.GOOGLE_PROVIDER).
                                setPermissions(
                                        Arrays.asList(Scopes.PROFILE, Scopes.EMAIL)
                                ).build(),

                        new AuthUI.IdpConfig.
                                Builder(AuthUI.FACEBOOK_PROVIDER).
                                build(),

                        new AuthUI.IdpConfig.
                                Builder(AuthUI.EMAIL_PROVIDER).
                                build());

                Intent intent = AuthUI.
                        getInstance().
                        createSignInIntentBuilder().
                        setLogo(R.drawable.logo).
                        setAvailableProviders(providers).build();
                startActivityForResult(intent, RC_SIGN_IN);
            } else {
                //We have a user:
                addShoppingListFragment();
            }
        }
    };

    private void addShoppingListFragment(){

        //If I'ts not already shown, Show it now.
        if (getSupportFragmentManager().findFragmentByTag("ShoppingList") == null)
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.container, new ShoppingListFragment(), "ShoppingList").
                    commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);


            if (resultCode == RESULT_OK) {
                //0) create a user
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                //convert it to a user:
                User user = new User(currentUser);

                //save the user
                //1) ref the table.
                DatabaseReference ref = FirebaseDatabase.getInstance().
                        getReference("Users").child(user.getUid());
                //2) push()... setValue
                ref.setValue(user);
            }// //else if(idpResponse!=null && idpResponse.getError)
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_sign_out:
                AuthUI.getInstance().signOut(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            addShoppingListFragment();
        } else if (id == R.id.nav_gallery) {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.container, new WhatsappFragment()).
                    commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

//"C:\Program Files\Java\jdk1.8.0_131\bin\keytool" -exportcert -alias AndroidDebugKey -keystore %HOMEPATH%\.android\debug.keystore | "C:\Users\Android2017\Desktop\openssl\bin\openssl" sha1 -binary | "C:\Users\Android2017\Desktop\openssl\bin\openssl" base64
