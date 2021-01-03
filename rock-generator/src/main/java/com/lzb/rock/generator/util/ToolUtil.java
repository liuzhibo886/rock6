
package com.lzb.rock.generator.util;

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

/**
 * 高频方法集合类
 */
public class ToolUtil {
	public static final String UNDERLINE = "_";
	public static final String EMPTY = "";

	/**
	 * 首字母变大写
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

	/**
	 * 去掉指定前缀
	 * 
	 * @param str    字符串
	 * @param prefix 前缀
	 * @return 切掉后的字符串，若前缀不是 preffix， 返回原字符串
	 */
	public static String removePrefix(String str, String prefix) {
		if (isEmpty(str) || isEmpty(prefix)) {
			return str;
		}

		if (str.startsWith(prefix)) {
			return str.substring(prefix.length());
		}
		return str;
	}

	/**
	 * 首字母变小写
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}

	public static Boolean isNotEmpty(String str) {

		if (str != null && str != "") {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isEmpty(String str) {

		if (str != null && str != "") {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 将下划线方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：hello_world->HelloWorld
	 *
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String toCamelCase(String name) {
		if (name == null) {
			return null;
		}
		if (name.contains(UNDERLINE)) {
			name = name.toLowerCase();

			StringBuilder sb = new StringBuilder(name.length());
			boolean upperCase = false;
			for (int i = 0; i < name.length(); i++) {
				char c = name.charAt(i);

				if (c == '_') {
					upperCase = true;
				} else if (upperCase) {
					sb.append(Character.toUpperCase(c));
					upperCase = false;
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		} else {
			return name;
		}

	}

	/**
	 * 数组是否为空
	 * 
	 * @param array 数组
	 * @return 是否为空
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	// ------------------------------------------------------------------------
	// Blank
	/**
	 * 字符串是否为空白 空白的定义如下： <br>
	 * 1、为null <br>
	 * 2、为不可见字符（如空格）<br>
	 * 3、""<br>
	 * 
	 * @param str 被检测的字符串
	 * @return 是否为空
	 */
	public static boolean isBlank(String str) {
		int length;
		if ((str == null) || ((length = str.length()) == 0)) {
			return true;
		}
		for (int i = 0; i < length; i++) {
			// 只要有一个非空字符即为非空字符串
			if (false == Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 格式化文本, {} 表示占位符<br>
	 * 例如：format("aaa {} ccc", "bbb") ----> aaa bbb ccc
	 * 
	 * @param template 文本模板，被替换的部分用 {} 表示
	 * @param values   参数值
	 * @return 格式化后的文本
	 */
	public static String format(String template, Object... values) {
		if (isEmpty(values) || isBlank(template)) {
			return template;
		}

		final StringBuilder sb = new StringBuilder();
		final int length = template.length();

		int valueIndex = 0;
		char currentChar;
		for (int i = 0; i < length; i++) {
			if (valueIndex >= values.length) {
				sb.append(sub(template, i, length));
				break;
			}

			currentChar = template.charAt(i);
			if (currentChar == '{') {
				final char nextChar = template.charAt(++i);
				if (nextChar == '}') {
					sb.append(values[valueIndex++]);
				} else {
					sb.append('{').append(nextChar);
				}
			} else {
				sb.append(currentChar);
			}

		}

		return sb.toString();
	}

	/**
	 * 改进JDK subString<br>
	 * index从0开始计算，最后一个字符为-1<br>
	 * 如果from和to位置一样，返回 "" <br>
	 * 如果from或to为负数，则按照length从后向前数位置，如果绝对值大于字符串长度，则from归到0，to归到length<br>
	 * 如果经过修正的index中from大于to，则互换from和to example: <br>
	 * abcdefgh 2 3 -> c <br>
	 * abcdefgh 2 -3 -> cde <br>
	 * 
	 * @param string    String
	 * @param fromIndex 开始的index（包括）
	 * @param toIndex   结束的index（不包括）
	 * @return 字串
	 */
	public static String sub(String string, int fromIndex, int toIndex) {
		int len = string.length();
		if (fromIndex < 0) {
			fromIndex = len + fromIndex;
			if (fromIndex < 0) {
				fromIndex = 0;
			}
		} else if (fromIndex >= len) {
			fromIndex = len - 1;
		}
		if (toIndex < 0) {
			toIndex = len + toIndex;
			if (toIndex < 0) {
				toIndex = len;
			}
		} else if (toIndex > len) {
			toIndex = len;
		}
		if (toIndex < fromIndex) {
			int tmp = fromIndex;
			fromIndex = toIndex;
			toIndex = tmp;
		}
		if (fromIndex == toIndex) {
			return EMPTY;
		}
		char[] strArray = string.toCharArray();
		char[] newStrArray = Arrays.copyOfRange(strArray, fromIndex, toIndex);
		return new String(newStrArray);
	}

	/**
	 * 判断是否是windows操作系统
	 *
	 * @author stylefeng
	 * @Date 2017/5/24 22:34
	 */
	public static Boolean isWinOs() {
		String os = System.getProperty("os.name");
		if (os.toLowerCase().startsWith("win")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取临时目录
	 *
	 * @author stylefeng
	 * @Date 2017/5/24 22:35
	 */
	public static String getTempPath() {
		return System.getProperty("java.io.tmpdir");
	}

	/**
	 * 把一个数转化为int
	 *
	 * @author fengshuonan
	 * @Date 2017/11/15 下午11:10
	 */
	public static Integer toInt(Object val) {
		if (val instanceof Double) {
			BigDecimal bigDecimal = new BigDecimal((Double) val);
			return bigDecimal.intValue();
		} else {
			return Integer.valueOf(val.toString());
		}

	}

	/**
	 * 获取项目路径
	 */
	public static String getWebRootPath(String filePath) {
		try {
			String path = ToolUtil.class.getClassLoader().getResource("").toURI().getPath();
			path = path.replace("/WEB-INF/classes/", "");
			path = path.replace("/target/classes/", "");
			path = path.replace("file:/", "");
			if (ToolUtil.isEmpty(filePath)) {
				return path;
			} else {
				return path + "/" + filePath;
			}
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 时间格式化
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		String formatDate = null;
		if (StringUtils.isNotBlank(pattern)) {
			formatDate = DateFormatUtils.format(date, pattern);
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}

	/**
	 * 当前时间
	 *
	 * @author stylefeng
	 * @Date 2017/5/7 21:56
	 */
	public static String currentTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 判断表是否包含该字段
	 * 
	 * @param tableInfo
	 * @param propertyName
	 * @return
	 */
	public Boolean isTableField(TableInfo tableInfo, String name) {
		if (tableInfo == null) {
			return false;
		}
		if (StringUtils.isBlank(name)) {
			return false;
		}
		Boolean flag = false;
		for (TableField tableField : tableInfo.getFields()) {
			if (name.equals(tableField.getName())) {
				flag = true;
				break;
			}
		}
		return flag;

	}
}