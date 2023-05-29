package de.dontletyoudie.frontendapp.ui.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.databinding.ActivityMainBinding;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.HomeFragment;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.JudgeFragment;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeCurrentFragment(new HomeFragment());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_item_home:
                    makeCurrentFragment(new HomeFragment());
                    break;
                case R.id.nav_item_friends:
                    makeCurrentFragment(new FriendsFragment());
                    break;
                case R.id.nav_item_judge:
                    makeCurrentFragment(new JudgeFragment());
                    break;
                case R.id.nav_item_settings:
                    makeCurrentFragment(new SettingsFragment());
                    break;
                case R.id.nav_item_add_proof:
                    makeCurrentFragment(new HomeFragment());
                    navigateToTakePictureActivity();
                    break;
            }
            return true;
        });

        binding.bottomNavigation.setSelectedItemId(R.id.nav_item_home);

    }

    private void makeCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper, fragment).commit();
    }

    public void navigateToTakePictureActivity() {
        //TODO this for some reason just doesn't work...
        // wanted to also select the icon to home after taking a photo
        binding.bottomNavigation.setSelectedItemId(R.id.nav_item_home);
        Intent i = new Intent(this, TakePictureActivity.class);
        startActivity(i);
    }

}