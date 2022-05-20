package com.example.myproject.AdditionalEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myproject.R;

public class AddEventParent extends Fragment {
    protected Animation eventAnimation;
    protected View view;
    protected TextView title;
    protected OnDestroyed events;
    protected TextView description;
    protected Button destroyButton;

    public AddEventParent(OnDestroyed events){
        this.events = events;
    }

    public interface OnDestroyed {
        void OnDestroyAddEvent();
    }

    public void onPause() {
        super.onPause();

        SetViewDestroyingAnim();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.add_event_fragment, container, false);

        return view;
    }


    protected void SetViewCreatingAnim(){
        eventAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphacreate);
        view.startAnimation(eventAnimation);
    }

    private void SetViewDestroyingAnim(){
        eventAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.alphadel);
        view.startAnimation(eventAnimation);
    }

    protected void SetDestroyButton(){
        destroyButton = view.findViewById(R.id.continueButton);

        destroyButton.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().remove(AddEventParent.this).commit();
            events.OnDestroyAddEvent();
            onDestroy();
        });
    }
}
