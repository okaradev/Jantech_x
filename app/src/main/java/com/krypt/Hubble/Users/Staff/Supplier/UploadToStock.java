package com.krypt.Hubble.Users.Staff.Supplier;

import static com.krypt.Hubble.utils.Urls.URL_UPLOAD_STOCK;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.krypt.Hubble.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UploadToStock extends AppCompatActivity {
    EditText nm,desc,quanity,image;
    Button uploadstock;
    ProgressBar progressBar;
    ImageView imageView;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_stock);
        progressBar=new ProgressBar(UploadToStock.this);
        nm=findViewById(R.id.stock_name);
        image=findViewById(R.id.stock_img);
        quanity=findViewById(R.id.stock_quantity);
        desc=findViewById(R.id.stock_des);
        progressBar.setVisibility(View.GONE);
        imageView=findViewById(R.id.imagestock);


        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== Activity.RESULT_OK){
                    Intent data=result.getData();
                    Uri uri=data.getData();
                    try {
                        bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_PICK);
                in.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(in);

            }
        });

        uploadstock=findViewById(R.id.stock_btn);
        uploadstock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentoStock(nm.getText().toString(),quanity.getText().toString(),desc.getText().toString());
            }
        });

    }

    private void sentoStock(String name,String quantity,String desc) {
        if(name.isEmpty()||quantity.isEmpty()||desc.isEmpty()){
            Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        ByteArrayOutputStream byteArrayOutputStream;
        byteArrayOutputStream=new ByteArrayOutputStream();
        if (bitmap!=null){
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] bytes=byteArrayOutputStream.toByteArray();
            final String base64= Base64.encodeToString(bytes,Base64.DEFAULT);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_STOCK,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", "is" + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("message");
                                if (status.equals("1")) {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                    finish();
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                    progressBar.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("quantity", quantity);
                    params.put("image", base64);
                    params.put("desc", desc);


                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);


        }

    }
}