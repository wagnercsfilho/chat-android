package app.wagnercsfilho.com.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import app.wagnercsfilho.com.whatsapp.fragment.ChatsFragment;
import app.wagnercsfilho.com.whatsapp.fragment.ContactsFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tabsTitle = {
      "CONVERSAS", "CONTATOS"
    };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ChatsFragment();
                break;
            case 1:
                fragment = new ContactsFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabsTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsTitle[position];
    }
}
