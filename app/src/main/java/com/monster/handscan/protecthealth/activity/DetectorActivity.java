/*
 * Copyright 2019 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monster.handscan.protecthealth.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Size;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.monster.handscan.protecthealth.R;
import com.monster.handscan.protecthealth.customview.OverlayView;
import com.monster.handscan.protecthealth.database.SQLiteDBHelper;
import com.monster.handscan.protecthealth.env.BorderedText;
import com.monster.handscan.protecthealth.env.ImageUtils;
import com.monster.handscan.protecthealth.env.Logger;
import com.monster.handscan.protecthealth.detection.tflite.Detector;
import com.monster.handscan.protecthealth.detection.tflite.TFLiteObjectDetectionAPIModel;
import com.monster.handscan.protecthealth.model.ScanHistoryModel;
import com.monster.handscan.protecthealth.tracking.MultiBoxTracker;

/**
 * An activity that uses a TensorFlowMultiBoxDetector and ObjectTracker to detect and then track
 * objects.
 */
public class DetectorActivity extends CameraActivity implements OnImageAvailableListener {
    private static final Logger LOGGER = new Logger();

    // Configuration values for the prepackaged SSD model.
    private static final int TF_OD_API_INPUT_SIZE = 600;
    private static final boolean TF_OD_API_IS_QUANTIZED = true;
    private static final String TF_OD_API_MODEL_FILE = "detect.tflite";
    private static final String TF_OD_API_LABELS_FILE = "labelmap.txt";
    private static final DetectorMode MODE = DetectorMode.TF_OD_API;
    // Minimum detection confidence to track a detection.
    private static final float MINIMUM_CONFIDENCE_TF_OD_API = 0.6f;
    private static final boolean MAINTAIN_ASPECT = false;
    private static final Size DESIRED_PREVIEW_SIZE = new Size(640, (int) (640 * 1.6));
    private static final boolean SAVE_PREVIEW_BITMAP = false;
    private static final float TEXT_SIZE_DIP = 10;
    OverlayView trackingOverlay;
    private Integer sensorOrientation;
    boolean isScanned = false;

    Point sizeRect = new Point();

    DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
    Random generator = new Random();
    private Detector detector;

    private long lastProcessingTimeMs;
    private Bitmap rgbFrameBitmap = null;
    private Bitmap croppedBitmap = null;
    private Bitmap cropCopyBitmap = null;

    private boolean computingDetection = false;

    private long timestamp = 0;

    private Matrix frameToCropTransform;
    private Matrix cropToFrameTransform;

    private MultiBoxTracker tracker;
    private RelativeLayout relativeLayout;

    private BorderedText borderedText;
    public CardView resultPopupCard;
    private TextView percentTxt, resultTxt, actionTxt;
    private String type;

    @Override
    public void onPreviewSizeChosen(final Size size, final int rotation) {
        final float textSizePx =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, TEXT_SIZE_DIP, getResources().getDisplayMetrics());
        borderedText = new BorderedText(textSizePx);
        borderedText.setTypeface(Typeface.MONOSPACE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = extras.getString("type");
            //The key argument here must match that used in the other activity
        }
        tracker = new MultiBoxTracker(this, getRelativeLayout());
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int cropSize = TF_OD_API_INPUT_SIZE;

        try {
            detector =
                    TFLiteObjectDetectionAPIModel.create(
                            this,
                            TF_OD_API_MODEL_FILE,
                            TF_OD_API_LABELS_FILE,
                            TF_OD_API_INPUT_SIZE,
                            TF_OD_API_IS_QUANTIZED);
            cropSize = TF_OD_API_INPUT_SIZE;
        } catch (final IOException e) {
            e.printStackTrace();
            LOGGER.e(e, "Exception initializing Detector!");
            Toast toast =
                    Toast.makeText(
                            getApplicationContext(), "Detector could not be initialized", Toast.LENGTH_SHORT);
            toast.show();
            finish();
        }

        previewWidth = size.getWidth();
        previewHeight = size.getHeight();

        sensorOrientation = rotation - getScreenOrientation();
        LOGGER.i("Camera orientation relative to screen canvas: %d", sensorOrientation);

