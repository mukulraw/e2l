package com.mrtecks.e2l;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.mrtecks.e2l.regiterPOJO.registerBean;
import com.mrtecks.e2l.singleTopicPOJO.Data;
import com.mrtecks.e2l.singleTopicPOJO.singleTopicBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.kexanie.library.MathView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.app.Activity.RESULT_OK;

public class videoTopic extends Fragment implements ProgressRequestBody.UploadCallbacks {


    String qid , mid;
    MathView question;
    ProgressBar progress;
    Button submit;
    YouTubePlayerView player;

    CustomViewPager pager;
    module co;
    TextView filelabel , videolabel , filename;
    CardView file;
YouTubePlayer youTubePlayer;
    ProgressDialog prog , prog2;
    int position;

    boolean last;

    private Uri uri;
    private File f1;

    String furl = "" , fname;

    ImageView image;

    DownloadZipFileTask downloadZipFileTask;
    private static final String TAG = "MainActivity";
    TextView mark;
    public void setData(CustomViewPager pager , String qid , int position , boolean last , String mid , module co)
    {
        this.pager = pager;
        this.qid = qid;
        this.position = position;
        this.last = last;
        this.mid = mid;
        this.co = co;
    }

    Toast toast;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_layout , container , false);


        toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);
        image = view.findViewById(R.id.image);
        prog = new ProgressDialog(getContext());
        prog.setMessage("Uploading File...");
        prog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prog.setIndeterminate(false);
        //prog.setProgress(0);

        prog2 = new ProgressDialog(getContext());
        prog2.setMessage("Downloading File...");
        prog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        prog2.setIndeterminate(false);

        mark = view.findViewById(R.id.mark);
        question = view.findViewById(R.id.textView6);
        progress = view.findViewById(R.id.progressBar6);
        submit = view.findViewById(R.id.button3);
        player = view.findViewById(R.id.youTubePlayerView);
        filelabel = view.findViewById(R.id.textView10);
        filename = view.findViewById(R.id.filename);
        videolabel = view.findViewById(R.id.textView12);
        file = view.findViewById(R.id.textView11);

        getLifecycle().addObserver(player);
        player.enableBackgroundPlayback(false);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (last)
                {
                    final CharSequence[] items = {"Take Photo from Camera",
                            "Choose from Gallery",
                            "Cancel"};
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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

                                uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()), BuildConfig.APPLICATION_ID + ".provider", f1);

                                Intent getpic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                getpic.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                getpic.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                startActivityForResult(getpic, 3);
                            } else if (items[item].equals("Choose from Gallery")) {
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(intent, 4);
                            } else if (items[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();

                    Log.d("last" , "false");
                }
                else
                {


                    final CharSequence[] items = {"Take Photo from Camera",
                            "Choose from Gallery",
                            "Cancel"};
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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

                                uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()), BuildConfig.APPLICATION_ID + ".provider", f1);

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

                    Log.d("last" , "false");

                }

            }
        });



        loadData();


        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

                Call<ResponseBody> call = cr.downloadFileByUrl(furl);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        downloadZipFileTask = new DownloadZipFileTask();
                        downloadZipFileTask.execute(response.body());


                        progress.setVisibility(View.GONE);

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            }
        });


        return view;
    }



    private class DownloadZipFileTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            saveToDisk(urls[0], fname);
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            Log.d("API123", progress[0].second + " ");

            if (progress[0].second == 100)
                prog2.dismiss();
                //Toast.makeText(getContext(), "File downloaded successfully", Toast.LENGTH_SHORT).show();


            if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);

                if (currentProgress == 100)
                {
                    prog2.dismiss();
                    Toast.makeText(getContext(), "File downloaded successfully in Downloads", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    prog2.setProgress(currentProgress);
                    prog2.show();
                }



            }

            if (progress[0].first == -1) {
                Toast.makeText(getContext(), "Download failed", Toast.LENGTH_SHORT).show();
                prog2.dismiss();
            }

        }

        void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                Log.d(TAG, "File Size=" + fileSize);
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadZipFileTask.doProgress(pairs);
                    Log.d(TAG, "Progress: " + progress + "/" + fileSize + " >>>> " + (float) progress / fileSize);
                }

                outputStream.flush();

                Log.d(TAG, destinationFile.getParent());
                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadZipFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadZipFileTask.doProgress(pairs);
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }




    private String getYouTubeId (String youTubeUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youTubeUrl);
        if(matcher.find()){
            return matcher.group();
        } else {
            return "error";
        }
    }

    void loadData()
    {

        progress.setVisibility(View.VISIBLE);

        Bean b = (Bean) getActivity().getApplicationContext();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.baseurl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Call<singleTopicBean> call = cr.getTopicById(qid , SharePreferenceUtils.getInstance().getString("user_id"));

        call.enqueue(new Callback<singleTopicBean>() {
            @Override
            public void onResponse(Call<singleTopicBean> call, Response<singleTopicBean> response) {

                if (response.body().getStatus().equals("1"))
                {

                    final Data item = response.body().getData();


                    question.config(
                            "MathJax.Hub.Config({\n" +
                                    "  tex2jax: {inlineMath: [['$','$'], ['\\\\(','\\\\)']]}\n" +
                                    "});");


                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).cacheInMemory(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage(item.getImage() , image , options);


                    question.setText(item.getQuestion());

                    if (item.getVideo().length() > 0)
                    {
                        player.setVisibility(View.VISIBLE);
                        videolabel.setVisibility(View.VISIBLE);
                        player.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                            @Override
                            public void onReady(YouTubePlayer youTubePlayer) {

                                videoTopic.this.youTubePlayer = youTubePlayer;

                                String videoId = getYouTubeId(item.getVideo());
                                youTubePlayer.loadVideo(videoId, 0);
                            }
                        });

                    }
                    else
                    {
                        player.setVisibility(View.GONE);
                        videolabel.setVisibility(View.GONE);
                    }

                    mark.setText(item.getMark() + " Mark");

                    if (item.getFile().length() > 0)
                    {
                        filename.setText(item.getFilename());
                        furl = item.getFile();
                        fname = item.getFilename();
                        file.setVisibility(View.VISIBLE);
                        filelabel.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        file.setVisibility(View.GONE);
                        filelabel.setVisibility(View.GONE);
                    }


                    if (item.getStatus().equals("1"))
                    {
                        submit.setVisibility(View.GONE);

                    }
                    else
                    {
                        submit.setVisibility(View.VISIBLE);
                    }

                }

                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<singleTopicBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri", String.valueOf(uri));

            String ypath = getPath(getContext(), uri);
            assert ypath != null;
            f1 = new File(ypath);

            Log.d("path", ypath);


            ImageLoader loader = ImageLoader.getInstance();

            Bitmap bmp = loader.loadImageSync(String.valueOf(uri));

            Log.d("bitmap", String.valueOf(bmp));


            MultipartBody.Part body = null;

            try {


                ProgressRequestBody fileBody = new ProgressRequestBody(f1, this);
                //RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("answer", f1.getName(), fileBody);

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                Call<registerBean> call = cr.submitFile(
                        SharePreferenceUtils.getInstance().getString("user_id"),
                        qid,
                        mid,
                        body
                );


                call.enqueue(new Callback<registerBean>() {
                    @Override
                    public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);

                        loadData();

                        pager.setCurrentItem(position + 1);

                    }

                    @Override
                    public void onFailure(Call<registerBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e1) {
                e1.printStackTrace();
            }




        } else if (requestCode == 1 && resultCode == RESULT_OK) {


            MultipartBody.Part body = null;

            try {

                ProgressRequestBody fileBody = new ProgressRequestBody(f1, this);
                //RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("answer", f1.getName(), fileBody);

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                Call<registerBean> call = cr.submitFile(
                        SharePreferenceUtils.getInstance().getString("user_id"),
                        qid,
                        mid,
                        body
                );


                call.enqueue(new Callback<registerBean>() {
                    @Override
                    public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);

                        loadData();

                        pager.setCurrentItem(position + 1);

                    }

                    @Override
                    public void onFailure(Call<registerBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e1) {
                e1.printStackTrace();
            }


        }else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {
            uri = data.getData();

            Log.d("uri", String.valueOf(uri));

            String ypath = getPath(getContext(), uri);
            assert ypath != null;
            f1 = new File(ypath);

            Log.d("path", ypath);


            ImageLoader loader = ImageLoader.getInstance();

            Bitmap bmp = loader.loadImageSync(String.valueOf(uri));

            Log.d("bitmap", String.valueOf(bmp));


            MultipartBody.Part body = null;

            try {

                ProgressRequestBody fileBody = new ProgressRequestBody(f1, this);
                //RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("answer", f1.getName(), fileBody);

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                Call<registerBean> call = cr.submitFilemodule(
                        SharePreferenceUtils.getInstance().getString("user_id"),
                        qid,
                        mid,
                        SharePreferenceUtils.getInstance().getString("school_id"),
                        SharePreferenceUtils.getInstance().getString("class"),
                        body
                );


                call.enqueue(new Callback<registerBean>() {
                    @Override
                    public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);

                        loadData();


                        co.loadData();


                    }

                    @Override
                    public void onFailure(Call<registerBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e1) {
                e1.printStackTrace();
            }



        } else if (requestCode == 3 && resultCode == RESULT_OK) {
            MultipartBody.Part body = null;

            try {
                ProgressRequestBody fileBody = new ProgressRequestBody(f1, this);
                //RequestBody reqFile1 = RequestBody.create(MediaType.parse("multipart/form-data"), f1);
                body = MultipartBody.Part.createFormData("answer", f1.getName(), fileBody);

                progress.setVisibility(View.VISIBLE);

                Bean b = (Bean) getActivity().getApplicationContext();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(b.baseurl)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                AllApiIneterface cr = retrofit.create(AllApiIneterface.class);


                Call<registerBean> call = cr.submitFilemodule(
                        SharePreferenceUtils.getInstance().getString("user_id"),
                        qid,
                        mid,
                        SharePreferenceUtils.getInstance().getString("school_id"),
                        SharePreferenceUtils.getInstance().getString("class"),
                        body
                );


                call.enqueue(new Callback<registerBean>() {
                    @Override
                    public void onResponse(Call<registerBean> call, Response<registerBean> response) {

                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);

                        loadData();

                        co.loadData();

                    }

                    @Override
                    public void onFailure(Call<registerBean> call, Throwable t) {
                        progress.setVisibility(View.GONE);
                    }
                });


            } catch (Exception e1) {
                e1.printStackTrace();
            }

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


    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (!visible && youTubePlayer != null)
            youTubePlayer.pause();
    }



    @Override
    public void onProgressUpdate(int percentage) {
        progress.setProgress(percentage);

        Log.d("percent" , String.valueOf(percentage));

        prog.setProgress(percentage);
        prog.show();

    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {
progress.setVisibility(View.GONE);
        prog.dismiss();
    }

    @Override
    public void uploadStart() {
        progress.setVisibility(View.VISIBLE);


    }
}
