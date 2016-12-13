package charlyn23.squadlyapp;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by charlynbuchanan on 12/11/16.
 */

public class InstructionActivity extends AppCompatActivity implements Animation.AnimationListener{

    Button gotIt;
    TextView step1;
    TextView step2;
    TextView step3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_instruction);

        Typeface font = Typeface.createFromAsset(getAssets(), "rez.ttf");

        TextView title = (TextView) findViewById(R.id.titleTextView);
        title.setTypeface(font);

        TextView slugline = (TextView) findViewById(R.id.sluglineTextView);
        slugline.setTypeface(font);

        gotIt = (Button) findViewById(R.id.gotItButton);
        gotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InstructionActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //Instructions + button to fade in
        step1 = (TextView) findViewById(R.id.step1);
        step2 = (TextView) findViewById(R.id.step2);
        step3 = (TextView) findViewById(R.id.step3);
        step1.setAlpha(0);
        step2.setAlpha(0);
        step3.setAlpha(0);
        gotIt.setAlpha(0);


        ObjectAnimator step1FadeIn = ObjectAnimator.ofFloat(step1, "alpha", 0, 1);
        ObjectAnimator step2FadeIn = ObjectAnimator.ofFloat(step2, "alpha", 0, 1);
        ObjectAnimator step3FadeIn = ObjectAnimator.ofFloat(step3, "alpha", 0, 1);
        ObjectAnimator gotItFadeIn = ObjectAnimator.ofFloat(gotIt, "alpha", 0, 1);

        List<Animator> animators = new ArrayList<Animator>();
        animators.add(step1FadeIn);
        animators.add(step2FadeIn);
        animators.add(step3FadeIn);
        animators.add(gotItFadeIn);

        AnimatorSet instructionsFadeIn = new AnimatorSet();
        instructionsFadeIn.setDuration(3000);
        instructionsFadeIn.playSequentially(animators);
        instructionsFadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        instructionsFadeIn.start();

    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        step2.startAnimation(animation);

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
