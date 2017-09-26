package com.lsp.tesseractorc;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lsp
 * @created 2017/9/26 15:26
 */
public class FileUtils {

  private static final String APP_ROOT_DIR_PATH = "tessORC";
  private static final String TESS_DATA = "tessdata";

  /**
   * 获取图片下载的路径
   */
  public static String getTessDataPath() {
    String tessDataPath = getWybxsDirPath() + TESS_DATA + File.separator;
    File tessDataFile = new File(tessDataPath);
    if (!tessDataFile.exists()) {
      tessDataFile.mkdirs();
    }
    return tessDataPath;
  }


  public static String getWybxsDirPath() {
    return Environment.getExternalStorageDirectory().getAbsolutePath()
        + File.separator
        + APP_ROOT_DIR_PATH
        + File.separator;
  }

  /**
   * 复制文件，可以选择是否删除源文件
   */
  public static boolean copyFileFromAsset(Context mContext, String assetName, String destFilePath) {

    InputStream in = null;
    OutputStream out = null;
    try {
      File file = new File(destFilePath);
      file.createNewFile();
      in = mContext.getResources().getAssets().open(assetName);
      out = new FileOutputStream(file);
      byte[] buffer = new byte[1024];
      int i = -1;
      while ((i = in.read(buffer)) > 0) {
        out.write(buffer, 0, i);
        out.flush();
      }
    } catch (Exception e) {
      Log.e("tag", "error");
      return false;
    } finally {
      close(out);
      close(in);
    }
    return true;
  }

  /**
   * 关闭流
   */
  public static boolean close(Closeable io) {
    if (io != null) {
      try {
        io.close();
      } catch (IOException e) {
      }
    }
    return true;
  }
}
