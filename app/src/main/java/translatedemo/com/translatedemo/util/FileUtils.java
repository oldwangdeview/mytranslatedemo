package translatedemo.com.translatedemo.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 文件操作工具包
 * 
 * @author wangshifu
 */
public class FileUtils {

	public static String SDCARD = null;// SDcard目录
	public static String USERBASEDIR = null;// 用户根目录
	public static String VOICE_DIR = null;// 录音路径
	public static String IMAGE_DIR = null;// 图片路径
	public static String FILE_DIR = null;// 文件路径
	public static String APP_DIR = null;// 应用根目录
	public static String APP_LOG = null;// 操作日志
	public static String APP_CRASH = null;// 错误日志
	public static String RESOURCE_DIR = null;// 资源存放
	public static String APK_DIR=null;// 存放下载的apk
	public static String TEST_DIR = null;// 存放随堂小考

	/**
	 * 写文本文件 在Android系统中，文件保存在 /data/data/PACKAGE_NAME/files 目录下
	 * @param context
	 * @param fileName
	 * @param content
     */
	public static void write(Context context, String fileName, String content) {
		if (content == null)
			content = "";

		try {
			FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(content.getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void installApk(Context context, String appName) {
		try {
			Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
			intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
			intent.putExtra(Intent.EXTRA_ALLOW_REPLACE, true);
			intent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME, context.getApplicationInfo().packageName);
			intent.setData(Uri.fromFile(new File(FileUtils.APK_DIR, appName)));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	/**
	 *  打开文件
	 * @param path  为文件的完整路径
	 * @param name 为文件名
	 * @param context
     */
	public static void openFile(String path, String name, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = name.lastIndexOf(".");
		if (dotIndex < 0) {
			Toast.makeText(context, "文件格式异常！", Toast.LENGTH_LONG).show();
			return;
		}

		File file = new File(path);
		if (!file.exists()) {
			Toast.makeText(context, "文件不存在！", Toast.LENGTH_LONG).show();
			return;
		}
		try {
			// 获取文件的后缀名
			String end = name.substring(dotIndex).toLowerCase();
			// 获取文件file的MIME类型
			String type = getMIMEType(end);
			// 设置intent的data和Type属性。
			intent.setDataAndType(/* uri */Uri.fromFile(new File(path)), type);
			// 跳转
			context.startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, "未安装打开此文件类型的程序！", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}

	/**
	 * 读取文本文件
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String read(Context context, String fileName) {
		try {
			FileInputStream in = context.openFileInput(fileName);
			return readInStream(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 *
	 * @param inStream
	 * @return
     */

	public static String readInStream(InputStream inStream) {
		try {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[512];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
			}

			outStream.close();
			inStream.close();
			return outStream.toString();
		} catch (IOException e) {
			Log.i("FileTest", e.getMessage());
		}
		return null;
	}

	public static File createFile(String folderPath, String fileName) {
		File destDir = new File(folderPath);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
		return new File(folderPath, fileName + fileName);
	}

	/**
	 * 向手机写图片
	 * 
	 * @param buffer
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static boolean writeFile(byte[] buffer, String folder, String fileName) {
		boolean writeSucc = false;
		File fileDir = new File(folder);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}

		File file = new File(folder + File.separator + fileName);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(buffer);
			writeSucc = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (out != null) {
					out.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return writeSucc;
	}

	/**
	 * 根据文件绝对路径获取文件名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (StringUtils.isEmpty(filePath))
			return "";
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1);
	}

	/**
	 * 根据文件的绝对路径获取文件名但不包含扩展名
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileNameNoFormat(String filePath) {
		if (StringUtils.isEmpty(filePath)) {
			return "";
		}
		int point = filePath.lastIndexOf('.');
		return filePath.substring(filePath.lastIndexOf(File.separator) + 1, point);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (StringUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	/**
	 * 获取文件大小
	 * 
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		long size = 0;

		File file = new File(filePath);
		if (file != null && file.exists()) {
			size = file.length();
		}
		return size;
	}

	/**
	 * 获取文件大小
	 * 
	 * @param size
	 *            字节
	 * @return
	 */
	public static String getFileSize(long size) {
		if (size <= 0)
			return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float) size / 1024;
		if (temp >= 1024) {
			return df.format(temp / 1024) + "M";
		} else {
			return df.format(temp) + "K";
		}
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 获取目录文件个数
	 * 
	 * @param dir
	 * @return
	 */
	public long getFileList(File dir) {
		long count = 0;
		File[] files = dir.listFiles();
		count = files.length;
		for (File file : files) {
			if (file.isDirectory()) {
				count = count + getFileList(file);// 递归
				count--;
			}
		}
		return count;
	}

	public static byte[] toBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			out.write(ch);
		}
		byte buffer[] = out.toByteArray();
		out.close();
		return buffer;
	}

	/**
	 * 检查文件是否存在
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkFileExists(String name) {
		boolean status;
		if (!name.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + name);
			status = newPath.exists();
		} else {
			status = false;
		}
		return status;
	}

	/**
	 * 检查路径是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean checkFilePathExists(String path) {
		return new File(path).exists();
	}

	/**
	 * 计算SD卡的剩余空间
	 * 
	 * @return 返回-1，说明没有安装sd卡
	 */
	public static long getFreeDiskSpace() {
		String status = Environment.getExternalStorageState();
		long freeSpace = 0;
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				long blockSize = stat.getBlockSizeLong();
				long availableBlocks = stat.getAvailableBlocksLong();
				freeSpace = availableBlocks * blockSize / 1024;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return -1;
		}
		return (freeSpace);
	}

	/**
	 * 新建目录
	 * 
	 * @param directoryName
	 * @return
	 */
	public static boolean createDirectory(String directoryName) {
		boolean status;
		if (!directoryName.equals("")) {
			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + directoryName);
			status = newPath.mkdir();
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装SD卡
	 * 
	 * @return
	 */
	public static boolean checkSaveLocationExists() {
		String sDCardStatus = Environment.getExternalStorageState();
		boolean status;
		if (sDCardStatus.equals(Environment.MEDIA_MOUNTED)) {
			status = true;
		} else
			status = false;
		return status;
	}

	/**
	 * 检查是否安装外置的SD卡
	 * 
	 * @return
	 */
	public static boolean checkExternalSDExists() {

		Map<String, String> evn = System.getenv();
		return evn.containsKey("SECONDARY_STORAGE");
	}

	/**
	 * 删除目录(包括：目录里的所有文件)
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteDirectory(String fileName) {
		boolean status;
		SecurityManager checker = new SecurityManager();

		if (!fileName.equals("")) {

			File path = Environment.getExternalStorageDirectory();
			File newPath = new File(path.toString() + fileName);
			checker.checkDelete(newPath.toString());
			if (newPath.isDirectory()) {
				String[] listfile = newPath.list();
				// delete all files within the specified directory and then
				// delete the directory
				try {
					for (int i = 0; i < listfile.length; i++) {
						File deletedFile = new File(newPath.toString() + "/" + listfile[i].toString());
						deletedFile.delete();
					}
					newPath.delete();
					Log.i("DirectoryManage", fileName);
					status = true;
				} catch (Exception e) {
					e.printStackTrace();
					status = false;
				}

			} else
				status = false;
		} else
			status = false;
		return status;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static boolean deleteFile(String filePath) {
		try {
			File file = new File(filePath);
			if (file.exists() && file.isFile()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// /**
	// * 删除文件
	// *
	// * @param fileName
	// * @return
	// */
	// public static boolean deleteFile(String fileName) {
	// boolean status;
	// SecurityManager checker = new SecurityManager();
	//
	// if (!fileName.equals("")) {
	//
	// File path = Environment.getExternalStorageDirectory();
	// File newPath = new File(path.toString() + fileName);
	// checker.checkDelete(newPath.toString());
	// if (newPath.isFile()) {
	// try {
	// Log.i("DirectoryManager deleteFile", fileName);
	// newPath.delete();
	// status = true;
	// } catch (SecurityException se) {
	// se.printStackTrace();
	// status = false;
	// }
	// } else
	// status = false;
	// } else
	// status = false;
	// return status;
	// }

	/**
	 * 删除空目录
	 * 
	 * 返回 0代表成功 ,1 代表没有删除权限, 2代表不是空目录,3 代表未知错误
	 * 
	 * @return
	 */
	public static int deleteBlankPath(String path) {
		File f = new File(path);
		if (!f.canWrite()) {
			return 1;
		}
		if (f.list() != null && f.list().length > 0) {
			return 2;
		}
		if (f.delete()) {
			return 0;
		}
		return 3;
	}

	/**
	 * 重命名
	 * 
	 * @param oldName
	 * @param newName
	 * @return
	 */
	public static boolean reNamePath(String oldName, String newName) {
		File f = new File(oldName);
		return f.renameTo(new File(newName));
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 */
	public static boolean deleteFileWithPath(String filePath) {
		SecurityManager checker = new SecurityManager();
		File f = new File(filePath);
		checker.checkDelete(filePath);
		if (f.isFile()) {
			Log.i("DirectoryManager", filePath);
			f.delete();
			return true;
		}
		return false;
	}

	/**
	 * 清空一个文件夹
	 * 
	 * @param filePath
	 */
	public static void clearFileWithPath(String filePath) {
		List<File> files = FileUtils.listPathFiles(filePath);
		if (files.isEmpty()) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				clearFileWithPath(f.getAbsolutePath());
			} else {
				f.delete();
			}
		}
	}

	/**
	 * 获取SD卡的根目录
	 * 
	 * @return
	 */
	public static String getSDRoot() {

		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 获取手机外置SD卡的根目录
	 * 
	 * @return
	 */
	public static String getExternalSDRoot() {

		Map<String, String> evn = System.getenv();

		return evn.get("SECONDARY_STORAGE");
	}

	/**
	 * 列出root目录下所有子目录
	 * 
	 * @param root
	 * @return 绝对路径
	 */
	public static List<String> listPath(String root) {
		List<String> allDir = new ArrayList<String>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		// 过滤掉以.开始的文件夹
		if (path.isDirectory()) {
			for (File f : path.listFiles()) {
				if (f.isDirectory() && !f.getName().startsWith(".")) {
					allDir.add(f.getAbsolutePath());
				}
			}
		}
		return allDir;
	}

	/**
	 * 获取一个文件夹下的所有文件
	 * 
	 * @param root
	 * @return
	 */
	public static List<File> listPathFiles(String root) {
		List<File> allDir = new ArrayList<File>();
		SecurityManager checker = new SecurityManager();
		File path = new File(root);
		checker.checkRead(root);
		File[] files = path.listFiles();
		for (File f : files) {
			if (f.isFile())
				allDir.add(f);
			else
				listPath(f.getAbsolutePath());
		}
		return allDir;
	}



	public enum PathStatus {
		SUCCESS, EXITS, ERROR
	}

	/**
	 * 创建目录
	 * 
	 * @param  newPath
	 */
	public static PathStatus createPath(String newPath) {
		File path = new File(newPath);
		if (path.exists()) {
			return PathStatus.EXITS;
		}
		if (path.mkdir()) {
			return PathStatus.SUCCESS;
		} else {
			return PathStatus.ERROR;
		}
	}

	/**
	 * 截取路径名
	 * 
	 * @return
	 */
	public static String getPathName(String absolutePath) {
		int start = absolutePath.lastIndexOf(File.separator) + 1;
		int end = absolutePath.length();
		return absolutePath.substring(start, end);
	}

	/**
	 * 获取应用程序缓存文件夹下的指定目录
	 * 
	 * @param context
	 * @param dir
	 * @return
	 */
	public static String getAppCache(Context context, String dir) {
		String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/";
		File savedir = new File(savePath);
		if (!savedir.exists()) {
			savedir.mkdirs();
		}
		savedir = null;
		return savePath;
	}

	public static String getMIMEType(String suffix) {
		String type = "*/*";
		/*
		 * String fName=file.getName(); //获取后缀名前的分隔符"."在fName中的位置。 int dotIndex
		 * = fName.lastIndexOf("."); if(dotIndex < 0){ return type; } 获取文件的后缀名
		 * String end=fName.substring(dotIndex,fName.length()).toLowerCase();
		 */
		if (suffix == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {

			if (suffix.equals(MIME_MapTable[i][0])) {
				type = MIME_MapTable[i][1];
			}

		}
		return type;
	}

	private static final String[][] MIME_MapTable = {
			// {后缀名，MIME类型}
			{ ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" },
			{ ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" },
			{ ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" },
			{ ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" },
			{ ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ ".xls", "application/vnd.ms-excel" },
			{ ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" },
			{ ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" },
			{ ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" },
			{ ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" },
			{ ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" },
			{ ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" },
			{ ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" },
			{ ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" },
			{ ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" },
			{ ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" },
			{ ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" },
			{ ".ppt", "application/vnd.ms-powerpoint" },
			{ ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ ".prop", "text/plain" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" },
			{ ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" },
			{ ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" },
			{ ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" },
			{ ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/x-zip-compressed" },
			{ "", "*/*" } };

	public static void deleteFile(String folder, String type) {

		final String ftype = type;
		File dir = new File(folder);
		// list out all the file name end with .type
		String[] list = dir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				return filename.endsWith(ftype);
			}
		});

		if (list.length == 0) {
			return;
		}
		File fileDelete;
		for (String filename : list) {
			String temp = new StringBuffer(folder).append(File.separator).append(filename).toString();
			fileDelete = new File(temp);
			fileDelete.delete();
		}
	}

	/**
	 * 读临时文件
	 * 
	 * @return
	 */
	public static byte[] readRandomFile(String filePath, int byteSize, int offset) {

		RandomAccessFile raf;
		byte[] b = new byte[byteSize];
		try {
			raf = new RandomAccessFile(filePath, "r");
			raf.seek(offset);
			raf.read(b);
			raf.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return b;
	}

	/**
	 * 写临时文件
	 * 
	 */
	public static void writeRandomFile(String filePath, byte[] b, int offset) {

		try {
			RandomAccessFile raf;
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			raf = new RandomAccessFile(filePath, "rwd");
			raf.seek(offset);
			raf.write(b, 0, b.length);
			raf.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 图片上加文字
	public static Bitmap createNewBitmap(Bitmap src, String title, Context context, int color) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		Paint paint = new Paint();
		// 加入图片
		// if (watermark != null) {
		// int ww = watermark.getWidth();
		// int wh = watermark.getHeight();
		// paint.setAlpha(50);
		// cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);//
		// 在src的右下角画入水印
		// }
		// 加入文字
		if (title != null) {
			String familyName = "宋体";
			Typeface font = Typeface.create(familyName, Typeface.BOLD);
			TextPaint textPaint = new TextPaint();
			textPaint.setColor(color);
			textPaint.setShadowLayer(3f, 1, 1, context.getResources().getColor(android.R.color.background_dark));
			textPaint.setTypeface(font);
			textPaint.setTextSize(35);
			// 这里是自动换行的
			// StaticLayout layout = new
			// StaticLayout(title,textPaint,w,Alignment.ALIGN_NORMAL,1.0F,0.0F,true);
			// layout.draw(cv);
			// 文字就加左上角算了
			cv.drawText(title, 40, 60, textPaint);
		}
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		cv.restore();// 存储
		return newb;

	}

	
   public static Properties readConfigPrt(String filePath){
		
		 Properties props = new Properties();
         try {
	         InputStream in = new BufferedInputStream(new FileInputStream(filePath));
	         props.load(in);
         } catch (Exception e) {
	         e.printStackTrace();
	         props=null;
         }
	        
	     return props;
	}
	
	
   public static boolean saveConfigPrt(final Properties properties, final String filePath){
		
		try {	    	
			 FileOutputStream fos = new FileOutputStream(filePath);
	         properties.store(fos, null);
	         return true;
        } catch (IOException e) {
             e.printStackTrace();
        }
		
		return false;
	     
	}

	/**
	 * bitmap转byte数组
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] datas = baos.toByteArray();
		return datas;

	}

	public static String getFilePathByUri(Context context, Uri uri) {
		String path = null;
		// 以 file:// 开头的
		if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
			path = uri.getPath();
			return path;
		}
		// 以 content:// 开头的，比如 content://media/extenral/images/media/17766
		if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					if (columnIndex > -1) {
						path = cursor.getString(columnIndex);
					}
				}
				cursor.close();
			}
			return path;
		}
		// 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (DocumentsContract.isDocumentUri(context, uri)) {
				if (isExternalStorageDocument(uri)) {
					// ExternalStorageProvider
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];
					if ("primary".equalsIgnoreCase(type)) {
						path = Environment.getExternalStorageDirectory() + "/" + split[1];
						return path;
					}
				} else if (isDownloadsDocument(uri)) {
					// DownloadsProvider
					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
							Long.valueOf(id));
					path = getDataColumn(context, contentUri, null, null);
					return path;
				} else if (isMediaDocument(uri)) {
					// MediaProvider
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
					final String[] selectionArgs = new String[]{split[1]};
					path = getDataColumn(context, contentUri, selection, selectionArgs);
					return path;
				}
			}
		}
		return null;
	}

	private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {column};
		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	private static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	private static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	private static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	public static String getRealPathFromUri(Context context, Uri uri) {
		int sdkVersion = Build.VERSION.SDK_INT;
		if (sdkVersion < 11) return getRealPathFromUri_BelowApi11(context, uri);
		if (sdkVersion < 19) return getRealPathFromUri_Api11To18(context, uri);
		else return getRealPathFromUri_AboveApi19(context, uri);
	}

	/**
	 * 适配api19以上,根据uri获取图片的绝对路径
	 */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
		String filePath = null;
		String wholeID = DocumentsContract.getDocumentId(uri);

		// 使用':'分割
		String id = wholeID.split(":")[1];

		String[] projection = {MediaStore.Images.Media.DATA};
		String selection = MediaStore.Images.Media._ID + "=?";
		String[] selectionArgs = {id};

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
				projection, selection, selectionArgs, null);
		int columnIndex = cursor.getColumnIndex(projection[0]);
		if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}

	/**
	 * 适配api11-api18,根据uri获取图片的绝对路径
	 */
	public  static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
		String filePath = null;
		String[] projection = {MediaStore.Images.Media.DATA};
		CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
		Cursor cursor = loader.loadInBackground();

		if (cursor != null) {
			cursor.moveToFirst();
			filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
			cursor.close();
		}
		return filePath;
	}

	/**
	 * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
	 */
	private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
		String filePath = null;
		String[] projection = {MediaStore.Images.Media.DATA};
		Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
			cursor.close();
		}
		return filePath;
	}


}