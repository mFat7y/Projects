package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Model {
	private int pointer;
	private int lowerPointer;
	private final int ADDCOND = 1, UPDATECOND = 2, REMOVECOND = 3, MAXOP = 20, NOTHING = -1;
	private LinkedList<Shape> list;
	private LinkedList<Integer> parents;
	private LinkedList<Integer> actions;
	private Map<String, Class<? extends Shape>> mySupportedClasses;

	public Model() {
		pointer = 0;
		lowerPointer = 0;
		list = new LinkedList<>();
		parents = new LinkedList<>();
		actions = new LinkedList<>();
		mySupportedClasses = new TreeMap<String, Class<? extends Shape>>();
	}

	public boolean addNewShape(Class<? extends Shape> newClass) {
		if (mySupportedClasses.containsKey(newClass.getSimpleName()))
			return false;
		mySupportedClasses.put(newClass.getSimpleName(), newClass);
		return true;
	}

	public Shape shapeGenerator(String name) {
		if (!mySupportedClasses.containsKey(name))
			return null;
		try {
			return mySupportedClasses.get(name).newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	public void load(String path) {
		path = path.toLowerCase();
		Point co = new Point();
		LinkedList<Shape> shapes = new LinkedList<Shape>();
		if (path.toLowerCase().endsWith(".xml")) {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder builder = fact.newDocumentBuilder();
				Document doc = builder.parse(path.toLowerCase());
				Node main = doc.getElementsByTagName("save").item(0);
				NodeList classes = main.getChildNodes();
				for (int i = 0; i < classes.getLength(); i++) {
					Node tc = classes.item(i);
					if (tc.getNodeType() == Node.ELEMENT_NODE) {
						Shape temp = shapeGenerator(tc.getNodeName());
						if (temp == null) {
							shapes.add(null);
							continue;
						}
						NodeList props = tc.getChildNodes();
						for (int j = 0; j < props.getLength(); j++) {
							Node tp = props.item(j);
							if (tp.getNodeType() == Node.ELEMENT_NODE) {
								if (tp.getNodeName() == "map") {
									Map<String, Double> mp = new HashMap<>();
									NodeList keys = tp.getChildNodes();
									boolean flag = false;
									for (int k = 0; k < keys.getLength(); k++) {
										if (keys.item(k).getNodeType() == Node.ELEMENT_NODE) {
											flag = true;
											mp.put(keys.item(k).getNodeName(),
													Double.parseDouble(keys.item(k).getTextContent()));
										}
									}
									if (flag == false)
										temp.setProperties(null);
									else
										temp.setProperties(mp);
								} else if (tp.getNodeName() == "position") {
									try {
										NodeList coos = tp.getChildNodes();
										for (int k = 0; k < coos.getLength(); k++) {
											if (coos.item(k).getNodeType() == Node.ELEMENT_NODE) {
												if (coos.item(k).getNodeName() == "x") {
													if (coos.item(k).getTextContent() != null
															&& coos.item(k).getTextContent() != "")
														co.x = Integer.parseInt(coos.item(k).getTextContent());
												} else if (coos.item(k).getNodeName() == "y"
														&& coos.item(k).getTextContent() != "")
													if (coos.item(k).getTextContent() != null)
														co.y = Integer.parseInt(coos.item(k).getTextContent());
											}
										}
										temp.setPosition(co);
									} catch (Exception ex) {
										continue;
									}
								} else if (tp.getNodeName() == "color") {
									try {
										int r = 0, g = 0, b = 0, a = 0;
										NodeList cr = tp.getChildNodes();
										boolean flag = false;
										for (int k = 0; k < cr.getLength(); k++) {
											if (cr.item(k).getNodeType() == Node.ELEMENT_NODE) {
												flag = true;
												if (cr.item(k).getNodeName() == "R")
													r = Integer.parseInt(cr.item(k).getTextContent());
												else if (cr.item(k).getNodeName() == "G")
													g = Integer.parseInt(cr.item(k).getTextContent());
												else if (cr.item(k).getNodeName() == "B")
													b = Integer.parseInt(cr.item(k).getTextContent());
												else if (cr.item(k).getNodeName() == "A")
													a = Integer.parseInt(cr.item(k).getTextContent());
											}
										}
										if (flag)
											temp.setColor(new Color(r, g, b, a));
										else
											temp.setColor(null);
									} catch (Exception ex) {
										continue;
									}

								} else if (tp.getNodeName() == "fillcolor") {
									try {
										int r = 0, g = 0, b = 0, a = 0;
										NodeList cr = tp.getChildNodes();
										boolean flag = false;
										for (int k = 0; k < cr.getLength(); k++) {
											if (cr.item(k).getNodeType() == Node.ELEMENT_NODE) {
												flag = true;
												if (cr.item(k).getNodeName() == "R")
													r = Integer.parseInt(cr.item(k).getTextContent());
												else if (cr.item(k).getNodeName() == "G")
													g = Integer.parseInt(cr.item(k).getTextContent());
												else if (cr.item(k).getNodeName() == "B")
													b = Integer.parseInt(cr.item(k).getTextContent());
												else if (cr.item(k).getNodeName() == "A")
													a = Integer.parseInt(cr.item(k).getTextContent());
											}
										}
										if (flag)
											temp.setFillColor(new Color(r, g, b, a));
										else
											temp.setFillColor(null);
									} catch (Exception ex) {
										continue;
									}
								}

							}
						}
						shapes.add(temp);
					}
				}
			} catch (ParserConfigurationException e) {
				throw new RuntimeException();
			} catch (IOException f) {
				throw new RuntimeException();
			} catch (SAXException s) {
				throw new RuntimeException("fails here ", s);
			}

		} else if (path.toLowerCase().endsWith(".json")) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(path.toLowerCase()));
				String str = br.readLine();
				str = br.readLine();
				str = br.readLine();
				str = br.readLine();

				Map<String, Double> mp = new Hashtable<String, Double>();
				Point p = new Point();
				int R = 0, G = 0, B = 0, A = 0;

				while (true) {
					str = str.replaceAll("\\s", "");
					if (str.charAt(0) == '{' || str.charAt(0) == '}') {
						str = br.readLine();
						continue;
					}
					if (str.charAt(0) == ']')
						break;
					str = str.replaceAll("\"|,", "");
					String temp[] = str.split(":");
					Shape shp = shapeGenerator(temp[1]);
					if (shp == null) {
						shapes.add(null);

						while (true) {
							str = br.readLine();
							if (str.contains("}") == true) {
								break;
							}
						}
						continue;
					}
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						p.x = Integer.parseInt(temp[1]);
					else
						p = null;
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						p.y = Integer.parseInt(temp[1]);
					shp.setPosition(p);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						R = Integer.parseInt(temp[1]);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						G = Integer.parseInt(temp[1]);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						B = Integer.parseInt(temp[1]);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null") {
						A = Integer.parseInt(temp[1]);
						shp.setColor(new Color(R, G, B, A));
					} else
						shp.setColor(null);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						R = Integer.parseInt(temp[1]);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						G = Integer.parseInt(temp[1]);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null")
						B = Integer.parseInt(temp[1]);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					temp = str.split(":");
					if (temp[1] != "null") {
						A = Integer.parseInt(temp[1]);
						shp.setFillColor(new Color(R, G, B, A));
					} else
						shp.setFillColor(null);
					str = br.readLine();
					str = str.replaceAll("\\s|\"|,", "");
					boolean flag = false;
					while (str.charAt(0) != '}') {
						temp = str.split(":");
						mp.put(temp[0], Double.parseDouble(temp[1]));
						flag = true;
						str = br.readLine();
						str = str.replaceAll("\\s|\"|,", "");
					}
					if (flag)
						shp.setProperties(mp);
					else
						shp.setProperties(null);
					str = br.readLine();
					shapes.add(shp);
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		} else {
			throw new RuntimeException();
		}
		list.clear();
		parents.clear();
		actions.clear();
		pointer = shapes.size();
		lowerPointer = pointer;
		for (int i = 0; i < shapes.size(); i++) {
			list.add(shapes.get(i));
			parents.add(i);
			actions.add(ADDCOND);
		}
	}

	public void save(String path) {
		path = path.toLowerCase();
		Shape[] shps = getShapes();
		if (path.toLowerCase().endsWith(".xml")) {
			try {
				PrintWriter writer = new PrintWriter(path.toLowerCase());
				writer.println("<save>");
				for (int i = 0; i < shps.length; i++) {
					writer.println("\t<" + shps[i].getClass().getSimpleName() + ">");
					addNewShape(shps[i].getClass());
					writer.println("\t\t<map>");
					Map<String, Double> mp = shps[i].getProperties();
					if (mp != null) {
						for (String key : mp.keySet()) {
							writer.println("\t\t\t<" + key + ">" + mp.get(key) + "</" + key + ">");
						}
						writer.println("\t\t</map>");
					} else
						writer.println("</map>");
					writer.println("\t\t<position>");
					if (shps[i].getPosition() != null) {
						System.out.println("");
						writer.println("\t\t\t<x>" + shps[i].getPosition().x + "</x>");
						writer.println("\t\t\t<y>" + shps[i].getPosition().y + "</y>");
						writer.println("\t\t</position>");
					} else
						writer.println("</position>");
					writer.println("\t\t<color>");

					if (shps[i].getColor() != null) {
						System.out.println("");
						writer.println("\t\t\t<R>" + shps[i].getColor().getRed() + "</R>");
						writer.println("\t\t\t<G>" + shps[i].getColor().getGreen() + "</G>");
						writer.println("\t\t\t<B>" + shps[i].getColor().getBlue() + "</B>");
						writer.println("\t\t\t<A>" + shps[i].getColor().getAlpha() + "</A>");
						writer.println("\t\t</color>");
					} else
						writer.println("</color>");

					writer.println("\t\t<fillcolor>");
					if (shps[i].getFillColor() != null) {
						writer.println("\t\t\t<R>" + shps[i].getFillColor().getRed() + "</R>");
						writer.println("\t\t\t<G>" + shps[i].getFillColor().getGreen() + "</G>");
						writer.println("\t\t\t<B>" + shps[i].getFillColor().getBlue() + "</B>");
						writer.println("\t\t\t<A>" + shps[i].getFillColor().getAlpha() + "</A>");
						writer.println("\t\t</fillcolor>");
					} else
						writer.println("</fillcolor>");
					writer.println("\t</" + shps[i].getClass().getSimpleName() + ">");
				}
				writer.println("</save>");
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (path.toLowerCase().endsWith(".json")) {
			try {
				PrintWriter writer = new PrintWriter(path.toLowerCase());
				writer.println("{\n\t\"Save\":[");
				for (int i = 0; i < shps.length; i++) {
					writer.println("\t\t{");
					if (shps[i] == null) {
						writer.println("\t\t\t\"class\" : \"" + "null" + "\",");
						if (i != shps.length - 1)
							writer.println("\t\t},");
						else
							writer.println("\t\t}");
						continue;
					}
					writer.println("\t\t\t\"class\" : \"" + shps[i].getClass().getSimpleName() + "\",");

					if (shps[i].getPosition() != null) {
						writer.println("\t\t\t\"x\" : \"" + shps[i].getPosition().x + "\",");
						writer.println("\t\t\t\"y\" : \"" + shps[i].getPosition().y + "\",");
					} else {
						writer.println("\t\t\t\"x\" : \"null\",");
						writer.println("\t\t\t\"y\" : \"null\",");
					}
					if (shps[i].getColor() != null) {
						writer.println("\t\t\t\"colorR\" : \"" + shps[i].getColor().getRed() + "\",");
						writer.println("\t\t\t\"colorG\" : \"" + shps[i].getColor().getGreen() + "\",");
						writer.println("\t\t\t\"colorB\" : \"" + shps[i].getColor().getBlue() + "\",");
						writer.println("\t\t\t\"colorA\" : \"" + shps[i].getColor().getAlpha() + "\",");
					} else {
						writer.println("\t\t\t\"colorR\" : \"null\",");
						writer.println("\t\t\t\"colorG\" : \"null\",");
						writer.println("\t\t\t\"colorB\" : \"null\",");
						writer.println("\t\t\t\"colorA\" : \"null\",");
					}
					if (shps[i].getFillColor() != null) {
						writer.println("\t\t\t\"fillColorR\" : \"" + shps[i].getFillColor().getRed() + "\",");
						writer.println("\t\t\t\"fillColorG\" : \"" + shps[i].getFillColor().getGreen() + "\",");
						writer.println("\t\t\t\"fillColorB\" : \"" + shps[i].getFillColor().getBlue() + "\",");
						writer.println("\t\t\t\"fillColorA\" : \"" + shps[i].getFillColor().getAlpha() + "\",");
					} else {
						writer.println("\t\t\t\"fillColorR\" : \"null\",");
						writer.println("\t\t\t\"fillColorG\" : \"null\",");
						writer.println("\t\t\t\"fillColorB\" : \"null\",");
						writer.println("\t\t\t\"fillColorA\" : \"null\",");
					}
					Map<String, Double> mp = shps[i].getProperties();
					if (mp != null) {
						for (String key : mp.keySet()) {
							writer.println("\t\t\t\"" + key + "\" : \"" + mp.get(key) + "\",");
						}
					}
					if (i != shps.length - 1)
						writer.println("\t\t},");
					else
						writer.println("\t\t}");
				}
				writer.println("\t]\n}");
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException();
		}
	}

	public void increment() {
		if (pointer < list.size()) {
			pointer++;
			if (pointer - lowerPointer > MAXOP)
				lowerPointer++;
		} else
			throw new RuntimeException();
	}

	public void decrement() {
		if (pointer > lowerPointer)
			pointer--;
		else
			throw new RuntimeException();
	}

	public ListIterator<Shape> getCurrShape() {
		return list.listIterator(pointer);
	}

	public ListIterator<Integer> getCurrParent() {
		return parents.listIterator(pointer);
	}

	public ListIterator<Integer> getCurrAction() {
		return actions.listIterator(pointer);
	}

	public Shape[] getShapes() {
		ListIterator<Shape> itS = list.listIterator();
		ListIterator<Integer> itP = parents.listIterator();
		ListIterator<Integer> itA = actions.listIterator();
		Map<Integer, Shape> tempMp = new TreeMap<Integer, Shape>();
		int i = 0;
		while (i < pointer && itS.hasNext()) {
			Shape cS = itS.next();
			Integer cP = itP.next();
			Integer cA = itA.next();
			tempMp.remove(cP);
			if (cA != REMOVECOND)
				tempMp.put(cP, cS);
			i++;
		}
		Shape temp[] = new Shape[tempMp.size()];
		i = 0;
		for (Entry<Integer, Shape> entry : tempMp.entrySet()) {
			temp[i++] = entry.getValue();
		}
		return temp;
	}

	public void addShape(Shape x, int action, Shape y) {
		while (pointer < list.size()) {
			list.removeLast();
			actions.removeLast();
			parents.removeLast();
		}
		int parent = list.size() + 1;
		if (action >= UPDATECOND) {
			ListIterator<Shape> it = list.listIterator();
			ListIterator<Integer> itP = parents.listIterator();
			parent = NOTHING;
			while (it.hasNext()) {
				Shape tS = it.next();
				int tP = itP.next();
				if (tS == x) {
					parent = tP;
					break;
				}
			}
			if (parent == NOTHING) {
				throw new RuntimeException();
			}
		}
		list.add(y);
		actions.add(action);
		parents.add(parent);
		increment();
	}
}