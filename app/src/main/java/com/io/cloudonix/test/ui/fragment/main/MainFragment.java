package com.io.cloudonix.test.ui.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.io.cloudonix.domain.view_models.MainViewModel;
import com.io.cloudonix.test.R;
import com.io.cloudonix.test.databinding.FragmentMainBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        binding.setFragment(this);
        binding.setViewModel(mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
    }

    private void setupUI() {
        observeIPAddressLiveData();
        observeErrorLiveData();
    }

    private void observeErrorLiveData() {
        mViewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show());
    }

    private void observeIPAddressLiveData() {
        mViewModel.getSendAddressResponseLiveData().observe(getViewLifecycleOwner(), response -> {
            setUIVisibility(true);
            setupResponseUI(response.getNat());
        });
    }

    private void setupResponseUI(Boolean nat) {
        if (nat) {
            binding.lottieAnimation.setAnimation("anim_ok.json");
        } else {
            binding.lottieAnimation.setAnimation("anim_cancel.json");
        }
    }

    public void sendIpAddressToServer() {
        mViewModel.reset();
        setUIVisibility(false);
    }

    private void setUIVisibility(Boolean isVisible) {
        if (isVisible) {
            binding.tvIPAddress.setVisibility(View.VISIBLE);
            binding.lottieAnimation.setVisibility(View.VISIBLE);
        } else {
            binding.tvIPAddress.setVisibility(View.GONE);
            binding.lottieAnimation.setVisibility(View.GONE);
        }
    }
}