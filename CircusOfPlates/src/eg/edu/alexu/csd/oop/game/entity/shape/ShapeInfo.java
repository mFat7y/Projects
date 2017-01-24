package eg.edu.alexu.csd.oop.game.entity.shape;

public class ShapeInfo {
	private int width;
	private int height;
	private String imgPath;
	
	public ShapeInfo(int width,int height, String path) {
		this.width = width;
		this.height = height;
		this.imgPath = path;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getImgPath() {
		return imgPath;
	}
	
}