        LOGGER.i("Initializing at size %dx%d", previewWidth, previewHeight);
        rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Config.ARGB_8888);
        croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Config.ARGB_8888);

        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        cropSize, cropSize,
                        sensorOrientation, MAINTAIN_ASPECT);

        cropToFrameTransform = new Matrix();
        frameToCropTransform.invert(cropToFrameTransform);

        trackingOverlay = (OverlayView) findViewById(R.id.tracking_overlay);
        trackingOverlay.addCallback(
                canvas -> {
                    if (!isScanned() && tracker.draw(canvas)) {
                        tracker.drawDebug(canvas);
                        setScanned(true);
                        if (getResultPopupCard().getVisibility() == View.INVISIBLE) {
                            getResultPopupCard().setVisibility(View.VISIBLE);
                            int value = generator.nextInt((100 - 50) + 1) + 50;
                            getPercentTxt().setText(value + "%");
                            if (value >= 50 && value <= 65) {
                                getActionTxt().setText("Wash Your Hand, Now!!");
                                getActionTxt().setTextColor(getResources().getColor(R.color.gnt_red));
                                getPercentTxt().setTextColor(getResources().getColor(R.color.gnt_red));
                                getResultTxt().setText("Your hand is full of germs");
                            } else if (value > 65 && value <= 80) {
                                getActionTxt().setText("Clean it.");
                                getActionTxt().setTextColor(getResources().getColor(R.color.gnt_orange));
                                getPercentTxt().setTextColor(getResources().getColor(R.color.gnt_orange));
                                getResultTxt().setText("Your hand still have germs");
                            } else if (value > 80) {
                                getActionTxt().setText("Nice.");
                                getActionTxt().setTextColor(getResources().getColor(R.color.gnt_green));
                                getPercentTxt().setTextColor(getResources().getColor(R.color.gnt_green));
                                getResultTxt().setText("Your hand is fully cleaned");
                            }
                            Calendar rightNow = Calendar.getInstance();
                            String date = df.format(rightNow.getTime());
                            SQLiteDBHelper sqLiteDBHelper = new SQLiteDBHelper(this);
                            ScanHistoryModel scanHistoryModel = new ScanHistoryModel();
                            scanHistoryModel.setPercent(value + "%");
                            scanHistoryModel.setTime(date);
                            scanHistoryModel.setDay(rightNow.get(Calendar.HOUR_OF_DAY) < 12);
                            scanHistoryModel.setNight(rightNow.get(Calendar.HOUR_OF_DAY) > 12);
                            if (type.equalsIgnoreCase("normal")) {
                                sqLiteDBHelper.addScanHistory(scanHistoryModel);
                            } else {
                                sqLiteDBHelper.addChallengeHistory(scanHistoryModel);
                            }
                        }
                    }
                });

        tracker.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation);
    }

    @Override
    protected void processImage() {
        ++timestamp;
        final long currTimestamp = timestamp;
        trackingOverlay.postInvalidate();

        // No mutex needed as this method is not reentrant.
        if (computingDetection) {
            readyForNextImage();
            return;
        }
        computingDetection = true;

        rgbFrameBitmap.setPixels(getRgbBytes(), 0, previewWidth, 0, 0, previewWidth, previewHeight);

        readyForNextImage();

        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);
        // For examining the actual TF input.
        if (SAVE_PREVIEW_BITMAP) {
            ImageUtils.saveBitmap(croppedBitmap);
        }

        runInBackground(
                new Runnable() {
                    @Override
                    public void run() {
                        final long startTime = SystemClock.uptimeMillis();
                        final List<Detector.Recognition> results = detector.recognizeImage(croppedBitmap);
                        lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime;

                        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap);
                        final Canvas canvas = new Canvas(cropCopyBitmap);
                        final Paint paint = new Paint();
                        paint.setColor(Color.RED);
                        paint.setStyle(Style.STROKE);
                        paint.setStrokeWidth(2.0f);

                        float minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                        switch (MODE) {
                            case TF_OD_API:
                                minimumConfidence = MINIMUM_CONFIDENCE_TF_OD_API;
                                break;
                        }

                        final List<Detector.Recognition> mappedRecognitions =
                                new ArrayList<Detector.Recognition>();

                        for (final Detector.Recognition result : results) {
                            final RectF location = result.getLocation();
                            if (location != null && result.getConfidence() >= minimumConfidence) {
                                canvas.drawRect(location, paint);

                                cropToFrameTransform.mapRect(location);

                                result.setLocation(location);
                                mappedRecognitions.add(result);
                            }
                        }

                        tracker.trackResults(mappedRecognitions, currTimestamp);
                        trackingOverlay.postInvalidate();

                        computingDetection = false;

                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.tfe_od_camera_connection_fragment_tracking;
    }

    @Override
    public CardView getResultPopupCard() {
        return this.resultPopupCard;
    }

    @Override
    public void setResultPopupCard(CardView resultPopupCard) {
        this.resultPopupCard = resultPopupCard;
    }

    @Override
    public boolean isScanned() {
        return this.isScanned;
    }

    @Override
    public void setScanned(boolean scanned) {
        this.isScanned = scanned;
    }


    @Override
    protected Size getDesiredPreviewFrameSize() {
        return DESIRED_PREVIEW_SIZE;
    }

    // Which detection model to use: by default uses Tensorflow Object Detection API frozen
    // checkpoints.
    private enum DetectorMode {
        TF_OD_API;
    }

    @Override
    protected void setUseNNAPI(final boolean isChecked) {
        runInBackground(
                () -> {
                    try {
                        detector.setUseNNAPI(isChecked);
                    } catch (UnsupportedOperationException e) {
                        runOnUiThread(
                                () -> {
                                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    }
                });
    }

    @Override
    public TextView getPercentTxt() {
        return this.percentTxt;
    }

    @Override
    public void setPercentTxt(TextView percentTxt) {
        this.percentTxt = percentTxt;
    }

    @Override
    public TextView getResultTxt() {
        return this.resultTxt;
    }

    @Override
    public void setResultTxt(TextView resultTxt) {
        this.resultTxt = resultTxt;
    }

    @Override
    public TextView getActionTxt() {
        return this.actionTxt;
    }

    @Override
    public void setActionTxt(TextView actionTxt) {
        this.actionTxt = actionTxt;
    }

    @Override
    public RelativeLayout getRelativeLayout() {
        return this.relativeLayout;
    }

    @Override
    public void setRelativeLayout(RelativeLayout relativeLayout) {
        this.relativeLayout = relativeLayout;
    }

    @Override
    protected void setNumThreads(final int numThreads) {
        runInBackground(() -> detector.setNumThreads(1));
    }
}
