package com.lzb.rock.base.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.lzb.rock.base.enums.RockEnum;
import com.lzb.rock.base.exception.BusException;

/**
 * 文件读取，删除，写入
 * 
 * @author lzb 2018年2月28日 上午11:44:59
 */
public class UtilFile extends FileUtils {
	/**
	 * 删除文件
	 * 
	 * @param root_patch
	 * @param name
	 */
	public static void delete(String rootPath, String fileName) {
		String path = "";
		if (rootPath.endsWith(File.separator)) {
			path = rootPath + fileName;
		} else {
			path = rootPath + File.separator + fileName;
		}
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 *
	 * @param str 写入字符串
	 * @param url 保存文件名字
	 */
	public static void out(String rootPath, String fileName, String str) {
		List<String> list = new ArrayList<String>();
		list.add(str);
		out(rootPath, fileName, list);
	}

	/**
	 *
	 * @param list 写入list
	 * @param url  保存文件名字
	 * @throws FileNotFoundException
	 */

	public static void out(String rootPath, String fileName, byte[] b) throws Exception {

		String path = "";
		if (rootPath.endsWith(File.separator)) {
			path = rootPath + fileName;
		} else {
			path = rootPath + File.separator + fileName;
		}
		File file = new File(path);
		OutputStream outputstream = new FileOutputStream(file, true);
		outputstream.write(b);
		outputstream.close();

	}

	/**
	 * 创建目录
	 * 
	 * @param path
	 */
	public static void mkdirs(String path) {
		File file = new File(path);
		if (!file.exists()) {
			/**
			 * 创建目录
			 */
			new File(path).mkdirs();
		}
	}

	public static void out(String rootPath, String fileName, List<String> list) {
		String path = "";
		if (rootPath.endsWith(File.separator)) {
			path = rootPath + fileName;
		} else {
			path = rootPath + File.separator + fileName;
		}
		File file = new File(path);
		BufferedWriter writer = null;
		OutputStreamWriter out = null;
		try {
			if (!file.exists()) {
				/**
				 * 创建目录
				 */
				new File(rootPath).mkdirs();
				/**
				 * 创建文件
				 */
				file.createNewFile();
			} //

			// true 表示追加
			out = new OutputStreamWriter(new FileOutputStream(path, true), "UTF-8");
			writer = new BufferedWriter(out);
			for (String str : list) {
				writer.write(str);
				writer.newLine();
			}
			writer.flush();
			// System.out.println("str:" + str + ",保存文本成功");

		} catch (IOException e) {
			System.out.println("文件不存在");
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @return List<String>
	 */
	public static List<String> input(String rootPath, String fileName) {
		String path = "";
		if (rootPath.endsWith(File.separator)) {
			path = rootPath + fileName;
		} else {
			path = rootPath + File.separator + fileName;
		}

		String str = new String();
		List<String> list = new ArrayList<String>();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			System.out.println("文件不存在");
			e1.printStackTrace();
		}

		if (fis != null) {
			InputStreamReader isr = null;
			BufferedReader br = null;
			try {
				isr = new InputStreamReader(fis, "UTF-8");
				br = new BufferedReader(isr);
				while ((str = br.readLine()) != null) {
					list.add(str);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null) {
						br.close();
					}
					if (isr != null) {
						isr.close();
					}
					if (fis != null) {
						fis.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return list;
	}

	/**
	 * NIO way
	 */
	public static byte[] toByteArray(String filename) {

		File f = new File(filename);
		if (!f.exists()) {
			throw new BusException(RockEnum.FILE_NOT_FOUND);
		}
		FileChannel channel = null;
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		} catch (IOException e) {
			throw new BusException(RockEnum.FILE_READING_ERROR);
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				throw new BusException(RockEnum.FILE_READING_ERROR);
			}
			try {
				fs.close();
			} catch (IOException e) {
				throw new BusException(RockEnum.FILE_READING_ERROR);
			}
		}
	}

	/**
	 * 获取指定文件夹下左右文件路径
	 * 
	 * @return
	 */
	public static void getAllFileName(String path, List<String> fileNameList) {
		File file = new File(path);
		File[] tempList = file.listFiles();
		if (tempList == null || tempList.length < 1) {
			return;
		}
		for (File file2 : tempList) {
			if (file2.isFile()) {
				fileNameList.add(file2.getAbsolutePath());
			} else if (file2.isDirectory()) {
				getAllFileName(file2.getAbsolutePath(), fileNameList);
			}
		}

	}

	/**
	 * 
	 * @param fileNameList     需要复制文件全路径
	 * @param destFileRootPath 复制文件的根路径
	 * @param suffixs          文件后缀
	 * @throws IOException
	 */
	public static void copyFiles(List<String> fileNameList, String destFileRootPath, String[] suffixs)
			throws IOException {
		for (String path : fileNameList) {
			boolean flag = false;

			for (String suffix : suffixs) {
				if (path.endsWith(suffix)) {
					flag = true;
					break;
				}
			}
			if (flag) {
				File srcFile = new File(path);
				String destFilePath = destFileRootPath + srcFile.getName();
				File destFile = new File(destFilePath);
				System.out.println(srcFile.getName());
				UtilFile.copyFile(srcFile, destFile);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		// UtilFile.delete("G:/", "rinetd.conf-20180228105538325");
		List<String> fileNameList = new ArrayList<String>();
		UtilFile.getAllFileName("G:\\正中集团", fileNameList);
		String[] suffixs = { "xls", "xlsx", "docx", "doc", "pdf", "txt", "pptx", "ppt" };
		String destFileRootPath = "G:\\文档模板\\";

		UtilFile.copyFiles(fileNameList, destFileRootPath, suffixs);
	}
}
