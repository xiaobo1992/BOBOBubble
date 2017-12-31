package com.bobo.normalman.bobobubble;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bobo.normalman.bobobubble.dribble.Dribble;
import com.bobo.normalman.bobobubble.view.bucket.BucketListFragment;
import com.bobo.normalman.bobobubble.view.shot_list.ShotListFragment;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawer)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, ShotListFragment.newInstance(ShotListFragment.KEY_POPULAR_SHOT))
                    .commit();
            setTitle(R.string.shots);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setupNavDrawer();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
    }

    public void setupNavDrawer() {
        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                /*if (item.isChecked()) {
                    drawerLayout.closeDrawers();
                    return true;
                }*/
                switch (item.getItemId()) {
                    case R.id.drawer_menu_like_shots:
                        fragment = ShotListFragment.newInstance(ShotListFragment.KEY_LIKE_SHOT);
                        setTitle(R.string.likes);
                        break;
                    case R.id.drawer_menu_popular_shots:
                        fragment = ShotListFragment.newInstance(ShotListFragment.KEY_POPULAR_SHOT);
                        setTitle(R.string.shots);
                        break;
                    case R.id.drawer_menu_attachments_shots:
                        fragment = ShotListFragment.newInstance(ShotListFragment.KEY_ATTACHMENTS_SHOT);
                        setTitle(R.string.attachments);
                        break;
                    case R.id.drawer_menu_debut_shots:
                        fragment = ShotListFragment.newInstance(ShotListFragment.KEY_DEBUTS_SHOT);
                        setTitle(R.string.debuts);
                        break;
                    case R.id.drawer_menu_animated_shots:
                        fragment = ShotListFragment.newInstance(ShotListFragment.KEY_ANIMATED_SHOT);
                        setTitle(R.string.animated);
                        break;
                    case R.id.drawer_menu_team_shots:
                        fragment = ShotListFragment.newInstance(ShotListFragment.KEY_TEAMS_SHOT);
                        setTitle(R.string.teams);
                        break;
                    case R.id.drawer_menu_buckets:
                        fragment = BucketListFragment.newInstance(false, null);
                        setTitle(R.string.buckets);
                        break;
                }

                drawerLayout.closeDrawers();
                if (fragment != null) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                    return true;
                }
                return false;
            }

        });
        setNavHeader();
    }

    public void setNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.nav_username);
        userName.setText(Dribble.getCurrentUser().name);

        ((SimpleDraweeView) headerView.findViewById(R.id.nav_avatar))
                .setImageURI(Uri.parse(Dribble.getCurrentUser().avatar_url));
        TextView logout = headerView.findViewById(R.id.nav_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dribble.logout(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
