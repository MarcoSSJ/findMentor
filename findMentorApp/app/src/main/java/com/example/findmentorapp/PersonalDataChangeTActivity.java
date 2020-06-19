package com.example.findmentorapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class PersonalDataChangeTActivity extends AppCompatActivity {

    private RadioGroup radioGroup_sex;
    private RadioButton radioButton_male;
    private RadioButton radioButton_female;
    private EditText editText_age;
    private EditText editText_intro;
    private EditText editText_range;
    private EditText editText_grade;
    private Button button;
    private String sex1;
    private ImageView imageView;
    private SharedPreferences sharedPreferences;

    private String imagePath;

    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
        String age = editText_age.getText().toString();
        String intro = editText_intro.getText().toString();
        String range = editText_range.getText().toString();
        String grade = editText_grade.getText().toString();
        String personal_data_change_url = Urls.api_url;

        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0) {
                    //不成功，弹窗
                    Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                    toast.show();
                } else if (msg.what == 1) {
//                    Intent intent = new Intent(PersonalDataChangeTActivity.this, PersonalDataTActivity.class);
//                    startActivity(intent);
                    finish();
                }
            }
        };
        try {
            URL url = new URL(personal_data_change_url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);

            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);

            MyApplication application = (MyApplication) getApplicationContext();
            String sessionID = application.getSessionID();

            String data = "action=ChangePersonalData"+
                    "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                    "&type=" + URLEncoder.encode("teacher","UTF-8") +
                    "&age="+URLEncoder.encode(age,"UTF-8")+
                    "&sex="+URLEncoder.encode(sex1,"UTF-8")+
                    "&intro="+URLEncoder.encode(intro,"UTF-8")+
                    "&range="+URLEncoder.encode(range,"UTF-8")+
                    "&grade="+URLEncoder.encode(grade,"UTF-8");

            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes());
            out.flush();
            out.close();

            InputStream is = conn.getInputStream();
            if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                byte[] b = new byte[1024];
                int len ;
                while((len = is.read(b))!=-1){
                    response.append(new String(b, 0, len));
                }
                is.close();
                conn.disconnect();

                String res = new String(response);
                System.out.println(res);
                JSONObject obj = new JSONObject(res);
                String isconnect = obj.getString("result");
                if(isconnect.equals("true")) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    {
                        editor.putString("sex",sex1);
                        editor.putString("age",age);
                        editor.putString("intro",intro);
                        editor.putString("range",range);
                        editor.putString("grade",grade);
                        editor.apply();
                    }
                    Message message = Message.obtain();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                else
                {
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
            else {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
            }
        } catch (MalformedURLException e) {
            Message message = Message.obtain();
            message.what = 0;
            handler.sendMessage(message);
            e.printStackTrace();
        } catch (IOException e) {
            Message message = Message.obtain();
            message.what = 0;
            handler.sendMessage(message);
            e.printStackTrace();
        } catch (JSONException e) {
            Message message = Message.obtain();
            message.what = 0;
            handler.sendMessage(message);
            e.printStackTrace();
        }
        }

    };


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_changedata_teacher);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //删除了姓名，加入了研究领域与职称（这个得加入处理）
        radioGroup_sex=(RadioGroup)findViewById(R.id.radioGroup_personalDataChange);
        radioButton_male=(RadioButton)findViewById(R.id.radioButton_personalDataChange_male);
        radioButton_female=(RadioButton)findViewById(R.id.radioButton_personalDataChange_female);
        editText_age = (EditText)findViewById(R.id.editText_personalDataChange_age);
        editText_intro = (EditText)findViewById(R.id.editText_personalDataChange_inTro);
        editText_range = (EditText)findViewById(R.id.editText_personalDataChange_range);
        editText_grade = (EditText)findViewById(R.id.editText_personalDataChange_grade);
        imageView = (ImageView)findViewById(R.id.imageView_personalDataChange);

        sharedPreferences = getSharedPreferences("remenberpass", Context.MODE_PRIVATE);
        String age = sharedPreferences.getString("age","");
        String sex = sharedPreferences.getString("sex","");
        String intro = sharedPreferences.getString("intro","");
        String range = sharedPreferences.getString("range","");
        String grade = sharedPreferences.getString("grade","");
        sex1 = sex;
        if(sex1 == "")
            sex1 = "male";
        MyApplication application = (MyApplication) getApplicationContext();
        String sessionID = application.getSessionID();

        if(sessionID.equals(""))
        {
            editText_age.setText("请登录");
            radioButton_male.setChecked(true);
            editText_intro.setText("请登录");
            editText_range.setText("请登录");
            editText_grade.setText("请登录");
        }
        else {

            if (age.equals(""))
                editText_age.setText("0");
            else
                editText_age.setText(age);

            if(sex.equals(""))
                radioButton_male.setChecked(true);
            else if(sex.equals("male"))
                radioButton_male.setChecked(true);
            else
                radioButton_female.setChecked(true);

            if (intro.equals(""))
                editText_intro.setText("请输入个人信息");
            else
                editText_intro.setText(intro);

            if (range.equals(""))
                editText_range.setText("未设置");
            else
                editText_range.setText(range);

            if (grade.equals(""))
                editText_grade.setText("未设置");
            else
                editText_grade.setText(grade);
        }

        //以下为sex的获取代码
        radioGroup_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String temp="male";
                if(radioButton_male.getId()==checkedId){
                    temp="male";
                }
                else if(radioButton_female.getId()==checkedId){
                    temp="female";
                }
                sex1 = temp;
            }
        });


        button = (Button)findViewById(R.id.button_personalDataChange);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                new Thread(runnable).start();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v) {
                showChoosePicDialog();
            }
        });
    }

    //以下为头像修改代码
    //todo 完成头像上传等操作（目前只使用本地图片，只剩下上传操作了），参考https://blog.csdn.net/ydxlt/article/details/48024017#t1
    //其他地方也可以开始着手头像设置了
    //设置图片如imageView.setImageBitmap(photo);
    protected void showChoosePicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("设置头像");
        String[] items = { "选择本地照片"};
        builder.setNegativeButton("取消", null);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case CHOOSE_PICTURE: // 选择本地照片
                        Intent openAlbumIntent = new Intent(
                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        //openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                        break;
                }
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
//        if (uri == null) {
//            Log.i("tag", "The uri is not exist.");
//        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo); // 这个时候的图片已经被处理成圆形的了
            imageView.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了
        String filepath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        imagePath = ImageUtils.savePhoto(bitmap, filepath, "myhead");
        if(imagePath != null){
            new Thread(runUpload).start();
        }
    }

    Runnable runUpload = new Runnable() {
        @Override
        public void run() {
            String personal_data_change_url = Urls.api_url;

            Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == 0) {
                        //不成功，弹窗
                        Toast toast = Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT);
                        toast.show();
                    } else if (msg.what == 1) {
                        Toast toast = Toast.makeText(getApplicationContext(), "设置成功", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            };
            try {
                URL url = new URL(personal_data_change_url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);

                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded;charset=UTF-8");

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);

                MyApplication application = (MyApplication) getApplicationContext();
                String sessionID = application.getSessionID();

                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(imagePath));
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                bis.close();

                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);//参数100表示不压缩
                byte[] bytes=bos.toByteArray();
                String pic = Base64.encodeToString(bytes, Base64.DEFAULT);

                String data = "action=UploadPic"+
                        "&sessionID="+ URLEncoder.encode(sessionID,"UTF-8")+
                        "&picture="+ URLEncoder.encode(pic,"UTF-8");

                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes());
                out.flush();
                out.close();

                InputStream is = conn.getInputStream();
                if(conn.getResponseCode()==HttpURLConnection.HTTP_OK) {
                    StringBuilder response = new StringBuilder();
                    byte[] b = new byte[1024];
                    int len ;
                    while((len = is.read(b))!=-1){
                        response.append(new String(b, 0, len));
                    }
                    is.close();
                    conn.disconnect();

                    String res = new String(response);
                    System.out.println(res);
                    JSONObject obj = new JSONObject(res);
                    String isconnect = obj.getString("result");
                    if(isconnect.equals("true")) {

                        Message message = Message.obtain();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                    else
                    {
                        Message message = Message.obtain();
                        message.what = 0;
                        handler.sendMessage(message);
                    }
                }
                else {
                    Message message = Message.obtain();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            } catch (MalformedURLException e) {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
                e.printStackTrace();
            } catch (IOException e) {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
                e.printStackTrace();
            } catch (JSONException e) {
                Message message = Message.obtain();
                message.what = 0;
                handler.sendMessage(message);
                e.printStackTrace();
            }
        }

    };
}
