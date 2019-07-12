package com.mrtecks.school;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mrtecks.school.classPOJO.classBean;
import com.mrtecks.school.regiterPOJO.Data;
import com.mrtecks.school.regiterPOJO.registerBean;
import com.mrtecks.school.schoolPOJO.schoolBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Signup extends AppCompatActivity {

    Button signup, signin, upload;

    TextInputEditText name, phone, rollno, password, confirm;
    Spinner school, classes;
    ProgressBar progress;

    List<String> cls, clid;
    List<String> sch, scid;
    String s, c;

    CircleImageView image;

    private Uri uri;
    private File f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        cls = new ArrayList<>();
        clid = new ArrayList<>();
        sch = new ArrayList<>();
        scid = new ArrayList<>();

        signup = findViewById(R.id.button);
        signin = findViewById(R.id.button2);
        upload = findViewById(R.id.button7);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        rollno = findViewById(R.id.rollno);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        school = findViewById(R.id.school);
        classes = findViewById(R.id.classes);
        progress = findViewById(R.id.progressBar);
        image = findViewById(R.id.imageView3);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String na = name.getText().toString();
                String ph = phone.getText().toString();
                String ro = rollno.getText().toString();
                String pa = password.getText().toString();
                String co = confirm.getText().toString();

                if (na.length()>0)
                {

                    if (ph.length() == 10)
                    {
                        if (s.length() > 0)
                        {
                            if (c.length() > 0)
                            {

                                if (ro.length() > 0)
                                {
                                    if (pa.length() > 0)
                                    {
                                        if (co.length() > 0)
                                        {


                                            MultipartBody.Part body = null;

                                            try {

                                                RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                                                body = MultipartBody.Part.createFormData("photo", f1.getName(), reqFile1);


                                            } catch (Exception e1) {
                                                e1.printStackTrace();
                                            }

                                            progress.setVisibility(View.VISIBLE);

                                            Bean b = (Bean) getApplicationContext();

                                            Retrofit retrofit = new Retrofit.Builder()
                                                    .baseUrl(b.baseurl)
                                                    .addConverterFactory(ScalarsConverterFactory.create())
                                                    .addConverterFactory(GsonConverterFactory.create())
                                                    .build();

                                            AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                                            Call<registerBean> call = cr.register(
                                                    na,
                                                    ph,
                                                    s,
                                                    c,
                                                    ro,
                                                    pa,
                                                    body
                                            );

                                            call.enqueue(new Callback<registerBean>() {
                                                @Override
                                                public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                                                    if (response.body().getStatus() == "1")
                                                    {

                                                        Toast.makeText(Signup.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();

                                                        Data item = response.body().getData();

                                                        SharePreferenceUtils.getInstance().saveString("user_id" , item.getUserId());
                                                        SharePreferenceUtils.getInstance().saveString("name" , item.getName());
                                                        SharePreferenceUtils.getInstance().saveString("photo" , item.getPhoto());
                                                        SharePreferenceUtils.getInstance().saveString("contact" , item.getContact());
                                                        SharePreferenceUtils.getInstance().saveString("school_id" , item.getSchoolId());
                                                        SharePreferenceUtils.getInstance().saveString("class" , item.getClass_());
                                                        SharePreferenceUtils.getInstance().saveString("rollno" , item.getRollno());
                                                        SharePreferenceUtils.getInstance().saveString("password" , item.getPassword());
                                                        SharePreferenceUtils.getInstance().saveString("status" , item.getStatus());
                                                        SharePreferenceUtils.getInstance().saveString("created" , item.getCreated());


                                                        Intent intent = new Intent(Signup.this, Survey.class);
                                                        startActivity(intent);
                                                        finishAffinity();

                                                    }
                                                    else
                                                    {
                                                        Toast.makeText(Signup.this, response.body().getMessage() , Toast.LENGTH_SHORT).show();
                                                    }

                                                    progress.setVisibility(View.GONE);

                                                }

                                                @Override
                                                public void onFailure(Call<registerBean> call, Throwable t) {
                                                    progress.setVisibility(View.GONE);
                                                }
                                            });




                                        }
                                        else
                                        {
                                            Toast.makeText(Signup.this, "Passwords did not match", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Signup.this, "Invalid password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(Signup.this, "Invalid roll no.", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else
                            {
                                Toast.makeText(Signup.this, "Invalid class", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {
                            Toast.makeText(Signup.this, "Invalid school", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Signup.this, "Invalid phone", Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(Signup.this, "Invalid name", Toast.LENGTH_SHORT).show();
                }




            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] items = {"Take Photo from Camera",
                        "Choose from Gallery",
                        "Cancel"};
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Signup.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo from Camera")) {
                            final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Folder/";
                            File newdir = new File(dir);
                            try {
                                newdir.mkdirs();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";


                            f1 = new File(file);
                            try {
                                f1.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            uri = FileProvider.getUriForFile(Objects.requireNonNull(Signup.this), BuildConfig.APPLICATION_ID + ".provider", f1);

                            Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivityForResult(getpic, 1);
                        } else if (items[item].equals("Choose from Gallery")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 2);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();

            }
        });


        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<schoolBean> call = cr.getSchools();

        call.enqueue(new Callback<schoolBean>() {
            @Override
            public void onResponse(Call<schoolBean> call, Response<schoolBean> response) {


                if (response.body().getStatus().equals("1")) {

                    sch.add("Select one --- ");


                    for (int i = 0; i < response.body().getData().size(); i++) {

                        sch.add(response.body().getData().get(i).getSchoolName());
                        scid.add(response.body().getData().get(i).getSchoolId());

                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signup.this,
                            R.layout.spinner_model, sch);

                    school.setAdapter(adapter);

                }
                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<schoolBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

        school.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {

                    s = scid.get(i - 1);

                    Log.d("asasd", s);

                    progress.setVisibility(View.VISIBLE);

                    Bean b = (Bean) getApplicationContext();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(b.baseurl)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                    Call<classBean> call = cr.getClasses(s);

                    call.enqueue(new Callback<classBean>() {
                        @Override
                        public void onResponse(Call<classBean> call, Response<classBean> response) {


                            if (response.body().getStatus().equals("1")) {

                                cls.add("Select one --- ");


                                for (int i = 0; i < response.body().getData().size(); i++) {

                                    cls.add(response.body().getData().get(i).getClass_());
                                    clid.add(response.body().getData().get(i).getId());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Signup.this,
                                        R.layout.spinner_model, cls);

                                classes.setAdapter(adapter);

                            }
                            progress.setVisibility(View.GONE);


                        }

                        @Override
                        public void onFailure(Call<classBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });


                } else {
                    s = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        classes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    c = clid.get(i - 1);
                } else {
                    c = "";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri", String.valueOf(uri));

            String ypath = getPath(Signup.this, uri);
            assert ypath != null;
            f1 = new File(ypath);

            Log.d("path", ypath);


            ImageLoader loader = ImageLoader.getInstance();

            Bitmap bmp = loader.loadImageSync(String.valueOf(uri));

            Log.d("bitmap", String.valueOf(bmp));

            image.setImageBitmap(bmp);

        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            image.setImageURI(uri);
        }


    }

    private static Bitmap decodeUriToBitmap(Context mContext, Uri sendUri) {
        Bitmap getBitmap = null;
        try {
            InputStream image_stream;
            try {
                image_stream = mContext.getContentResolver().openInputStream(sendUri);
                getBitmap = BitmapFactory.decodeStream(image_stream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap;
    }


    private static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }


}
