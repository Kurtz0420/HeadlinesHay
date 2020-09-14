package com.client.headlineshay.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.homie.psychq.BaseApplication;
import com.homie.psychq.R;
import com.homie.psychq.main.models.feeds.Announcement;
import com.homie.psychq.utils.SharedPreferences;

import java.util.List;


/*PagerAdapter For announcements*/

public class HeadlinesPagerAdapter extends PagerAdapter {

    private static final String TAG = "JokesAdapter";

    private Context context;
    private List<Announcement> announcementList;


//    private JokeAdapterCallbacks jokeAdapterCallbacks;
    private SharedPreferences sharedPreferences;

    public HeadlinesPagerAdapter(Context context, SharedPreferences sharedPreferences, List<Announcement> announcementList) {
    this.context = context;
    this.announcementList = announcementList;
//    this.jokeAdapterCallbacks = jokeAdapterCallbacks;
    this.sharedPreferences = sharedPreferences;

    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater mInflater = LayoutInflater.from(context);

        View item_view ;

        //user isPaid, so we show joke at every position
        item_view = mInflater.inflate(R.layout.announcement_item_layout, container, false);


        TextView titleTv = item_view.findViewById(R.id.heading);
        final TextView messageTv = item_view.findViewById(R.id.cc_header_tv);
        final TextView noteTv = item_view.findViewById(R.id.note_feeds_header);



        titleTv.setText(announcementList.get(position).getHeading());
        messageTv.setText(announcementList.get(position).getMessage());



        if(!sharedPreferences.getBooleanPref(BaseApplication.get().getString(R.string.isSubscribed),false)){
            noteTv.setVisibility(View.VISIBLE);
        }else {
            noteTv.setVisibility(View.GONE);
        }
        container.addView(item_view);
        return item_view;
    }






    public void slideUp(final TextView view){

        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight()/4,  // fromYDelta
                - view.getHeight());                // toYDelta
        animate.setDuration(250);

        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                view.setVisibility(View.INVISIBLE);



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        view.startAnimation(animate);

    }

    public void slideDown(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                - view.getHeight() ,                 // fromYDelta
                view.getHeight()/4); // toYDelta
        animate.setDuration(250);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    private void animationUsingConstraintSet() {
//        final boolean[] set = {false};
//        final ConstraintSet constraint1 = new ConstraintSet();
//        constraint1.clone(revealRootLayout);
//        final ConstraintSet constraint2 = new ConstraintSet();
//        constraint2.clone(context, R.layout.snippet_reveal_after_btn);
//
//
//        revealBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                    TransitionManager.beginDelayedTransition(revealRootLayout);
//
//                    ConstraintSet constraint = set[0] ?  constraint1 : constraint2;
//                    constraint.applyTo(revealRootLayout);
//                    set[0] = !set[0];
//                }
//            }
//        });
    }


    @Override
    public int getCount() {
        return announcementList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout) object);

    }

    public void addAnnouncements(List<Announcement> announcements){
        int lastCount = getCount();
        announcementList.addAll(announcements);
//        notifyItemRangeInserted(lastCount, jokes.size());
    }




    public interface JokeAdapterCallbacks{

        void onRevealClick();
    }


}
