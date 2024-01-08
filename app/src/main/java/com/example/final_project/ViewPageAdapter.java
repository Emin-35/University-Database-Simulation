package com.example.final_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAdapter extends FragmentStateAdapter {
    //Constructor
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //Will return the page according to the position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdministrationFragment();
            case 1:
                return new StudentFragment();
            case 2:
                return new RegisterFragment();
            default:
                return new AdministrationFragment();
        }
    }

    //Total of Pages we have which is 3. Increase if new tab added
    @Override
    public int getItemCount() {
        return 3;
    }
}
