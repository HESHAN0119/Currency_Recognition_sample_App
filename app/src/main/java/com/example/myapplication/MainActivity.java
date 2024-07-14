package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Surface;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.ml.Model;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSION = 101;

    private TextToSpeech textToSpeech;
    private TextToSpeech textToSpeech2;
    private String[] REQUIRED_PERMISSION= new String[] {"android.permission.CAMERA","android.permission.WRITE_EXTERNAL_STORAGE"};

    TextureView textureView;
    ImageView imageView;
    TextView resultText;
    int imageSize = 128;
    public File file;
    List<Integer> integerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getSupportActionBar().hide();

        textureView=findViewById(R.id.tcView);
        imageView =findViewById(R.id.imageView2);
        resultText= findViewById(R.id.result);

        if (allPermissionGranted()){
            startCamera();
        }else {
            ActivityCompat.requestPermissions(this,REQUIRED_PERMISSION,REQUEST_CODE_PERMISSION);
        }

        textToSpeech = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                // Set the language to Sinhala
                Locale sinhalaLocale = new Locale("si", "LK");
                int result = textToSpeech.setLanguage(sinhalaLocale);

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Sinhala language is not supported");
                    Toast.makeText(MainActivity.this, "Sinhala language is not supported", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TTS", "Initialization failed");
                Toast.makeText(MainActivity.this, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
            }
        });

        textToSpeech2 = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                // Set the language to Sinhala
                Locale sinhalaLocale = new Locale("si", "LK");
                int result = textToSpeech2.setLanguage(sinhalaLocale);

                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Sinhala language is not supported");
                    Toast.makeText(MainActivity.this, "Sinhala language is not supported", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TTS", "Initialization failed");
                Toast.makeText(MainActivity.this, "Text-to-speech initialization failed", Toast.LENGTH_SHORT).show();
            }
        });


        resultText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to navigate to the new activity
                Intent intent = new Intent(MainActivity.this, Cal.class);

                // Pass the value of resultText to the new activity
                intent.putExtra("RESULT_VALUE", resultText.getText().toString());

                // Start the new activity
                startActivity(intent);
            }
        });


    }

    private void startCamera() {
        CameraX.unbindAll();
        Rational aspectRatio=new Rational(textureView.getWidth(),textureView.getHeight());
        Size screen = new Size(textureView.getWidth(),textureView.getHeight());

        PreviewConfig pConfig= new PreviewConfig.Builder().setTargetAspectRatio(aspectRatio).setTargetResolution(screen).build();
        Preview preview = new Preview(pConfig);
        preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                ViewGroup parent=(ViewGroup) textureView.getParent();
                parent.removeView(textureView);
                parent.addView(textureView);

                textureView.setSurfaceTexture(output.getSurfaceTexture());
                updateTransform();
            }
        });

        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation()).build();
        final ImageCapture imgCap= new ImageCapture(imageCaptureConfig);

        textureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                File file = new File(Environment.getExternalStoragePublicDirectory("Internal storage/DCIM/Camera/"), "CameraX" + System.currentTimeMillis() + ".jpg");
               file = createImageFile();
                imgCap.takePicture(file, new ImageCapture.OnImageSavedListener() {
                    @Override
                    public void onImageSaved(@NonNull File file) {
                        String msg= "Pic Captured at" + file.getAbsolutePath();
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        showCapturedImage(file);
                    }

                    @Override
                    public void onError(@NonNull ImageCapture.UseCaseError useCaseError, @NonNull String message, @Nullable Throwable cause) {
                        showCapturedImage(file);
                        String msg= "Pic Captured failed "+ message;
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();

                        if (cause !=null){
                            cause.printStackTrace();
                        }
                    }
                });
            }
        });
        CameraX.bindToLifecycle(this,preview,imgCap);
    }

    private void showCapturedImage(File file) {
        // Load the captured image into the ImageView
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        int targetWidth = 128;
        int targetHeight = 128;

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false);

        int width = scaledBitmap.getWidth();
        int height = scaledBitmap.getHeight();
        System.out.println(width);
        //resultText.setText(width);
        imageView.setImageBitmap(scaledBitmap);
        classifyImage(scaledBitmap);
        file.delete();

    }


    public void classifyImage(Bitmap image){
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            //iterate over each pixel and extract R, G, and B values. Add those values individually to the byte buffer.
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            System.out.println(Arrays.toString(confidences));
            // find the index of the class with the biggest confidence.
            int maxPos = 100;
            float maxConfidence = 0.75f;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            if(maxPos==100){
                textToSpeech2.speak(String.valueOf("නැවත උත්සහ කරන්න"), TextToSpeech.QUEUE_FLUSH, null, null);
            }else{
            int[] classes = {1000, 1000, 100, 100, 20, 20, 5000, 5000, 500, 500, 50, 50};
            resultText.setText(String.valueOf(classes[maxPos]));
            textToSpeech2.speak(String.valueOf(classes[maxPos] + " i"), TextToSpeech.QUEUE_FLUSH, null, null);
            integerList.add(classes[maxPos]);
// Calculate and print the sum of the elements
            int sum = calculateSum(integerList);
            System.out.println("Sum of the elements: " + sum);

            // Create a Handler
            Handler handler = new Handler();

            // Post a delayed action after 500 milliseconds (0.5 seconds)
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Code to be executed after the delay
                    resultText.setText(String.valueOf(sum));
                    textToSpeech.speak("mulu ekathuve  " + String.valueOf(sum) + " i", TextToSpeech.QUEUE_FLUSH, null, null);
                }
            }, 500); // 500 milliseconds
        }
//            try {
//                Thread.sleep(0, 1000000);// Sleep for 0.5 milliseconds
//                resultText.setText(String.valueOf(sum));
//                textToSpeech.speak("mulu ekathuve  "+String.valueOf(sum)+" i" ,TextToSpeech.QUEUE_FLUSH, null, null);
//                // Releases model resources if no longer used.
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private static int calculateSum(List<Integer> list) {
        int sum = 0;
        for (Integer value : list) {
            sum += value;
        }
        return sum;
    }

    public File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            // Create the File object
            File imageFile = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateTransform() {
        Matrix mx =new Matrix();
        float w= textureView.getMeasuredWidth();
        float h= textureView.getMeasuredHeight();

        float cX =w/2f;
        float cY =h/2f;

        int rotationDgr;
        int rotation=(int)textureView.getRotation();

        switch (rotation){
            case Surface.ROTATION_0:
                rotationDgr =0;
                break;
            case Surface.ROTATION_90:
                rotationDgr=90;
                break;
            case Surface.ROTATION_180:
                rotationDgr=180;
                break;
            case Surface.ROTATION_270:
                rotationDgr=270;
                break;
            default:
                return;
        }
        mx.postRotate((float)rotationDgr,cX,cY);
        textureView.setTransform(mx);
    }

    private boolean allPermissionGranted() {
        for (String permission : REQUIRED_PERMISSION){
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
        
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}