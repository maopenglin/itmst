package com.cmstop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageHelper {
	
	
	public static void resizeImage(String oldImage, String outImgPath,
			int new_w, int new_h) {

		try{
		BufferedImage src = InputImage(oldImage);

		int old_w = src.getWidth();

		// 得到源图宽

		int old_h = src.getHeight();

		// 得到源图长

		// 根据原图的大小生成空白画布

		BufferedImage tempImg = new BufferedImage(old_w, old_h,

		BufferedImage.TYPE_INT_ARGB);

		// 在新的画布上生成原图的缩略图

		Graphics2D g = tempImg.createGraphics();

		g.setColor(Color.white);

		g.fillRect(0, 0, old_w, old_h);

		g.drawImage(src, 0, 0, old_w, old_h, Color.white, null);

		g.dispose();

		BufferedImage newImg = new BufferedImage(new_w, new_h,

		BufferedImage.TYPE_INT_RGB);

		newImg.getGraphics().drawImage(

		tempImg.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0,

		0, null);

		// 调用方法输出图片文件

		outImage(outImgPath, newImg, 1f);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private static void outImage(String outImgPath, BufferedImage newImg, float per) { 
		File file=new File(outImgPath);
		try {
			ImageIO.write(newImg,"PNG", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		    } 
	
	
	
	private static BufferedImage InputImage(String srcImgPath)
			throws RuntimeException {

		BufferedImage srcImage = null;

		FileInputStream in = null;

		try {

			// 构造BufferedImage对象

			File file = new File(srcImgPath);

			in = new FileInputStream(file);

			byte[] b = new byte[5];

			in.read(b);

			srcImage = javax.imageio.ImageIO.read(file);

		} catch (IOException e) {

			e.printStackTrace();

			throw new RuntimeException("读取图片文件出错！", e);

		} finally {

			if (in != null) {

				try {

					in.close();

				} catch (IOException e) {

					throw new RuntimeException("读取图片文件出错！", e);

				}

			}

		}

		return srcImage;

	} 

}
