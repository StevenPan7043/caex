package com.image.util;  
import java.io.*;  
import java.awt.*;  
import java.awt.image.*;  
import javax.imageio.ImageIO;  
import com.sun.image.codec.jpeg.*;  

/** 
 * 图片压缩处理 
 * @author 王唯 
 */  
public class ImgCompress {  
    /** 
     * 按照宽度还是高度进行压缩 
     * @param w int 最大宽度 
     * @param h int 最大高度 
     */  
    public static void resizeFix(File file,int w, int h,String filepath,String filename) throws IOException {  
	    Image img = ImageIO.read(file);      // 构造Image对象    
	    int   width = img.getWidth(null);    // 得到源图宽    
	    int  height = img.getHeight(null);  // 得到源图长  
    	if (width / height > w / h) {  
            resizeByWidth(file,w,filepath,filename);  
        } else {  
            resizeByHeight(file,h,filepath,filename);  
        }  
    }  
    /** 
     * 以宽度为基准，等比例放缩图片 
     * @param w int 新宽度 
     */  
    public static void resizeByWidth(File file,int w,String filepath,String filename) throws IOException {  
    	Image img = ImageIO.read(file);      // 构造Image对象    
  	    int   width = img.getWidth(null);    // 得到源图宽    
  	    int  height = img.getHeight(null);  // 得到源图长  
    	int h = (int) (height * w / width);  
        resize(file,w, h,filepath,filename);  
    }  
    /** 
     * 以高度为基准，等比例缩放图片 
     * @param h int 新高度 
     */  
    public static void resizeByHeight(File file,int h,String filepath,String filename) throws IOException {  
    	Image img = ImageIO.read(file);      // 构造Image对象    
  	    int   width = img.getWidth(null);    // 得到源图宽    
  	    int  height = img.getHeight(null);  // 得到源图长  
    	int w = (int) (width * h / height);  
        resize(file,w, h,filepath,filename);  
    }  
    /** 
     * 强制压缩/放大图片到固定的大小 
     * @param w int 新宽度 
     * @param h int 新高度 
     */  
    @SuppressWarnings("restriction")
	public static void resize(File file,int w, int h,String filepath,String filename) throws IOException {  
    	Image img = ImageIO.read(file);      // 构造Image对象    
    	// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢  
        BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB );   
        image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图  
        File destFile = new File(filepath+filename);  
        FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流  
        // 可以正常实现bmp、png、gif转jpg  
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);  
        encoder.encode(image); // JPEG编码  
        out.close();  
    }  
}  