package t.sql.dynamic.proxy.spring;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/***
 * 用来动态注册Bean
 * 
 * @author zhangj
 * @date 2018年9月30日 上午10:44:10
 * @email zhangjin0908@hotmail.com
 */
public class SpringDynamicRegisteredBean implements ApplicationContextAware {

	private static final char SLASH_CHAR = '/';
	private static final char DOT_CHAR = '.';
	private String packagePath;
	private t.sql.SessionFactory sessionFactory;

	public SpringDynamicRegisteredBean(String packagePath, t.sql.SessionFactory sessionFactory) {
		this.packagePath = packagePath;
		this.sessionFactory = sessionFactory;
	}

	/**
	 * 在当前项目中寻找指定包下的所有类
	 * 
	 * @param packageName 用'.'分隔的包名
	 * @param recursion   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */
	public static List<Class<?>> getClass(String packageName, boolean recursive)
			throws ClassNotFoundException, IOException, URISyntaxException {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		Enumeration<URL> iterator = Thread.currentThread().getContextClassLoader()
				.getResources(packageName.replace(DOT_CHAR, File.separatorChar));
		while (iterator.hasMoreElements()) {
			URL url = iterator.nextElement();
			String protocol = url.getProtocol();
			List<Class<?>> childClassList = Collections.emptyList();
			switch (protocol) {
			case "file":
				childClassList = getClassInFile(url, packageName, recursive);
				break;
			case "jar":
				childClassList = getClassInJar(url, packageName, recursive);
				break;
			default:
				System.out.println("unknown protocol " + protocol);
				break;
			}
			classList.addAll(childClassList);
		}
		return classList;
	}

	/**
	 * 在给定的文件或文件夹中寻找指定包下的所有类
	 * 
	 * @param filePath    包的路径
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static List<Class<?>> getClassInFile(String filePath, String packageName, boolean recursive)
			throws ClassNotFoundException, IOException {
		Path path = Paths.get(filePath);
		return getClassInFile(path, packageName, recursive);
	}

	/**
	 * 在给定的文件或文件夹中寻找指定包下的所有类
	 * 
	 * @param url         包的统一资源定位符
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException
	 */
	public static List<Class<?>> getClassInFile(URL url, String packageName, boolean recursive)
			throws ClassNotFoundException, IOException, URISyntaxException {
		Path path = Paths.get(url.toURI());
		return getClassInFile(path, packageName, recursive);
	}

	/**
	 * 在给定的文件或文件夹中寻找指定包下的所有类
	 * 
	 * @param path        包的路径
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("resource")
	public static List<Class<?>> getClassInFile(Path path, String packageName, boolean recursive)
			throws IOException, ClassNotFoundException {
		if (!Files.exists(path)) {
			return Collections.emptyList();
		}
		List<Class<?>> classList = new ArrayList<Class<?>>();
		if (Files.isDirectory(path)) {
			if (!recursive) {
				return Collections.emptyList();
			}
			// 获取目录下的所有文件
			Stream<Path> stream = Files.list(path);
			Iterator<Path> iterator = stream.iterator();
			while (iterator.hasNext()) {
				classList.addAll(getClassInFile(iterator.next(), packageName, recursive));
			}
		} else {
			// 由于传入的文件可能是相对路径, 这里要拿到文件的实际路径, 如果不存在则报IOException
			path = path.toRealPath();
			String pathStr = path.toString();
			int lastDotIndex = pathStr.lastIndexOf(DOT_CHAR);
			// Class.forName只允许使用用'.'分隔的类名的形式
			String className = pathStr.replace(File.separatorChar, DOT_CHAR);
			// 获取包名的起始位置
			int beginIndex = className.indexOf(packageName);
			if (beginIndex == -1) {
				return Collections.emptyList();
			}
			className = lastDotIndex == -1 ? className.substring(beginIndex)
					: className.substring(beginIndex, lastDotIndex);
			classList.add(Class.forName(className));
		}
		return classList;
	}

	/**
	 * 在给定的jar包中寻找指定包下的所有类
	 * 
	 * @param filePath    包的路径
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 */
	public static List<Class<?>> getClassInJar(String filePath, String packageName, boolean recursive) {
		try {
			JarFile jar = new JarFile(filePath);
			return getClassInJar(jar, packageName, recursive);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * 在给定的jar包中寻找指定包下的所有类
	 * 
	 * @param url         jar包的统一资源定位符
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 */
	public static List<Class<?>> getClassInJar(URL url, String packageName, boolean recursive) {
		try {
			JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
			return getClassInJar(jar, packageName, recursive);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	/**
	 * 在给定的jar包中寻找指定包下的所有类
	 * 
	 * @param jar         jar对象
	 * @param packageName 用'.'分隔的包名
	 * @param recursive   是否递归搜索
	 * @return 该包名下的所有类
	 */
	public static List<Class<?>> getClassInJar(JarFile jar, String packageName, boolean recursive) {
		List<Class<?>> classList = new ArrayList<Class<?>>();
		// 该迭代器会递归得到该jar底下所有的目录和文件
		Enumeration<JarEntry> iterator = jar.entries();
		while (iterator.hasMoreElements()) {
			JarEntry jarEntry = iterator.nextElement();
			if (!jarEntry.isDirectory()) {
				String name = jarEntry.getName();
				// 对于拿到的文件,要去除末尾的.class
				int lastDotClassIndex = name.lastIndexOf(".class");
				if (lastDotClassIndex != -1) {
					int lastSlashIndex = name.lastIndexOf(SLASH_CHAR);
					name = name.replace(SLASH_CHAR, DOT_CHAR);
					if (name.startsWith(packageName)) {
						if (recursive || packageName.length() == lastSlashIndex) {
							String className = name.substring(0, lastDotClassIndex);
							System.out.println(className);
							try {
								classList.add(Class.forName(className));
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		}
		return classList;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		try {
			List<Class<?>> beans = getClass(packagePath, true);
			DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext
					.getAutowireCapableBeanFactory();
			for (Class<?> bean : beans) {
				Object beanObject = t.sql.dynamic.proxy.DynamicProxy.getInstance(bean, sessionFactory);
				beanFactory.registerSingleton("dynamic" + java.util.UUID.randomUUID().toString().replaceAll("-", ""),
						beanObject);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
