package com.lsp.tesseractorc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.googlecode.tesseract.android.TessBaseAPI;

public class MainActivity extends AppCompatActivity {

  public static final String ENGLISH = "eng";
  public static final String CHINESE = "chi_sim";
  private TextView tvOrc;
  private TextView tvSwitch;
  private ImageView imvOrc;
  private TextView tvReady;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    tvOrc = (TextView) findViewById(R.id.tv_orc);
    tvSwitch = (TextView) findViewById(R.id.tv_switch);
    tvReady = (TextView) findViewById(R.id.tv_ready);
    imvOrc = (ImageView) findViewById(R.id.imv_orc);
    final String assetName = "eng.traineddata";
    String assetNameChinese = "chi_sim.traineddata";
    copytessData(assetName, assetNameChinese);
    tvSwitch.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        EnglishOCR();
      }
    });
  }

  private void copytessData(final String assetName, final String assetName2) {
    final String tessPath = FileUtils.getTessDataPath();
    new Thread(new Runnable() {
      @Override public void run() {
        FileUtils.copyFileFromAsset(MainActivity.this, assetName, tessPath + assetName);
        FileUtils.copyFileFromAsset(MainActivity.this, assetName2, tessPath + assetName2);
        runOnUiThread(new Runnable() {
          @Override public void run() {
            tvReady.setText("准备就绪");
          }
        });
      }
    }).start();
  }

  public void EnglishOCR() {
    //设置图片可以缓存
    imvOrc.setDrawingCacheEnabled(true);
    //获取缓存的bitmap
    final Bitmap bmp = imvOrc.getDrawingCache();
    final TessBaseAPI baseApi = new TessBaseAPI();
    //初始化OCR的训练数据路径与语言
    baseApi.init(FileUtils.getWybxsDirPath(),
        ENGLISH); //这里的路径是不包含tessdata/的 内部查找训练数据是会自行添加tessdata/路径
    //设置识别模式
    baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
    //设置要识别的图片
    baseApi.setImage(bmp);
    imvOrc.setImageBitmap(bmp);
    tvOrc.setText(baseApi.getUTF8Text());
    baseApi.clear();
    baseApi.end();
  }
}
