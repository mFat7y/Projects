package eg.edu.alexu.csd.oop.draw;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Observable;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import javax.swing.JButton;
import javax.swing.JComponent;

public class Controller extends Observable implements DrawingEngine {
	private Model model;
	private View view;
	private Shape lastShape;
	private final int ADDCOND = 1, UPDATECOND = 2, REMOVECOND = 3;

	public Controller() {
		initialize();
	}

	private void initialize() {
		JComponent sentCanv = canvasGenerator();
		sentCanv.addMouseListener(new myMouseListener());
		model = new Model();
		try {
			view = new View();
			view.initialize(sentCanv, new ButtMouseListener());
			addObserver(view);
		} catch (Exception ex) {
			view = null;
		}
		addSupportedShapes("default.jar");
	}

	private void updateCanvas() {
		setChanged();
		notifyObservers();
	}

	private class myMouseListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (view == null)
				return;
			if (lastShape == null) {
				ListIterator<Shape> it = model.getCurrShape();
				Shape tS = null;
				while (it.hasPrevious()) {
					Shape tmp = it.previous();
					Point tP = tmp.getPosition();
					if (Math.abs(tP.x - e.getX()) < 25 && Math.abs(tP.y - e.getY()) < 25) {
						try {
							tS = (Shape) tmp.clone();
						} catch (CloneNotSupportedException e1) {
							e1.printStackTrace();
							return;
						}
						Color myClr = tS.getColor();
						Color myFillClr = tS.getFillColor();
						Map<String, Double> mp = tS.getProperties();
						mp.put("#CR", 1.0 * myClr.getRed());
						mp.put("#CG", 1.0 * myClr.getGreen());
						mp.put("#CB", 1.0 * myClr.getBlue());
						mp.put("#CA", 1.0 * myClr.getAlpha());
						mp.put("#CFR", 1.0 * myFillClr.getRed());
						mp.put("#CFG", 1.0 * myFillClr.getGreen());
						mp.put("#CFB", 1.0 * myFillClr.getBlue());
						mp.put("#CFA", 1.0 * myFillClr.getAlpha());
						tmp.setFillColor(tmp.getColor().darker().darker());
						updateCanvas();
						mp = view.getProp(mp, 2);
						if (mp == null) {
							tmp.setFillColor(myFillClr);
							updateCanvas();
							return;
						} else if (mp.containsKey("#REMOVE#")) {
							tmp.setFillColor(myFillClr);
							removeShape(tmp);
							updateCanvas();
						} else {
							tmp.setFillColor(myFillClr);
							myClr = new Color(mp.get("#CR").intValue(), mp.get("#CG").intValue(),
									mp.get("#CB").intValue(), mp.get("#CA").intValue());
							myFillClr = new Color(mp.get("#CFR").intValue(), mp.get("#CFG").intValue(),
									mp.get("#CFB").intValue(), mp.get("#CFA").intValue());
							mp.remove("#CR");
							mp.remove("#CG");
							mp.remove("#CB");
							mp.remove("#CA");
							mp.remove("#CFR");
							mp.remove("#CFG");
							mp.remove("#CFB");
							mp.remove("#CFA");
							tS.setProperties(mp);
							tS.setColor(myClr);
							tS.setFillColor(myFillClr);
							updateShape(tmp, tS);
							updateCanvas();
						}
						break;
					}
				}
			} else {
				Shape tS = lastShape;
				Map<String, Double> mp = tS.getProperties();
				mp = view.getProp(mp, 1);
				if (mp == null) {
					lastShape = null;
					return;
				}
				Color myClr = new Color(mp.get("#CR").intValue(), mp.get("#CG").intValue(), mp.get("#CB").intValue(),
						mp.get("#CA").intValue());
				Color myFillClr = new Color(mp.get("#CFR").intValue(), mp.get("#CFG").intValue(),
						mp.get("#CFB").intValue(), mp.get("#CFA").intValue());
				mp.remove("#CR");
				mp.remove("#CG");
				mp.remove("#CB");
				mp.remove("#CA");
				mp.remove("#CFR");
				mp.remove("#CFG");
				mp.remove("#CFB");
				mp.remove("#CFA");
				tS.setProperties(mp);
				tS.setPosition(new Point(e.getX(), e.getY()));
				tS.setColor(myClr);
				tS.setFillColor(myFillClr);
				addShape(tS);
				lastShape = null;
			}
		}
	}

	private class ButtMouseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (view == null)
				return;
			JButton temp = (JButton) e.getSource();
			if (temp.getName() == "undo") {
				try {
					undo();
				} catch (Exception ex) {
					view.showError("This is the first step.");
				}
			} else if (temp.getName() == "redo") {
				try {
					redo();
				} catch (Exception ex) {
					view.showError("This is the last step.");
				}
			} else if (temp.getName() == "save") {
				String tempPath = view.saveFile();
				if (tempPath == null)
					return;
				try {
					save(tempPath);
				} catch (Exception ex) {
					view.showError("Save File Error");
				}
			} else if (temp.getName() == "load") {
				String tempPath = view.loadFile();
				if (tempPath == null)
					return;
				try {
					load(tempPath);
				} catch (Exception ex) {
					view.showError("Load File Error");
				}
			} else if (temp.getName() == "plugin") {
				String tempPath = view.loadFile();
				if (tempPath == null)
					return;
				if (!tempPath.endsWith(".jar"))
					view.showError("Load File Error");
				try {
					addSupportedShapes(tempPath);
				} catch (Exception ex) {
					view.showError("Load File Error");
				}
			} else {
				String shapeType = ((JButton) e.getSource()).getName();
				Shape tS = model.shapeGenerator(shapeType);
				if (tS == null)
					throw new RuntimeException();
				lastShape = tS;
			}
		}

	}

	private JComponent canvasGenerator() {
		class temp extends JComponent {
			private static final long serialVersionUID = 7L;

			@Override
			public void paint(Graphics g) {
				if (g instanceof Graphics2D) {
					Graphics2D g2d = (Graphics2D) g;
					g2d.setStroke(new BasicStroke(2));
					g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				}
				refresh(g);
			}
		}
		return new temp();
	}

	@Override
	public void refresh(Graphics canvas) {
		if (canvas == null)
			return;
		Shape arr[] = getShapes();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null)
				arr[i].draw(canvas);
		}
	}

	@Override
	public void addShape(Shape shape) {
		model.addShape(shape, ADDCOND, shape);
		updateCanvas();
	}

	@Override
	public void removeShape(Shape shape) {
		if (model.getShapes().length == 0)
			throw new RuntimeException();
		if (shape == null)
			return;
		model.addShape(shape, REMOVECOND, shape);
		updateCanvas();
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		if (oldShape == null || newShape == null)
			return;
		model.addShape(oldShape, UPDATECOND, newShape);
		updateCanvas();
	}

	private void addSupportedShapes(String path) {
		List<Class<? extends Shape>> supShapes = null;
		if (path == null) {
			supShapes = getSupportedShapes();
		} else {
			LinkedList<String> tempList = new LinkedList<String>();
			tempList.add(path);
			supShapes = getSupportedShapes(tempList);
		}
		LinkedList<String> classList = new LinkedList<String>();
		java.util.Iterator<Class<? extends Shape>> it = supShapes.iterator();
		while (it.hasNext()) {
			Class<? extends Shape> curr = it.next();
			if (!model.addNewShape(curr))
				continue;
			classList.add(curr.getSimpleName());
		}
		if (view != null)
			view.addShapes(classList);
	}

	@Override
	public Shape[] getShapes() {
		return model.getShapes();
	}

	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		List<String> folders = new LinkedList<String>();
		URL[] urls = ((URLClassLoader) cl).getURLs();

		for (URL url : urls) {
			folders.add(url.getFile());
		}
		return getSupportedShapes(folders);
	}

	private List<Class<? extends Shape>> getSupportedShapes(List<String> folders) {
		List<Class<? extends Shape>> tempList = new LinkedList<Class<? extends Shape>>();
		List<String> files = new LinkedList<String>();
		while (!folders.isEmpty()) {
			File directory = new File(folders.get(0).replace("%20", " "));
			folders.remove(0);

			if (directory.isFile()) {
				files.add(directory.getPath());
				continue;
			}

			if (!directory.exists())
				continue;

			File[] fList = directory.listFiles();
			for (File file : fList) {
				if (file.isFile()) {
					if (file.getName().endsWith(".jar"))
						files.add(file.getPath());
				}
			}
		}
		for (String s : files) {
			findClasses(s, tempList);
		}
		return tempList;
	}

	private void findClasses(String s, List<Class<? extends Shape>> tempList) {
		File selected = new File(s);
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
						if (tC.newInstance() instanceof Shape && Shape.class.isAssignableFrom(tC)) {
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

	@Override
	public void undo() {
		model.decrement();
		updateCanvas();
	}

	@Override
	public void redo() {
		model.increment();
		updateCanvas();

	}

	@Override
	public void save(String path) {
		if (path == null)
			return;
		model.save(path);
		updateCanvas();
	}

	@Override
	public void load(String path) {
		if (path == null)
			return;
		model.load(path);
		updateCanvas();

	}

	public static void main(String[] args) {
		new Controller();

	}
}
