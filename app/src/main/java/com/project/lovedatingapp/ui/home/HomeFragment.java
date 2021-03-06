 package com.project.lovedatingapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.project.lovedatingapp.MainActivity;
import com.project.lovedatingapp.adapters.SwipeAdapter;
import com.project.lovedatingapp.databinding.FragmentHomeBinding;
import com.project.lovedatingapp.interfaces.IOnClickCard;
import com.project.lovedatingapp.models.Image;
import com.project.lovedatingapp.models.User;
import com.project.lovedatingapp.utils.Common;
import com.project.lovedatingapp.views.DetailActivity;
import com.yalantis.library.Koloda;
import com.yalantis.library.KolodaListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements IOnClickCard {
    public static final String EXTRA_CANCEL_CARD = "EXTRA_CANCEL_CARD";

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private List<User> mListUser;
    private SwipeAdapter swipeAdapter;
    private Koloda kldCard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        String cancelCard = (getActivity()).getIntent().getStringExtra(EXTRA_CANCEL_CARD);

        homeViewModel.getListUser().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users != null){
                    mListUser = users;
                    swipeAdapter.setListUser(users);
                    Common.mListUserCategory = users;
                }
            }
        });

        homeViewModel.getUserCurrent().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Common.user = user;
            }
        });

        mListUser = new ArrayList<>();
        kldCard = binding.kldCard;

        swipeAdapter = new SwipeAdapter(getContext(), mListUser, this);
        kldCard.setAdapter(swipeAdapter);
        kldCard.setKolodaListener(new KolodaListener() {
            @Override
            public void onNewTopCard(int i) {

            }

            @Override
            public void onCardDrag(int i, @NotNull View view, float v) {

            }

            @Override
            public void onCardSwipedLeft(int i) {

            }

            @Override
            public void onCardSwipedRight(int i) {

            }

            @Override
            public void onClickRight(int i) {

            }

            @Override
            public void onClickLeft(int i) {

            }

            @Override
            public void onCardSingleTap(int i) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailActivity.EXTRA_ID_USER, mListUser.get(i+1).getId());
                intent.putExtra(DetailActivity.EXTRA_URL_IMAGE, mListUser.get(i+1).getListImage().get(0).getUrl());
//                intent.putExtra(DetailActivity.EXTRA_CHECK_HOME, "Home");
                startActivity(intent);
            }

            @Override
            public void onCardDoubleTap(int i) {

            }

            @Override
            public void onCardLongPress(int i) {

            }

            @Override
            public void onEmptyDeck() {

            }
        });

        homeViewModel.callToGetImage();
        homeViewModel.callToGetUserCurrent();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        String cancelCard = (getActivity()).getIntent().getStringExtra(EXTRA_CANCEL_CARD);
//        if(cancelCard != null){
//            kldCard.onButtonClick(false);
//
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickBtnLike(User user) {
        kldCard.onButtonClick(true);
    }

    @Override
    public void onClickBtnMessage(User user) {

    }

    @Override
    public void onClickBtnDelete(User user) {
        kldCard.onButtonClick(false);
    }
}