package com.example.copyversion;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_IDLE;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

class MessageSwipeController extends ItemTouchHelper.Callback implements SwipeControllerActions{


    private  Drawable imageDrawable;
    private Drawable shareRound=null;
    private float replyButtonProgress = 0;
    private long lastReplyButtonAnimationTime=0;
    private boolean swipeBack = false;
    private boolean isVibrate = false;
    SwipeControllerActions swipeControllerActions;
    Context context;
    View mView;
    Float dX=0f;
    RecyclerView l;
    boolean startTracking=false;
    RecyclerView.ViewHolder currentItemViewHolder=null;
    MessageSwipeController(RecyclerView List, Context context, SwipeControllerActions swipeControllerActions) {
        super();
        this.context=context;
        this.swipeControllerActions=swipeControllerActions;
        this.l=List;
    }

    private static int convertTodp(int x,Context context)
    {
        AndroidUtils androidUtils=new AndroidUtils();
        return androidUtils.dp(((float) x),context);
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        mView=viewHolder.itemView;
        imageDrawable = context.getDrawable(R.drawable.share);
                shareRound = context.getDrawable(R.drawable.circle_corner);

        return ItemTouchHelper.Callback.makeMovementFlags(ACTION_STATE_IDLE, ItemTouchHelper.RIGHT);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

         l.getAdapter().notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
    }
   @Override
   public int convertToAbsoluteDirection(int flags,int layoutDirection) {
        if (swipeBack) {
            swipeBack = false;
            return 0;
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        float newDx=dX;
        if(newDx>=100f)
        {
            newDx=100f;
        }

        super.onChildDraw(c, recyclerView, viewHolder, newDx, dY, actionState, isCurrentlyActive);
        // Set elevation for dragged itemView



        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            setTouchListener(recyclerView, viewHolder);
        }

        if (viewHolder.itemView.getTranslationX() < convertTodp(130,context) || dX < this.dX) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            this.dX = dX;
            startTracking = true;
        }
        currentItemViewHolder = viewHolder;
        drawReplyButton(c);

    }


    private void drawReplyButton(Canvas c)
    {
        if (currentItemViewHolder == null) {
            return;
        }
        float translationX = mView.getTranslationX();
        long newTime = System.currentTimeMillis();
        long dt = Math.min(17, newTime - lastReplyButtonAnimationTime);
        lastReplyButtonAnimationTime = newTime;
        boolean showing = translationX >= convertTodp(30,context)?true:false;
        if (showing) {
            if (replyButtonProgress < 1.0f) {
                replyButtonProgress += dt / 180.0f;
                if (replyButtonProgress > 1.0f) {
                    replyButtonProgress = 1.0f;
                } else {
                    mView.invalidate();
                }
            }
        } else if (translationX <= 0.0f) {
            replyButtonProgress = 0f;
            startTracking = false;
            isVibrate = false;
        } else {
            if (replyButtonProgress > 0.0f) {
                replyButtonProgress -= dt / 180.0f;
                if (replyButtonProgress < 0.1f) {
                    replyButtonProgress = 0f;
                } else {
                    mView.invalidate();
                }
            }
        }
        int alpha;
        float scale;
        if (showing) {
            if (replyButtonProgress <= 0.8f) {
               scale= 1.2f * (replyButtonProgress / 0.8f);
            } else {
               scale= 1.2f - 0.2f * ((replyButtonProgress - 0.8f) / 0.2f);
            }
            alpha = (int)(Math.min(255f, 255 * (replyButtonProgress / 0.8f)));
        } else {
            scale = replyButtonProgress;
            alpha = (int) Math.min(255f, 255 * replyButtonProgress);
        }
        shareRound.setAlpha(alpha) ;

        imageDrawable.setAlpha(alpha);
        if (startTracking) {
            if (!isVibrate && mView.getTranslationX() >= convertTodp(100,context)) {
                mView.performHapticFeedback(
                        HapticFeedbackConstants.KEYBOARD_TAP,
                        HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
                );
                isVibrate = true;
            }
        }

        int x;
         if (mView.getTranslationX() > convertTodp(130,context)) {
       x= convertTodp(130,context) / 2;
    } else {
             x = (int) (mView.getTranslationX() / 2);
    }

        float y = (float) (mView.getTop() + mView.getMeasuredHeight() / 2);
        shareRound.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context,R.color.primaryColor), PorterDuff.Mode.MULTIPLY));


        shareRound.setBounds(
                (int) (x - convertTodp(18,context) * scale),
                (int) (y - convertTodp(18,context) * scale),
                (int) (x + convertTodp(18,context) * scale),
                (int) (y + convertTodp(18,context) * scale)

        );
        shareRound.draw(c);
        imageDrawable.setBounds(
                (int) (x - convertTodp(12,context) * scale),
                (int)(y - convertTodp(11,context) * scale),
                (int)(x + convertTodp(12,context) * scale),
                (int)(y + convertTodp(10,context) * scale));


        imageDrawable.draw(c);
        shareRound.setAlpha(255);
        imageDrawable.setAlpha(255) ;
    }

    private void setTouchListener(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder)
    {
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean swipeBack = event.ACTION_CANCEL == MotionEvent.ACTION_CANCEL || event.ACTION_UP== MotionEvent.ACTION_UP;
                if (swipeBack) {

                    if (Math.abs(viewHolder.itemView.getTranslationX()) >= MessageSwipeController.convertTodp(100,context)) {

                         swipeControllerActions.showReplyUI(viewHolder.getAbsoluteAdapterPosition());
                    }
                }
                return false;
            }
        });
    }


}