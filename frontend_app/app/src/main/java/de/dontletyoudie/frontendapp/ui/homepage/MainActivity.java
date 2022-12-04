package de.dontletyoudie.frontendapp.ui.homepage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.dontletyoudie.frontendapp.R;
import de.dontletyoudie.frontendapp.databinding.ActivityMainBinding;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.FriendsFragment;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.HomeFragment;
import de.dontletyoudie.frontendapp.ui.homepage.fragments.JudgeFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();
        JudgeFragment judgeFragment = new JudgeFragment();
        FriendsFragment friendsFragment = new FriendsFragment();

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
            }

            return true;
        });

    }

    private void makeCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_wrapper, fragment).commit();
    }


}