package com.vladan.dragdropimages;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DragDropActivity extends AppCompatActivity {

    List<BasisGrid> basisGrids = new ArrayList<>();
    GridView gridView;
    ClipData dragData;
    int position;
    int dragPosition;
    Object current = null;
    RelativeLayout screenLayout;
    public static final String TAG = "drag_drop";
    Timer timer;
    int yLocation;
    ImageView trashZone;
    Drawable originalBackground;
    GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop);

        int[] imageId = new int[10];
        imageId[0] = R.drawable.image_1;
        imageId[1] = R.drawable.image_2;
        imageId[2] = R.drawable.image_3;
        imageId[3] = R.drawable.image_4;
        imageId[4] = R.drawable.image_5;
        imageId[5] = R.drawable.image_6;
        imageId[6] = R.drawable.image_7;
        imageId[7] = R.drawable.image_8;
        imageId[8] = R.drawable.image_9;
        imageId[9] = R.drawable.image_10;

        String[] textComment = new String[10];
        for (int i = 0; i < 10; i++) {
            textComment[i] = "Image number" + " " + String.valueOf(i + 1);
        }
        for (int i = 0; i < 10; i++) {
            BasisGrid bg = new BasisGrid(textComment[i], imageId[i]);
            basisGrids.add(bg);
        }
        screenLayout = (RelativeLayout) findViewById(R.id.screenLayout);
        gridView = (GridView) findViewById(R.id.gridImage);
        trashZone = (ImageView) findViewById(R.id.trash);
        gridAdapter = new GridAdapter(this, basisGrids);
        gridView.setAdapter(gridAdapter);
        gridAdapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressWarnings("deprecation")

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int p, long id) {
                position = p;
                current = view;

                if (position > AdapterView.INVALID_POSITION) {

                    ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
                    dragData = new ClipData((CharSequence) view.getTag(),
                            new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                    view.startDrag(dragData,
                            myShadow,
                            null,
                            0);
                    screenLayout.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View v, DragEvent dragEvent) {
                            v = (View) current;
                            boolean result = true;


                            int action = dragEvent.getAction();

                            switch (action) {
                                case DragEvent.ACTION_DRAG_STARTED:
                                    Log.d(TAG, "started");
                                    break;
                                case DragEvent.ACTION_DRAG_ENTERED:
                                    if (timer != null) {
                                        timer.cancel();
                                        timer.purge();
                                        timer = null;
                                    }
                                    Log.d(TAG, "entered");
                                    break;
                                case DragEvent.ACTION_DRAG_EXITED:
                                    if (yLocation < 50) {
                                        scroll(-40, 2);
                                    }
                                    if (yLocation > 950) {
                                        scroll(40, 2);
                                    }
                                    Log.d(TAG, "exited");
                                    break;
                                case DragEvent.ACTION_DROP:
                                    int dropX = (int) dragEvent.getX();
                                    int dropY = (int) dragEvent.getY();
                                    dragPosition = gridView.pointToPosition(dropX, dropY);
                                    Log.i(TAG, "drop" + " " + String.valueOf(dragPosition));
                                    int trashY = (int) trashZone.getY();

                                    if (dragEvent.getLocalState() == v) {
                                        return false;
                                    } else {
                                        if (yLocation > trashY) {
                                            originalBackground = ((View) current).getBackground();
                                            ((View) current).setBackgroundColor(Color.RED);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(DragDropActivity.this);
                                            builder.setMessage("Do you really want to delete this image")
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            ((View) current).setBackgroundDrawable(originalBackground);
                                                            basisGrids.remove(position);
                                                            for (int i = 0; i < basisGrids.size() - position; i++) {
                                                                basisGrids.get(position + i)
                                                                        .setTextComment("Image number" + " "
                                                                                + String.valueOf(position + i + 1));

                                                            }
                                                            gridAdapter.notifyDataSetChanged();
                                                        }
                                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    ((View) current).setBackgroundDrawable(originalBackground);
                                                    dialog.cancel();
                                                }
                                            });
                                            builder.create().show();
                                        } else {
                                            BasisGrid pickImage = basisGrids.get(position);
                                            pickImage.setTextComment("Image number" + " " + String.valueOf(dragPosition + 1));
                                            BasisGrid pickPlace = basisGrids.get(dragPosition);
                                            pickPlace.setTextComment("Image number" + " " + String.valueOf(position + 1));
                                            basisGrids.remove(position);
                                            basisGrids.add(position, pickPlace);
                                            basisGrids.remove(dragPosition);
                                            basisGrids.add(dragPosition, pickImage);
                                            gridAdapter.notifyDataSetChanged();

                                        }


                                    }

                                    break;
                                case DragEvent.ACTION_DRAG_ENDED:
                                    if (timer != null) {
                                        timer.cancel();
                                        timer.purge();
                                        timer = null;
                                    }
                                    Log.d(TAG, "ended");
                                    break;
                                case DragEvent.ACTION_DRAG_LOCATION:
                                    yLocation = (int) dragEvent.getY();

                                    Log.d(TAG, String.valueOf(yLocation));
                                    break;
                                default:
                                    result = false;
                                    break;
                            }
                            return result;
                        }
                    });
                }
                return false;
            }
        });
    }

    private void scroll(final int d, final int t) {

        TimerTask startScroll = new TimerTask() {
            @Override
            public void run() {
                gridView.smoothScrollBy(d, t);
            }
        };
        timer = new Timer();
        timer.scheduleAtFixedRate(startScroll, 0, 250);
    }


}
