package eg.edu.alexu.csd.oop.game.entity.shape;

import java.util.List;

import eg.edu.alexu.csd.oop.game.configurations.GameConfigurations;
import eg.edu.alexu.csd.oop.game.gfx.ImageLoader;
import eg.edu.alexu.csd.oop.game.worlds.MyWorld;

public class ShapeFlyWeightFactory {
	private static ShapeFlyWeightFactory inst;
	public static ShapeFlyWeightFactory getInst(){
		if(inst == null)
			inst = new ShapeFlyWeightFactory();
		return inst;
	}
	private int last;
	private int wildShapeFreq;
	private List<ShapeInfo> supportedNormalShapes;
	private List<Class<? extends Shape>>supportedShapes;
	private ShapeFlyWeightFactory() {
		wildShapeFreq = GameConfigurations.getInst().getWildShapeFreq();
		supportedShapes = ShapeLoader.getInst().getSupportedShapes();
	}
	public Shape generateRandomShape(MyWorld world){
		supportedNormalShapes = world.getSupportedShapes();
		ShapeInfo shapeInfo = getRandomShapeInfo();
		int x = getRandomX(world);
		int y = 40;
		int color = ImageLoader.getInst().getRandomColor();
		int rnd = getRandomNum(1000);
		if( rnd % wildShapeFreq != 0 || supportedShapes.size() == 0){
			return ShapeFactory.getInst().generateNormalShape(world,x,y,shapeInfo.getWidth(),shapeInfo.getHeight(),shapeInfo.getImgPath(),shapeInfo.getImgPath(), color,world.getShapeSpeed());
		}
		return ShapeFactory.getInst().generateWildShape(getRandomShape(),world,x,y,shapeInfo.getWidth(),shapeInfo.getHeight(),shapeInfo.getImgPath(),shapeInfo.getImgPath(),color,world.getShapeSpeed());
	}
	private int getRandomNum(int limit){
		return (int)(Math.random()*limit);
	}
	private int getRandomX(MyWorld world){
		int x=world.getPosition((int)(Math.random()*4));
		while (x==last)x=world.getPosition((int)(Math.random()*4));
		last=x;		
		return last;
	}
	private ShapeInfo getRandomShapeInfo(){
		return supportedNormalShapes.get(getRandomNum(supportedNormalShapes.size()));
	}
	private Class<? extends Shape> getRandomShape(){
		int index = getRandomNum(supportedShapes.size());
		Class<? extends Shape> ret = supportedShapes.get(index);
		return ret;
	}
}
