package eg.edu.alexu.csd.oop.game.entity.shape;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import eg.edu.alexu.csd.oop.game.GameLogger;

public class ShapeLoader {
	private static ShapeLoader inst;
	public static ShapeLoader getInst(){
		if(inst == null)
			inst = new ShapeLoader();
		return inst;
	}
	private File folder;
	private final String PATH = "shapes/";
	private ShapeLoader() {
		folder = new File(PATH);
		if( !folder.exists() )
			folder.mkdir();
		if(!folder.exists()){
			GameLogger.getInst().fatal("Failed to load shapes folder.");
			System.exit(1);
		}
	}
	public List<Class<? extends Shape>> getSupportedShapes(){
		List<Class<? extends Shape>> shapeList = new ArrayList<Class<? extends Shape>>();
		for( File f : folder.listFiles()){
			if( f.getName().toLowerCase().endsWith(".jar") ){
				findClasses(f, shapeList);
			}
		}
		for(Class<? extends Shape>shape : shapeList ){
			GameLogger.getInst().info(shape.getSimpleName() + " loaded") ;
		}
		if(shapeList.isEmpty()){
			GameLogger.getInst().warn("No wildshapes found") ;
		}
		return shapeList;
	}
	private void findClasses(File selected, List<Class<? extends Shape>> tempList) {
		ClassLoader loader;
		try {
			loader = URLClassLoader.newInstance(new URL[] { selected.toURI().toURL() }, getClass().getClassLoader());
		} catch (MalformedURLException e) {
			return;
		}
		String path = selected.getPath();

		try {
			JarInputStream jis = new JarInputStream(new FileInputStream(path));
			JarEntry je;
			while (true) {
				je = jis.getNextJarEntry();
				if (je == null)
					break;
				if (je.getName().endsWith(".class")) {
					String className = je.getName().substring(0, je.getName().length() - 6);
					className = className.replaceAll("/", ".");
					try {
						Class<?> tC = loader.loadClass(className);
						if (Shape.class.isAssignableFrom(tC)) {
							if (!tempList.contains(tC))
								tempList.add((Class<? extends Shape>) tC);
						}
					} catch (Exception ex) {
						continue;
					}
				}
			}
			jis.close();
		} catch (Exception ex) {

		}

	}
}
